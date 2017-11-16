import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Scanner;
import java.io.PrintWriter;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.babelfy.commons.BabelfyConstraints;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation.Source;
import it.uniroma1.lcl.babelfy.commons.annotation.TokenOffsetFragment;
import it.uniroma1.lcl.jlt.util.Language;

public class ExampleFile {
	public static void main(String[] args) throws Exception {
		Path currentPath = Paths.get(System.getProperty("user.dir"));
		String fileName = "se2013_doc1.txt";
		Path file = Paths.get(currentPath.toString(), "input", fileName);
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\Z");  // \Z = end of the string anchor
		String inputText = scanner.next();
		scanner.close();

		BabelfyConstraints constraints = new BabelfyConstraints();
		SemanticAnnotation a = new SemanticAnnotation(new TokenOffsetFragment(0, 0), "bn:03083790n",
				"http://dbpedia.org/resource/BabelNet", Source.OTHER);

		constraints.addAnnotatedFragments(a);
		BabelfyParameters bp = new BabelfyParameters();
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);
		bp.setScoredCandidates(ScoredCandidates.ALL);

		Babelfy bfy = new Babelfy(bp);
		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(inputText, Language.EN, constraints);

		// Create printer
		Path out_dir = Paths.get("output");
		Files.createDirectories(out_dir);
		Path out_file = Paths.get(currentPath.toString(), out_dir.toString(), fileName);
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
