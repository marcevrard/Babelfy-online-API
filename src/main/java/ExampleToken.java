import java.util.List;
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

public class ExampleToken {
	public static void main(String[] args) throws Exception {
		String inputText = "Life is life";
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
		PrintWriter writer = new PrintWriter("output/output.txt", "UTF-8");

		// bfyAnnotations is the result of Babelfy.babelfy() call
		for(SemanticAnnotation annotation:bfyAnnotations) {
			// splitting the input text using the CharOffsetFragment start and end anchors
			String frag = inputText.substring(annotation.getCharOffsetFragment().getStart(),
					annotation.getCharOffsetFragment().getEnd() + 1);
			String output = annotation.getBabelSynsetID() + "\t"
							+ annotation.getSource() + "\t" + frag;
			System.out.println(output);
			// Print to file
			writer.println(output);
		}
		writer.close();
	}
}

// public class Example {
// public static void main(String[] args) throws Exception {
// Babelfy bfy = Babelfy.getInstance(AccessType.ONLINE);
// BabelNet bn = BabelNet.getInstance();
// String word = " ";
// String inputText = "He has a passion for music";
// Annotation annotations = bfy.babelfy(" ",
// inputText,
// BabelfyParameters.MatchingType.EXACT_MATCHING,
// Language.EN);
// System.out.println("inputText: " + inputText + "\nannotations:");
// for (BabelSynsetAnchor annotation : annotations.getAnnotations()) {
// word = annotation.getBabelSynset().getId();
// System.out.println(annotation.getAnchorText() + "\t" + word + "\t" +
// annotation.getBabelSynset());
// // BabelSynset by = bn.getSynsetFromId(("bn:03083790n"));
// for (BabelSense sense : bn.getSynsetFromId((word))) {
// if (sense.getSource().toString().equals("WN"))
// System.out.println("Sense: " + sense.getLemma() + "\tSource: " +
// sense.getSource().toString());
// }
// }
//
// }
// }
