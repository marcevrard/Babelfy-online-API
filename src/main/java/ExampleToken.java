import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationType;
import it.uniroma1.lcl.babelfy.commons.BabelfyToken;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;


public class ExampleToken {

    public static void main(String[] args) throws Exception {

        // Convert POS input to POS objects
        Map<String, PosTag> posDic = buildPosDic();

        // Setup Babelfy parameters
        // BabelfyParameters bfyParams = getParams("BN", "ALL");
        BabelfyParameters bfyParams = getParams("WN", "CONCEPTS");

        List<Path> filesInFolder = getFiles(args);
        for (Path inputFile : filesInFolder) {

            // Input tokens to Babelfy
            List<BabelfyToken> tokenizedInput = getInput(inputFile, posDic);

            // Launch Babelfy annotation
            Babelfy bfy = new Babelfy(bfyParams);
            List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(tokenizedInput, Language.EN);
            // System.out.println(tokenizedInput);

            // Write to file
            Path outputFile = getOutputPath(inputFile);
            writeResults(bfyAnnotations, tokenizedInput, outputFile);
        }
    }

    public static Map<String, PosTag> buildPosDic() {
        Map<String, PosTag> posDic = new HashMap<String, PosTag>();
        posDic.put("n", PosTag.NOUN);
        posDic.put("v", PosTag.VERB);
        posDic.put("a", PosTag.ADJECTIVE);
        posDic.put("r", PosTag.ADVERB);
        posDic.put("x", PosTag.OTHER);
        return posDic;
    }

    public static BabelfyParameters getParams(String annRes, String annType) {
        BabelfyParameters bfyParams = new BabelfyParameters();
        if (annRes == "WN") {
            bfyParams.setAnnotationResource(SemanticAnnotationResource.WN);
        } else {
            bfyParams.setAnnotationResource(SemanticAnnotationResource.BN);
        }
        if (annRes == "CONCEPTS") {
            bfyParams.setAnnotationType(SemanticAnnotationType.CONCEPTS);
        }
        bfyParams.setMCS(MCS.ON_WITH_STOPWORDS);
        bfyParams.setScoredCandidates(ScoredCandidates.TOP);

        // Sets the disambiguation threshold (theta)
        bfyParams.setThreshold(0.8);  // default = 0.7

        return bfyParams;
    }

    public static List<int[]> buildCharOffsets(List<BabelfyToken> tokenizedInput) {
        List<int[]> charOffsets = new ArrayList<int[]>();
        Integer offsetCharStart = 0;
        Integer offsetCharEnd;
        for (BabelfyToken token : tokenizedInput) {
            offsetCharEnd = offsetCharStart + token.getWord().length() - 1;
            charOffsets.add(new int[] { offsetCharStart, offsetCharEnd });
            offsetCharStart = offsetCharEnd + 2;
        }
        return charOffsets;
    }

    public static void writeResults(List<SemanticAnnotation> bfyAnnotations,
                                    List<BabelfyToken> tokenizedInput, Path out_file)
            throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(out_file.toString(), "UTF-8");

        List<int[]> charOffsets = buildCharOffsets(tokenizedInput);

        // bfyAnnotations is the result of Babelfy.babelfy() call
        for (SemanticAnnotation annotation : bfyAnnotations) {
            Integer offsetTokenStart = annotation.getTokenOffsetFragment().getStart();
            Integer offsetTokenEnd = annotation.getTokenOffsetFragment().getEnd();

            Integer offsetCharStart = charOffsets.get(offsetTokenStart)[0];
            Integer offsetCharEnd = charOffsets.get(offsetTokenStart)[1];

            String output = annotation.getBabelSynsetID()
                            + "\t" + offsetTokenStart
                            + "\t" + offsetTokenEnd
                            + "\t" + offsetCharStart
                            + "\t" + offsetCharEnd
                            + "\t" + annotation.getSource()
                            + "\t" + annotation.getGlobalScore()
                            + "\t" + annotation.getDBpediaURL();
            // Print to file
            writer.println(output);
        }
        writer.close();
    }

    public static List<BabelfyToken> getInput(Path inputFile, Map<String, PosTag> posDic)
            throws IOException {
        List<BabelfyToken> tokenizedInput = new ArrayList<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] lineParts = line.split("\\t");
            if (lineParts[0].equals("<eos>")) {
                tokenizedInput.add(BabelfyToken.EOS);
            } else {
                tokenizedInput.add(
                    new BabelfyToken(lineParts[0], posDic.get(lineParts[1])));
            }
        }
        scanner.close();
        return tokenizedInput;
    }

    public static List<Path> getFiles(String[] args) {
        String dirName = "se2007_task7_tokens";
        if (args.length != 0) {
            dirName = args[0];
        }
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path inputPath = currentPath.resolve("input").resolve(dirName);
        List<Path> filesInFolder = new ArrayList<>();
        try {
            filesInFolder = Files.walk(inputPath)
                                 .filter(Files::isRegularFile)
                                 .collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        return filesInFolder;
    }

    public static Path getOutputPath(Path inputFile) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        String outputFileName = "out_" + inputFile.getFileName().toString();
        Path outputPath = currentPath.resolve("output")
                                     .resolve(inputFile.getParent()
                                                       .getFileName());
        Path file = outputPath.resolve(outputFileName);
        // Create outputPath if does not exist
        if (Files.notExists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
                System.out.println("Path did not exist, created: " + outputPath.toString());
            } catch (java.io.IOException e) {
                System.out.println("createDirectory failed:" + e);
            }
        }
        return file;
    }
}
