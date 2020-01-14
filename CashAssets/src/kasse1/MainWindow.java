package kasse;

import java.util.Calendar;
import java.util.Locale;

import javafx.application.Application;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class, that will build and display the Main Window of the Application.
 * @author Haeldeus
 * @version 1.0
 */
public class MainWindow extends Application {
  
  /**
   * The Main-Method to start the Application from a Java Environment without JavaFX.
   * @param args  The Arguments to be passed.
   * @since 1.0
   */
  public static void main(String[] args) {
    launch(args);
  }
    
  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
     * Basic Scenery Settings.
     */
    primaryStage.setTitle("Kassenbestand - v0.9");
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
            + "können Sie die Eingabe korrigieren, nachdem Sie den \"Reset\"-Button gedrückt "
            + "haben." + System.lineSeparator() + "Mit dem \"Export\"-Button können Sie das "
            + "Ergebnis in eine neue Excel-Datei übertragen lassen, die in dem Ordner, wo dieses "
            + "Programm ausgeführt wird, erstellt wird." + System.lineSeparator() + "Dieser Button "
            + "ist erst verfügbar, nachdem das Programm eine Berechnung ausgeführt hat.");
        dialogVbox.getChildren().add(t);
        Scene dialogScene = new Scene(dialogVbox, 300, 260);
        t.setWrappingWidth(dialogScene.getWidth());
        dialogScene.getStylesheets().add("controlStyle1.css");
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
    
    Calendar cal = Calendar.getInstance(Locale.GERMANY);
    ComboBox<String> dayBox = new ComboBox<String>();
    for (int i = 1; i <= 31; i++) {
      dayBox.getItems().add(i + ".");
    }
    ComponentStorer cs = new ComponentStorer();
    dayBox.getSelectionModel().select(cal.get(Calendar.DAY_OF_MONTH) - 1);
    dayBox.setMaxWidth(70.0);
    cs.setDayBox(dayBox);
    grid.add(dayBox, 3, 0);
    
    ComboBox<String> monthBox = new ComboBox<String>();
    for (int i = 1; i <= 12; i++) {
      monthBox.getItems().add(i + ".");
    }
    monthBox.getSelectionModel().select(cal.get(Calendar.MONTH));
    monthBox.setMaxWidth(70.0);
    cs.setMonthBox(monthBox);
    grid.add(monthBox, 4, 0);
    
    TextField year = new TextField("" + cal.get(Calendar.YEAR));
    year.setMaxWidth(50.0);
    cs.setYear(year);
    grid.add(year, 5, 0);
    
    Image img2 = new Image(getClass().getResourceAsStream("/res/export.png"));
    ImageView imageview = new ImageView(img2);
    Button export = new Button("Export", imageview);
    export.setDisable(true);
    export.addEventFilter(MouseEvent.MOUSE_RELEASED, new ExportButtonHandler(cs));
    grid.add(export, 7, 0);
    
    final String[] coinLabelText = new String[] {"1ct:", "2ct:", "5ct:", "10ct:", 
        "20ct:", "50ct:", "1€:", "2€:"};
    final String[] billLabelText = new String[] {"5€:", "10€:", "20€:", "50€:", 
        "100€:", "200€:", "500€:", "Gezählt:"};
    Label[] resultLabelCoin = new Label[8];
    TextField[] coinTfs = new TextField[8];
    TextField[] billTfs = new TextField[7];
    Label[] resultLabelBill = new Label[7];
    
    for (int i = 0; i <= 7; i++) {
      Label coinLabel = new Label(coinLabelText[i]);
      grid.add(coinLabel, 0, i + 1);
      TextField coinTf = new TextField("0");
      coinTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
          + coinLabelText[i].replaceAll(":", "") + "-Münzen ein."));
      coinTf.setMaxWidth(50.0);
      coinTfs[i] = coinTf;
      grid.add(coinTf, 1, i + 1);
      Label resLabelCoin = new Label("=");
      resultLabelCoin[i] = resLabelCoin;
      resLabelCoin.setMinWidth(65.0);
      resLabelCoin.setMaxWidth(65.0);
      grid.add(resLabelCoin, 2, i + 1);
      Label billLabel = new Label(billLabelText[i]);
      grid.add(billLabel, 4, i + 1);
      TextField billTf = new TextField("0");
      billTf.setMaxWidth(50.0);
      billTf.setTooltip(new Tooltip("Geben Sie hier die Anzahl der " 
          + billLabelText[i].replaceAll(":", "") + "-Scheine ein."));
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
    
    cs.setCoinResults(resultLabelCoin);
    cs.setBillsResults(resultLabelBill);
    cs.setCoinTextFields(coinTfs);
    cs.setBillsTextFields(billTfs);
    cs.setExportButton(export);
    
    final Separator sep1 = new Separator();
    sep1.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep1, 0, 1);
    GridPane.setColumnSpan(sep1, 11);
    grid.add(sep1, 0, 9);
    
    Label cashNecessity = new Label("Kassenschnitt Bar:");
    GridPane.setColumnSpan(cashNecessity, 2);
    grid.add(cashNecessity, 0, 10);
    
    TextField cashNecessityEuro = new TextField("0");
    cashNecessityEuro.setMaxWidth(45.0);
    Label labelCashEuro = new Label("€");
    GridPane cashEuro = new GridPane();
    cashEuro.setHgap(5.0);
    cashEuro.add(cashNecessityEuro, 0, 0);
    cashEuro.add(labelCashEuro, 1, 0);
    grid.add(cashEuro, 2, 10);
    
    TextField cashNecessityCent = new TextField("0");
    cashNecessityCent.setMaxWidth(45.0);
    Label labelCashCent = new Label("ct");
    GridPane cashCent = new GridPane();
    cashCent.setHgap(5.0);
    cashCent.add(cashNecessityCent, 0, 0);
    cashCent.add(labelCashCent, 1, 0);
    grid.add(cashCent, 3, 10);
    
    cs.setCashNecessityEuroTextField(cashNecessityEuro);
    cs.setCashNecessityCentTextField(cashNecessityCent);
    
    Button reset = new Button("Reset");
    reset.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(reset, 2);
    GridPane.setFillHeight(reset, true);
    GridPane.setFillWidth(reset, true);
    reset.addEventFilter(MouseEvent.MOUSE_RELEASED, new ResetButtonHandler(cs));
    cs.setResetButton(reset);
    grid.add(reset, 7, 10);
    
    final Separator sep2 = new Separator();
    sep2.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep2, 0, 1);
    GridPane.setColumnSpan(sep2, 11);
    grid.add(sep2, 0, 11);
    
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
    
    final Separator sep3 = new Separator();
    sep3.setValignment(VPos.CENTER);
    GridPane.setConstraints(sep3, 0, 1);
    GridPane.setColumnSpan(sep3, 11);
    grid.add(sep3, 0, 14);
    
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
    
    Button calc = new Button("Berechne");
    calc.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(calc, 4);
    GridPane.setRowSpan(calc, 2);
    GridPane.setFillHeight(calc, true);
    GridPane.setFillWidth(calc, true);
    calc.addEventFilter(MouseEvent.MOUSE_RELEASED, new CalcButtonHandler(cs));
    grid.add(calc, 0, 20);
    
    Button edit = new Button("Edit");
    edit.setMaxSize(1000, 1000);
    GridPane.setColumnSpan(edit, 4);
    GridPane.setRowSpan(edit, 2);
    GridPane.setFillHeight(edit, true);
    GridPane.setFillWidth(edit, true);
    edit.addEventFilter(MouseEvent.MOUSE_RELEASED, new EditButtonHandler(cs));
    grid.add(edit, 4, 20);
    
    BorderPane bp = new BorderPane();
    bp.setTop(grid);
    
    cs.updateToolTips();
    
    Scene scene = new Scene(bp, 500, 550);
    scene.getStylesheets().add("controlStyle1.css");
    primaryStage.setScene(scene);
    primaryStage.setMinHeight(595.0);
    primaryStage.setMinWidth(615.0);
    primaryStage.show();
  }
}
