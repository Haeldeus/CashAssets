package mainwindow.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.Util;

/**
 * The EventHandler for the Settings MenuItem. This will create a new Window to show all possible 
 * settings.
 * @author Haeldeus
 * @version 1.0
 */
public class SettingsHandler implements EventHandler<ActionEvent> {

  /**
   * The Stage, this Settings Item was clicked in.
   */
  private Stage primaryStage;
  
  /**
   * The Path to the Settings File.
   */
  private Path path;
  
  /**
   * Creates a new Handler and sets {@link #primaryStage} to the given Stage and sets {@link #path} 
   * to the default path of the Settings File.
   * @param primary The Stage, this MenuItem was clicked in and inherits the new Window to be 
   *     created.
   * @since 1.0
   */
  public SettingsHandler(Stage primary) {
    this.primaryStage = primary;
    path = Paths.get("data" + File.separator + "Settings.stg");
  }

  @Override
  public void handle(ActionEvent arg0) {
    /*
     * Creates a new Stage to display the Settings Scene.
     */
    final Stage dialog = new Stage();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    /*
     * Creates a new GridPane, that will contain all fields of this Scene.
     */
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    /*
     * Creates a Label, that informs the User, that there are Tooltips with further Information.
     */
    Label desc = new Label("Bewegen Sie die Maus über die einzelnen Textpassagen um im Tooltip "
        + "weitere Informationen zu erhalten.");
    desc.setWrapText(true);
    grid.add(desc, 0, 0);
    GridPane.setColumnSpan(desc, 10);
    GridPane.setRowSpan(desc, 2);
    /*
     * Creates a Label, that describes the purpose of the simple Design CheckBox.
     */
    Label simpleDesign = new Label("Verwende kleines Design");
    simpleDesign.setTooltip(new Tooltip("Statt das gesamte Kleingeld einzugeben muss "
        + "nur die Gesamtsumme eingegeben werden."));
    grid.add(simpleDesign, 0, 3);
    
    /*
     * Checks, if there is a Settings File, where previous settings were saved.
     */
    boolean existingSettings = Files.exists(path);
    
    /*
     * Creates a new CheckBox for the simple Design. If a Settings File exists, the setting for 
     * this will be taken from that, if not this will be set to be selected by default.
     */
    CheckBox cbSimpleDesign = new CheckBox();
    if (!existingSettings) {
      cbSimpleDesign.setSelected(true);
    } else {
      cbSimpleDesign.setSelected(getSimpleDesignSelect());
    }
    grid.add(cbSimpleDesign, 1, 3);
    
    /*
     * Creates a Label, that describes the purpose of the open Folder CheckBox.
     */
    Label openFolder = new Label("Verzeichnis öffnen");
    openFolder.setTooltip(new Tooltip("Nach dem Export der Excel-Tabelle wird das Verzeichnis "
        + "automatisch geöffnet."));
    grid.add(openFolder, 0, 4);
    
    /*
     * Creates a new CheckBox for the open Folder Setting. If a Settings File exists, the setting 
     * for this will be taken from that, if not this will be set to be selected by default.
     */
    CheckBox cbOpenFolder = new CheckBox();
    if (!existingSettings) {
      cbOpenFolder.setSelected(true);
    } else {
      cbOpenFolder.setSelected(getOpenFolderSelect());
    }
    grid.add(cbOpenFolder, 1, 4);
    
    /*
     * Creates a Label, that describes the purpose of the Nightmode-CheckBox.
     */
    Label nightmode = new Label("Nachtmodus benutzen");
    nightmode.setTooltip(new Tooltip("Dunklere Farben zur Darstellung benutzen. Erfordert Neustart "
        + "der Anwendung."));
    grid.add(nightmode, 0, 5);
    
    /*
     * Creates a new CheckBox for the Nightmode Setting. If a Settings File exists, the setting 
     * for this will be taken from that, if not this will be set to be selected by default.
     */
    CheckBox cbNightmode = new CheckBox();
    if (!existingSettings) {
      cbNightmode.setSelected(true);
    } else {
      cbNightmode.setSelected(getNightmodeSelect());
    }
    grid.add(cbNightmode, 1, 5);
    
    /*
     * Creates a Button to save the current Settings and adds a Handler to it.
     */
    Button btSave = new Button("Speichern");
    btSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Creates a Writer to write the Settings into the File.
         */
        PrintWriter writer;
        try {
          File directory = new File("data");
          if (!directory.exists()) {
            directory.mkdir();
          }
          writer = new PrintWriter(path.toString(), "UTF-8");
          /*
           * Prints the Settings into the File.
           */
          if (cbSimpleDesign.isSelected()) {
            writer.println("simpleDesign = 1");
          } else {
            writer.println("simpleDesign = 0");
          }
          
          if (cbOpenFolder.isSelected()) {
            writer.println("openFolder = 1");
          } else {
            writer.println("openFolder = 0");
          }
          
          if (cbNightmode.isSelected()) {
            writer.println("nightMode = 1");
          } else {
            writer.println("nightMode = 0");
          }
          /*
           * Closes the Writer to prevent leakage.
           */
          writer.close();
        } catch (FileNotFoundException e) {
          /*
           * Just for debugging purposes. Usually this shouldn't be called at any time.
           */
          e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
          /*
           * Just for debugging purposes. Usually this shouldn't be called at any time.
           */
          e.printStackTrace();
        }
        /*
         * Closes the Stage after saving it.
         */
        dialog.close();
      }   
    });
    grid.add(btSave, 0, 8);
    GridPane.setColumnSpan(btSave, 2);
    
    /*
     * Creates a Button to cancel the current changes and adds a Handler to it.
     */
    Button btCancel = new Button("Abbrechen");
    btCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Simply closes the Stage without saving.
         */
        dialog.close();
      }  
    });
    grid.add(btCancel, 2, 8);
    GridPane.setColumnSpan(btCancel, 2);
    
    /*
     * Adds a BorderPane to ensure, that the Grid is always at the Top of the Stage.
     */
    BorderPane bp = new BorderPane();
    bp.setTop(grid);
    /*
     * Basic Stage Settings, which control the Stage and it's Dimension. Also, displays 
     * the Stage to the User.
     */
    Scene dialogScene = new Scene(bp, 400, 290);
    if (Util.checkNightmode()) {
      dialogScene.getStylesheets().add("nightControlStyle1.css");
    } else {
      dialogScene.getStylesheets().add("controlStyle1.css");
    }
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Settings");
    dialog.show();
  }

  /**
   * Returns, if the CheckBox for simple design should be selected. This is determined by 
   * checking the Settings File.
   * @return  {@code true}, if the CheckBox should be selected, {@code false} if not.
   * @since 1.0
   */
  private boolean getSimpleDesignSelect() {
    try {
      /*
       * Creates a BufferedReader of the given FileReader to read from the File.
       */
      FileReader fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      /*
       * Reads line 0 and saves it. This line contains the simple Design Setting.
       */
      String s = readLine(0, br);
      /*
       * Creates a new Tokenizer to get the Setting.
       */
      StringTokenizer st = new StringTokenizer(s, "=");
      /*
       * Skips the first Token, since this is only "simpleDesign"
       */
      st.nextToken();
      /*
       * Closes the Reader to prevent leakage.
       */
      br.close();
      /*
       * Returns, if the second Token is 1, which means the simple Design should be used.
       */
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (IOException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (NullPointerException e) {
      /*
       * This happens, if there is no String to be tokenized by st. In this case there 
       * is no Setting for this and false will be returned by default.
       */
    }
    /*
     * Returns false, in case any error occurred while reading the File.
     */
    return false;
  }
  
  /**
   * Returns, if the CheckBox for open folder should be selected. This is determined by checking 
   * the Settings File.
   * @return  {@code true}, if the CheckBox should be selected, {@code false} if not.
   * @since 1.0
   */
  private boolean getOpenFolderSelect() {
    try {
      /*
       * Creates a BufferedReader with the given FileReader to read the Settings File.
       */
      FileReader fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      /*
       * Reads the second Line, which contains the openFolder Setting.
       */
      String s = readLine(1, br);
      /*
       * Creates a Tokenizer to get the Setting.
       */
      StringTokenizer st = new StringTokenizer(s, "=");
      /*
       * Skips the first Token, since this is only "openFolder".
       */
      st.nextToken();
      /*
       * Closes the Reader to prevent leakage.
       */
      br.close();
      /*
       * Returns if the second token is 1, which means the Folder should be opened.
       */
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (IOException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (NullPointerException e) {
      /*
       * This happens, if there is no String to be tokenized by st. In this case there 
       * is no Setting for this and false will be returned by default.
       */
    }
    /*
     * Returns false, in case an error occurred while reading from the File.
     */
    return false;
  }
  
  /**
   * Returns, if the CheckBox for Nightmode should be selected. This is determined by checking 
   * the Settings File.
   * @return  {@code true}, if the CheckBox should be selected, {@code false} if not.
   * @since 1.0
   */
  private boolean getNightmodeSelect() {
    try {
      /*
       * Creates a BufferedReader with the given FileReader to read the Settings File.
       */
      FileReader fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      /*
       * Reads the second Line, which contains the openFolder Setting.
       */
      String s = readLine(2, br);
      /*
       * Creates a Tokenizer to get the Setting.
       */
      StringTokenizer st = new StringTokenizer(s, "=");
      /*
       * Skips the first Token, since this is only "openFolder".
       */
      st.nextToken();
      /*
       * Closes the Reader to prevent leakage.
       */
      br.close();
      /*
       * Returns if the second token is 1, which means the Folder should be opened.
       */
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (IOException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time.
       */
      e.printStackTrace();
    } catch (NullPointerException e) {
      /*
       * This happens, if there is no String to be tokenized by st. In this case there 
       * is no Setting for this and false will be returned by default.
       */
    }
    /*
     * Returns false, in case an error occurred while reading from the File.
     */
    return false;
  }
  
  /**
   * Reads the line specified by index (first line equals 0) from the given BufferedReader and 
   * returns it. All Lines before this index are deleted.
   * @param index The Line to be read.
   * @param br  The BufferedReader the Line should be read from.
   * @return  The Content of the given Line as a String.
   * @since 1.0
   */
  private String readLine(int index, BufferedReader br) {
    /*
     * While there are lines before the specified index, this loop while skip them.
     */
    for (int i = 0; i < index; i++) {
      try {
        /*
         * This will skip the Lines that aren't needed.
         */
        br.readLine();
      } catch (IOException e) {
        /*
         * Just for debugging purposes. Usually this shouldn't be called at any time.If it gets 
         * called however, it returns an empty String for consistency.
         */
        e.printStackTrace();

        return "";
      }
    }
    try {
      /*
       * Returns the line specified by the given index.
       */
      return br.readLine();
    } catch (IOException e) {
      /*
       * Just for debugging purposes. Usually this shouldn't be called at any time. If it gets 
       * called however, it returns an empty Strig for consistency.
       */
      e.printStackTrace();
      return "";
    }
  }
}
