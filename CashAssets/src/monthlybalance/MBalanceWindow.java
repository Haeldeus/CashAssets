package monthlybalance;

import java.math.BigDecimal;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
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
   * The Labels, where the Sum for each register type is displayed as an ArrayList.
   */
  private ArrayList<Label> registerLabels;
  
  /**
   * The TextFields for the amount of stored rouleaus as an ArrayList.
   */
  private ArrayList<TextField> rouleauFields;
  
  /**
   * The Labels, where the Sum for each rouleau can be displayed in as an ArrayList.
   */
  private ArrayList<Label> rouleauLabels;
  
  /**
   * The TextFields for the amount of coinage as an ArrayList.
   */
  private ArrayList<TextField> coinageFields;
  
  /**
   * The Labels, where the Sum for each coinage type is displayed as an ArrayList.
   */
  private ArrayList<Label> coinageLabels;
  
  /**
   * The TextFields for the amount of bills as an ArrayList.
   */
  private ArrayList<TextField> billFields;
  
  /**
   * The Labels, where the Sum for each bill type is displayed as an ArrayList.
   */
  private ArrayList<Label> billLabels;
  
  /**
   * All Labels, that display the total Sum for each Pane at the Bottom of the Scene.
   */
  private ArrayList<Label> sumLabels;
  
  /**
   * The {@link KeyCombination} for Ctrl+Up.
   */
  private final KeyCombination kcUp = new KeyCodeCombination(KeyCode.UP, 
      KeyCombination.CONTROL_DOWN);
  
  /**
   * The {@link KeyCombination} for Ctrl+Down.
   */
  private final KeyCombination kcDown = new KeyCodeCombination(KeyCode.DOWN, 
      KeyCombination.CONTROL_DOWN);
  
  /**
   * The {@link KeyCombination} for Ctrl+Plus.
   */
  private final KeyCombination kcPlus = new KeyCodeCombination(KeyCode.PLUS, 
      KeyCombination.CONTROL_DOWN);
  
  /**
   * The {@link KeyCombination} for Ctrl+Minus.
   */
  private final KeyCombination kcMinus = new KeyCodeCombination(KeyCode.MINUS, 
      KeyCombination.CONTROL_DOWN);
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Creates a new ArrayList to store the sum-Labels in.
     */
    sumLabels = new ArrayList<Label>();
    /*
     * Creates a TabPane, that contains all Tabs needed for the Monthly Balance. The creation of 
     * the Tabs itself is done in getTabs(). Also, since we don't want the Tabs to be closable, we 
     * make this option unavailable.
     */
    TabPane tabs = new TabPane();
    tabs.getTabs().addAll(getTabs());
    tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    
    /*
     * Sets KeyHandlers to all TextFields.
     */
    for (int i = 0; i <= 7; i++) {
      /*
       * The index has to be finalized to be able to configure the handle-Methods while iterating 
       * over the list.
       */
      final int j = i;
      /*
       * Since there are only 2 Register TextFields, we have to make sure that when the index 
       * reaches 2, we don't try to configure another register Field.
       */
      if (j <= 1) {
        /* GENERAL DISCLAIMER:
         * We are using Math.floorMod(x,y) to make sure, we request Focus with the correct 
         * Field, when the KeyCombination is pressed. Since in Standard Java (-1) % x = (-1) 
         * and not x - 1 as it should be, we have to use the specialized Method floorMod for 
         * this operation.
         */
        registerFields.get(j).setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              /*
               * Since we have 2 Register TextFields, we have to calculate with mod 2. To get the 
               * Field below the currently selected, we calculate j + 1 mod 2, so it jumps to the 
               * top if the lowest Field was selected.
               */
              registerFields.get(Math.floorMod(j + 1, 2)).requestFocus();
              /*
               * Consuming the event, so it doesn't get passed to the scene itself. Because in this 
               * case, it's KeyHandler would handle it as well, and would override this handling.
               */
              event.consume();
            } else if (kcUp.match(event)) {
              /*
               * Calculating j - 1 mod 2, to get to the Field above or the lowest Field, if the 
               * Field on top was selected.
               */
              registerFields.get(Math.floorMod(j - 1, 2)).requestFocus();
              /*
               * Consuming the Event.
               */
              event.consume();
            } else if (kcPlus.match(event)) {
              /*
               * Changes the Tab to the next Tab to the right. If the Tab on the rightmost side 
               * was already selected, the first one will be selected. The first Field of that Tab 
               * will select Focus as well.
               */
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() + 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              /*
               * Consuming the Event.
               */
              event.consume();
            } else if (kcMinus.match(event)) {
              /*
               * Changes the Tab to the next Tab to the left. If the Tab on the leftmost side was 
               * already selected, the last one will be selected. The first Field of that Tab will 
               * request Focus as well.
               */
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() - 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              /*
               * Consuming the Event.
               */
              event.consume();
            }
          }
        });
      }
      /*
       * Since there are only 5 Rouleau TextFields, we have to make sure that when the index 
       * reaches 5, we don't try to configure another Rouleau Field.
       */
      if (j <= 4) {
        rouleauFields.get(j).setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              /*
               * See Documentation for the RegisterFields for Information. The Calculation is 
               * the same, only the residue class changes, since we have to calculate with mod 5.
               */
              rouleauFields.get(Math.floorMod(j + 1, 5)).requestFocus();
              event.consume();
            } else if (kcUp.match(event)) {
              rouleauFields.get(Math.floorMod(j - 1, 5)).requestFocus();
              event.consume();
            } else if (kcPlus.match(event)) {
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() + 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              event.consume();
            } else if (kcMinus.match(event)) {
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() - 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              event.consume();
            }
          }
        });
      }
      /*
       * Since there are only 7 BillTextFields, we have to make sure that when the index 
       * reaches 7, we don't try to configure another Bill Field.
       */
      if (j <= 6) {
        billFields.get(j).setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              /*
               * See Documentation for the RegisterFields for Information. The Calculation is 
               * the same, only the residue class changes, since we have to calculate with mod 7.
               */
              billFields.get(Math.floorMod(j + 1, 7)).requestFocus();
              event.consume();
            } else if (kcUp.match(event)) {
              billFields.get(Math.floorMod(j - 1, 7)).requestFocus();
              event.consume();
            } else if (kcPlus.match(event)) {
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() + 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              event.consume();
            } else if (kcMinus.match(event)) {
              tabs.getSelectionModel().select(Math.floorMod(
                  tabs.getSelectionModel().getSelectedIndex() - 1, 4));
              getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
              event.consume();
            }
          }
        });
      }
      
      /*
       * Since the for-loop is configured to stop after 8 iterations, we don't have to ensure, 
       * that no more than 8 coinageFields are configured.
       */
      coinageFields.get(j).setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
            /*
             * See Documentation for the RegisterFields for Information. The Calculation is 
             * the same, only the residue class changes, since we have to calculate with mod 8.
             */
            coinageFields.get(Math.floorMod(j + 1, 8)).requestFocus();
            event.consume();
          } else if (kcUp.match(event)) {
            coinageFields.get(Math.floorMod(j - 1, 8)).requestFocus();
            event.consume();
          } else if (kcPlus.match(event)) {
            tabs.getSelectionModel().select(Math.floorMod(
                tabs.getSelectionModel().getSelectedIndex() + 1, 4));
            getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
            event.consume();
          } else if (kcMinus.match(event)) {
            tabs.getSelectionModel().select(Math.floorMod(
                tabs.getSelectionModel().getSelectedIndex() - 1, 4));
            getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
            event.consume();
          }
        }
      });
    }
    
    /*
     * Creates a GridPane to display the total Sum for each Tab.
     */
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 20, 10, 20));
    
    /*
     * Adds Columns to the GridPane, so they can be configured.
     */
    for (int i = 0; i <= 3; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
      grid.getColumnConstraints().get(i).setHalignment(HPos.CENTER);
    }
    
    /*
     * Creates the Area, where all Labels are shwon, that display the Sums for 
     * each Tab as well as the Total Sum.
     */
    Label totalSumsLabel = new Label("Gesamtsummen:");
    grid.add(totalSumsLabel, 1, 0);
    GridPane.setColumnSpan(totalSumsLabel, 2);
    
    Label sumPursesLabel = new Label("Börsen & Kassen");
    grid.add(sumPursesLabel, 0, 1);
    
    Label sumPurses = new Label("0,00€");
    grid.add(sumPurses, 0, 2);
    sumLabels.add(sumPurses);
    
    Label sumRouleausLabel = new Label("Münzrollen");
    grid.add(sumRouleausLabel, 1, 1);
    
    Label sumRouleaus = new Label("0,00€");
    grid.add(sumRouleaus, 1, 2);
    sumLabels.add(sumRouleaus);
    
    Label sumCoinageLabel = new Label("Münzgeld");
    grid.add(sumCoinageLabel, 2, 1);
    
    Label sumCoinage = new Label("0,00€");
    grid.add(sumCoinage, 2, 2);
    sumLabels.add(sumCoinage);
    
    Label sumBillsLabel = new Label("Geldscheine");
    grid.add(sumBillsLabel, 3, 1);
    
    Label sumBills = new Label("0,00€");
    grid.add(sumBills, 3, 2);
    sumLabels.add(sumBills);
    
    Label sumTotalLabel = new Label("Gesamtsumme:");
    grid.add(sumTotalLabel, 0, 3);
    GridPane.setColumnSpan(sumTotalLabel, 2);
    
    Label sumTotal = new Label("0,00€");
    grid.add(sumTotal, 2, 3);
    GridPane.setColumnSpan(sumTotal, 2);
    GridPane.setHalignment(sumTotal, HPos.LEFT);
    sumLabels.add(sumTotal);
    
    
    /*
     * Creates a MenuBar, a Menu and a MenuItem, so the User can export the Data.
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
    borderPane.setBottom(grid);
    
    /*
     * Creates the Scene, which will display the created content and configures it's Stylesheet.
     */
    Scene scene = new Scene(borderPane, 500, 550);
    scene.getStylesheets().add(Util.getControlStyle());
    
    /*
     * Adds a KeyHandler to the Scene, so it reacts to some of the User's Key presses.
     */
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
          /*
           * Checks if 'Enter' or the KeyCombination for Field-Up or Field-Down is pressed.
           */
          if (event.getCode() == KeyCode.ENTER || kcUp.match(event) || kcDown.match(event)) {
            /*
             * Requests Focus for the first Field in the currently selected Tab.
             */
            getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
            /*
             * Checks, if the KeyCombination for Next-Tab is pressed.
             */
          } else if (kcPlus.match(event)) {
            /*
             * Switches to the next Tab to the right (first One, if the rightmost Tab was already 
             * selected) and requests Focus for it's first Field.
             */
            tabs.getSelectionModel().select(Math.floorMod(
                tabs.getSelectionModel().getSelectedIndex() + 1, 4));
            getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
            /*
             * Checks, if the KeyCombination for Previous-Tab is pressed.
             */
          } else if (kcMinus.match(event)) {
            /*
             * Switches to the next Tab to the left (first One, if the leftmost Tab was already 
             * selected) and requests Focus for it's first Field.
             */
            tabs.getSelectionModel().select(Math.floorMod(
                tabs.getSelectionModel().getSelectedIndex() - 1, 4));
            getFieldForIndex(tabs.getSelectionModel().getSelectedIndex()).requestFocus();
          }
      }      
    });
    /*
     * Basic Stage-Settings.
     */
    primaryStage.setTitle("Monatsabrechnung");
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
    res.add(getCoinageTab());
    res.add(getBillTab());
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
     * Create a new ArrayList, where the Labels can be stored in.
     */
    registerLabels = new ArrayList<Label>();
    
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
    for (int i = 0; i <= 4; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
      grid.getColumnConstraints().get(i).setHalignment(HPos.CENTER);
    }
    /*
     * Configures the Width and Alignment for all columns, so they match the wanted design.
     */
    grid.getColumnConstraints().get(0).setMinWidth(60);
    grid.getColumnConstraints().get(1).setMaxWidth(60);
    grid.getColumnConstraints().get(2).setMinWidth(100);
    grid.getColumnConstraints().get(2).setMaxWidth(300);
    grid.getColumnConstraints().get(3).setMinWidth(50);
    grid.getColumnConstraints().get(3).setMaxWidth(50);
    grid.getColumnConstraints().get(4).setMinWidth(100);
    grid.getColumnConstraints().get(4).setMaxWidth(100);
    grid.getColumnConstraints().get(4).setHalignment(HPos.LEFT);

    /*
     * Creates the Label for the purses.
     */
    Label purses = new Label("Geldbörsen mit Bestand:");
    grid.add(purses, 0, 0);
    
    /*
     * Creates the TextField for the purses.
     */
    TextField tfPurses = new TextField("0");
    tfPurses.setAlignment(Pos.CENTER);
    tfPurses.setTooltip(new Tooltip("Geben Sie hier die Anzahl der Geldbörsen mit Bestand ein."));
    tfPurses.focusedProperty().addListener(new TextFieldFocusChangedListener(tfPurses, this));
    tfPurses.textProperty().addListener(new TextFieldTextChangedListener(this));
    grid.add(tfPurses, 1, 0);
    registerFields.add(tfPurses);
    
    /*
     * Creates the Label, where the Sum in each Purse is displayed.
     */
    Label pursesInfo = new Label("(Bestand: 325€)");
    grid.add(pursesInfo, 2, 0);
    
    /*
     * Creates the Label for the external registers.
     */
    Label externalRegisters = new Label("Ath Kassen mit Bestand:");
    grid.add(externalRegisters, 0, 2);
    
    /*
     * Creates the TextField for the external registers.
     */
    TextField tfExternalRegisters = new TextField("0");
    tfExternalRegisters.setAlignment(Pos.CENTER);
    tfExternalRegisters.setTooltip(new Tooltip("Geben Sie hier die Anzahl der Außentheken Kassen "
        + "mit Bestand ein."));
    tfExternalRegisters.focusedProperty().addListener(new TextFieldFocusChangedListener(
        tfExternalRegisters, this));
    tfExternalRegisters.textProperty().addListener(new TextFieldTextChangedListener(this));
    grid.add(tfExternalRegisters, 1, 2);
    registerFields.add(tfExternalRegisters);
    
    /*
     * Creates the Label, where the Sum in each external Register is displayed.
     */
    Label externalRegisterInfo = new Label("(Bestand: 462€)");
    grid.add(externalRegisterInfo, 2, 2);
    
    /*
     * Adds 2 Labels to the Grid, which will only display a simple Text.
     */
    grid.add(new Label("Summe:"), 3, 0);
    grid.add(new Label("Summe:"), 3, 2);
    
    /*
     * Creates a Label, where the total Sum of Money in the purses will be displayed after 
     * calculation.
     */
    Label purseSum = new Label("0,00€");
    registerLabels.add(purseSum);
    grid.add(purseSum, 4, 0);
    
    /*
     * Creates a Label, where the total Sum of Money in the external Registers will be displayed
     * after calculation.
     */
    Label externalSum = new Label("0,00€");
    registerLabels.add(externalSum);
    grid.add(externalSum, 4, 2);
    
    /*
     * Creates a new Tab to store the GridPane and returns it.
     */
    Tab registerTab = new Tab();
    registerTab.setText("Börsen & Kassen");
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
    grid.getColumnConstraints().get(4).setMinWidth(70);
    grid.getColumnConstraints().get(4).setMaxWidth(70);
    grid.getColumnConstraints().get(4).setHalignment(HPos.RIGHT);
    grid.getColumnConstraints().get(5).setMinWidth(50);
    grid.getColumnConstraints().get(5).setHalignment(HPos.LEFT);
    
    /*
     * The Values for the Cells with given Content. This makes the creation easier.
     */
    String[] rouleauBase = {"0,10€", "0,20€", "0,50€", "1€", "2€"};
    String[] rouleauCoinAmount = {"(40x)", "(40x)", "(40x)", "(25x)", "(25x)"};
    String[] rouleauSum = {"(= 4€)", "(= 8€)", "(= 20€)", "(= 25€)", "(= 50€)"};
    
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
    for (int i = 0; i <= 4; i++) {
      Label lbCoinAmount = new Label(rouleauCoinAmount[i]);
      grid.add(lbCoinAmount, 0, i + 1);
      
      Label lbRouleauBase = new Label(rouleauBase[i]);
      grid.add(lbRouleauBase, 1, i + 1);
      
      Label lbRouleauSum = new Label(rouleauSum[i]);
      grid.add(lbRouleauSum, 2, i + 1);
      
      TextField tfAmount = new TextField("0");
      tfAmount.setAlignment(Pos.CENTER);
      tfAmount.focusedProperty().addListener(new TextFieldFocusChangedListener(tfAmount, this));
      tfAmount.textProperty().addListener(new TextFieldTextChangedListener(this));
      tfAmount.setTooltip(new Tooltip("Geben Sie hier die Anzahl der \"" + rouleauBase[i] 
          + "\"-Geldrollen ein."));
      grid.add(tfAmount, 3, i + 1);
      rouleauFields.add(tfAmount);
      
      Label lbSum = new Label("Summe:");
      grid.add(lbSum, 4, i + 1);
      
      Label lbSumDisplay = new Label("0,00€");
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
  
  /**
   * Creates and returns the Coinage Tab for the TabPane.
   * @return  A new Tab, that contains all Nodes needed for the User to enter the coinage.
   * @since 1.0
   */
  private Tab getCoinageTab() {
    /*
     * Creates a new ArrayList for the Fields to be stored in.
     */
    coinageFields = new ArrayList<TextField>();
    /*
     * Creates a new ArrayList for the Labels to be stored in.
     */
    coinageLabels = new ArrayList<Label>();
    
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
    grid.getColumnConstraints().get(4).setMinWidth(70);
    grid.getColumnConstraints().get(4).setMaxWidth(70);
    grid.getColumnConstraints().get(4).setHalignment(HPos.RIGHT);
    grid.getColumnConstraints().get(5).setMinWidth(50);
    grid.getColumnConstraints().get(5).setHalignment(HPos.LEFT);
    
    /*
     * The Content for the Cells with given Content.
     */
    String[] coinageBase = {"0,01€", "0,02€", "0,05€", "0,10€", "0,20€", "0,50€", "1€", "2€"};
    
    /*
     * Creates the first Row, where basic Information is displayed.
     */
    Label lbChange = new Label("Wechselgeld:");
    grid.add(lbChange, 0, 0);
    
    Label lbRoleau = new Label("Lose:");
    grid.add(lbRoleau, 1, 0);
    
    Label lbAmount = new Label("Anzahl:");
    grid.add(lbAmount, 3, 0);
    
    /*
     * Creates the Area, where the User can enter the amount of each Coinage type,
     */
    for (int i = 0; i <= 7; i++) {
      Label lbCoinageBase = new Label(coinageBase[i]);
      grid.add(lbCoinageBase, 1, i + 1);
      
      TextField tfAmount = new TextField("0");
      tfAmount.setAlignment(Pos.CENTER);
      tfAmount.focusedProperty().addListener(new TextFieldFocusChangedListener(tfAmount, this));
      tfAmount.textProperty().addListener(new TextFieldTextChangedListener(this));
      tfAmount.setTooltip(new Tooltip("Geben Sie hier die Anzahl der \"" + coinageBase[i] 
          + "\"-Münzen ein."));
      grid.add(tfAmount, 3, i + 1);
      coinageFields.add(tfAmount);
      
      Label lbSum = new Label("Summe:");
      grid.add(lbSum, 4, i + 1);
      
      Label lbSumDisplay = new Label("0,00€");
      grid.add(lbSumDisplay, 5, i + 1);
      coinageLabels.add(lbSumDisplay);
    }
    
    /*
     * Creates a new Tab to store the created content in.
     */
    Tab coinageTab = new Tab();
    coinageTab.setText("Münzgeld");
    coinageTab.setContent(grid);
    return coinageTab;
  }
  
  /**
   * Creates and returns the Bill Tab for the TabPane.
   * @return  A new Tab, that contains all Nodes needed for the User to enter the bills.
   * @since 1.0
   */
  private Tab getBillTab() {
    /*
     * Creates a new ArrayList for the Fields to be stored in.
     */
    billFields = new ArrayList<TextField>();
    /*
     * Creates a new ArrayList for the Labels to be stored in.
     */
    billLabels = new ArrayList<Label>();
    
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
    grid.getColumnConstraints().get(4).setMinWidth(70);
    grid.getColumnConstraints().get(4).setMaxWidth(70);
    grid.getColumnConstraints().get(4).setHalignment(HPos.RIGHT);
    grid.getColumnConstraints().get(5).setMinWidth(50);
    grid.getColumnConstraints().get(5).setHalignment(HPos.LEFT);
    
    /*
     * The Content for the Cells with given Content.
     */
    String[] billBase = {"5€", "10€", "20€", "50€", "100€", "200€", "500€"};
    
    /*
     * Creates the first Row, where basic Information is displayed.
     */
    Label lbChange = new Label("Geldscheine:");
    grid.add(lbChange, 0, 0);
    
    Label lbAmount = new Label("Anzahl:");
    grid.add(lbAmount, 3, 0);
    
    /*
     * Creates the Area, where the User can enter the amount of each Coinage type,
     */
    for (int i = 0; i <= 6; i++) {
      Label lbBillBase = new Label(billBase[i]);
      grid.add(lbBillBase, 1, i + 1);
      
      TextField tfAmount = new TextField("0");
      tfAmount.setAlignment(Pos.CENTER);
      tfAmount.focusedProperty().addListener(new TextFieldFocusChangedListener(tfAmount, this));
      tfAmount.textProperty().addListener(new TextFieldTextChangedListener(this));
      tfAmount.setTooltip(new Tooltip("Geben Sie hier die Anzahl der \"" + billBase[i] 
          + "\"-Scheine ein."));
      grid.add(tfAmount, 3, i + 1);
      billFields.add(tfAmount);
      
      Label lbSum = new Label("Summe:");
      grid.add(lbSum, 4, i + 1);
      
      Label lbSumDisplay = new Label("0,00€");
      grid.add(lbSumDisplay, 5, i + 1);
      billLabels.add(lbSumDisplay);
    }
    
    /*
     * Creates a new Tab to store the created content in.
     */
    Tab billTab = new Tab();
    billTab.setText("Geldscheine");
    billTab.setContent(grid);
    return billTab;
  }

  /**
   * Returns the TextField, that is at the first index of the ArrayList of TextFields, which are 
   * displayed in the Tab with the given index.
   * @param selectedIndex The Index of the Tab, which contains the to be returned TextField.
   * @return  A TextField, that is displayed in the given Tab at the first Position.
   * @since 1.0
   */
  private TextField getFieldForIndex(int selectedIndex) {
    if (selectedIndex == 0) {
      return registerFields.get(0);
    } else if (selectedIndex == 1) {
      return rouleauFields.get(0);
    } else if (selectedIndex == 2) {
      return coinageFields.get(0);
    } else {
      return billFields.get(0);
    }
  }
  
  /**
   * Calculates the Sum for each of the User's inputs. This is called, whenever a Field is changed 
   * or the focus is changed.
   * @since 1.0
   */
  public void calc() {
    /*
     * Creates BigDecimals for each Tab.
     */
    BigDecimal registerSum = new BigDecimal("0.00");
    BigDecimal rouleauSum = new BigDecimal("0.00");
    BigDecimal coinageSum = new BigDecimal("0.00");
    BigDecimal billSum = new BigDecimal("0.00");
    /*
     * Iterates over all TextFields, where the User might have entered values into.
     */
    for (int i = 0; i <= 7; i++) {
      /*
       * If i <= 1 is true, we still have registerFields to add to their BigDecimal.
       */
      if (i <= 1) {
        try {
          /*
           * Stores the Text of the RegisterField at the index i with all non-digit characters.
           */
          String str = registerFields.get(i).getText().replaceAll("\\D", "");
          /*
           * Creates a new BigDecimal, that is created by getting the Factor of the current 
           * RegisterField and multiplying this with the input.
           */
          BigDecimal result = getFactor(i, FactorType.REGISTER).multiply(new BigDecimal(str));
          /*
           * Displays the result of the multiplication above in the associated Label.
           */
          registerLabels.get(i).setText(result.toString().replace('.', ',') + "€");
          /*
           * Adds the result of the multiplication to the BigDecimal for the RegisterTab.
           */
          registerSum = registerSum.add(result);
        } catch (NumberFormatException nfe) {
          //If the Input doesn't contain any numbers at all, a NumberFormatException is thrown, 
          //but we don't have to do anything in this case
        }
      }
      /*
       * If i <= 4 is true, we still have rouleauFields to add to their BigDecimal.
       */
      if (i <= 4) {
        try {
          /*
           * See Documentation for the registerFields for Information.
           */
          String str = rouleauFields.get(i).getText().replaceAll("\\D", "");
          BigDecimal sum = getFactor(i, FactorType.ROULEAU).multiply(new BigDecimal(str));
          rouleauLabels.get(i).setText(sum.toString().replace('.', ',') + "€");
          rouleauSum = rouleauSum.add(sum);
        } catch (NumberFormatException nfe) {
          //See above.
        }
      }
      /*
       * If i <= 6 is true, we still have billFields to add to their BigDecimal.
       */
      if (i <= 6) {
        try {
          /*
           * See Documentation for the registerFields for Information.
           */
          String str = billFields.get(i).getText().replaceAll("\\D", "");
          BigDecimal sum = getFactor(i, FactorType.BILL).multiply(new BigDecimal(str));
          billLabels.get(i).setText(sum.toString().replace('.', ',') + "€");
          billSum = billSum.add(sum);
        } catch (NumberFormatException nfe) {
          //See above.
        }
      }
      /*
       * We have 8 coinageFields, so since the for-condition is 0 <= i <=7, we don't have to check 
       * if i is in range and can simply calculate it's sum.
       */
      try {
        /*
         * See Documentation for the registerFields for Information.
         */
        String str = coinageFields.get(i).getText().replaceAll("\\D", "");
        BigDecimal sum = getFactor(i, FactorType.COINAGE).multiply(new BigDecimal(str));
        coinageLabels.get(i).setText(sum.toString().replace('.', ',') + "€");
        coinageSum = coinageSum.add(sum);
      } catch (NumberFormatException nfe) {
        //See above.
      }
    }
    /*
     * Displays all results in the associated Labels.
     */
    sumLabels.get(0).setText(registerSum.toString().replace('.', ',') + "€");
    sumLabels.get(1).setText(rouleauSum.toString().replace('.', ',') + "€");
    sumLabels.get(2).setText(coinageSum.toString().replace('.', ',') + "€");
    sumLabels.get(3).setText(billSum.toString().replace('.', ',') + "€");
    sumLabels.get(4).setText(billSum.add(coinageSum).add(rouleauSum).add(registerSum).toString()
        .replace('.', ',') + "€");
  }
  
  /**
   * Returns the Factor, for the Field at the given index. Since this is a one-for-all solution, a 
   * {@link FactorType} has to be specified, so this Method can return the Factor, which the 
   * Field's input has to be multiplied with.
   * @param index The Index of the Field, this Method should return the Factor for.
   * @param ft  The FactorType of the Field or, in simpler terms: What Tab this Field is stored in.
   * @return  The Factor for the given index and FactorType as a new BigDecimal.
   * @since 1.0
   */
  private BigDecimal getFactor(int index, FactorType ft) {
    if (ft == FactorType.REGISTER) {
      switch (index) {
        case 0: return new BigDecimal("325.00");
        case 1: return new BigDecimal("462.00");
        default: System.out.println("Default case in getFactor type: " + ft.toString() + " index: " 
            + index);
            return new BigDecimal("0.00");
      }
    } else if (ft == FactorType.ROULEAU) {
      switch (index) {
        case 0: return new BigDecimal("4.00");
        case 1: return new BigDecimal("8.00");
        case 2: return new BigDecimal("20.00");
        case 3: return new BigDecimal("25.00");
        case 4: return new BigDecimal("50.00");
        default: System.out.println("Default case in getFactor type: " + ft.toString() + " index: " 
            + index);
            return new BigDecimal("0.00");
      }
    } else if (ft == FactorType.COINAGE) {
      switch (index) {
        case 0: return new BigDecimal("0.01");
        case 1: return new BigDecimal("0.02");
        case 2: return new BigDecimal("0.05");
        case 3: return new BigDecimal("0.10");
        case 4: return new BigDecimal("0.20");
        case 5: return new BigDecimal("0.50");
        case 6: return new BigDecimal("1.00");
        case 7: return new BigDecimal("2.00");
        default: System.out.println("Default case in getFactor type: " + ft.toString() + " index: " 
            + index);
            return new BigDecimal("0.00");
      }
    } else if (ft == FactorType.BILL) {
      switch (index) {
        case 0: return new BigDecimal("5.00");
        case 1: return new BigDecimal("10.00");
        case 2: return new BigDecimal("20.00");
        case 3: return new BigDecimal("50.00");
        case 4: return new BigDecimal("100.00");
        case 5: return new BigDecimal("200.00");
        case 6: return new BigDecimal("500.00");
        default: System.out.println("Default case in getFactor type: " + ft.toString() + " index: " 
            + index);
            return new BigDecimal("0.00");
      }
    }
    System.out.println("Default case in getFactor type: " + ft.toString() + " index: " + index 
        + "! Returned null since there was no if clause for the given type");
    return null;
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
   * Returns the registerLabels, that were created in {@link #getRegisterTab()}.
   * @return The Labels, where the Sum for each register Type is displayed in as an ArrayList.
   * @since 1.0
   */
  public ArrayList<Label> getRegisterLabels() {
    return registerLabels;
  }

  /**
   * Returns the rouleauFields, that were created in {@link #getRouleauTab()}.
   * @return The TextFields for the rouleaus as an ArrayList.
   * @since 1.0
   */
  public ArrayList<TextField> getRouleauFields() {
    return rouleauFields;
  }

  /**
   * Returns the rouleauLabels, that were created in {@link #getRouleauTab()}.
   * @return  The Labels, where the Sum for each rouleau is displayed as an ArrayList.
   * @since 1.0
   */
  public ArrayList<Label> getRouleauLabels() {
    return rouleauLabels;
  }

  /**
   * Returns the coinageFields, that were created in {@link #getCoinageTab()}.
   * @return The TextFields for the coinage as an ArrayList.
   * @since 1.0 
   */
  public ArrayList<TextField> getCoinageFields() {
    return coinageFields;
  }

  /**
   * Returns the coinageLabels, that were created in {@link #getCoinageTab()}.
   * @return The Labels, where the Sum for each coinage is displayed as an ArrayList.
   * @since 1.0
   */
  public ArrayList<Label> getCoinageLabels() {
    return coinageLabels;
  }
  
  /**
   * Returns the billFields, that were created in {@link #getBillTab()}.
   * @return The TextFields for the bills as an ArrayList.
   * @since 1.0 
   */
  public ArrayList<TextField> getBillFields() {
    return billFields;
  }

  /**
   * Returns the billLabels, that were created in {@link #getBillTab()}.
   * @return The Labels, where the Sum for each bill is displayed as an ArrayList.
   * @since 1.0
   */
  public ArrayList<Label> getBillLabels() {
    return billLabels;
  }
  
  /**
   * Returns the sumLabels, that display the total Sum for each Tab.
   * @return The Labels, where the total Sum for each Tab is displayed as an ArrayList.
   * @since 1.0
   */
  public ArrayList<Label> getSumLabels() {
    return sumLabels;
  }

}
