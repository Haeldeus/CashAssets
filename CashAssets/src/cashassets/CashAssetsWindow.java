package cashassets;

import cashassets.handlers.CalcButtonHandler;
import cashassets.handlers.EditButtonHandler;
import cashassets.handlers.ExportButtonHandler;
import cashassets.handlers.ResetButtonHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DayComboBoxKeyHandler;
import util.MonthComboBoxKeyHandler;
import util.Util;

/**
 * The Class, that will build and display the Main Window of the Application.
 * @author Haeldeus
 * @version 1.0
 */
public class CashAssetsWindow extends Application {
    
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
     * Adding the Help-Button
     */
    Image img = new Image(getClass().getResourceAsStream("/res/about.png"));
    ImageView iw = new ImageView(img); 
    Button help = new Button("", iw);
    /*
     * Adding an EventHandler that handles clicking this Button.
     */
    help.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent arg0) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        Text t = new Text("Geben Sie zunächst in die Boxen oben das Datum ein, "
            + "von dem der Kassenschnitt erstellt werden soll." 
            + System.lineSeparator() + "Danach die Anzahl der Münzen und Scheine, "
            + "die jeweils gezählt wurden. Außerdem die Zahl der gezählten Geldbörsen. "
            + System.lineSeparator() + "Danach, unter dem ersten Seperator, geben Sie "
            + "bitte den vom Kassensystem berechneten Bar-Kassenschnitt ein. " 
            + System.lineSeparator() + "Nun können Sie auf \"Berechnen\" drücken und das "
            + "Programm rechnet Ihnen alle unten aufgeführten Werte aus."
            + System.lineSeparator() + "Falls Ihnen bei der Eingabe ein Fehler unterlaufen ist, "
            + "können Sie die Eingabe korrigieren, nachdem Sie den \"Edit\"-Button gedrückt "
            + "haben." + System.lineSeparator() + "Mit dem \"Reset\"-Button können Sie die "
                + "komplette Eingabe zurücksetzen." 
            + System.lineSeparator() + "Mit dem \"Export\"-Button können Sie das "
            + "Ergebnis in eine neue Excel-Datei übertragen lassen, die in dem Ordner, wo dieses "
            + "Programm ausgeführt wird, erstellt wird." + System.lineSeparator() + "Dieser Button "
            + "ist erst verfügbar, nachdem das Programm eine Berechnung ausgeführt hat.");
        dialogVbox.getChildren().add(t);
        Scene dialogScene = new Scene(dialogVbox, 300, 290);
        t.setWrappingWidth(dialogScene.getWidth());
        dialogScene.getStylesheets().add(Util.getControlStyle());
        if (Util.checkNightmode()) {
          t.setId("text");
        }
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();
      }
       
    });
    grid.add(help, 0, 0);
    
    /*
     * Adding the Date Label
     */
    Label date = new Label("Datum:");
    grid.add(date, 2, 0);
    
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
    dayBox.setMaxWidth(70.0);
    /*
     * Creates a new ComponentStorer, so this Object can store alternating Fields in it. Adds 
     * the Daybox to this Storer.
     */
    ComponentStorer cs = new ComponentStorer();
    cs.setDayBox(dayBox);
    grid.add(dayBox, 3, 0);
    
    /*
     * Creates the ComboBox for the User to select the month for this Calculation.
     */
    ComboBox<String> monthBox = new ComboBox<String>();
    monthBox.getItems().addAll("Januar", "Februar", "März", "April", "Mai", "Juni", 
        "Juli", "August", "September", "Oktober", "November", "Dezember");
    monthBox.getSelectionModel().select(cal.get(Calendar.MONTH));
    monthBox.setOnKeyPressed(new MonthComboBoxKeyHandler(monthBox));
    monthBox.setMaxWidth(70.0);
    cs.setMonthBox(monthBox);
    grid.add(monthBox, 4, 0);
    
    /*
     * Adds a TextField to input the Year.
     */
    TextField year = new TextField("" + cal.get(Calendar.YEAR));
    year.setMaxWidth(50.0);
    cs.setYear(year);
    grid.add(year, 5, 0);

    /*
     * Checks, if the simple design should be used.
     */
    boolean simple = checkSimple();
    
    /*
     * Creates the Export Button.
     */
    Image img2 = new Image(getClass().getResourceAsStream("/res/export.png"));
    ImageView imageview = new ImageView(img2);
    Button export = new Button("Export", imageview);
    export.setDisable(true);
    export.addEventFilter(MouseEvent.MOUSE_RELEASED, new ExportButtonHandler(cs, simple, 
        checkOpen()));
    grid.add(export, 7, 0);
    
    /*
     * Creates String-Arrays with the Text for the Labels as content.
     * This is used to create these Labels in a for-Loop to save some space.
     */
    final String[] coinLabelText = new String[] {"1ct:", "2ct:", "5ct:", "10ct:", 
        "20ct:", "50ct:", "1€:", "2€:"};
    final String[] billLabelText = new String[] {"5€:", "10€:", "20€:", "50€:", 
        "100€:", "200€:", "500€:", "Geldbörsen:"};
    
    /*
     * Creates some Arrays where the created Labels and Textfields, that can be altered 
     * by the User, can be stored into.
     */
    Label[] resultLabelCoin = new Label[8];
    TextField[] coinTfs = new TextField[8];
    TextField[] billTfs = new TextField[7];
    Label[] resultLabelBill = new Label[7];
    
    /*
     * Creates all needed TextFields and Labels for the user to input the counted Money.
     */
    for (int i = 0; i <= 7; i++) {
      if (!simple) {
        Label coinLabel = new Label(coinLabelText[i]);
        grid.add(coinLabel, 0, i + 1);
        TextField coinTf = new TextField("0");
        coinTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
            + coinLabelText[i].replaceAll(":", "") + "-Münzen ein."));
        coinTf.setMaxWidth(50.0);
        coinTf.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
            if (!oldP && newP) {
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                  coinTf.selectAll();
                }              
              });
            }
          }
        });
        coinTfs[i] = coinTf;
        grid.add(coinTf, 1, i + 1);
        Label resLabelCoin = new Label("=");
        resultLabelCoin[i] = resLabelCoin;
        resLabelCoin.setMinWidth(65.0);
        resLabelCoin.setMaxWidth(65.0);
        grid.add(resLabelCoin, 2, i + 1);
      } else if (i == 7) {
        Label coinLabel = new Label("Kleingeld:");
        grid.add(coinLabel, 0, i + 1);
        TextField coinTf = new TextField("0");
        coinTf.setTooltip(new Tooltip("Geben Sie hier die Summe des Kleingelds ein"));
        coinTf.focusedProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
            if (!oldP && newP) {
              Platform.runLater(new Runnable() {
                @Override
                public void run() {
                  coinTf.selectAll();
                }              
              });
            }
          }
        });
        coinTfs[i] = coinTf;
        grid.add(coinTf, 1, i + 1);
        GridPane.setColumnSpan(coinTf, 2);
        Label resLabelCoin = new Label("=");
        resultLabelCoin[i] = resLabelCoin;
        resLabelCoin.setMinWidth(65.0);
        resLabelCoin.setMaxWidth(65.0);
        grid.add(resLabelCoin, 3, i + 1);
      } else {
        TextField empty = new TextField("0");
        coinTfs[i] = empty;
        Label lbEmpty = new Label("= 0,00€");
        resultLabelCoin[i] = lbEmpty;
      }
      Label billLabel = new Label(billLabelText[i]);
      grid.add(billLabel, 4, i + 1);
      TextField billTf = new TextField("0");
      billTf.setMaxWidth(50.0);
      billTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
          + billLabelText[i].replaceAll(":", "") + "-Scheine ein."));
      billTf.focusedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
          if (!oldP && newP) {
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                billTf.selectAll();
              }              
            });
          }
        }
      });
      if (i == 7) {
        billTf.getStyleClass().add("purseTF");
        billTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der gezählten Geldbörsen ein."));
        cs.setPurseTextField(billTf);
      }
      if (i < 7) {
        billTfs[i] = billTf;
        Label resLabelBill = new Label("=");
        resultLabelBill[i] = resLabelBill;
        resLabelBill.setMinWidth(65.0);
        resLabelBill.setMaxWidth(65.0);
        grid.add(resLabelBill, 6, i + 1);
      }
      grid.add(billTf, 5, i + 1);
    }
    
    /*
     * Stores all needed Components in the ComponentStorer, so they can be updated by the Buttons 
     * later on.
     */
    cs.setCoinResults(resultLabelCoin);
    cs.setBillsResults(resultLabelBill);
    cs.setCoinTextFields(coinTfs);
    cs.setBillsTextFields(billTfs);
    cs.setExportButton(export);
    
    /*
     * Creates a Separator to separate the input of the Money and the Cash Necessity.
     */
    final Separator sep1 = new Separator();
    sep1.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep1, 0, 1);
    GridPane.setColumnSpan(sep1, 11);
    grid.add(sep1, 0, 9);
    
    /*
     * Creates the Area for the Cash Necessity.
     */
    Label cashNecessity = new Label("Kassenschnitt Bar:");
    if (!simple) {
      GridPane.setColumnSpan(cashNecessity, 2);
    } else {
      GridPane.setColumnSpan(cashNecessity, 3);
    }
    grid.add(cashNecessity, 0, 10);
    
    TextField cashNecessityEuro = new TextField("0");
    cashNecessityEuro.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
        if (!oldP && newP) {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              cashNecessityEuro.selectAll();
            }              
          });
        }
      }
    });
    grid.add(cashNecessityEuro, 2, 10);
    GridPane.setColumnSpan(cashNecessityEuro, 2);
    
    Label labelCashCent = new Label("€");
    grid.add(labelCashCent, 4, 10);
    
    /*
     * Adds the needed Components to the ComponentStorer.
     */
    cs.setCashNecessityEuroTextField(cashNecessityEuro);
    
    /*
     * Adds a Reset Button to reset all TextFields and Labels to their original value.
     */
    Button reset = new Button("Reset");
    reset.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(reset, 2);
    GridPane.setFillHeight(reset, true);
    GridPane.setFillWidth(reset, true);
    reset.addEventFilter(MouseEvent.MOUSE_RELEASED, new ResetButtonHandler(cs));
    cs.setResetButton(reset);
    grid.add(reset, 7, 10);
    
    /*
     * Creates a second Separator to separate the Cash Necessity Area from the Area of the 
     * calculated Money Results.
     */
    final Separator sep2 = new Separator();
    sep2.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep2, 0, 1);
    GridPane.setColumnSpan(sep2, 11);
    grid.add(sep2, 0, 11);
    
    /*
     * All Labels for the calculated Values of the Money to be stored in.
     */
    Label coinage = new Label("Kleingeld:");
    GridPane.setColumnSpan(coinage, 2);
    grid.add(coinage, 0, 12);
    
    Label coinageSum = new Label("0,00€");
    cs.setCoinSumLabel(coinageSum);
    grid.add(coinageSum, 2, 12);
    
    Label bills = new Label("Scheine:");
    GridPane.setColumnSpan(bills, 2);
    grid.add(bills, 4, 12);
    
    Label billSum = new Label("0,00€");
    cs.setBillSumLabel(billSum);
    grid.add(billSum, 6, 12);
    
    Label total = new Label("Gesamt:");
    GridPane.setColumnSpan(total, 2);
    grid.add(total, 0, 13);
    
    Label totalSum = new Label("0,00€");
    cs.setSumLabel(totalSum);
    grid.add(totalSum, 2, 13);
    
    /*
     * A last Separator to separate the Money-Info Area from the advanced Information Area.
     */
    final Separator sep3 = new Separator();
    sep3.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep3, 0, 1);
    GridPane.setColumnSpan(sep3, 11);
    grid.add(sep3, 0, 14);
    
    /*
     * Adds all Labels for the advanced Information to be stored in.
     */
    Label coinageNecessity = new Label("Muss Kleingeld:");
    GridPane.setColumnSpan(coinageNecessity, 2);
    grid.add(coinageNecessity, 0, 15);
    
    Label coinageNecessityLabel = new Label("0,00€");
    cs.setCoinNecessityLabel(coinageNecessityLabel);
    grid.add(coinageNecessityLabel, 2, 15);
    
    Label coinageCleared = new Label("Kleingeld bereinigt:");
    GridPane.setColumnSpan(coinageCleared, 2);
    grid.add(coinageCleared, 4, 15);
    
    Label coinageClearedLabel = new Label("0,00€");
    cs.setCoinCleanedLabel(coinageClearedLabel);
    grid.add(coinageClearedLabel, 6, 15);
    
    Label coinageDifference = new Label("Differenz Kleingeld:");
    if (simple) {
      coinageDifference.setText("Diff. Kleingeld:");
      coinageDifference.setTooltip(new Tooltip("Differenz Kleingeld"));
    }
    coinageDifference.setMinWidth(150.0);
    GridPane.setColumnSpan(coinageDifference, 2);
    grid.add(coinageDifference, 0, 16);
    
    Label coinDifferenceLabel = new Label("0,00€");
    cs.setCoinDifferenceLabel(coinDifferenceLabel);
    grid.add(coinDifferenceLabel, 2, 16);
    
    Label cashNecessityLow = new Label("Kassenschnitt:");
    GridPane.setColumnSpan(cashNecessityLow, 2);
    grid.add(cashNecessityLow, 4, 16);
    
    Label cashNecessitySum = new Label("0,00€");
    cs.setCashNecessityLabel(cashNecessitySum);
    grid.add(cashNecessitySum, 6, 16);
    
    Label tipSum = new Label("Rest Tip:");
    GridPane.setColumnSpan(tipSum, 2);
    grid.add(tipSum, 4, 17);
    
    Label tipSumLabel = new Label("0,00€");
    cs.setTipSumLabel(tipSumLabel);
    grid.add(tipSumLabel, 6, 17);
    
    /*
     * Creates the Button for the User to press to calculate all Information from the given input.
     */
    Button calc = new Button("Berechne");
    calc.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(calc, 4);
    GridPane.setRowSpan(calc, 2);
    GridPane.setFillHeight(calc, true);
    GridPane.setFillWidth(calc, true);
    calc.setOnMouseClicked(new CalcButtonHandler(cs, simple));
    grid.add(calc, 0, 20);
    
    /*
     * Creates a Button that allows the User to edit his Input after calculating with a set of 
     * given values.
     */
    Button edit = new Button("Edit");
    edit.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(edit, 4);
    GridPane.setRowSpan(edit, 2);
    GridPane.setFillHeight(edit, true);
    GridPane.setFillWidth(edit, true);
    edit.setOnMouseClicked(new EditButtonHandler(cs));
    grid.add(edit, 4, 20);
    
    /*
     * Adds a BorderPane to the Scene, so the Grid will always be displayed at the Top of the Scene.
     */
    BorderPane bp = new BorderPane();
    bp.setTop(grid);
    
    /*
     * Updates the ToolTips for all Labels in the Application.
     */
    cs.updateToolTips();
    
    addKeyHandlers(cs, simple);
    
    /*
     * Sets the Size of the Scene, it's restrictions and the Stylesheet. Afterwards, it displays 
     * the primaryStage to the User.
     */
    Scene scene = new Scene(bp, 500, 550);
    scene.getStylesheets().add(Util.getControlStyle());
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(595.0);
    primaryStage.setMinWidth(615.0);
    primaryStage.show();
  }

  /**
   * Adds KeyEvent Handlers to all TextFields, so they change to the TF below them, whenever the 
   * Enter-Key is pressed.
   * @param cs  The ComponentStorer, where all TextFields are stored. The Method accesses the TFs 
   *     from there.
   * @param simple  The boolean Value, if the simple Design is used. Depending on this Value, the 
   *     sequence of selected TextFields is different.
   * @since 1.0
   */
  private void addKeyHandlers(ComponentStorer cs, boolean simple) {
    /*
     * Adds KeyEventHandler to each TextField, so they change focus whenever the Enter Key was 
     * pressed.
     */
    if (simple) {    
      cs.getYear().setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[7].requestFocus();
          }
        }     
      });
      cs.getCoinTextFields()[7].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getBillsTextFields()[0].requestFocus();
          }
        }      
      });
    } else {    
      cs.getYear().setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[0].requestFocus();
          }
        }     
      });
      cs.getCoinTextFields()[0].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[1].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[1].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[2].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[2].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[3].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[3].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[4].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[4].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[5].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[5].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[6].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[6].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getCoinTextFields()[7].requestFocus();
          }
        }      
      });
      cs.getCoinTextFields()[7].setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
          if (event.getCode() == KeyCode.ENTER) {
            cs.getBillsTextFields()[0].requestFocus();
          }
        }      
      });
    }
    
    cs.getBillsTextFields()[0].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[1].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[1].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[2].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[2].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[3].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[3].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[4].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[4].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[5].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[5].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getBillsTextFields()[6].requestFocus();
        }
      }
    });
    cs.getBillsTextFields()[6].setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getPurseTextField().requestFocus();
        }
      }
    });
    cs.getPurseTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getCashNecessityEuroTextField().requestFocus();
        }
      }      
    });
    
    cs.getCashNecessityEuroTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          cs.getYear().requestFocus();
        }
      }      
    });
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
