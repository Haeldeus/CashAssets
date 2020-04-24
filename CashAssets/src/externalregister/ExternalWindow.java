package externalregister;

import java.math.BigDecimal;

import externalregister.listener.TextFieldFocusChangedListener;
import externalregister.listener.TextFieldTextChangedListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import tipcalculator.handler.ExportHandler;
import util.Util;

/**
 * A Class which Objects will display the Window for the Calculation for the External Bar.
 * @author Haeldeus
 * @version 1.0
 */
public class ExternalWindow extends Application {

  private Label[] resultLabelCoin;
  private TextField[] coinTfs;
  private TextField[] billTfs;
  private Label[] resultLabelBill;
  private Label resTotalSum;
  private final KeyCombination kcUp = new KeyCodeCombination(KeyCode.UP, 
      KeyCombination.CONTROL_DOWN);
  private final KeyCombination kcDown = new KeyCodeCombination(KeyCode.DOWN, 
      KeyCombination.CONTROL_DOWN);
  private final KeyCombination kcRight = new KeyCodeCombination(KeyCode.RIGHT, 
      KeyCombination.CONTROL_DOWN);
  private final KeyCombination kcLeft = new KeyCodeCombination(KeyCode.LEFT, 
      KeyCombination.CONTROL_DOWN);
  
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
     * Creates String-Arrays with the Text for the Labels as content.
     * This is used to create these Labels in a for-Loop to save some space.
     */
    final String[] coinLabelText = new String[] {"1ct:", "2ct:", "5ct:", "10ct:", 
        "20ct:", "50ct:", "1Ä:", "2Ä:"};
    final String[] billLabelText = new String[] {"5Ä:", "10Ä:", "20Ä:", "50Ä:", 
        "100Ä:", "200Ä:", "500Ä:"};
    
    /*
     * Creates some Arrays where the created Labels and Textfields, that can be altered 
     * by the User, can be stored into.
     */
    resultLabelCoin = new Label[8];
    coinTfs = new TextField[8];
    billTfs = new TextField[7];
    resultLabelBill = new Label[7];
    
    /*
     * Creates all needed TextFields and Labels for the user to input the counted Money.
     */
    for (int i = 0; i <= 7; i++) {
      final int j = i;
      Label coinLabel = new Label(coinLabelText[i]);
      grid.add(coinLabel, 0, i);
      TextField coinTf = new TextField("0");
      coinTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
          + coinLabelText[i].replaceAll(":", "") + "-M¸nzen ein."));
      coinTf.setMaxWidth(50.0);
      coinTf.focusedProperty().addListener(new TextFieldFocusChangedListener(coinTf, this));
      coinTf.textProperty().addListener(new TextFieldTextChangedListener(this));
      coinTfs[i] = coinTf;
      grid.add(coinTf, 1, i);
      Label resLabelCoin = new Label("= ");
      resLabelCoin.setMinWidth(65.0);
      resLabelCoin.setMaxWidth(65.0);
      resultLabelCoin[i] = resLabelCoin;
      grid.add(resLabelCoin, 2, i);
      
      if (i < 7) {
        Label billLabel = new Label(billLabelText[i]);
        grid.add(billLabel, 4, i);
        TextField billTf = new TextField("0");
        billTf.setMaxWidth(50.0);
        billTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
            + billLabelText[i].replaceAll(":", "") + "-Scheine ein."));
        billTf.focusedProperty().addListener(new TextFieldFocusChangedListener(billTf, this));
        billTf.textProperty().addListener(new TextFieldTextChangedListener(this));
        billTfs[i] = billTf;
        grid.add(billTf, 5, i);
        Label resLabelBill = new Label("= ");
        resLabelBill.setMinWidth(65.0);
        resLabelBill.setMaxWidth(65.0);
        resultLabelBill[i] = resLabelBill;
        grid.add(resLabelBill, 6, i);
      } else {
        Button btReset = new Button("Reset (Doppelklick)");
        btReset.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                  for (int i = 0; i <= 7; i++) {
                    coinTfs[i].setText("0");
                    if (i < 7) {
                      billTfs[i].setText("0");
                    }
                  }
                }
              }
            });         
          }         
        });
        btReset.setTooltip(new Tooltip("Setzt alle Eingaben zur¸ck. Funktioniert nur nach einem "
            + "Doppelklick!"));
        btReset.setMaxWidth(Double.MAX_VALUE);
        grid.add(btReset, 4, i);
        GridPane.setColumnSpan(btReset, 2);
        GridPane.setHgrow(btReset, Priority.ALWAYS);
      }
    }
    
    for (int i = 0; i <= 7; i++) {
      final int j = i;
      if (i == 0) {
        coinTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              coinTfs[j + 1].requestFocus();
            } else if (kcUp.match(event)) {
              coinTfs[coinTfs.length - 1].requestFocus();
            } else if (kcRight.match(event)) {
              billTfs[j].requestFocus();
            }
          }     
        });
        
        billTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              billTfs[j + 1].requestFocus();
            } else if (kcUp.match(event)) {
              billTfs[billTfs.length - 1].requestFocus();
            } else if (kcLeft.match(event)) {
              coinTfs[j].requestFocus();
            }
          }     
        });
      } else if (i == 6) {
        coinTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              coinTfs[j + 1].requestFocus();
            } else if (kcUp.match(event)) {
              coinTfs[j - 1].requestFocus();
            }
          }     
        });
        
        billTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
              coinTfs[0].requestFocus();
            } else if (kcUp.match(event)) {
              billTfs[j - 1].requestFocus();
            } else if (kcLeft.match(event)) {
              coinTfs[j].requestFocus();
            } else if (kcDown.match(event)) {
              billTfs[0].requestFocus();
            }
          }     
        });
      } else if (i == 7) {
        coinTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
              billTfs[0].requestFocus();
            } else if (kcUp.match(event)) {
              coinTfs[j - 1].requestFocus();
            } else if (kcDown.match(event)) {
              coinTfs[0].requestFocus();
            }
          }     
        });
      } else {
        coinTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              coinTfs[j + 1].requestFocus();
            } else if (kcUp.match(event)) {
              coinTfs[j - 1].requestFocus();
            } else if (kcRight.match(event)) {
              billTfs[j].requestFocus();
            }
          }     
        });
        
        billTfs[i].setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER || kcDown.match(event)) {
              billTfs[j + 1].requestFocus();
            } else if (kcUp.match(event)) {
              billTfs[j - 1].requestFocus();
            } else if (kcLeft.match(event)) {
              coinTfs[j].requestFocus();
            }
          }     
        });
      }
    }
    
    
    /*
     * A Separator to separate the Area, where the User can enter the Information from the Area, 
     * where the Results of the Calculations are displayed.
     */
    final Separator sep3 = new Separator();
    sep3.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep3, 0, 1);
    GridPane.setColumnSpan(sep3, 11);
    grid.add(sep3, 0, 9);
    
    GridPane smallGrid = new GridPane();
    smallGrid.setHgap(10);
    smallGrid.setVgap(10);
    Label totalSum = new Label("Gesammtsumme:");
    smallGrid.add(totalSum, 0, 0);
    resTotalSum = new Label("0,00Ä");
    smallGrid.add(resTotalSum, 1, 0);
    
    grid.add(smallGrid, 0, 10);
    GridPane.setColumnSpan(smallGrid, 11);
    
    
    

    final Menu exportMenu = new Menu("Export");
    final MenuItem exportItem = new MenuItem("Export...");
    exportItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN, 
        KeyCombination.SHIFT_DOWN));
    exportMenu.getItems().addAll(exportItem);
    MenuBar menu = new MenuBar();
    menu.getMenus().addAll(exportMenu);
    
    /*
     * Creates a BorderPane, that contains the total GridPane. This is done, so that the Content 
     * of the GridPane can be displayed at the Center of the Scene.
     */
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(menu);
    borderPane.setCenter(grid);
    
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
   * Calculates the total amount of Money in the External register.
   */
  public void calc() {
    BigDecimal totalAmount = new BigDecimal("0.00");
    for (int i = 0; i <= 7; i++) {
      TextField tf = coinTfs[i];
      String str = tf.getText().replaceAll("[\\D]", "").trim();
      try {
        int integ = Integer.parseInt(str);
        BigDecimal factor = getFactor(i);
        BigDecimal augend = factor.multiply(new BigDecimal(integ));
        totalAmount = totalAmount.add(augend);
        resultLabelCoin[i].setText("= " + augend.toString().replace('.', ',') + "Ä");
      } catch (NumberFormatException nfe) {
        //This is fired, in case the TextField is empty.
        resultLabelCoin[i].setText("= 0,00Ä");
      }
      
      if (i < 7) {
        tf = billTfs[i];
        str = tf.getText().replaceAll("[\\D]", "").trim();
        try {
          int integ = Integer.parseInt(str);
          BigDecimal factor = getFactor(i + 8);
          BigDecimal augend = factor.multiply(new BigDecimal(integ));
          totalAmount = totalAmount.add(augend);
          resultLabelBill[i].setText("= " + augend.toString().replace('.', ',') + "Ä");
        } catch (NumberFormatException nfe) {
          nfe.printStackTrace();
        }
      }
    }
    final BigDecimal res = totalAmount;
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        resTotalSum.setText(res.toString().replace('.', ',') + "Ä");
      }     
    });
  }
  
  private BigDecimal getFactor(int index) {
    switch (index) {
      case 0: 
        return new BigDecimal("0.01");
      case 1:
        return new BigDecimal("0.02");
      case 2:
        return new BigDecimal("0.05");
      case 3:
        return new BigDecimal("0.10");
      case 4:
        return new BigDecimal("0.20");
      case 5:
        return new BigDecimal("0.50");
      case 6:
        return new BigDecimal("1.00");
      case 7:
        return new BigDecimal("2.00");
      case 8:
        return new BigDecimal("5.00");
      case 9:
        return new BigDecimal("10.00");
      case 10:
        return new BigDecimal("20.00");
      case 11:
        return new BigDecimal("50.00");
      case 12:
        return new BigDecimal("100.00");
      case 13:
        return new BigDecimal("200.00");
      case 14:
        return new BigDecimal("500.00");
      default:
        System.err.println("An Error occured when parsing the TextFields, please report this!");
        return new BigDecimal("0.00");   
    }
  }
  
  /**
   * Just for testing until this window is integrated to the Main Application.
   * @param args  Unused.
   * @since 1.0
   */
  public static void main(String[] args) {
    ExternalWindow.launch(args);
  }
}
