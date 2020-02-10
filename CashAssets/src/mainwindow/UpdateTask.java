package mainwindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

public class UpdateTask extends Task<Void>{

  private final ProgressBar bar;
  
  private final MainWindow primary;
  
  private final Label response;
  
  /**
   * @param bar
   * @param primary
   * @param response
   */
  public UpdateTask(ProgressBar bar, MainWindow primary, Label response) {
    this.bar = bar;
    this.primary = primary;
    this.response = response;
  }
  
  @Override
  protected Void call() throws Exception {
    try {
      // Make a URL to the web page
      URL url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/version.txt");
  
      // Get the input stream through URL Connection
      URLConnection con = url.openConnection();
      InputStream is = con.getInputStream();
      
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
  
      String line = null;
      
      int i = 0;
      int max = 10000;
      
      String s = "";
      boolean version = false;
      // read each line and write to System.out
      while ((line = br.readLine()) != null) {
        if (line.contains("#End Version File")) {
          version = false;
        }
        
        if (version) {
          while (line.contains(">")) {
            line = line.replaceFirst(line.substring(line.indexOf("<"), 
                line.indexOf(">") + 1), "");
          }
          if (line.trim().length() != 0) {
            s = s.concat(line.trim() + System.lineSeparator());
          }
        }
        if (line.contains("#Begin Version File")) {
          version = true;
        }
        i++;
        updateProgress(i, max);
      }
      StringTokenizer st = new StringTokenizer(s, System.lineSeparator());
      double newest = 0.0;
      ArrayList<Double> older = new ArrayList<Double>();
      String str = st.nextToken();
      if (str.equals("Current")) {
        newest = Double.parseDouble(st.nextToken());
      }
      while (st.hasMoreTokens()) {
        str = st.nextToken();
        if (str.equals("Old")) {
          older.add(Double.parseDouble(st.nextToken()));
        } else {
          try {
            older.add(Double.parseDouble(str));
          } catch (Exception e) {
            System.err.println("Error while parsing a double. Skipping this entry...");
          }
        }
      }
      if (primary.getVersion() == newest) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            primary.setResponseText("Aktuellste Version vorhanden"); 
          }
        });
      } else if (older.contains(primary.getVersion())) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            primary.setResponseText("Update verfügbar. Klicken Sie hier zum Download");
            primary.addResponseHandler(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent arg0) {
                try {
                  java.awt.Desktop.getDesktop().browse(new URI("https://github.com/Haeldeus/CashAssets/releases"));
                } catch (IOException e) {
                  Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                      primary.setResponseText("Fehler beim Öffnen der Seite");
                    }
                  });
                } catch (URISyntaxException e) {
                  Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                      primary.setResponseText("Fehler beim Öffnen der Seite");
                    }                   
                  });
                }
              }            
            });
          }
        });
      } else {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            primary.setResponseText("Fehler beim Update"); 
          }
        });
      }
    } catch (IOException e) {
      e.printStackTrace();
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          primary.setResponseText("Fehler beim Update. Prüfen Sie ihre Internetverbindung");
        }      
      });
    }
    bar.setVisible(false);
    return null;
  }
}
