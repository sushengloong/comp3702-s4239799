/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import machl.*;

public class NBClassifier extends LetterClassifier{
	private static String name = "Naive Bayes Classifier";
	
	/** Naive Bayes learning*/
	private NB nb = null;
	/** training output */
	private String[] targetValues = null;
	/** the 32 * 32 features of n number of letter*/
	private boolean[][] features = null;
	
	/**
	 * Test the test data, and return the probabilities of all possible results
	 * 
	 * @param map	test set
	 * @return		probabilities of all possible results
	 */
	public double[] test(Bitmap map) {
		//boolean[] feature = centralize(map.toBooleanArray());
		return nb.getAllProbabilities(map.toBooleanArray());
	}
	
	/**
	 * Train the machine with given training set.
	 * @param maps	training set
	 */
	public void train(ClassifiedBitmap[] maps) {
		features = new boolean[maps.length][];
	    targetValues = new String[maps.length];
	    for (int p=0; p<maps.length; p++) {
	      features[p]=((Bitmap)maps[p]).toBooleanArray();
	      features[p] = centralize(features[p]);
	      targetValues[p]=getLabel(maps[p].getTarget());
	    }
	    nb = new NB(features, targetValues, getClassCount());
	    nb.train();
	}
	
	/**
	 * Get the name of this classifier
	 * @return name of the classifier
	 */
	public String getName() {
		return name;
	}

}
