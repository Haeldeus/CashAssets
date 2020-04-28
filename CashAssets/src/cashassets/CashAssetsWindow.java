package cashassets;

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

import cashassets.handlers.ExportHandler;
import cashassets.handlers.KeyEventHandler;
import cashassets.listener.TextFieldFocusChangedListener;
import cashassets.listener.TextFieldTextChangedListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import util.Util;

public class CashAssetsWindow extends Application {

  private boolean simple;
  
  private ArrayList<TextField> coinFields;
  
  private ArrayList<TextField> billFields;
  
  private ArrayList<Label> coinFieldRes;
  
  private ArrayList<Label> billFieldRes;
  
  private TextField cashNeeded;
  
  private TextField purses;
  
  private HashMap<String, Label> labels;
  
  /**
   * The String identifier for the coinage sum Label.
   */
  public final String coinageSumId = "COINAGE_SUM";
  
  public final String billsSumId = "BILLS_SUM";
  
  public final String totalSumId = "TOTAL_SUM";
  
  public final String coinageNeededId = "COINAGE_NEEDED";
  
  public final String diffCoinageId = "DIFF_COINAGE";
  
  public final String coinageSumCalculatedId = "COINAGE_SUM_CALCULATED";
  
  public final String cashNeededId = "CASH_NEEDED";
  
  public final String tipSumId = "TIP_SUM";
  
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
    
    fillInfoGrid(grid, fillInputGrid(grid, primaryStage));
    
    addHandlers();
    /*
     * Creates a Menu for exporting the Data into a File. This has to be done down here, since 
     * this uses the staffGrid.
     */
    MenuBar menu = new MenuBar();
    final Menu exportMenu = new Menu("Export");
    final MenuItem exportItem = new MenuItem("Export...");
    exportItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN, 
        KeyCombination.SHIFT_DOWN));
    exportItem.setOnAction(new ExportHandler(primaryStage, this));
    exportMenu.getItems().addAll(exportItem);
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

  private void addHandlers() {
    for (TextField tf : billFields) {
      tf.focusedProperty().addListener(new TextFieldFocusChangedListener(tf, this));
      tf.textProperty().addListener(new TextFieldTextChangedListener(this));
    }
    
    cashNeeded.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, 
          Boolean newP) {
        /*
         * Calculates the current input, if the Focus is lost.
         */
        if (oldP && !newP) {
          /*
           * Replaces all non numerical characters from the Text in the TextField except for '.' 
           * and ','.
           */
          cashNeeded.setText(cashNeeded.getText().replaceAll("[\\D&&[^,]&&[^\\.]]",
              "").trim());
          if (cashNeeded.getText().equals("") || coinFields.get(0).getText() == null) {
            cashNeeded.setText("0");
          }
          /*
           * Calculates the current input.
           */
          calc();
          /*
           * Selects the Text in this TextField, if the Focus is gained.
           */
        } else if (!oldP && newP) {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              cashNeeded.selectAll();
            }        
          });
        }
      }       
    });
    cashNeeded.textProperty().addListener(new TextFieldTextChangedListener(this));
    purses.focusedProperty().addListener(new TextFieldFocusChangedListener(purses, this));
    purses.textProperty().addListener(new TextFieldTextChangedListener(this));
    
    if (simple) {
      coinFields.get(0).focusedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, 
            Boolean newP) {
          /*
           * Calculates the current input, if the Focus is lost.
           */
          if (oldP && !newP) {
            /*
             * Replaces all non numerical characters from the Text in the TextField except for '.' 
             * and ','.
             */
            coinFields.get(0).setText(coinFields.get(0).getText().replaceAll("[\\D&&[^,]&&[^\\.]]",
                "").trim());
            if (coinFields.get(0).getText().equals("") || coinFields.get(0).getText() == null) {
              coinFields.get(0).setText("0");
            }
            /*
             * Calculates the current input.
             */
            calc();
            /*
             * Selects the Text in this TextField, if the Focus is gained.
             */
          } else if (!oldP && newP) {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                coinFields.get(0).selectAll();
              }        
            });
          }
        }       
      });
      coinFields.get(0).textProperty().addListener(new TextFieldTextChangedListener(this));
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
      for (int i = 0; i < coinFields.size(); i++) {
        TextField tf = coinFields.get(i);
        tf.focusedProperty().addListener(new TextFieldFocusChangedListener(tf, this));
        tf.textProperty().addListener(new TextFieldTextChangedListener(this));
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
      cashNeeded.setOnKeyPressed(new KeyEventHandler(purses, coinFields.get(coinFields.size() - 1), 
          coinFields.get(0), null, purses));
      purses.setOnKeyPressed(new KeyEventHandler(coinFields.get(0), 
          billFields.get(billFields.size() - 1), billFields.get(0), cashNeeded, null));
    }
  }
  
  private void fillInfoGrid(GridPane grid, int rowIndex) {
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
    
    rowIndex++;
    
    Separator sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    rowIndex++;
    
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
    
    rowIndex++;
    
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
    
    rowIndex++;
    
    id = new Label("Rest Tip:");
    grid.add(id, 3, rowIndex);
    GridPane.setColumnSpan(id, 2);
    
    Label tipSum = new Label("0,00€");
    grid.add(tipSum, 5, rowIndex);
    labels.put(tipSumId, tipSum);
  }
  
  private int fillInputGrid(GridPane grid, Stage primaryStage) {
    for (int i = 0; i <= 5; i++) {
      grid.getColumnConstraints().add(new ColumnConstraints());
    }
    grid.getColumnConstraints().get(0).setMinWidth(60);
    grid.getColumnConstraints().get(0).setMaxWidth(100);
    grid.getColumnConstraints().get(1).setMaxWidth(60);
    grid.getColumnConstraints().get(2).setMinWidth(100);
    grid.getColumnConstraints().get(3).setMinWidth(60);
    grid.getColumnConstraints().get(3).setMaxWidth(100);
    grid.getColumnConstraints().get(4).setMaxWidth(60);
    grid.getColumnConstraints().get(5).setMinWidth(100);
    
    
    String[] billIds = {"5€:", "10€:", "20€:", "50€:", "100€:", "200€:", "500€:"};
    String[] coinIds = {"1ct:", "2ct:", "5ct:", "10ct:", "20ct:", "50ct:", "1€:", "2€:"};
    int rowIndex;
    if (simple) {
      for (int i = 0; i <= 1; i++) {
        for (int k = 0; k <= 3; k++) {
          Label identifier;
          TextField tf = new TextField("0");
          Label result = new Label("= 0,00€");
          if (i == k && k == 0) {
            identifier = new Label("Kleingeld:");
            coinFields.add(0, tf);
            coinFieldRes.add(0, result);
            grid.add(identifier, 0, 0);
            grid.add(tf, 1, 0);
            grid.add(result, 2, 0);
          } else {
            int index = 4 * i + (k - 1);
            identifier = new Label(billIds[index]);
            billFields.add(index, tf);
            billFieldRes.add(index, result);
            grid.add(identifier, 3 * i, k);
            grid.add(tf, 3 * i + 1, k);
            grid.add(result, 3 * i + 2, k);
          }
        }
      }
      rowIndex = 4;
      
      primaryStage.setMinHeight(450.0);
      primaryStage.setMinWidth(615.0);
      primaryStage.setHeight(450);
    } else {
      for (int i = 0; i <= 7; i++) {
        Label id = new Label(coinIds[i]);
        grid.add(id, 0, i);
        
        TextField tf = new TextField("0");
        grid.add(tf, 1, i);
        coinFields.add(i, tf);
        
        Label res = new Label("= 0,00€");
        grid.add(res, 2, i);
        coinFieldRes.add(i, res);
        
        if (i < 7) {
          id = new Label(billIds[i]);
          grid.add(id, 3, i);
          
          tf = new TextField("0");
          grid.add(tf, 4, i);
          billFields.add(i, tf);
          
          res = new Label("= 0,00€");
          grid.add(res, 5, i);
          billFieldRes.add(i, res);
        }
      }
      
      rowIndex = 8;
      
      primaryStage.setMinHeight(550.0);
      primaryStage.setMinWidth(615.0);
      primaryStage.setHeight(550);
    }
    Separator sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    rowIndex++;
    
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
    
    Button btReset = new Button("Reset (Doppelklick)");
    btReset.setStyle("-fx-font: 10 Tahoma; -fx-font-weight: bold;");
    btReset.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
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
    
    rowIndex++;
    
    sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    rowIndex++;
    
    sep = new Separator();
    grid.add(sep, 0, rowIndex);
    GridPane.setColumnSpan(sep, 6);
    
    rowIndex++;
    
    return rowIndex;
  }
  
  /**
   * Checks, if the Simple design of this application should be used.
   * @return The boolean value, if the simple design should be used.
   * @since 1.0
   */
  private boolean checkSimple() {
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
  
  public void calc() {
    BigDecimal coinage;
    if (simple) {
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
      coinFields.get(0).setText(str.replace('.', ','));
      String s = str.replace(',', '.').trim();
      if (s.equals("") || s == null) {
        s = "0.00";
      }
      coinage = new BigDecimal(s);
      coinage = coinage.setScale(2, RoundingMode.DOWN);
      coinFieldRes.get(0).setText("= " + coinage.toString().replace('.', ','));
    } else {
      coinage = new BigDecimal("0.00");
      for (int i = 0; i <= 7; i++) {
        int value;
        try {
          value = Integer.parseInt(coinFields.get(i).getText());
        } catch (NumberFormatException nfe) {
          value = 0;
        }
        BigDecimal tmp = new BigDecimal(value * getCoinFactor(i));
        tmp = tmp.setScale(2, RoundingMode.DOWN);
        coinage = coinage.add(tmp);
        coinFieldRes.get(i).setText("= " + tmp.toString().replace('.', ',') + "€");
      }
      coinage = coinage.setScale(2, RoundingMode.DOWN);
    }
    
    BigDecimal bills = new BigDecimal("0.00");
    for (int i = 0; i <= 6; i++) {
      int value;
      try {
        value = Integer.parseInt(billFields.get(i).getText());
      } catch (NumberFormatException nfe) {
        value = 0;
      }
      BigDecimal tmp = new BigDecimal(value * getBillFactor(i));
      tmp = tmp.setScale(2, RoundingMode.DOWN);
      bills = bills.add(tmp);
      billFieldRes.get(i).setText("= " + tmp.toString().replace('.', ',') + "€");
    }
    bills = bills.setScale(2, RoundingMode.DOWN);
    
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
    cashNeeded.setText(str.replace('.', ','));
    double value;
    try {
      value = Double.parseDouble(cashNeeded.getText().replace(',', '.'));
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
   * @return the simple
   */
  public boolean isSimple() {
    return simple;
  }

  /**
   * @return the coinFields
   */
  public ArrayList<TextField> getCoinFields() {
    return coinFields;
  }

  /**
   * @return the billFields
   */
  public ArrayList<TextField> getBillFields() {
    return billFields;
  }

  /**
   * @return the coinFieldRes
   */
  public ArrayList<Label> getCoinFieldRes() {
    return coinFieldRes;
  }

  /**
   * @return the billFieldRes
   */
  public ArrayList<Label> getBillFieldRes() {
    return billFieldRes;
  }

  /**
   * @return the cashNeeded
   */
  public TextField getCashNeeded() {
    return cashNeeded;
  }

  /**
   * @return the purses
   */
  public TextField getPurses() {
    return purses;
  }

  /**
   * @return the labels
   */
  public HashMap<String, Label> getLabels() {
    return labels;
  }
  
}
