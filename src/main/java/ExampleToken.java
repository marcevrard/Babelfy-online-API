import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
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

		File file = new File("input/input_tokens.tsv");
		Scanner scanner = new Scanner(file);
		List<BabelfyToken> tokenizedInput = new ArrayList<>();

		Map<String, PosTag> posDic = new HashMap<String, PosTag>();
		posDic.put("n", PosTag.NOUN);
		posDic.put("v", PosTag.VERB);
		posDic.put("j", PosTag.ADJECTIVE);
		posDic.put("r", PosTag.ADVERB);

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
		File dir = new File("output");
		dir.mkdir();
		PrintWriter writer = new PrintWriter("output/output_tokens.tsv", "UTF-8");

		// bfyAnnotations is the result of Babelfy.babelfy() call
		for(SemanticAnnotation annotation:bfyAnnotations) {
			// splitting the input text using the CharOffsetFragment start and end anchors
			String output = annotation.getBabelSynsetID() + "\t"
							+ annotation.getSource() + "\t"
							+ annotation.getDBpediaURL();
			System.out.println(output);
			// Print to file
			writer.println(output);
		}
		writer.close();
	}
}
