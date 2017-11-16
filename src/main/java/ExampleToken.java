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
import it.uniroma1.lcl.babelfy.commons.BabelfyConstraints;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.SemanticAnnotationResource;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.ScoredCandidates;
import it.uniroma1.lcl.babelfy.commons.BabelfyParameters.MCS;
import it.uniroma1.lcl.babelfy.commons.PosTag;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation.Source;
import it.uniroma1.lcl.babelfy.commons.annotation.TokenOffsetFragment;
import it.uniroma1.lcl.jlt.util.Language;


public class ExampleToken {

	public static void main(String[] args) throws Exception {

		BabelfyConstraints constraints = new BabelfyConstraints();
		SemanticAnnotation a = new SemanticAnnotation(new TokenOffsetFragment(0, 0), "bn:03083790n",
				"http://dbpedia.org/resource/BabelNet", Source.OTHER);

		constraints.addAnnotatedFragments(a);
		BabelfyParameters bp = new BabelfyParameters();
		bp.setAnnotationResource(SemanticAnnotationResource.BN);
		bp.setMCS(MCS.ON_WITH_STOPWORDS);
		bp.setScoredCandidates(ScoredCandidates.ALL);

		Path currentPath = Paths.get(System.getProperty("user.dir"));
		String fileName = "tokens_13.tsv";
		Path file = Paths.get(currentPath.toString(), "input", fileName);
		Scanner scanner = new Scanner(file);
		List<BabelfyToken> tokenizedInput = new ArrayList<>();

		Map<String, PosTag> posDic = new HashMap<String, PosTag>();
		posDic.put("n", PosTag.NOUN);
		posDic.put("v", PosTag.VERB);
		posDic.put("j", PosTag.ADJECTIVE);
		posDic.put("r", PosTag.ADVERB);
		posDic.put("x", PosTag.OTHER);

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

		Babelfy bfy = new Babelfy(bp);
		List<SemanticAnnotation> bfyAnnotations = bfy.babelfy(tokenizedInput, Language.EN, constraints);

		// Create printer
		Path out_dir = Paths.get("output");
		Files.createDirectories(out_dir);
		Path out_file = Paths.get(currentPath.toString(), out_dir.toString(), fileName);
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
