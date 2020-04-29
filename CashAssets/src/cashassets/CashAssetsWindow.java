package cashassets;

import cashassets.handlers.ExportHandler;
import cashassets.handlers.KeyEventHandler;
import cashassets.listener.TextFieldFocusChangedListener;
import cashassets.listener.TextFieldTextChangedListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import util.Util;

/**
 * The Window, where the User can calculate the total amount of money in the purses.
 * @author Haeldeus
 * @version 1.0
 */
public class CashAssetsWindow extends Application {

  /**
   * Determines, if the simple design should be used.
   */
  private boolean simple;
  
  /**
   * All TextFields, where the User can enter the values for each coin counted.
   */
  private ArrayList<TextField> coinFields;
  
  /**
   * All TextFields, where the User can enter the values for each bill counted.
   */
  private ArrayList<TextField> billFields;
  
  /**
   * All Labels, where the amount of Money is displayed, that was counted using the 
   * corresponding coins.
   */
  private ArrayList<Label> coinFieldRes;
  
  /**
   * All Labels, where the amount of Money is displayed, that was counted using the 
   * corresponding bills.
   */
  private ArrayList<Label> billFieldRes;
  
  /**
   * The TextField, where the User can enter the amount of Money needed, that was calculated 
   * by the register's system.
   */
  private TextField cashNeeded;
  
  /**
   * The TextField, where the User can enter the amount of purses counted.
   */
  private TextField purses;
  
  /**
   * All Labels, that can be altered by the Application via {@link #calc()}.
   */
  private HashMap<String, Label> labels;
  
  /**
   * The String identifier for the coinage sum Label.
   */
  public final String coinageSumId = "COINAGE_SUM";
  
  /**
   * The String identifier for the bills sum Label.
   */
  public final String billsSumId = "BILLS_SUM";
  
  /**
   * The String identifier for the total sum Label.
   */
  public final String totalSumId = "TOTAL_SUM";
  
  /**
   * The String identifier for the coinage needed Label.
   */
  public final String coinageNeededId = "COINAGE_NEEDED";
  
  /**
   * The String identifier for the Label, which shows the difference in coinage needed and coinage 
   * in the purses.
   */
  public final String diffCoinageId = "DIFF_COINAGE";
  
  /**
   * The String identifier for the Label, which shows the amount of money after the coinage 
   * difference is equalized.
   */
  public final String coinageSumCalculatedId = "COINAGE_SUM_CALCULATED";
  
  /**
   * The String identifier for the cash needed Label.
   */
  public final String cashNeededId = "CASH_NEEDED";
  
  /**
   * The String identifier for the tip sum Label.
   */
  public final String tipSumId = "TIP_SUM";
  
  /**
   * The Constructor for this Window. Checks, if the simple design should be used and instantiates 
   * all Lists and the HashMap.
   * @since 1.0
   */
  public CashAssetsWindow() {
    this.simple = checkSimple();
    this.coinFields = new ArrayList<TextField>();
    this.billFields = new ArrayList<TextField>();
    this.coinFieldRes = new ArrayList<Label>();
    this.billFieldRes = new ArrayList<Label>();
    this.labels = new HashMap<String, Label>();
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Basic Scenery Settings.
     */
    primaryStage.setTitle("Kassenbestand");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    
    /*
     * Fills the Grid with the needed TextFields and Labels.
     */
    fillInfoGrid(grid, fillInputGrid(grid, primaryStage));
    
    /*
     * Adds Handlers to all TextFields.
     */
    addHandlers();
    
    /*
     * Updates the Tooltips for all Labels.
     */
    for (Label lb : labels.values()) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    
    for (Label lb : billFieldRes) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    
    for (Label lb : coinFieldRes) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    
    /*
     * Creates a Menu for exporting the Data into a File. This has to be done down here, since 
     * this uses the staffGrid.
     */
    final Menu exportMenu = new Menu("Export");
    final MenuItem exportItem = new MenuItem("Export...");
    exportItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN, 
        KeyCombination.SHIFT_DOWN));
    exportItem.setOnAction(new ExportHandler(primaryStage, this));
    exportMenu.getItems().addAll(exportItem);
    MenuBar menu = new MenuBar();
    menu.getMenus().addAll(exportMenu);
    
    /*
     * Adds a BorderPane to the Scene, so the Grid will always be displayed at the Top of the Scene.
     */
    BorderPane bp = new BorderPane();
    bp.setTop(menu);
    bp.setCenter(grid);
    
    /*
     * Sets the Size of the Scene, it's restrictions and the Stylesheet. Afterwards, it displays 
     * the primaryStage to the User.
     */
    Scene scene = new Scene(bp, 450, 550);
    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Adds all Handlers to all TextFields.
   * @since 1.0
   * @see KeyEventHandler
   * @see TextFieldFocusChangedListener
   * @see TextFieldTextChangedListener
   */
  private void addHandlers() {
    /*
     * Since the BillFields are always added to the Window, regardless of the design used, we can 
     * add the FocusChangedListener and TextChangedListener to all of them without checking for 
     * the design.
     */
    for (TextField tf : billFields) {
      tf.focusedProperty().addListener(new TextFieldFocusChangedListener(tf, this, false));
      tf.textProperty().addListener(new TextFieldTextChangedListener(this));
    }
    
    /*
     * Adds the Listener to the cashNeeded and purses TextField.
     */
    cashNeeded.focusedProperty().addListener(new TextFieldFocusChangedListener(cashNeeded, this, 
        true));
    cashNeeded.textProperty().addListener(new TextFieldTextChangedListener(this));
    purses.focusedProperty().addListener(new TextFieldFocusChangedListener(purses, this, false));
    purses.textProperty().addListener(new TextFieldTextChangedListener(this));
    
    /*
     * Adds the KeyEventHandler to all Fields.
     */
    if (simple) {
      /*
       * Adds the FocusChanged and TextChanged Listener to the first coinField TextField.
       */
      coinFields.get(0).focusedProperty().addListener(new TextFieldFocusChangedListener(
          coinFields.get(0), this, true));
      coinFields.get(0).textProperty().addListener(new TextFieldTextChangedListener(this));
      /*
       * Adds the KeyEventHandler to all TextFields. Since every Field has different Fields to jump 
       * to when pressing Ctrl + Arrow, this has to be done separately.
       */
      coinFields.get(0).setOnKeyPressed(new KeyEventHandler(billFields.get(0), cashNeeded, 
          billFields.get(0), null, billFields.get(3)));
      billFields.get(0).setOnKeyPressed(new KeyEventHandler(billFields.get(1), coinFields.get(0), 
          billFields.get(1), null, billFields.get(4)));
      billFields.get(1).setOnKeyPressed(new KeyEventHandler(billFields.get(2), billFields.get(0), 
          billFields.get(2), null, billFields.get(5)));
      billFields.get(2).setOnKeyPressed(new KeyEventHandler(billFields.get(3), billFields.get(1), 
          cashNeeded, null, billFields.get(6)));
      billFields.get(3).setOnKeyPressed(new KeyEventHandler(billFields.get(4), purses, 
          billFields.get(4), coinFields.get(0), null));
      billFields.get(4).setOnKeyPressed(new KeyEventHandler(billFields.get(5), billFields.get(3), 
          billFields.get(5), billFields.get(0), null));
      billFields.get(5).setOnKeyPressed(new KeyEventHandler(billFields.get(6), billFields.get(4), 
          billFields.get(6), billFields.get(1), null));
      billFields.get(6).setOnKeyPressed(new KeyEventHandler(cashNeeded, billFields.get(5), purses, 
          billFields.get(2), null));
      cashNeeded.setOnKeyPressed(new KeyEventHandler(purses, billFields.get(2), coinFields.get(0), 
          null, purses));
      purses.setOnKeyPressed(new KeyEventHandler(coinFields.get(0), billFields.get(6), 
          billFields.get(3), cashNeeded, null));
    } else {
      /*
       * If the simple design wasn't used, we have multiple coin TextFields, so we have to assign 
       * all Listener and the Handler to each of them.
       */
      for (int i = 0; i < coinFields.size(); i++) {
        TextField tf = coinFields.get(i);
        /*
         * Adds the Change Listener.
         */
        tf.focusedProperty().addListener(new TextFieldFocusChangedListener(tf, this, false));
        tf.textProperty().addListener(new TextFieldTextChangedListener(this));
        /*
         * Adds the KeyEventHandler to all TextFields. Since only the first and the last one are 
         * different from the others, we only have to specify where to jump from them. Every other 
         * Handler can be configured using i as jump index.
         */
        if (i == 0) {
          tf.setOnKeyPressed(new KeyEventHandler(coinFields.get(i + 1), cashNeeded, 
              coinFields.get(i + 1), null, billFields.get(i)));
        } else if (i > 0 && i < coinFields.size() - 1) {
          tf.setOnKeyPressed(new KeyEventHandler(coinFields.get(i + 1), coinFields.get(i - 1), 
              coinFields.get(i + 1), null, billFields.get(i)));
        } else {
          tf.setOnKeyPressed(new KeyEventHandler(billFields.get(0), coinFields.get(i - 1), 
              cashNeeded, null, null));
        }
      }
      
      /*
       * Adds the KeyEventHandler to all bill TextFields. Similar to the coin TextFields, we only 
       * have to specify the first and last one, every other can be configured by using i as jump 
       * index.
       */
      for (int i = 0; i < billFields.size(); i++) {
        TextField tf = billFields.get(i);
        if (i == 0) {
          tf.setOnKeyPressed(new KeyEventHandler(billFields.get(i + 1), purses, 
              billFields.get(i + 1), coinFields.get(i), null));
        } else if (i > 0 && i < billFields.size() - 1) {
          tf.setOnKeyPressed(new KeyEventHandler(billFields.get(i + 1), billFields.get(i - 1), 
              billFields.get(i + 1), coinFields.get(i), null));
        } else {
          tf.setOnKeyPressed(new KeyEventHandler(cashNeeded, billFields.get(i - 1), 
              purses, coinFields.get(i), null));
        }       
      }
      /*
       * Adds the KeyEventHandler to the cashNeeded and the purses TextField.
       */
      cashNeeded.setOnKeyPressed(new KeyEventHandler(purses, coinFields.get(coinFields.size() - 1), 
          coinFields.get(0), null, purses));
      purses.setOnKeyPressed(new KeyEventHandler(coinFields.get(0), 
          billFields.get(billFields.size() - 1), billFields.get(0), cashNeeded, null));
    }
  }
  
  /**
   * Fills the Part of the Grid, where the Information for the User is displayed using Labels, that 
   * can be altered by the Application.
   * @param grid  The GridPane, where the Labels will be added to.
   * @param rowIndex  The index of the Row, where the function will start to add Labels to.
   * @since 1.0
   */
  private void fillInfoGrid(GridPane grid, int rowIndex) {
    /*
     * Adds the Row, where the Sums are displayed for each type of money (coins and bills). This is 
     * done by using smaller Grids for each Sum, which contain a describing Label as well as a 
     * Label to display the Sum. 
     */
    GridPane small = new GridPane();
    small.setAlignment(Pos.CENTER);
    small.setHgap(1);
    
    Label id = new Label("Münzgeld:");
    small.add(id, 0, 0);
    GridPane.setHalignment(id, HPos.CENTER);
    
    Label coinageSum = new Label("0,00€");
    small.add(coinageSum, 1, 0);
    labels.put(coinageSumId, coinageSum);
    
    grid.add(small, 0, rowIndex);
    GridPane.setColumnSpan(small, 2);
    
    small = new GridPane();
    small.setAlignment(Pos.CENTER);
    small.setHgap(1);
    
    id = new Label("Scheine:");
    small.add(id, 0, 0);
    GridPane.setHalignment(id, HPos.CENTER);
    
    Label billSum = new Label("0,00€");
    small.add(billSum, 1, 0);
    labels.put(billsSumId, billSum);
    
    grid.add(small, 4, rowIndex);
    GridPane.setColumnSpan(small, 2);
    
    small = new GridPane();
    small.setAlignment(Pos.CENTER);
    small.setHgap(1);
    
    id = new Label("Gesamt:");
    small.add(id, 0, 0);
    GridPane.setHalignment(id, HPos.CENTER);
    
    Label totalSum = new Label("0,00€");
    small.add(totalSum, 1, 0);
    labels.put(totalSumId, totalSum);
    
    grid.add(small, 2, rowIndex);
    GridPane.setColumnSpan(small, 2);
    
    /*
     * Increments the rowIndex to allow the creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds a Separator to the Grid.
     */
    Separator sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    /*
     * Increments the rowIndex to allow the creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds the Row to the Grid, where the amount of coinage needed and the sum of money after 
     * equalizing the coinage Difference is displayed.
     */
    id = new Label("Muss Kleingeld:");
    grid.add(id, 0, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label coinageNeeded = new Label("0,00€");
    grid.add(coinageNeeded, 2, rowIndex);
    labels.put(coinageNeededId, coinageNeeded);
    
    id = new Label("Kleingeld bereinigt:");
    grid.add(id, 3, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label coinageSumCalculated = new Label("0,00€");
    grid.add(coinageSumCalculated, 5, rowIndex);
    labels.put(coinageSumCalculatedId, coinageSumCalculated);
    
    /*
     * Increments the rowIndex to allow the creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds the Row to the Grid, where the Difference in coinage and the amount of cash needed, 
     * that was calculated by the register's system, is displayed.
     */
    id = new Label("Differenz Kleingeld:");
    grid.add(id, 0, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label diffCoinage = new Label("0,00€");
    grid.add(diffCoinage, 2, rowIndex);
    labels.put(diffCoinageId, diffCoinage);
    
    id = new Label("Kassenschnitt:");
    grid.add(id, 3, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label cashNeeded = new Label("0,00€");
    grid.add(cashNeeded, 5, rowIndex);
    labels.put(cashNeededId, cashNeeded);
    
    /*
     * Increments the rowIndex to allow the creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds the Row to the Grid, where the total Sum of Tips left in the purses is displayed.
     */
    id = new Label("Rest Tip:");
    grid.add(id, 3, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label tipSum = new Label("0,00€");
    grid.add(tipSum, 5, rowIndex);
    labels.put(tipSumId, tipSum);
  }
  
  /**
   * Fills the Part of the Grid, where the User can enter the values for each coin and bill, that 
   * were counted in the purses.
   * @param grid The GridPane, this Area will be added to.
   * @param primaryStage  The Stage, that will display the Grid.
   * @return  An Integer, that describes the Row in the Grid below the added Area.
   * @since 1.0
   */
  private int fillInputGrid(GridPane grid, Stage primaryStage) {
    /*
     * Adds Columns to the GridPane, so they can be configured.
     */
    for (int i = 0; i <= 5; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
    }
    /*
     * Configures the Width for all columns, so they match the wanted design.
     */
    grid.getColumnConstraints().get(0).setMinWidth(60);
    grid.getColumnConstraints().get(0).setMaxWidth(100);
    grid.getColumnConstraints().get(1).setMaxWidth(60);
    grid.getColumnConstraints().get(2).setMinWidth(100);
    grid.getColumnConstraints().get(3).setMinWidth(60);
    grid.getColumnConstraints().get(3).setMaxWidth(100);
    grid.getColumnConstraints().get(4).setMaxWidth(60);
    grid.getColumnConstraints().get(5).setMinWidth(100);
    
    
    /*
     * Identifiers for all types of coins and bills.
     */
    String[] billIds = {"5€:", "10€:", "20€:", "50€:", "100€:", "200€:", "500€:"};
    String[] coinIds = {"1ct:", "2ct:", "5ct:", "10ct:", "20ct:", "50ct:", "1€:", "2€:"};
    /*
     * Creates an index for the Row, so the lower area, which is the same for both design 
     * patterns, can be created out of the if statement.
     */
    int rowIndex;
    if (simple) {
      /*
       * Creates the Input Area for the simple design. This looks like the following:
       * |-------|     |-------|
       * |Coinage|     |  50€  |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |   5€  |     |  100€ |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  10€  |     |  200€ |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  20€  |     |  500€ |
       * |-------|     |-------|
       */
      for (int i = 0; i <= 1; i++) {
        for (int k = 0; k <= 3; k++) {
          Label identifier;
          TextField tf = new TextField("0");
          Label result = new Label("= 0,00€");
          if (i == k && k == 0) {
            identifier = new Label("Kleingeld:");
            tf.setTooltip(new Tooltip("Geben Sie hier die Anteil an Kleingeld ein."));
            coinFields.add(0, tf);
            coinFieldRes.add(0, result);
            grid.add(identifier, 0, 0);
            grid.add(tf, 1, 0);
            grid.add(result, 2, 0);
          } else {
            int index = 4 * i + (k - 1);
            identifier = new Label(billIds[index]);
            tf.setTooltip(new Tooltip("Geben sie hier die Anzahl an " + billIds[index].replace(":", 
                "") + "-Scheinen ein."));
            billFields.add(index, tf);
            billFieldRes.add(index, result);
            grid.add(identifier, 3 * i, k);
            grid.add(tf, 3 * i + 1, k);
            grid.add(result, 3 * i + 2, k);
          }
        }
      }
      /*
       * Since we used 4 Rows (0 - 3), rowIndex will be set to 4, which is the index of the first 
       * empty row below the Area.
       */
      rowIndex = 4;
      
      /*
       * Sets the minimum Height and Width for the Stage.
       */
      primaryStage.setMinHeight(450.0);
      primaryStage.setMinWidth(615.0);
      primaryStage.setHeight(450);
    } else {
      /*
       * Creates the Input Area for the extended design. This looks like the following:
       * |-------|     |-------|
       * |  1ct  |     |   5€  |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  2ct  |     |  10€  |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  5ct  |     |  20€  |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  10ct |     |  50€  |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  20ct |     |  100€ |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |  50ct |     |  200€ |
       * |-------|     |-------|
       * 
       * |-------|     |-------|
       * |   1€  |     |  500€ |
       * |-------|     |-------|
       * 
       * |-------|
       * |   2€  |
       * |-------|
       */
      for (int i = 0; i <= 7; i++) {
        Label id = new Label(coinIds[i]);
        grid.add(id, 0, i);
        
        TextField tf = new TextField("0");
        tf.setTooltip(new Tooltip("Geben sie hier die Anzahl an " + coinIds[i] 
            + "-Münzen ein."));
        grid.add(tf, 1, i);
        coinFields.add(i, tf);
        
        Label res = new Label("= 0,00€");
        grid.add(res, 2, i);
        coinFieldRes.add(i, res);
        
        if (i < 7) {
          id = new Label(billIds[i]);
          grid.add(id, 3, i);
          
          tf = new TextField("0");
          tf.setTooltip(new Tooltip("Geben sie hier die Anzahl an " + billIds[i] 
              + "-Scheinen ein."));
          grid.add(tf, 4, i);
          billFields.add(i, tf);
          
          res = new Label("= 0,00€");
          grid.add(res, 5, i);
          billFieldRes.add(i, res);
        }
      }
      
      /*
       * Since we used 8 Rows for the Input Area, rowIndex is set to 8.
       */
      rowIndex = 8;
      
      /*
       * Sets the minimum Height and Width for the Stage.
       */
      primaryStage.setMinHeight(550.0);
      primaryStage.setMinWidth(615.0);
      primaryStage.setHeight(550);
    }
    /*
     * Adds a Separator to the Grid below the Input Area for the coins and bills.
     */
    Separator sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    /*
     * Increments rowIndex to allow a creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds the Row, where the User can enter the amount of Money needed, that was calculated by 
     * the register's system, as well as the amount of purses counted. At the end of the Row, there 
     * will be a Button to fully reset the input.
     */
    Label id = new Label("Kassenschnitt Bar:");
    grid.add(id, 0, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    GridPane caGrid = new GridPane();
    caGrid.setHgap(5);
    cashNeeded = new TextField("0,00");
    caGrid.add(cashNeeded, 0, 0);
    Label euro = new Label("€");
    euro.setMaxWidth(25);
    caGrid.add(euro, 1, 0);
    
    grid.add(caGrid, 2, rowIndex);
    
    id = new Label("Geldbörsen:");
    grid.add(id, 3, rowIndex);
    purses = new TextField("0");
    purses.getStyleClass().add("purseTF");
    grid.add(purses, 4, rowIndex);
    
    /*
     * Adds the Button to the Row. This will have an unique EventHandler for when the User clicked 
     * it, as well as a special Font, since if the standard Font would be used, this Button would 
     * be to wide for the Column it is placed in.
     */
    Button btReset = new Button("Reset (Doppelklick)");
    btReset.setStyle("-fx-font: 10 Tahoma; -fx-font-weight: bold;");
    btReset.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        /*
         * To prevent Missclicks, this Button only resets the Input, if it was doubleclicked.
         */
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
          for (TextField tf : coinFields) {
            tf.setText("0");
          }
          for (TextField tf : billFields) {
            tf.setText("0");
          }
        }
      }       
    });
    grid.add(btReset, 5, rowIndex);
    
    /*
     * Increments rowIndex to allow a creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds a Separator below the Input Area for cashNeeded and purses.
     */
    sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    /*
     * Increments rowIndex to allow a creation of a new Row.
     */
    rowIndex++;
    
    /*
     * Adds a second Separator below the other one to indicate, that the input area for the User 
     * is above and below these separators, the Application will display its calculations.
     */
    sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    /*
     * Increments rowIndex, so the index of the Row below the newly created Area will be returned..
     */
    rowIndex++;
    
    /*
     * Returns rowIndex.
     */
    return rowIndex;
  }
  
  /**
   * Checks, if the Simple design of this application should be used.
   * @return The boolean value, if the simple design should be used.
   * @since 1.0
   */
  private boolean checkSimple() {
    /*
     * Opens the File for the Settings and checks, if the Simple design should be used, which is 
     * specified in the first row.
     */
    Path path = Paths.get("data/Settings.stg");
    FileReader fr;
    try {
      fr = new FileReader(path.toString());
      BufferedReader br = new BufferedReader(fr);
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
    }
    return false;
  }
  
  /**
   * Calculates the current input.
   * @since 1.0
   */
  public void calc() {
    /*
     * A BigDecimal for the coinage Money.
     */
    BigDecimal coinage;
    /*
     * If the simple design was used, the total coinage money is already entered in the first 
     * TextField of coinFields.
     */
    if (simple) {
      /*
       * Resets the Style of the TextField, since it might be altered in case a wrong input was 
       * given before.
       */
      coinFields.get(0).getStyleClass().clear();
      coinFields.get(0).getStyleClass().addAll("text-field","text-input");
      /*
       *Gets the Text of the coinage Field and replaces all Non-Digits. 
       */
      String str = coinFields.get(0).getText();
      str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
      /*
       * If there are more than 1 chars that equal '.' or ',' then make the TextField red to show 
       * that there was an Error in the input. This will also cancel the operation.
       */
      int count = 0;
      for (char c : str.toCharArray()) {
        if (c == '.' || c == ',') {
          count++;
        }
        if (count > 1) {
          coinFields.get(0).getStyleClass().clear();
          coinFields.get(0).getStyleClass().addAll("text-field","text-input", "wrongTF");
          return;
        }
      }
      /*
       * Sets the Text of the TextField and sets coinage to the input if it was correct or 0.00 if 
       * not.
       */
      coinFields.get(0).setText(str.replace('.', ','));
      String s = str.replace(',', '.').trim();
      if (s.equals("") || s == null) {
        s = "0.00";
      }
      coinage = new BigDecimal(s);
      coinage = coinage.setScale(2, RoundingMode.DOWN);
      coinFieldRes.get(0).setText("= " + coinage.toString().replace('.', ',') + "€");
      /*
       * If the extended Design was used, we have to calculate the coinage money by using every 
       * TextField for the coins.
       */
    } else {
      coinage = new BigDecimal("0.00");
      /*
       * Iterates over each TextField to add it to coinage.
       */
      for (int i = 0; i <= 7; i++) {
        /*
         * Creates an Integer, where the entered value can be stored in, as well as a String, 
         * which stores the Text entered in TextField with all nondigits removed.
         */
        int value;
        String str = coinFields.get(i).getText();
        str = str.replaceAll("[\\D]", "");
        /*
         * Parses the Text of the TextField to an Integer.
         */
        try {
          value = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
          value = 0;
        }
        /*
         * Stores the result of amount*factor for this TextField in a new BigDecimal, sets it scale 
         * to 2 to prevent too long floating points and adds it to coinage. Afterwards, the 
         * corresponding Label will display the amount of Money for this coin.
         */
        BigDecimal tmp = new BigDecimal(value * getCoinFactor(i));
        tmp = tmp.setScale(2, RoundingMode.DOWN);
        coinage = coinage.add(tmp);
        coinFieldRes.get(i).setText("= " + tmp.toString().replace('.', ',') + "€");
      }
      /*
       * Sets the Scale of coinage to 2 to prevent too long floating points.
       */
      coinage = coinage.setScale(2, RoundingMode.DOWN);
    }
    
    /*
     * Adds all billsFields to a new BigDecimal in the same way as the coinFields to coinage in 
     * the extended design.
     */
    BigDecimal bills = new BigDecimal("0.00");
    for (int i = 0; i <= 6; i++) {
      int value;
      String str = billFields.get(i).getText();
      str = str.replaceAll("[\\D]", "");
      try {
        value = Integer.parseInt(str);
      } catch (NumberFormatException nfe) {
        value = 0;
      }
      BigDecimal tmp = new BigDecimal(value * getBillFactor(i));
      tmp = tmp.setScale(2, RoundingMode.DOWN);
      bills = bills.add(tmp);
      billFieldRes.get(i).setText("= " + tmp.toString().replace('.', ',') + "€");
    }
    bills = bills.setScale(2, RoundingMode.DOWN);
    
    /*
     * Resets the Style of the cashNeeded TextField, if it was altered before to show a false input.
     */
    cashNeeded.getStyleClass().clear();
    cashNeeded.getStyleClass().addAll("text-field","text-input");
    /*
     *Gets the Text of the coinage Field and replaces all Non-Digits. 
     */
    String str = cashNeeded.getText();
    str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
    /*
     * If there are more than 1 chars that equal '.' or ',' then make the TextField red to show 
     * that there was an Error in the input. This will also cancel the operation.
     */
    int count = 0;
    for (char c : str.toCharArray()) {
      if (c == '.' || c == ',') {
        count++;
      }
      if (count > 1) {
        cashNeeded.getStyleClass().clear();
        cashNeeded.getStyleClass().addAll("text-field","text-input", "wrongTF");
        return;
      }
    }   
    
    /*
     * Sets the Text of cashNeeded.
     */
    cashNeeded.setText(str.replace('.', ','));
    
    /*
     * Stores the value of cashNeeded in a new BigDecimal.
     */
    double value;
    try {
      value = Double.parseDouble(str.replace(',', '.'));
    } catch (NumberFormatException nfe) {
      value = 0;
    }
    BigDecimal cashNeededDec = new BigDecimal(value);
    cashNeededDec = cashNeededDec.setScale(2, RoundingMode.DOWN);   
    
    /*
     * Calculates the amount of coinage, that has to be in each purse and was in it beforehand.
     */
    int coinNecessity;
    try {
      coinNecessity = Integer.parseInt(purses.getText()) * 25;
    } catch (NumberFormatException nfe) {
      coinNecessity = 0;
    }
    
    /*
     * Creates a new BigDecimal to calculate the difference between the coinage in the purses and 
     * the amount of needed coinage.
     */
    BigDecimal coinageDiff = coinage.subtract(new BigDecimal(coinNecessity));
    
    /*
     * There is a given discrepancy of coinage of 25€. When counting the purses, this discrepancy 
     * will be fixed, by taking the amount of Euro from the Bill Money. So we have to subtract the 
     * coin Difference from the billMoney. The result will be displayed in the belonging Label.
     */
    BigDecimal revenueWithTips = bills.add(coinageDiff);
    
    /*
     * Calculates the tip in the Purses by subtracting Cash Necessity from the revenue calculated 
     * before.
     */
    BigDecimal tips = revenueWithTips.subtract(cashNeededDec);
    
    /*
     * A new BigDecimal for the total amount of Money in the purses.
     */
    BigDecimal totalEuros = coinage.add(bills);
    /*
     * Updates all Labels to show their values.
     */
    labels.get(billsSumId).setText(bills.toString().replace('.', ',') + "€");
    labels.get(coinageSumId).setText(coinage.toString().replace('.', ',') + "€");
    labels.get(totalSumId).setText(totalEuros.toString().replace('.', ',') + "€");
    labels.get(coinageNeededId).setText(coinNecessity + ",00€");
    labels.get(diffCoinageId).setText(coinageDiff.toString().replace('.', ',') + "€");
    labels.get(coinageSumCalculatedId).setText(revenueWithTips.toString().replace('.', ',') + "€");
    labels.get(cashNeededId).setText(cashNeededDec.toString().replace('.', ',') + "€");
    labels.get(tipSumId).setText(tips.toString().replace('.', ',') + "€");
    
    /*
     * Updates the Tooltips for all Labels.
     */
    for (Label lb : labels.values()) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    
    for (Label lb : billFieldRes) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    
    for (Label lb : coinFieldRes) {
      lb.setTooltip(new Tooltip(lb.getText()));
    }
    /*
     * Compares the coinage difference BigDecimal to 0 to check if it's negative or positive. If 
     * it's negative, the Label will display it's Text in red, else in normal black.
     */
    if (coinageDiff.compareTo(new BigDecimal("0.0")) == -1) {
      labels.get(diffCoinageId).getStyleClass().clear();
      labels.get(diffCoinageId).getStyleClass().addAll("minusLabel");
    } else {
      labels.get(diffCoinageId).getStyleClass().clear();
      labels.get(diffCoinageId).getStyleClass().addAll("label");
    }

    /*
     * Compares the Tip Sum BigDecimal to 0 to check if it's negative or positive. If 
     * it's negative, the Label will display it's Text in red, else in normal black.
     */
    if (tips.compareTo(new BigDecimal("0.0")) < 0) {
      labels.get(tipSumId).getStyleClass().clear();
      labels.get(tipSumId).getStyleClass().addAll("minusLabel");
    } else {
      labels.get(tipSumId).getStyleClass().clear();
      labels.get(tipSumId).getStyleClass().addAll("label");
    }
  }
  
  /**
   * Returns the Factor for the bill with the given index. If index isn't in the Interval [0,6], 
   * 0 will be returned as default.
   * @param index The index of the factor to be returned.
   * @return  The Factor for the given index.
   * @since 1.0
   */
  private int getBillFactor(int index) {
    switch (index) {
      case 0: return 5;
      case 1: return 10;
      case 2: return 20;
      case 3: return 50;
      case 4: return 100;
      case 5: return 200;
      case 6: return 500;
      default: return 0; 
    }
  }
  
  /**
   * Returns the Factor for the coin with the given index. If index isn't in the Interval [0,7], 
   * 0 will be returned as default.
   * @param index The index of the factor to be returned.
   * @return  The Factor for the given index.
   * @since 1.0
   */
  private double getCoinFactor(int index) {
    switch (index) {
      case 0: return 0.01;
      case 1: return 0.02;
      case 2: return 0.05;
      case 3: return 0.1;
      case 4: return 0.2;
      case 5: return 0.5;
      case 6: return 1;
      case 7: return 2;
      default: return 0;
    }
  }
  
  /**
   * Returns, if the simple design is used.
   * @return {@code true}, if the simple design is used, {@code false} if the extended design is 
   *     used.
   * @since 1.0
   */
  public boolean isSimple() {
    return simple;
  }

  /**
   * Returns {@link #coinFields}, an ArrayList of all TextFields used for the coins.
   * @return All TextFields, where the User entered the amount of coins as an ArrayList.
   * @since 1.0
   */
  public ArrayList<TextField> getCoinFields() {
    return coinFields;
  }

  /**
   * Returns {@link #billFields}, an ArrayList of all TextFields used for the bills.
   * @return All TextFields, where the User entered the amount of bills as an ArrayList.
   * @since 1.0
   */
  public ArrayList<TextField> getBillFields() {
    return billFields;
  }

  /**
   * Returns {@link #coinFieldRes}, an ArrayList of all Labels used for the coins values.
   * @return All Labels, where the Application entered the value of the amount of each coin.
   * @since 1.0
   */
  public ArrayList<Label> getCoinFieldRes() {
    return coinFieldRes;
  }

  /**
   * Returns {@link #billFieldRes}, an ArrayList of all Labels used for the bills values.
   * @return All Labels, where the Application entered the value of the amount of each bill.
   * @since 1.0
   */
  public ArrayList<Label> getBillFieldRes() {
    return billFieldRes;
  }

  /**
   * Returns {@link #cashNeeded}, the TextField, where the User entered the amount of cash needed, 
   * that was calculated by the register's system.
   * @return The TextField, where the User entered the amount of cash needed.
   * @since 1.0
   */
  public TextField getCashNeeded() {
    return cashNeeded;
  }

  /**
   * Returns {@link #purses}, the TextField, where the User entered the amount of purses counted.
   * @return The TextField, where the User entered the amount of purses counted.
   * @since 1.0
   */
  public TextField getPurses() {
    return purses;
  }

  /**
   * Returns all Labels, that might be altered by the Application. These Labels are the ones, that 
   * display the results of the calculations done in {@link #calc()}. To access the Labels use 
   * {@link #coinageSumId} etc.
   * @return All alterable Labels as a HashMap of String, Label.
   * @since 1.0
   */
  public HashMap<String, Label> getLabels() {
    return labels;
  }
  
}
