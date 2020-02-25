package mainwindow.tasks;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import mainwindow.MainWindow;

/**
 * The Objects of this Class are used to check for possible Updates.
 * @author Haeldeus
 * @version 1.0
 */
public class UpdateTask extends Task<Void> {

  /**
   * The {@link ProgressBar}, that will display the Progress of this Task to the User.
   */
  private final ProgressBar bar;
  
  /**
   * The {@link MainWindow}, this Task was called from. This Field is used to update the MainWindow.
   */
  private final MainWindow primary;

  /**
   * Creates a new UpdateTask with the given ProgressBar and MainWindow as fields.
   * @param bar The {@link ProgressBar}, that will display the Progress of this Task to the User.
   * @param primary The {@link MainWindow}, this Task was called from.
   * @since 1.0
   */
  public UpdateTask(ProgressBar bar, MainWindow primary) {
    this.bar = bar;
    this.primary = primary;
  }
  
  @Override
  protected Void call() throws Exception {
    try {
      // Make a URL to the web page
      URL url;
      url = new URL("https://github.com/Haeldeus/CashAssets/blob/master/version.txt");
      // Get the input stream through URL Connection
      URLConnection con = url.openConnection();
      con.connect();
      InputStream is = con.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = null;
      
      /*
       * Used to update the ProgressBar. max is set to a randomly high number.
       */
      int i = 0;
      int max = 10000;
      
      /*
       * Creates an empty String to store version numbers in.
       */
      String s = "";
      
      /*
       * A boolean to determine, if the current part of the InputStream contains 
       * information about the Version.
       */
      boolean version = false;
      
      /*
       * Go through each line of the InputStream to search for version information.
       */
      while ((line = br.readLine()) != null) {
        if (isCancelled()) {
          bar.setVisible(false);
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              primary.setResponseText("Zeitüberschreitung");
              primary.removeResponseHandler();
            }           
          });
          return null;
        }
        /*
         * This String is always at the end of the version information.
         */
        if (line.contains("#End Version File")) {
          version = false;
        }
        
        /*
         * We are now at the information part of the File. Here begins the allocation of 
         * all versions that were released.
         */
        if (version) {
          /*
           * Remove HTML-Code from the String.
           */
          while (line.contains(">")) {
            if (isCancelled()) {
              bar.setVisible(false);
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                  primary.setResponseText("Zeitüberschreitung");
                  primary.removeResponseHandler();
                }           
              });
              return null;
            }
            line = line.replaceFirst(line.substring(line.indexOf("<"), 
                line.indexOf(">") + 1), "");
          }
          /*
           * If the Line still contains Information after removing all HTML-Code, then there 
           * are version numbers stored in it. It is added to the String defined before.
           */
          if (line.trim().length() != 0) {
            s = s.concat(line.trim() + System.lineSeparator());
          }
        }
        /*
         * This Line is always at the start of the Information about versions. This is checked 
         * here to remove a line from s.
         */
        if (line.contains("#Begin Version File")) {
          version = true;
        }
        
        /*
         * Updates the ProgressBar.
         */
        i++;
        updateProgress(i, max);
      }
      /*
       * Creates a StringTokenizer to separate each version Number.
       */
      StringTokenizer st = new StringTokenizer(s, System.lineSeparator());
      /*
       * This stores the latest version.
       */
      final double latest;
      /*
       * This List stores all older versions.
       */
      ArrayList<Double> older = new ArrayList<Double>();
      /*
       * Starting to iterate through all tokens of st.
       */
      String str = st.nextToken();
      /*
       * If the current token is "Current", then the following token contains the latest version.
       */
      if (str.equals("Current")) {
        /*
         * Sets latest to the next Token.
         */
        latest = Double.parseDouble(st.nextToken());
      } else {
        latest = 0.0;
      }
      /*
       * Going through the rest of the Tokens to get all older versions.
       */
      while (st.hasMoreTokens()) {
        if (isCancelled()) {
          bar.setVisible(false);
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              primary.setResponseText("Zeitüberschreitung");
              primary.removeResponseHandler();
            }           
          });
          return null;
        }
        /*
         * Gets the next Token from the Tokenizer.
         */
        str = st.nextToken();
        /*
         * If the current Token is "Old", then the following Token will be the first Token to store 
         * a old version number.
         */
        if (str.equals("Old")) {
          /*
           * Adds the next Token to older.
           */
          older.add(Double.parseDouble(st.nextToken()));
          /*
           * Every other Token will be another version number, but without the "Old" in front of it.
           */
        } else {
          /*
           * In Case there was an version that wasn't described by a double, this catches the Error.
           */
          try {
            /*
             * Adds the version number to older.
             */
            older.add(Double.parseDouble(str));
          } catch (Exception e) {
            System.err.println("Error while parsing a double. Skipping this entry...");
          }
        }
      }
      /*
       * Checks if the version of this Application is the latest version available.
       */
      if (primary.getVersion() == latest) {
        /*
         * Runs a new Runnable to change the responseText and removes all responseHandlers.
         */
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            primary.setResponseText("Aktuellste Version vorhanden");
            primary.removeResponseHandler();
          }
        });
        /*
         * Checks, if the version of this Application is defined in older.
         */
      } else if (older.contains(primary.getVersion())) {
        /*
         * Runs a new Runnable to change the responseText and add a responseHandler.
         */
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            /*
             * Since there is a newer Version, the User will be prompted to Download this Version.
             */
            primary.setResponseText("Update verfügbar. Klicken Sie hier zum Download");
            /*
             * Adds a Handler to the response Label to direct him to the Download page of the new 
             * Version.
             */
            primary.addResponseHandler(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent arg0) {
                try {
                  /*
                   * Opens the Download page with the Systems default browser.
                   */
                  java.awt.Desktop.getDesktop().browse(
                      new URI("https://github.com/Haeldeus/CashAssets/releases/download/v" + latest + "/WeyherCalculator.rar"));
                } catch (IOException e) {
                  /*
                   * If there was an Error while opening the Site, this text will be shown to the 
                   * User and the Handler will be removed to prevent more errors.
                   */
                  Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                      primary.setResponseText("Fehler beim Öffnen der Seite");
                      primary.removeResponseHandler();
                    }
                  });
                  /*
                   * In case the Syntax of the URI was set falsely, the User will be informed by 
                   * this and the Handler will be removed.
                   */
                } catch (URISyntaxException e) {
                  Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                      primary.setResponseText("Fehler beim Öffnen der Seite");
                      primary.removeResponseHandler();
                    }                   
                  });
                }
              }            
            });
          }
        });
        /*
         * If the current Version isn't listed, this Message will be displayed.
         */
      } else {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            primary.setResponseText("Fehler beim Update");
            primary.removeResponseHandler();
          }
        });
      }
      /*
       * Updates the ProgressBar one last time.
       */
      updateProgress(max, max);
      /*
       * In Case there was an error when connecting to the Internet while checking for an Update, 
       * this Message will be displayed.
       */
    } catch (IOException e) {
      e.printStackTrace();
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          primary.setResponseText("Fehler beim Update. Prüfen Sie ihre Internetverbindung");
          primary.removeResponseHandler();
        }      
      });
    }
    /*
     * Removes the Bar from the Scene and returns null to finish this Task.
     */
    bar.setVisible(false);
    return null;
  }
}
