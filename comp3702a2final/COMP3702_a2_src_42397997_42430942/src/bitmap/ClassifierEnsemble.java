/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import java.util.Hashtable;

/**
 * This class is an ensemble of multiple classifiers of different
 * machine learning techniques for letter recognition. 
 * 
 * @author SU Sheng Loong & TEE Lip Jian
 */
public class ClassifierEnsemble extends LetterClassifier {
	private static String name="Classifier Ensemble";
	private LetterClassifier[] cList;
	private double[] bitmapWeights;
	private Hashtable<LetterClassifier, Double> classifierWeights;

	/**
	   * Construct a classifier ensemble for bitmaps of specified size.
	   * 
	   * @param nRows number of rows in the bitmap
	   * @param nCols number of columns in the bitmap
	   */
	public ClassifierEnsemble(int nRows, int nCols) {
		setClassifierList(new LetterClassifier[4]);
		cList[0] = new ID3Classifier(nRows, nCols);
		cList[1] = new NNClassifier(nRows, nCols);
		cList[2] = new NNClassifier2(nRows, nCols);
		cList[3] = new NBClassifier();
	}

	/**
	 * Identifies the classifier, e.g. by the name of the author/contender
	 * @return the identifier
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the list of all classifiers embodied
	 * @return the classifier list
	 */
	public LetterClassifier[] getClassifierList() {
		return cList;
	}

	/**
	 * Setter method for the list of all classifiers embodied
	 * @param cList a list of LetterClassifier objects
	 */
	public void setClassifierList(LetterClassifier[] cList) {
		this.cList = cList;
	}

	/**
	 * Classifies the bitmap
	 * @param map the bitmap to classify
	 * @return the probabilities of all the classes (should add up to 1).
	 */
	public double[] test(Bitmap map) {
		return weightedMajority(map);
	}

	/**
	 * Trains the ClassifierEmsemble classifier on provided samples.
	 * @param maps the bitmaps which are used as training inputs
	 */
	public void train(ClassifiedBitmap[] maps) {
		initialiseBitmapWeights(maps.length);
		initialiseClassifierWeights();
		for (LetterClassifier c: cList) {
			if (c instanceof ID3Classifier) {
				System.out.println("ID3Classifer starts training...");
				((ID3Classifier)cList[0]).train(maps);
				System.out.println("ID3Classifer finished training...");
			}
			else if (c instanceof NNClassifier) {
				System.out.println("NNClassifer starts training...");
				((NNClassifier)cList[1]).train(maps, 50000, 0.1);
				System.out.println("NNClassifer finished training...");
			}
			else if (c instanceof NNClassifier2) {
				System.out.println("NN2Classifer starts training...");
				((NNClassifier2)cList[2]).train(maps, 50000, 0.1);
				System.out.println("NN2Classifer finished training...");
			}
			else if (c instanceof NBClassifier) {
				System.out.println("NBClassifer starts training...");
				((NBClassifier)cList[3]).train(maps);
				System.out.println("NBClassifer finished training...");
			}
			else
				throw new RuntimeException(
				"Classfier type not supported!");
			double error = calcError(maps, c);
			if (error < 0.0001)
				break;
			updateBitmapWeights(maps, c, error);
			double newClassifierWeight = classifierWeights.get(c)
				* Math.log((1.0 - error) / error);
			classifierWeights.put(c, newClassifierWeight);
		}
		System.out.println("Train completed");
	}
	
	/**
	 * Initialise all bitmap weights 
	 * @param size number of bitmaps for training
	 */
	public void initialiseBitmapWeights(int size) {
		double value = 1.0 / (1.0 * size);
		bitmapWeights = new double[size];
		for (int i = 0; i < size; i++)
			bitmapWeights[i] = value;
	}
	
	/**
	 * Initialise all classifier weights
	 */
	public void initialiseClassifierWeights() {
		classifierWeights = new Hashtable<LetterClassifier, Double>();
		for (LetterClassifier c: cList)
			classifierWeights.put(c, 1.0);
	}

	/**
	 * Calculate error made by the classifier when trained on
	 * the bitmaps.
	 * 
	 * @param maps dataset for training
	 * @param c the letter classifier to be trained
	 * @return the error calculated
	 */
	public double calcError(ClassifiedBitmap[] maps, LetterClassifier c) {
		double error = 0.0;
		for (int i = 0; i < maps.length; i++) {
			int actual=c.index((Bitmap)maps[i]);
			int target=maps[i].getTarget();
			// if actual output is not same as desired output, 
			// 		increment error by the example weight
			if (actual != target)
				error = error + bitmapWeights[i] * 1;
		}
		return error;
	}
	
	/**
	 * Update the bitmap weights after training based
	 * on the error calculated
	 * 
	 * @param maps the bitmaps for training
	 * @param c the trained classifier
	 * @param error the error calculated during training
	 */
	private void updateBitmapWeights(ClassifiedBitmap[] maps, 
			LetterClassifier c, double error) {
		double epsilon = error / (1.0 - error);
		for (int i = 0; i < maps.length; i++) {
			int actual=c.index((Bitmap)maps[i]);
			int target=maps[i].getTarget();
			// if actual output is not same as desired output, 
			// 		increment weight by epsilon
			if (actual != target)
				bitmapWeights[i] = bitmapWeights[i] * epsilon;
		}
		bitmapWeights = normalise(bitmapWeights);
	}
	
	/**
	 * Normalise the value in an array of double variables.
	 * 
	 * @param weights an array of weights
	 * @return the normalised array of weights
	 */
	public double[] normalise(double[] weights) {
		double total = 0.0;
		for (double w : weights)
			total = total + w;
		double[] normalised = new double[weights.length];
		if (total != 0) 
			for (int i = 0; i < weights.length; i++) 
				normalised[i] = weights[i] / total;
		double totalN = 0.0;
		for (double d : normalised)
			totalN = totalN + d;
		return normalised;
	}
	
	/**
	 * Calculate the probability for each of the classifications
	 * by using the weighted majority algorithm. 
	 *
	 * @param map the bitmap for testing
	 * @return the probability for each of the classifications
	 */
	public double[] weightedMajority(Bitmap map) {
		double[][] table=new double[cList.length][getClassCount()];
		for (int i=0; i<cList.length; i++) {
			int predicted=cList[i].index(map);
			table[i][predicted] += classifierWeights.get(cList[i]);
		}
		double[] out = new double[getClassCount()];
		for (int i=0; i<getClassCount(); i++)
			out[i] = scoreOfClass(i, table);
		return out;
	}
	
	/**
	 * Calculate the score for each classification. 
	 * 
	 * @param classification a particular classification
	 * @param table holds the score for each classification
	 * @return score of the classification
	 */
	public double scoreOfClass(int classification, double[][] table) {
		double score = 0.0;
		for (int i = 0; i < table.length; i++)
			score += table[i][classification];
		return score;
	}
	
}
