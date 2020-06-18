package monthlybalance;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import monthlybalance.handlers.ExportHandler;
import monthlybalance.listener.TextFieldFocusChangedListener;
import monthlybalance.listener.TextFieldTextChangedListener;
import util.Util;

/**
 * The Window, where the User can enter the Data, that is needed to calculate the Monthly Balance.
 * @author Haeldeus
 * @version 1.0
 */
public class MBalanceWindow extends Application {

  /**
   * The TextFields for the amount of registers as an ArrayList.
   */
  private ArrayList<TextField> registerFields;
  
  /**
   * The TextFields for the amount of stored rouleaus as an ArrayList.
   */
  private ArrayList<TextField> rouleauFields;
  
  /**
   * The Labels, where the Sum for each rouleau can be displayed in as an ArrayList.
   */
  private ArrayList<Label> rouleauLabels;
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Creates a GridPane, that contains all Other Panes and Nodes.
     */
    TabPane tabs = new TabPane();
    tabs.getTabs().addAll(getTabs());
    tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    
    /*
     * Creates a MenuBar, a MenuBar and a MenuItem, so the User can export the Data.
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
     * Creates a BorderPane, that contains the total GridPane. This is done, so that the Content 
     * of the GridPane can be displayed at the Center of the Scene.
     */
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menu);
    borderPane.setCenter(tabs);
    
    /*
     * Sets the Size of the Scene, it's restrictions and the Stylesheet. Afterwards, it displays 
     * the primaryStage to the User.
     */
    Scene scene = new Scene(borderPane, 500, 550);
    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setTitle("Auﬂentheke");
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(595.0);
    primaryStage.setMinWidth(615.0);
    primaryStage.show();
  }

  /**
   * Creates and returns all Tabs for the TabPane.
   * @return  An ArrayList of Tabs for the TabPane.
   * @since 1.0
   * @see #getRegisterTab()
   */
  private ArrayList<Tab> getTabs() {
    ArrayList<Tab> res = new ArrayList<Tab>();
    res.add(getRegisterTab());
    res.add(getRouleauTab());
    return res;
  }
  
  /**
   * Creates and returns the Register Tab for the TabPane.
   * @return  A new Tab, that contains all Nodes needed for the User to enter the registers.
   * @since 1.0
   */
  private Tab getRegisterTab() {
    /*
     * Creates a new ArrayList, where the TextFields can be stored in.
     */
    registerFields = new ArrayList<TextField>();
    /*
     * Creates a GridPane to display all Nodes for this Tab.
     */
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    
    /*
     * Adds Columns to the GridPane, so they can be configured.
     */
    for (int i = 0; i <= 3; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
    }
    /*
     * Configures the Width for all columns, so they match the wanted design.
     */
    grid.getColumnConstraints().get(0).setMinWidth(60);
    grid.getColumnConstraints().get(1).setMaxWidth(60);
    grid.getColumnConstraints().get(2).setMinWidth(100);
    grid.getColumnConstraints().get(2).setMaxWidth(300);
    grid.getColumnConstraints().get(3).setMinWidth(100);

    /*
     * Creates the Label for the purses.
     */
    Label purses = new Label("Geldbˆrsen mit Bestand:");
    grid.add(purses, 0, 0);
    
    /*
     * Creates the TextField for the purses.
     */
    TextField tfPurses = new TextField();
    tfPurses.setTooltip(new Tooltip("Geben Sie hier die Anzahl der Geldbˆrsen mit Bestand ein."));
    grid.add(tfPurses, 1, 0);
    
    /*
     * Creates the Label, where the Sum in each Purse is displayed.
     */
    Label pursesInfo = new Label("Bestand: 325Ä");
    grid.add(pursesInfo, 3, 0);
    
    /*
     * Creates the Label for the external registers.
     */
    Label externalRegisters = new Label("Ath Kassen mit Bestand:");
    grid.add(externalRegisters, 0, 2);
    
    /*
     * Creates the TextField for the external registers.
     */
    TextField tfExternalRegisters = new TextField();
    tfExternalRegisters.setTooltip(new Tooltip("Geben Sie hier die Anzahl der Auﬂentheken Kassen "
        + "mit Bestand ein."));
    grid.add(tfExternalRegisters, 1, 2);
    
    /*
     * Creates the Label, where the Sum in each external Register is displayed.
     */
    Label externalRegisterInfo = new Label("Bestand: 462Ä");
    grid.add(externalRegisterInfo, 3, 2);
    
    /*
     * Creates a new Tab to store the GridPane and returns it.
     */
    Tab registerTab = new Tab();
    registerTab.setText("Bˆrsen & Kassen");
    registerTab.setContent(grid);
    return registerTab;
  }
  
  /**
   * Creates and returns the Rouleau Tab for the TabPane.
   * @return  A new Tab, that contains all Nodes needed for the User to enter the rouleaus.
   * @since 1.0
   */
  private Tab getRouleauTab() {
    /*
     * Creates a new ArrayList, where the TextFields can be stored in.
     */
    rouleauFields = new ArrayList<TextField>();
    /*
     * Creates a new ArrayList, where the Labels can be stored in.
     */
    rouleauLabels = new ArrayList<Label>();
    /*
     * Creates a GridPane to display all Nodes for this Tab.
     */
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    
    /*
     * Adds Columns to the GridPane, so they can be configured.
     */
    for (int i = 0; i <= 5; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
      grid.getColumnConstraints().get(i).setHalignment(HPos.CENTER);
    }
    /*
     * Configures the Width for all columns, so they match the wanted design.
     */
    grid.getColumnConstraints().get(0).setMinWidth(60);
    grid.getColumnConstraints().get(1).setMaxWidth(60);
    grid.getColumnConstraints().get(2).setMinWidth(50);
    grid.getColumnConstraints().get(2).setMaxWidth(50);
    grid.getColumnConstraints().get(3).setMinWidth(50);
    grid.getColumnConstraints().get(3).setMaxWidth(50);
    grid.getColumnConstraints().get(4).setMinWidth(80);
    grid.getColumnConstraints().get(4).setMaxWidth(80);
    grid.getColumnConstraints().get(5).setMinWidth(50);
    
    /*
     * The Values for the Cells with given Content. This makes the creation easier.
     */
    String[] rouleauBase = {"0,10Ä", "0,20Ä", "0,50Ä", "1Ä", "2Ä"};
    String[] rouleauCoinAmount = {"(40x)", "(40x)", "(40x)", "(25x)", "(25x)"};
    String[] rouleauSum = {"4Ä", "8Ä", "20Ä", "25Ä", "50Ä"};
    
    /*
     * Creates the first Row, where basic Information is displayed.
     */
    Label lbChange = new Label("Wechselgeld:");
    grid.add(lbChange, 0, 0);
    
    Label lbRoleau = new Label("Rollen:");
    grid.add(lbRoleau, 1, 0);
    
    Label lbAmount = new Label("Anzahl:");
    grid.add(lbAmount, 3, 0);
    
    /*
     * Creates the Area, where the User can enter the amount of each rouleau,
     */
    for (int i = 0; i < 5; i++) {
      Label lbCoinAmount = new Label(rouleauCoinAmount[i]);
      grid.add(lbCoinAmount, 0, i + 1);
      
      Label lbRouleauBase = new Label(rouleauBase[i]);
      grid.add(lbRouleauBase, 1, i + 1);
      
      Label lbRouleauSum = new Label(rouleauSum[i]);
      grid.add(lbRouleauSum, 2, i + 1);
      
      TextField tfAmount = new TextField();
      tfAmount.focusedProperty().addListener(new TextFieldFocusChangedListener(tfAmount, this));
      tfAmount.textProperty().addListener(new TextFieldTextChangedListener(this));
      grid.add(tfAmount, 3, i + 1);
      rouleauFields.add(tfAmount);
      
      Label lbSum = new Label("Summe:");
      grid.add(lbSum, 4, i + 1);
      
      Label lbSumDisplay = new Label("0,00Ä");
      grid.add(lbSumDisplay, 5, i + 1);
      rouleauLabels.add(lbSumDisplay);
    }
    
    /*
     * Creates a new Tab to store the GridPane and returns it.
     */
    Tab rouleauTab = new Tab();
    rouleauTab.setText("Geldrollen");
    rouleauTab.setContent(grid);
    return rouleauTab;
  }
  
  public void calc() {
    //TODO
    System.out.println("Calced");
  }
  
  /**
   * Returns the registerFields, that were created in {@link #getRegisterTab()}.
   * @return The TextFields for the Registers as an ArrayList.
   * @since 1.0
   */
  public ArrayList<TextField> getRegisterFields() {
    return registerFields;
  }

  /**
   * Returns the rouleauFields, that were created in {@link #getRouleauTab()}.
   * @return The TextFields for the rouleaus as an ArrayList.
   * @since 1.0
   */
  public ArrayList<TextField> getRouleauFields() {
    return rouleauFields;
  }

  public ArrayList<Label> getRouleauLabels() {
    return rouleauLabels;
  }
}
