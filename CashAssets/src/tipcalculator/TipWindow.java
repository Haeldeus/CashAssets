package tipcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tipcalculator.handler.AddToTipHandler;
import tipcalculator.handler.ExportHandler;
import tipcalculator.handler.StaffHandler;
import tipcalculator.listener.TextFieldFocusChangeListener;
import tipcalculator.listener.TextFieldTextChangeListener;
import util.Util;

/**
 * A Class which Objects will display the Window for the Tip Calculation.
 * @author Haeldeus
 * @version 1.0
 */
public class TipWindow extends Application {
  
  /**
   * The Label, that displays the total Hours worked.
   */
  private Label lbHoursTotal;
  
  /**
   * The TextField, where the User enters the total amount of Tips.
   */
  private TextField tfTip;
  
  /**
   * The boolean value, that determines, if a Row was deleted as the last action in the staffGrid.
   */
  private boolean deleted;
  
  /**
   * The BigDecimal, that stores the calculated Amount of tip per hour from the total Sum of Tips.
   */
  private BigDecimal tipPerHour;
  
  /**
   * The Constructor for this Window. Sets {@link #deleted} to false, since there was no deletion 
   * yet.
   * @since 1.0
   */
  public TipWindow() {
    deleted = false;
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Creates a GridPane, that contains all Other Panes and Nodes.
     */
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    
    /*
     * Creates a MenuBar, where the User can edit settings and export the calculation to a file.
     */
    MenuBar menu = new MenuBar();
    final Menu settingsMenu = new Menu("Einstellungen");
    final MenuItem settingsItem = new MenuItem("Mitarbeiter...");
    settingsItem.setOnAction(new StaffHandler(primaryStage));
    settingsMenu.getItems().addAll(settingsItem);
    menu.getMenus().addAll(settingsMenu);
    
    /*
     * Creates a Label, that describes the tipSum TextField besides it.
     */
    Label tipSum = new Label("Summe Tip: ");
    grid.add(tipSum, 0, 0);
    
    /*
     * The GridPane, that contains tfTip and a Label displaying "€".
     */
    GridPane tipPane = new GridPane();
    tipPane.setHgap(5);
    
    /*
     * Creates the tipSum TextField and the Label for "€".
     */
    tfTip = new TextField();
    tfTip.setMaxWidth(100);
    tipPane.add(tfTip, 0, 0);
    Label tipEuroLabel = new Label("€");
    tipPane.add(tipEuroLabel, 1, 0);
    
    /*
     * Adds the small GridPane for the TipSum to the total GridPane.
     */
    grid.add(tipPane, 1, 0);
    
    /*
     * Creates a Label, that describes the Label for total Hours besides it.
     */
    Label totalHours = new Label("Gesamtstunden: ");
    grid.add(totalHours, 0, 1);
    
    /*
     * The Label, that displays the total hours worked.
     */
    lbHoursTotal = new Label("0,00h");
    lbHoursTotal.setMaxWidth(50);
    grid.add(lbHoursTotal, 1, 1);
    
    /*
     * Sets ColumnConstraints, so the Grid follows a wanted Design Pattern.
     */
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().add(new ColumnConstraints());
    //    grid.getColumnConstraints().get(1).setMaxWidth(120);
    grid.getColumnConstraints().add(new ColumnConstraints());
    //    grid.getColumnConstraints().get(2).setMaxWidth(100);
    grid.getColumnConstraints().add(new ColumnConstraints());
    grid.getColumnConstraints().get(3).setMinWidth(75);
    
    /*
     * Creates a Separator to separate the TipSum Area and the Staff Member Area.
     */
    Separator sep = new Separator();
    grid.add(sep, 0, 2);
    GridPane.setColumnSpan(sep, 2);
    
    /*
     * Creates a new GridPane, where the Staff Members can be added to.
     */
    GridPane staffGrid = new GridPane();
    staffGrid.setHgap(5);
    staffGrid.setVgap(5);
    
    /*
     * Creates a new Add Button.
     */
    Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
    ImageView imgView = new ImageView(img);
    Button add = new Button("", imgView);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(this, staffGrid, 1));
    staffGrid.add(add, 0, 0);
    
    /*
     * Creates a new ScrollPane. This will make the GridPane for the Staff scrollable.
     */
    ScrollPane sp = new ScrollPane();
    sp.setContent(staffGrid);
    sp.setFitToWidth(true);
    grid.add(sp, 0, 3);
    GridPane.setColumnSpan(sp, 4);
    
    /*
     * Adds a KeyEventHandler to tfTip, so whenever Enter is pressed while focusing the Tip 
     * TextField, the next TextField will be focused. This has to be done down here, since this 
     * uses the staffGrid.
     */
    tfTip.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent arg0) {
        if (arg0.getCode() == KeyCode.ENTER) {
          if (staffGrid.getChildren().size() > 1) {
            GridPane smallGrid = (GridPane)staffGrid.getChildren().get(1);
            TextField tfTmp = (TextField)smallGrid.getChildren().get(0);
            tfTmp.requestFocus();
          } else {
            tfTip.requestFocus();
          }
        }
      }   
    });
    /*
     * Adds Listener to tfTip, so the Scene is updated whenever the Text is changed or the 
     * TextField loses focus.
     */
    tfTip.focusedProperty().addListener(new TextFieldFocusChangeListener(tfTip, true, staffGrid, 
        this, true));
    tfTip.textProperty().addListener(new TextFieldTextChangeListener(true, staffGrid, this, true));
    
    /*
     * Creates a Menu for exporting the Data into a File. This has to be done down here, since 
     * this uses the staffGrid.
     */
    final Menu exportMenu = new Menu("Export");
    final MenuItem exportItem = new MenuItem("Export...");
    exportItem.setOnAction(new ExportHandler(primaryStage, this, staffGrid));
    exportMenu.getItems().addAll(exportItem);
    menu.getMenus().addAll(exportMenu);
    
    /*
     * Creates a BorderPane, that contains the total GridPane. This is done, so that the Content 
     * of the GridPane can be displayed at the Center of the Scene.
     */
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menu);
    borderPane.setCenter(grid);
    
    /*
     * Basic Scene Settings.
     */
    Scene scene = new Scene(borderPane, 600, 250);
    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setTitle("Tip-Rechner");
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(270);
    primaryStage.setMinWidth(310);
    primaryStage.show();
  }
  
  /**
   * Returns the Label for the total amount of hours, so it can be altered by other Objects.
   * @return  The Label for the Total amount of hours.
   * @see #lbHoursTotal
   * @since 1.0
   */
  public Label getLbHoursTotal() {
    return lbHoursTotal;
  }
  
  /**
   * Returns the value of {@link #deleted}. This determines, if a row was deleted as the last 
   * action.
   * @return  {@code true}, if a row was deleted, {@code false} if not.
   */
  public boolean getDeleted() {
    return deleted;
  }
  
  /**
   * Sets {@link #deleted} to the new value. This means, that a row was deleted.
   * @param deleted The new value of {@link #deleted}.
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
  
  /**
   * Returns the TextField, where the total amount of tip can be entered.
   * @return  The TextField, where the total amount of tip is stored in.
   * @see #tfTip
   * @since 1.0
   */
  public TextField getTfTip() {
    return tfTip;
  }
  
  /**
   * Sets the value of {@link #tipPerHour} to the given BigDecimal. Sets the Scale to 2 as well.
   * @param tipPerHour  The new BigDecimal, that will be saved as tipPerHour.
   * @since 1.0
   */
  public void setTipPerHour(BigDecimal tipPerHour) {
    this.tipPerHour = tipPerHour.setScale(2, RoundingMode.HALF_DOWN);
  }
  
  /**
   * Returns {@link #tipPerHour}. This is used when exporting the File to display the Value of the 
   * BigDecimal.
   * @return  {@link #tipPerHour}.
   * @since 1.0
   */
  public BigDecimal getTipPerHour() {
    return this.tipPerHour;
  }
}
