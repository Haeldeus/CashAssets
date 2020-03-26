package tipcalculator.handler;

import java.util.Calendar;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
   * A Constructor for the Handler. Will set the given Stage as {@link #primaryStage}. 
   * @param primaryStage  The Stage, that will be set as primary Stage.
   * @since 1.0
   */
  public ExportHandler(Stage primaryStage) {
    this.primaryStage = primaryStage;
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
    TextField tfYear = new TextField();
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
    export.setOnKeyReleased(null); //TODO
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
    dialogScene.getStylesheets().add(Util.getControlStyle());
    dialog.setScene(dialogScene);
    dialog.setResizable(false);
    dialog.setTitle("Export...");
    dialog.show();
  }

}
