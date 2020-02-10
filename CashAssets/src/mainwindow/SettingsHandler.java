package mainwindow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
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
    path = Paths.get("Settings.stg");
  }

  @Override
  public void handle(ActionEvent arg0) {
    final Stage dialog = new Stage();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    Label desc = new Label("Bewegen Sie die Maus über die einzelnen Textpassagen um im Tooltip "
        + "weitere Informationen zu erhalten.");
    desc.setWrapText(true);
    grid.add(desc, 0, 0);
    GridPane.setColumnSpan(desc, 10);
    GridPane.setRowSpan(desc, 2);
    Label simpleDesign = new Label("Verwende kleines Design");
    simpleDesign.setTooltip(new Tooltip("Statt das gesamte Kleingeld einzugeben muss "
        + "nur die Gesamtsumme eingegeben werden."));
    grid.add(simpleDesign, 0, 3);
    
    boolean existingSettings = Files.exists(path);
    
    CheckBox cbSimpleDesign = new CheckBox();
    if (!existingSettings) {
      cbSimpleDesign.setSelected(true);
    } else {
      cbSimpleDesign.setSelected(getSimpleDesignSelect());
    }
    grid.add(cbSimpleDesign, 1, 3);
    
    Label openFolder = new Label("Verzeichnis öffnen");
    openFolder.setTooltip(new Tooltip("Nach dem Export der Excel-Tabelle wird das Verzeichnis "
        + "automatisch geöffnet."));
    grid.add(openFolder, 0, 4);
    
    
    CheckBox cbOpenFolder = new CheckBox();
    if (!existingSettings) {
      cbOpenFolder.setSelected(true);
    } else {
      cbOpenFolder.setSelected(getOpenFolderSelect());
    }
    grid.add(cbOpenFolder, 1, 4);
    
    Button btSave = new Button("Speichern");
    btSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        PrintWriter writer;
        try {
          writer = new PrintWriter(path.toString(), "UTF-8");
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
          
          writer.close();
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        dialog.close();
      }   
    });
    grid.add(btSave, 0, 7);
    GridPane.setColumnSpan(btSave, 2);
    
    Button btCancel = new Button("Abbrechen");
    btCancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        dialog.close();
      }  
    });
    grid.add(btCancel, 2, 7);
    GridPane.setColumnSpan(btCancel, 2);
    
    BorderPane bp = new BorderPane();
    bp.setTop(grid);
    Scene dialogScene = new Scene(bp, 400, 290);
    dialogScene.getStylesheets().add("controlStyle1.css");
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Settings");
    dialog.show();
  }

  /**
   * Returns, if the Checkbox for simple design is selected.
   * @return  {@code true}, if the Checkbox is selected, {@code false} if not.
   * @since 1.0
   */
  private boolean getSimpleDesignSelect() {
    try {
      FileReader fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      String s = readLine(0, br);
      StringTokenizer st = new StringTokenizer(s, "=");
      st.nextToken();
      br.close();
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
    return false;
  }
  
  /**
   * Returns, if the Checkbox for open folder is selected.
   * @return  {@code true}, if the Checkbox is selected, {@code false} if not.
   * @since 1.0
   */
  private boolean getOpenFolderSelect() {
    try {
      FileReader fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
      String s = readLine(1, br);
      StringTokenizer st = new StringTokenizer(s, "=");
      st.nextToken();
      br.close();
      return st.nextToken().trim().equals("1");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
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
    for (int i = 0; i < index; i++) {
      try {
        br.readLine();
      } catch (IOException e) {
        e.printStackTrace();
        return "";
      }
    }
    try {
      return br.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
}
