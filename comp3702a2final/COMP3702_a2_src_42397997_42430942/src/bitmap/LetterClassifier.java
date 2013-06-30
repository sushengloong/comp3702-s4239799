/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import java.io.*;
import java.util.*;

/**
 * <p>This class extends Classifier and provides some functionality specific to letter recognition.</p>
 * @author Mikael Boden
 * @version 1.0
 */

public class LetterClassifier extends Classifier {

  private static String name = "Letter Classifier";
  private static int nClasses=('Z'-'A')+1;

  /**
   * Identifies the classifier, e.g. by the name of the author/contender
   * @return the identifier
   */
  public String getName() {
    return name;
  }

  /**
   * Determines the number of possible classes that this classifier discriminates between.
   * @return the number of classes
   */
  public static int getClassCount() {
    return nClasses;
  }

  /**
   * Determine the name of the class specified by index (0..getClassCount)
   * @param index the index number of the class
   * @return the label/name of the specified class
   */
  public String getLabel(int index) {
    Character letter=new Character((char)('A'+index));
    return letter.toString();
  }

  /**
   * Helper method for loading a text-file with classified bitmaps (each representing a letter).
   * It ignores rows with format problems.
   * @param filename the filename of the text-file that holds the classified bitmaps
   * @return an array of classified bitmaps
   * @throws IOException if the file operation fails
   */
  public static ClassifiedBitmap[] loadLetters(String filename) throws IOException {
    Vector bmaps=new Vector();
    BufferedReader reader=new BufferedReader(new FileReader(filename));
    String line=reader.readLine();
    while (line!=null) {
      ClassifiedBitmap bmap=null;
      try {
        bmap=new ClassifiedBitmap(line);
        bmaps.add(bmap);
      } catch (RuntimeException ex) {
        ; // the line does not conform to the Bitmap format or does not specify the target class correctly
      }
      line=reader.readLine();
    }
    ClassifiedBitmap[] bitmaps=new ClassifiedBitmap[bmaps.size()];
    Iterator iter=bmaps.iterator();
    for (int i=0; iter.hasNext(); i++)
      bitmaps[i]=(ClassifiedBitmap)iter.next();
    return bitmaps;
  }
  
  public boolean[] centralize(boolean[] feature){
	  int charStart = 31, charEnd = 0;
	  
	  for (int r = 0; r < 32; r++) {
		  boolean end = true;
	  	  for(int c = 0; c < 32; c++) {
	  		  if (feature[r*32+c]) {
	  			  if(r <= charStart)
	  				  charStart = r;
	  			  end = false;
	  		  }
	  	  }
	  	  if(end)
	  		  charEnd = r;
	  }
	  int charCenter = (charStart + charEnd) / 2;
	  //shift left
	  if (charCenter > 15){
		  for(int x = (15 - (charCenter - charStart)); charStart < charEnd; x++, charStart++) {
			  for( int c = 0; c < 32; c++){
				  feature[x * 32 + c] = feature[charStart * 32 + c];
				  if (feature[x * 32 + c] != feature[charStart * 32 + c])
					  feature[charStart * 32 + c] = false;
			  }
		  }
	  } else { //shift right
		  for(int x = (15 + (charEnd - charCenter)); charEnd > charStart; x--, charEnd--) {
			  for( int c = 0; c < 32; c++){
				  feature[x * 32 + c] = feature[charEnd * 32 + c];
				  if (feature[x * 32 + c] != feature[charEnd * 32 + c])
					  feature[charEnd * 32 + c] = false;
			  }
		  }
	  }
	  return feature;
  }
}