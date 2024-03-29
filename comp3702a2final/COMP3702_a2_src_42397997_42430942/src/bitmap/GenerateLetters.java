/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package bitmap;

import java.awt.*;
import java.io.*;

/**
 * <p>An application for generating handwritten letter/character data.</p>
 * @author Mikael Bod�n
 * @version 1.0
 */

public class GenerateLetters {

  public GenerateLetters(String[] args) {
    GenBitmapFrame frame = new GenBitmapFrame();
    frame.validate();
    // Center the window
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize=frame.getSize();
    if (frameSize.height>screenSize.height) {
      frameSize.height=screenSize.height;
    }
    if (frameSize.width>screenSize.width) {
      frameSize.width=screenSize.width;
    }
    frame.setLocation((screenSize.width-frameSize.width)/2, (screenSize.height-frameSize.height)/2);
    frame.setVisible(true);
  }

  /**
   * The main application starts here.
   */
  public static void main(String[] args) {
    new GenerateLetters(args);
  }
}