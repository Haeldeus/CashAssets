package mainwindow.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javafx.application.Platform;
import javafx.concurrent.Task;
import mainwindow.MainWindow;

/**
 * The Task, that will get the Text from the updates.txt File in the repository.
 * @author Haeldeus
 * @version 1.0
 */
public class NewsTask extends Task<Void> {

  /**
   * The MainWindow, this Task was called from.
   */
  private final MainWindow primary;
  
  /**
   * The Constructor for this Task.
   * @param primary The {@link MainWindow}, this Task was called from.
   * @since 1.0
   */
  public NewsTask(MainWindow primary) {
    this.primary = primary;
  }
  
  @Override
  protected Void call() throws Exception {
    try {
      /*
       * Connects to the URL of the Updates File in the Git and creates a BufferedReader to 
       * read from that File.
       */
      URL url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/updates.txt");
  
      URLConnection con = url.openConnection();
      InputStream is = con.getInputStream();
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
  
      /*
       * Sets all needed variables to a default value.
       */
      String line = null;
      
      String s = "";
      
      boolean updates = false;
      
      /*
       * As long as there are lines to read left, these lines will be stored in 'line'.
       */
      while ((line = br.readLine()) != null) {
        /*
         * If the Task was cancelled due to a timeout, this Clause will be true and the 
         * task will end.
         */
        if (isCancelled()) {
          return null;
        }
        
        /*
         * Checks, if the current line contains the given String, that signals the end of the file 
         * itself. In this case, updates is set to false, so no more lines will be added to s.
         */
        if (line.contains("#End Updates File")) {
          updates = false;
        }
        
        /*
         * Checks, if the current line is in the area, where the File itself is stored in the 
         * HTML-Code. If yes, the HTML-Code will be removed, so there is only the line of the File 
         * left, which will be added to s.
         */
        if (updates) {
          /*
           * Removes all HTML related Code.
           */
          while (line.contains(">")) {
            line = line.replaceFirst(line.substring(line.indexOf("<"), 
                line.indexOf(">") + 1), "");
          }
          /*
           * Checks, if there is still some Text left after removing all unnecessary Spaces. 
           * Sometimes, there is no Text left, since that line only contained HTML Code.
           */
          if (line.trim().length() != 0) {
            /*
             * Checks for the "New Line"-Code. If the Line is only this Code, there is a new 
             * Line added to s. Else the current line is added to s with a line Separator at 
             * the End of it.
             */
            if (line.trim().equals("#New Line#")) {
              s = s.concat(line.trim().replace("#New Line#", "") + System.lineSeparator());
            } else {
              s = s.concat(line.trim().replace("&quot;", "\"") + System.lineSeparator());
            }
          }
        }

        /*
         * Checks, if the current line contains the given String, that signals the start of the 
         * file itself. In this case, updates is set to true, so the lines containing updates-Text 
         * will be added to s.
         */
        if (line.contains("#Begin Updates File")) {
          updates = true;
        }
      }
      /*
       * Sets the Result-String to s. This has to be done, since the Result-String has to be final 
       * to be passed to the primary-Window in the Platform.runLater part. Also "returns" the 
       * String to the primary Window, so it can show the Updates-File to the User.
       */
      final String res = s;
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          primary.showUpdate(res);
        }      
      });
    } catch (IOException e) {
      /*
       * In case there was an Error while connecting to the URL, this Part catches the Error and 
       * displays a Message to the User.
       */
      e.printStackTrace();
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          primary.showUpdate("Probleme bei der Abfrage der Änderungen. "
              + "Überprüfen Sie Ihre Internetverbindung.");
        }      
      });
    }
    return null;
  }
}
