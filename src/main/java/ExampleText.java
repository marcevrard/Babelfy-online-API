import java.util.List;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.jlt.util.Language;


public class ExampleText {

    public static void main(String[] args) throws Exception {

        // Get input text from file
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        String fileName = "se2013_doc1.txt";	// toy_example.txt
        Path file = Paths.get(currentPath.toString(), "input", fileName);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");  // \Z = end of the string anchor
        String inputText = scanner.next();
        scanner.close();

        // Setup Babelfy paramteres
        BabelfyParameters bfyParams = new BabelfyParameters();
        bfyParams.setAnnotationResource(SemanticAnnotationResource.BN);
        bfyParams.setMCS(MCS.ON_WITH_STOPWORDS);
        bfyParams.setScoredCandidates(ScoredCandidates.TOP);

        // Launch Babelfy annotation
        Babelfy bfy = new Babelfy(bfyParams);
        List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(inputText, Language.EN);

        // Create printer
        Path out_dir = Paths.get("output");
        Files.createDirectories(out_dir);
        String out_fileName = "out_" + fileName + ".tsv";
        Path out_file = Paths.get(currentPath.toString(), out_dir.toString(), out_fileName);
        PrintWriter writer = new PrintWriter(out_file.toString(), "UTF-8");

        // bfyAnnotations is the result of Babelfy.babelfy() call
        for(SemanticAnnotation annotation:bfyAnnotations) {
            // splitting the input text using the CharOffsetFragment start and end anchors
            Integer offsetStart = annotation.getCharOffsetFragment().getStart();
            Integer offsetEnd = annotation.getCharOffsetFragment().getEnd();
            String frag = inputText.substring(offsetStart, offsetEnd + 1);
            String output = annotation.getBabelSynsetID() + "\t"
                            + annotation.getTokenOffsetFragment().getStart() + "\t"
                            + annotation.getTokenOffsetFragment().getEnd() + "\t"
                            + offsetStart.toString() + "\t"
                            + offsetEnd.toString() + "\t"
                            + annotation.getSource() + "\t"
                            + frag;
            // System.out.println(output);
            // Print to file
            writer.println(output);
        }
        writer.close();
    }
}
