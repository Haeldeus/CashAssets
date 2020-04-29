package tipcalculator.handler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tipcalculator.TipWindow;
import util.DayComboBoxKeyHandler;
import util.MonthComboBoxKeyHandler;
import util.Util;

/**
 * A Handler, that handles the Events for the Export MenuItem.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportHandler implements EventHandler<ActionEvent> {

  /**
   * The Stage, this Handler was called from.
   */
  private final Stage primaryStage;
  
  /**
   * The TipWindow, which the MenuItem is part of.
   */
  private TipWindow primary;
  
  /**
   * The GridPane, where all the Staff Members were added to.
   */
  private GridPane staffGrid;
  
  /**
   * A Constructor for the Handler. Will set the given Stage as {@link #primaryStage}. 
   * @param primaryStage  The Stage, that will be set as primary Stage.
   * @param primary The TipWindow, which the MenuItem is part of.
   * @param staffGrid The GridPane, where all the Staff Members were added to.
   * @since 1.0
   */
  public ExportHandler(Stage primaryStage, TipWindow primary, GridPane staffGrid) {
    this.primaryStage = primaryStage;
    this.primary = primary;
    this.staffGrid = staffGrid;
  }
  
  @Override
  public void handle(ActionEvent arg0) {
    /*
     * Creates a new Stage to display the Export Dialog.
     */
    final Stage dialog = new Stage();
    dialog.initOwner(primaryStage);
    dialog.initModality(Modality.APPLICATION_MODAL);
    
    /*
     * Creates a BorderPane, which will display all other Panes and Nodes at their positions.
     */
    BorderPane bp = new BorderPane();
    
    /*
     * Creates a Label with the basic Info for the User and adds it to the top of the BorderPane.
     */
    Label labelInfo = new Label("Geben Sie hier das Datum ein, für das die neue Datei erstellt "
        + "werden soll");
    labelInfo.setWrapText(true);
    bp.setTop(labelInfo);
    
    
    /*
     * Adding a Calendar to get the Current Date. This will be used in the afterwards added 
     * ComboBoxes for the Day and Month.
     */
    Calendar cal = Calendar.getInstance(Locale.GERMANY);
    /*
     * Creates the ComboBox for the User to select the day for this Calculation.
     */
    ComboBox<String> dayBox = new ComboBox<String>();
    for (int i = 1; i <= 31; i++) {
      dayBox.getItems().add(i + ".");
    }
    dayBox.getSelectionModel().select(cal.get(Calendar.DAY_OF_MONTH) - 1);
    dayBox.setOnKeyPressed(new DayComboBoxKeyHandler(dayBox));
    
    /*
     * Creates a GridPane, where the User can enter the date for the new File.
     */
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.add(dayBox, 0, 0);
    
    /*
     * Creates the ComboBox for the User to select the Month for this Calculation.
     */
    ComboBox<String> monthBox = new ComboBox<String>();
    monthBox.getItems().addAll("Januar", "Februar", "März", "April", "Mai", "Juni", 
        "Juli", "August", "September", "Oktober", "November", "Dezember");
    monthBox.getSelectionModel().select(cal.get(Calendar.MONTH));
    monthBox.setOnKeyPressed(new MonthComboBoxKeyHandler(monthBox));
    grid.add(monthBox, 1, 0);
     
    /*
     * Creates a TextField, where the User can enter the Year for this Calculation.
     */
    TextField tfYear = new TextField("" + cal.get(Calendar.YEAR));
    tfYear.setMaxWidth(75);
    grid.add(tfYear, 2, 0);
    
    /*
     * Creates a GridPane, where the Buttons are displayed.
     */
    GridPane lowGrid = new GridPane();
    lowGrid.setAlignment(Pos.CENTER);
    lowGrid.setHgap(10);
    /*
     * Creates the Button to export the Calculation into a new File.
     */
    Button export = new Button("Export");
    export.setMaxWidth(200);
    export.setOnMouseClicked(new ExportButtonHandler(primary, staffGrid, checkOpen(), dayBox, 
        monthBox, tfYear, dialog));
    lowGrid.add(export, 0, 0);
    GridPane.setHgrow(export, Priority.ALWAYS);
    
    /*
     * Creates the Button to cancel the Export.
     */
    Button cancel = new Button("Abbrechen");
    cancel.setMaxWidth(200);
    cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Closes the dialog without exporting.
         */
        dialog.close();
      }     
    });
    lowGrid.add(cancel, 1, 0);
    GridPane.setHgrow(cancel, Priority.ALWAYS);
    
    
    /*
     * Adds all Panes to the BorderPane.
     */
    bp.setCenter(grid);
    bp.setBottom(lowGrid);
    /*
     * Basic Scene settings.
     */
    Scene dialogScene = new Scene(bp, 250, 100);
    /*
     * Adds an EventHandler, that fires Events for the Export Button or Cancel Button, in case 
     * Enter or Escape was pressed.
     */
    dialogScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent arg0) {
        /*
         * Checks, if the current input is the enter Key.
         */
        if (arg0.getCode() == KeyCode.ENTER) {
          export.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 
              MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, 
              false, false, false, null));
          /*
           * Checks, if the current input is the Escape Key.
           */
        } else if (arg0.getCode() == KeyCode.ESCAPE) {
          cancel.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 
              MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, 
              false, false, false, null));
        }
      }     
    });
    dialogScene.getStylesheets().add(Util.getControlStyle());
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Export...");
    dialog.show();
  }

  /**
   * Checks, if the Directory should be opened after exporting.
   * @return  The boolean value, if the Directory should be opened.
   * @since 1.0
   */
  private boolean checkOpen() {
    Path path = Paths.get("data/Settings.stg");
    FileReader fr;
    try {
      fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
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
      e.printStackTrace();
    }
    return false;
  }
  
}
