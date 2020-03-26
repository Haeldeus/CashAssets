package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
   * @since 1.0
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
      /*
       * If this happens, then there is no data Folder or Settings File. In this case, the 
       * default value false will be returned.
       */
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
  
  /**
   * Updates the File at data/staff.stg to set it's content to the given ArrayList of Strings, that 
   * contains the new List of Staff Members.
   * @param staff The new List of Staff Members as an ArrayList of Strings.
   * @since 1.0
   */
  public static void setStaffMembers(ArrayList<String> staff) {
    staff.sort(null);
    Path path = Paths.get("data" + File.separator + "Staff.stg");
    try {
      PrintWriter writer = new PrintWriter(path.toString(), "UTF-8");
      for (String s : staff) {
        writer.println(s);
      }
      writer.close();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Returns all Staff Members, located in the File at data/Staff.stg.
   * @return  All Staff Members, that are saved in Staff.stg as an ArrayList of Strings.
   * @since 1.0
   */
  public static ArrayList<String> getStaffMembers() {
    /*
     * Creates a new, empty ArrayList, which will be filled and returned.
     */
    ArrayList<String> res = new ArrayList<String>();
    
    /*
     * The Path to the Staff-File.
     */
    Path path = Paths.get("data" + File.separator + "Staff.stg");
    try {
      /*
       * Creates a BufferedReader with the given FileReader to read the Settings File.
       */
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(path.toString()), "UTF8"));
      
      /*
       * Reads all Lines of the File. Each of these represent a Staff Member.
       */
      String s = br.readLine();
      while (s != null) {
        res.add(s);
        s = br.readLine();
      }
      /*
       * Closes the Reader to prevent leakage.
       */
      br.close();
      /*
       * Returns if the second token is 1, which means the Folder should be opened.
       */
    } catch (FileNotFoundException e) {
      /*
       * In case there is no Staff Members File. In this case the empty ArrayList will be 
       * returned so the User can add the Staff Members to the List.
       */
      return res;
    } catch (IOException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    }
    return res;
  }
  
  /**
   * Returns the Control Style, that should be used.
   * @return  The Control Style, as a String.
   * @since 1.0
   */
  public static String getControlStyle() {
    if (checkNightmode()) {
      return "nightControlStyle1.css";
    } else {
      return "controlStyle1.css";
    }
  }
}
