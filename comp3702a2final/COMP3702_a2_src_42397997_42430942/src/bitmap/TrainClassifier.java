/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import java.io.*;

/**
 * This program trains a classifier and saves it in a file to be read when used.
 * @author Mikael Boden
 * @version 1.0
 */

public class TrainClassifier {

  public TrainClassifier(String[] args) {
    // create the classifier
       NNClassifier c=new NNClassifier(32, 32);
    // or - for NN2 - replace with the following line of code
    //   NNClassifier2 c=new NNClassifier2(32, 32);
    // or - for ID3 - replace with the following line of code
    //   ID3Classifier c=new ID3Classifier(32, 32);
	// or - for pruned ID3 - replace with the following line of code
	//   PruneID3Classifier c=new PruneID3Classifier(32, 32);
	// or - for Naive Bayes - replace with the following line of code
	//	 NBClassifier c=new NBClassifier();
	// or - for ensemble of classifiers - replace with the following line of code
	//   ClassifierEnsemble c=new ClassifierEnsemble(32, 32);
	// or - for NN2 with validation - replace with the following line of code
	//   NNClassifier2Validation c=new NNClassifier2Validation(32, 32);

    // load data
    try {
      ClassifiedBitmap[] bitmaps=LetterClassifier.loadLetters(args[1]);
      // training with neural network
      c.train(bitmaps,50000,0.1);
      // training with ID3 / classifier ensemble/ naive bayes
      // c.train(bitmaps);
      int trainingErrorCount = 0; // counter for training error
      for (int i=0; i<bitmaps.length; i++) {
        int actual=c.index((Bitmap)bitmaps[i]);
        int target=bitmaps[i].getTarget();
        // if actual output is not same as desired output, increment training error count
        if (actual != target)
        	  trainingErrorCount++;
        System.out.println(i+" \t"+c.getLabel(target)+" \t"+c.getLabel(actual)+" \t"+(target==actual?"YES":"NO"));
      }
      System.out.printf("Training error: %d/%d\n", trainingErrorCount, bitmaps.length);
    } catch (IOException ex) {
      System.err.println("Error loading data.txt: "+ex.getMessage());
    }
    try {
      Classifier.save(c, args[0]);
    } catch (Exception ex) {
      System.err.println("Failed to serialize and save file: "+ex.getMessage());
    }
  }

  public static void main(String[] args) {
    if (args.length!=2) {
      System.err.println("Usage: TrainClassifier <classifier-file> <bitmap-file>");
      System.exit(1);
    }
    new TrainClassifier(args);
    System.out.println("Done.");
  }

}