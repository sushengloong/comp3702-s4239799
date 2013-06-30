/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package machl;

import java.util.*;
import java.io.*;

public class NB implements Serializable{
	/** training output */
	private String[] targetValues;
	/** the 32 * 32 features of n number of letter*/
	private boolean[][] features;
	/** the total Features count for each letter A-Z*/
	private int[][] featuresOfChar;
	
	/**
	 * A constructor that initialize all attributes.
	 * 
	 * @param features		the 32 * 32 features of n number of letter
	 * @param targetValues	training output
	 * @param numOfClasses	total target classes
	 */
	public NB(boolean[][] features, String[] targetValues, int numOfClasses) {
		this.targetValues = targetValues;
		this.featuresOfChar = new int[numOfClasses][features[0].length + 1];
		this.features = features;
	}
	
	/**
	 * Return the probabilities of all the classes, which used to predict
	 * the result of a test data based on learned dataset.
	 * 
	 * @param feature	32*32 features of a letter
	 * @return			probabilities of all classes
	 */
	public double[] getAllProbabilities(boolean[] feature){
		double[] probabilities = new double[this.featuresOfChar.length];
		//Given X is the features that wanted to examine
		//Calculate probabilities of all classes
		for(int i = 0; i < featuresOfChar.length; i++){
			// P(C) = count(C) / count(all)
			probabilities[i] = (double)(featuresOfChar[i][0]) / targetValues.length;		
			for(int f = 0; f < feature.length; f++){
				if(feature[f])
					//P(pixel = true | X = true) =  count(pixel = true ^ C) / count(C)  
					probabilities[i] *= (double)(featuresOfChar[i][f+1]) / featuresOfChar[i][0];
				else
					//P(pixel = false | X = false) =  count(pixel = false ^ C) / count(C)
					probabilities[i] *= (double)(featuresOfChar[i][0]-featuresOfChar[i][f+1]) 
											/ featuresOfChar[i][0];
			}
		}
		return probabilities;
	}
	/**
	 * The machine learning method that learn based on training set, which is
	 * targetValues in this case.
	 */
	public void train() {
		for (int i = 0; i < targetValues.length; i++) {
			int index = targetValues[i].charAt(0) - 'A';
			// increment the character's count
			featuresOfChar[index][0]++;
			for (int f = 0; f < features[i].length; f++){
				if(features[i][f])
					//let (f+1) = r * 32 + c, increment if true value is found
					featuresOfChar[index][f+1]++;
			}
		}
	}
}
