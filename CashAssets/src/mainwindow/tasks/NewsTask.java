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
      URL url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/updates.txt");
  
      URLConnection con = url.openConnection();
      InputStream is = con.getInputStream();
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
  
      String line = null;
      
      String s = "";
      
      boolean updates = false;
      
      while ((line = br.readLine()) != null) {
        if (line.contains("#End Updates File")) {
          updates = false;
        }
        
        if (updates) {
          while (line.contains(">")) {
            line = line.replaceFirst(line.substring(line.indexOf("<"), 
                line.indexOf(">") + 1), "");
          }
          if (line.trim().length() != 0) {
            if (line.trim().equals("#New Line#")) {
              s = s.concat(line.trim().replace("#New Line#", "") + System.lineSeparator());
            } else {
              s = s.concat(line.trim() + System.lineSeparator());
            }
          }
        }

        if (line.contains("#Begin Updates File")) {
          updates = true;
        }
      }
      final String res = s;
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          primary.showUpdate(res);
        }      
      });
    } catch (IOException e) {
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
