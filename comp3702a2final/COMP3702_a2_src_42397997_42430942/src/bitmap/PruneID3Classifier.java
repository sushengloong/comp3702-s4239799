/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 *
 * Packages cern.colt* , cern.jet*, cern.clhep
 *
 * Copyright (c) 1999 CERN - European Organization for Nuclear Research.
 */
package bitmap;

import machl.*;

public class PruneID3Classifier extends ID3Classifier {
	private static String name = "Prune ID3 Classifier";
	private PruneBinID3 pID3 = null;
	
	public PruneID3Classifier(int nRow, int nCol) {
		super(nRow, nCol);
	}

	  /**
	   * Trains the ID3 classifier on provided samples.
	   * @param maps the bitmaps which are used as training inputs
	   */
	  public void train(ClassifiedBitmap[] maps) {
	    features=new boolean[maps.length][];
	    targetValues=new String[maps.length];
	    for (int p=0; p<maps.length; p++) {
	      features[p]=((Bitmap)maps[p]).toBooleanArray();
	      features[p] = centralize(features[p]);
	      targetValues[p]=getLabel(maps[p].getTarget());
	    }
	    pID3 = new machl.PruneBinID3(labels, features, targetValues, classValues);
	    tree = pID3.induce();
	  }
	
	public String getName(){
		return name;
	}
}
