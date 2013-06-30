/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import machl.*;

/**
 * <p>A neural network handwritten letter recognizer. Use as an example for writing LetterClassifiers.</p>
 * @author Mikael Boden
 * @version 1.0
 */

public class NNClassifier2Validation extends LetterClassifier {

  private static String name="NN Classifier 2";
  private NN2 nn=null;
  private Random rand;
  private double[][] targets=null; // target vectors;

  /**
   * Identifies the classifier, e.g. by the name of the author/contender, or by whatever you want to
   * identify this instance when loaded elsewhere.
   * @return the identifier
   */
  public String getName() {
    return name;
  }

  /**
   * Classifies the bitmap
   * @param map the bitmap to classify
   * @return the probabilities of all the classes (should add up to 1).
   */
  public double[] test(Bitmap map) {
    double[] out=nn.feedforward(map.toDoubleArray());
    return out;
  }

  /**
   * Trains the neural network classifier on randomly picked samples from specified training data.
   * @param maps the bitmaps which are used as training inputs including targets
   * @param nPresentations the number of samples to present
   * @param eta the learning rate
   */
  public void train(ClassifiedBitmap[] maps, int nPresentations, double eta) {
	int q = maps.length/2;
	int r = maps.length%2;
	int count=0;
	ClassifiedBitmap[] trainingSets = new ClassifiedBitmap[q+r];
	for (int i=0; i<trainingSets.length; i++)
		trainingSets[i] = maps[count++];
	ClassifiedBitmap[] validationSets = new ClassifiedBitmap[q];
	for (int i=0; i<validationSets.length; i++)
		validationSets[i] = maps[count++];
	double trainingError = 0.0;
	double validationError = 0.0;
    for (int p=0; p<nPresentations; p++) {
      int sample=rand.nextInt(trainingSets.length);
      trainingError += nn.train(((Bitmap)trainingSets[sample]).toDoubleArray(), 
    		  targets[trainingSets[sample].getTarget()], eta);
      int vrand=rand.nextInt(validationSets.length);
      int actual=index((Bitmap)validationSets[vrand]);
      int target=validationSets[vrand].getTarget();
      validationError += Math.sqrt(Math.abs(actual-target)/26.0);
      System.out.println("Number of iteration: "+p);
      if (validationError == trainingError) 
    	  break;
    }
  }

  /**
   * Construct a neural network classifier for bitmaps of specified size.
   * @param nRows number of rows in the bitmap
   * @param nCols number of columns in the bitmap
   */
  public NNClassifier2Validation(int nRows, int nCols) {
    rand=new Random(System.currentTimeMillis());
    // Use NN2 object which requires 4 parameters - 
    //     number of inputs, number of classifications, 
    //     number of hidden units and seed.
    nn=new NN2(nRows*nCols, getClassCount(), 30, rand.nextInt());
    targets=new double[getClassCount()][getClassCount()];
    for (int c=0; c<getClassCount(); c++)
      targets[c][c]=1;
  }

  /**
   * This method accepts the initial training dataset and 
   * fold it into 5 subsets and then return the list.
   * 
   * @param maps the dataset for training initially
   * @return a list of lists of classified bitmaps
   
  public List<List<ClassifiedBitmap>> fold(ClassifiedBitmap[] maps) {
      List<List<ClassifiedBitmap>> bList = new ArrayList<List<ClassifiedBitmap>>();
      for (int i=0; i<5;i++)
    	  bList.add(new ArrayList<ClassifiedBitmap>());
      for (int i=0; i<maps.length; i++)
    	  bList.get(i%5).add(maps[i]);
      return bList;
  }
  */
  
}