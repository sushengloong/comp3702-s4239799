/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 *
 * Packages cern.colt* , cern.jet*, cern.clhep
 *
 * Copyright (c) 1999 CERN - European Organization for Nuclear Research.
 */
package machl;

import cern.jet.stat.Probability;

public class PruneBinID3 extends BinID3{
	
	public PruneBinID3(String[] label, boolean[][] input, String[] output, String[] classes){
		super(label, input, output, classes);
	}
	
	public BinTree induceTree(int[] partition, int[] features) {
	    // if the partition is empty, we can not return a tree
	    if (partition.length==0) {
	      return null;
	    }
	    // check if all entries in partition belong to the same class. If so, return node, labeled with class value
	    // you may want to check if pruning is applicable here (and then just return the majority class).
	    int[] classCnt=new int[classes.length];
	    String sameValue=null;
	    boolean sameClass=true;
	    for (int p=0; p<partition.length; p++) {
	      String targetValue=output[partition[p]];
	      for (int n=0; n<classes.length; n++) {
	        if (targetValue.equals(classes[n])) {
	          classCnt[n]++;
	          break;
	        }
	      }
	      if (p==0)
	        sameValue=targetValue;
	      else {
	        if (!sameValue.equalsIgnoreCase(targetValue)) {
	          sameClass=false;
	          break;
	        }
	      }
	    }
	    if (sameClass)  {
	      return new BinTree(sameValue);
	    } else {
	      int max=0;
	      for (int n=1; n<classes.length; n++)
	        if (classCnt[max]<classCnt[n])
	          max=n;
	      if ((double)classCnt[max]/(double)partition.length>0.50 || partition.length<5) { // if more than 50% of samples in partition are of the same class OR fewer than 5 samples
	        System.out.print(".");
	        return new BinTree(classes[max]);
	      }
	    }

	    // if no features are available, we can not return a tree
	    if (features != null && features.length==0) {
	      return null;
	    }

	    // class values are not equal so we select a particular feature to split the partition
	    int selectedFeature=selectFeature(partition, features);

	    // create new partition of samples
	    // use only corresponding subset of full partition
	    int[] partTrue=matches(partition, selectedFeature, true);
	    int[] partFalse=matches(partition, selectedFeature, false);
	    // remove the feature from the new set (to be sent to subtrees)
	    	
	    int[] nextFeatures = features; 
		while (true){
			
			nextFeatures = new int[nextFeatures.length-1];
		    int cnt=0;
		    for (int f=0; f<features.length; f++) {
		      if (features[f] != selectedFeature)
		        nextFeatures[cnt++]= features[f];
		    }
		    
		    if (nextFeatures != null && nextFeatures.length==0) {
		        return null;
		    }
			int pos = partTrue.length;
			int neg = partFalse.length;

			int leftFeature = selectFeature(partTrue, nextFeatures);
			int rightFeature = selectFeature(partFalse, nextFeatures);
			
			int leftPos = matches(partTrue, leftFeature, true).length;
			int leftNeg = matches(partTrue, leftFeature, false).length;
			int rightPos = matches(partFalse, rightFeature,true).length;
			int rightNeg = matches(partFalse, rightFeature,false).length;
			
			double leftExpectedPos = pos * (leftPos + leftNeg)/(pos + neg);
			double leftExpectedNeg = neg * (leftPos + leftNeg)/(pos + neg);
			double rightExpectedPos = pos * (rightPos + rightNeg)/(pos + neg);
			double rightExpectedNeg = neg * (rightPos + rightNeg)/(pos + neg);

			double delta = 0;
			if (leftExpectedPos > 0)
				delta += Math.pow((leftPos - leftExpectedPos),2)/leftExpectedPos;
			if (leftExpectedNeg > 0)
				delta += Math.pow((leftNeg - leftExpectedNeg),2)/leftExpectedNeg;
			if (rightExpectedPos > 0)
				delta += Math.pow((rightPos - rightExpectedPos),2)/rightExpectedPos;
			if(rightExpectedNeg > 0)
				delta += Math.pow((rightNeg - rightExpectedNeg),2)/rightExpectedNeg;
			if (Probability.chiSquareComplemented(1, delta) <= 0.05)
				break;
			
			selectedFeature = selectFeature(partition, nextFeatures);
		    partTrue = matches(partition, selectedFeature, true);
		    partFalse = matches(partition, selectedFeature, false);
		    features = nextFeatures;
		}
	    // construct the subtrees using the new partitions and reduced set of features
	    BinTree branchTrue=induceTree(partTrue, nextFeatures);
	    BinTree branchFalse=induceTree(partFalse, nextFeatures);

	    // if either of the subtrees failed, we have confronted a problem, use the most likely class value of the current partition
	    BinTree defaultTree=null;
	    if (branchTrue==null || branchFalse==null) {
	      // indicate a majority vote
	      int[] freq=new int[classes.length];
	      int most=0;
	      for (int c=0; c<classes.length; c++) {
	        int[] pos = matches(partition, classes[c]);
	        freq[c] = pos.length;
	        if (freq[c]>=freq[most])
	          most=c;
	      }
	      // the majority class value can replace any null trees...
	      defaultTree=new BinTree(classes[most]);
	      if (branchTrue==null && branchFalse==null)
	        return defaultTree;
	      else // return the unlabeled node with subtrees attached
	        return new BinTree(labelFeature(selectedFeature), (branchTrue==null?defaultTree:branchTrue), (branchFalse==null?defaultTree:branchFalse));
	    } else { // if both subtrees were successfully created we can either
	      if (branchTrue.classValue != null && branchFalse.classValue != null) {
	        if (branchTrue.classValue.equals(branchFalse.classValue)) {
	          // return the the current node with the classlabel common to both subtrees, or
	          return new BinTree(branchTrue.classValue);
	        }
	      }
	      // return the unlabeled node with subtrees attached
	      return new BinTree(labelFeature(selectedFeature), branchTrue, branchFalse);
	    }
	  }
}
