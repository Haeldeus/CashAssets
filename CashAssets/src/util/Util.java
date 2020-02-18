package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * A Utility Class to provide Methods used in multiple packages and Objects. This 
 * way, the Methods don't have to be implemented multiple times nor a reference has to 
 * be passed to every Object.
 * @author Haeldeus
 * @version 1.0
 */
public class Util {

  /**
   * Checks, if the Nightmode should be used.
   * @return  {@code true}, if the Nightmode should be used, {@code false} if not.
   */
  public static boolean checkNightmode() {
    Path path = Paths.get("data/Settings.stg");
    FileReader fr;
    try {
      fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      br.readLine();
      br.readLine();
      String s = br.readLine();
      StringTokenizer st = new StringTokenizer(s, "=");
      st.nextToken();
      br.close();
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      /*
       * This happens, if there is no String to be tokenized by st. In this case there 
       * is no Setting for this and false will be returned by default.
       */
    }
    return false;
  }
}
