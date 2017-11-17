import java.util.List;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelfy.commons.BabelfyToken;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.jlt.util.Language;


public class ExampleToken {

    public static void main(String[] args) throws Exception {

        // Get input data from file
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        String fileName = "se2013_tokens_1.tsv";
        Path file = Paths.get(currentPath.toString(), "input/se2013_tokens", fileName);
        Scanner scanner = new Scanner(file);

        List<BabelfyToken> tokenizedInput = new ArrayList<>();

        // Convert POS input to POS objects
        Map<String, PosTag> posDic = new HashMap<String, PosTag>();
        posDic.put("n", PosTag.NOUN);
        posDic.put("v", PosTag.VERB);
        posDic.put("j", PosTag.ADJECTIVE);
        posDic.put("r", PosTag.ADVERB);
        posDic.put("x", PosTag.OTHER);

        // Input tokens to Babelfy
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] lineParts = line.split("\\t");
            if (lineParts[0].equals("<eos>")) {
                tokenizedInput.add(BabelfyToken.EOS);
            } else {
                tokenizedInput.add(new BabelfyToken(lineParts[0], posDic.get(lineParts[1])));
            }
        }
        scanner.close();

        // Setup Babelfy paramteres
        BabelfyParameters bfyParams = new BabelfyParameters();
        bfyParams.setAnnotationResource(SemanticAnnotationResource.BN);
        bfyParams.setMCS(MCS.ON_WITH_STOPWORDS);
        bfyParams.setScoredCandidates(ScoredCandidates.TOP);

        // Launch Babelfy annotation
        Babelfy bfy = new Babelfy(bfyParams);
        List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(tokenizedInput, Language.EN);

        // Create printer
        Path out_dir = Paths.get("output");
        Files.createDirectories(out_dir);
        String out_fileName = "out_" + fileName;
        Path out_file = Paths.get(currentPath.toString(), out_dir.toString(), out_fileName);
        PrintWriter writer = new PrintWriter(out_file.toString(), "UTF-8");

        // bfyAnnotations is the result of Babelfy.babelfy() call
        for(SemanticAnnotation annotation:bfyAnnotations) {
            // splitting the input text using the CharOffsetFragment start and end anchors
            String output = annotation.getBabelSynsetID() + "\t"
                            + annotation.getTokenOffsetFragment().getStart() + "\t"
                            + annotation.getTokenOffsetFragment().getEnd() + "\t"
                            + annotation.getSource() + "\t"
                            + annotation.getDBpediaURL();
            // System.out.println(output);
            // Print to file
            writer.println(output);
        }
        writer.close();
    }
}
