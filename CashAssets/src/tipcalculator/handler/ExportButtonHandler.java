package tipcalculator.handler;

import java.math.BigDecimal;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tipcalculator.TipWindow;
import util.ExportUtils;

/**
 * The Handler for the Export Button.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The TipWindow, that opened the Export Stage, where this Button was clicked.
   */
  private final TipWindow primary;
  
  /**
   * The GridPane, where the User entered the Staff Members and their hours worked.
   */
  private final GridPane staffGrid;
  
  /**
   * The boolean value that determines, if the Directory should be opened after exporting.
   */
  private final boolean open;
  
  /**
   * The ComboBox, where the user entered the Day of the newly created File. This has to be stored, 
   * since the User enters the Value after creating this Handler. So the Handler needs to have a 
   * way to access this ComboBox to get the date.
   */
  private ComboBox<String> dayBox;
  
  /**
   * The ComboBox, where the user entered the Month of the newly created File. This has to be 
   * stored, since the User enters the Value after creating this Handler. So the Handler needs 
   * to have a way to access this ComboBox to get the date.
   */
  private ComboBox<String> monthBox;
  
  /**
   * The TextField, where the user entered the Year of the newly created File. This has to be 
   * stored, since the User enters the Value after creating this Handler. So the Handler needs to 
   * have a way to access this TextField to get the date.
   */
  private TextField tfYear;
  
  /**
   * The Stage, this Button is part of. Used to close the dialog after exporting.
   */
  private final Stage dialog;
  
  /**
   * The Constructor for this Handler. Sets all Fields to the given values.
   * @param primary The TipWindow, that opened the Export Stage, where this Button was clicked.
   *      Used to get all needed Components, that aren't saved in the staffGrid.
   * @param staffGrid The GridPane, where the User entered the Staff Members and their hours worked.
   * @param open  The boolean value, if the Directory should be opened after exporting the File.
   * @param dayBox  The ComboBox, where the user entered the Day of the newly created File. This 
   *      has to be stored, since the User enters the Value after creating this Handler. So the 
   *      Handler needs to have a way to access this ComboBox to get the date.
   * @param monthBox  The ComboBox, where the user entered the Month of the newly created File. 
   *      This has to be stored, since the User enters the Value after creating this Handler. So 
   *      the Handler needs to have a way to access this ComboBox to get the date.
   * @param tfYear  The TextField, where the user entered the Year of the newly created File. 
   *      This has to be stored, since the User enters the Value after creating this Handler. So 
   *      the Handler needs to have a way to access this TextField to get the date.
   * @param dialog  The Dialog Stage. this Button is part of. This is used to close the dialog 
   *     after exporting.
   * @since 1.0
   */
  public ExportButtonHandler(TipWindow primary, GridPane staffGrid, boolean open, 
      ComboBox<String> dayBox, ComboBox<String> monthBox, TextField tfYear, Stage dialog) {
    this.primary = primary;
    this.staffGrid = staffGrid;
    this.open = open;
    this.dayBox = dayBox;
    this.monthBox = monthBox;
    this.tfYear = tfYear;
    this.dialog = dialog;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    /*
     * Creates a new Workbook to edit the Excel File. In this Workbook, a new Sheet is created as 
     * well.
     */
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(getDate());
    
    /*
     * Basic Sheet Settings. Setting the Column width to 3cm and enables Landscape View.
     */
    for (int i = 0; i <= 7; i++) {
      sheet.setColumnWidth(i, 3900);
    }
    sheet.getPrintSetup().setLandscape(true);
    
    /*
     * Creates all Styles in ExportUtils and saves them in a HashMap.
     */
    HashMap<Integer, XSSFCellStyle> styles = ExportUtils.getStyles(workbook);
    
    /*
     * Creates the Header.
     */
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
    XSSFRow row = sheet.createRow(0);
    ExportUtils.createCell(row, 2, ExportUtils.EXPORT_HEADER, styles.get(ExportUtils
        .STANDARD_BLACK), null, false);
    
    /*
     * Creates the Second Row with the Cell, where the Title for this Sheet is displayed in.
     */
    row = sheet.createRow(2);
    ExportUtils.createCell(row, 2, "Trinkgeld", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    
    /*
     * Creates the Date Cells.
     */
    ExportUtils.createDateCells(row, 4, styles.get(ExportUtils.STANDARD_BLACK), 
        getDate().replaceAll("x", "\\."));
    
    /**
     * Creates the Area, where the total Sum of Tip is displayed, as well as the Tip per Hour 
     * worked.
     */
    row = sheet.createRow(4);
    ExportUtils.createCell(row, 2, "Summe Tip:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 3, primary.getTfTip().getText() + "€", 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 5, "Tip pro Stunde:", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    ExportUtils.createCell(row, 6, primary.getTipPerHour().toString().replace('.', ',') + "€", 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Creates the Area where the Tip for each Staff Member is displayed
     */
    row = sheet.createRow(6);
    ExportUtils.createCell(row, 1, "Mitarbeiter:", styles.get(ExportUtils.RED_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 2, "Stunden:", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 3, "Trinkgeld:", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    
    int index = createData(sheet, row, styles, 7);
    
    ExportUtils.createSignatureArea(sheet, row, styles, index + 2);
    /*
     * Exporting the created Excel File.
     */
    String date = getDate();
    String year = date.substring(date.lastIndexOf('x') + 1);
    String month = monthBox.getSelectionModel().getSelectedItem();
    ExportUtils.export(workbook, "Trinkgeld", date, year, month, open);
    dialog.close();
  }
  
  /**
   * Creates the Data Area, where the Tip for each Staff Member is displayed in the Excel Sheet. 
   * Returns the index of the Row below the created area.
   * @param sheet The Sheet, the area will be created in.
   * @param row The XSSFRow. This is used to prevent creating a new Object for each Row to be 
   *     created.
   * @param styles  All possible CellStyles in an HashMap of Integer and XSSFCellStyle.
   * @param index The index, where to start the creation of the new Area.
   * @return  An Integer value, that represents the index of the row below the created area.
   * @since 1.0
   */
  @SuppressWarnings("unchecked")
  private int createData(XSSFSheet sheet, XSSFRow row, HashMap<Integer, XSSFCellStyle> styles, 
      int index) {
    /*
     * Creates the necessary variables. newIndex is used to index all new rows and will be returned 
     * as the index of the line below this area. tipSum is used to display the total amount of Tips 
     * to be payed. This can be different from the total Sum entered by the User due to rounding 
     * issues. Mainly, this is used to monitor correctness of the calculation.
     */
    int newIndex = index;
    BigDecimal tipSum = new BigDecimal("0.00");
    /*
     * Iterates over each Row in the staffGrid to put it's content into the Excel Sheet.
     */
    for (int i = 0; i < staffGrid.getChildren().size() - 1; i += 3) {
      /*
       * Creates a new Row.
       */
      row = sheet.createRow(newIndex);
      
      /*
       * Fetches the Staff Member, that was selected in the ComboBox and adds it to the Sheet.
       */
      ComboBox<String> staffBox = (ComboBox<String>)staffGrid.getChildren().get(i);
      ExportUtils.createCell(row, 1, staffBox.getSelectionModel().getSelectedItem(), 
          styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
      
      /*
       * Since the last 2 entries needed are stored in another GridPane, this step is necessary to 
       * fetch them.
       */
      GridPane smallGrid = (GridPane)staffGrid.getChildren().get(i + 1);
      
      /*
       * Fetches the Hours worked by the given Staff Member, which was entered in the TextField and 
       * adds this value to the Sheet.
       */
      TextField tfHours = (TextField)smallGrid.getChildren().get(0);
      ExportUtils.createCell(row, 2, tfHours.getText(), styles.get(ExportUtils.BLACK_WITH_BORDERS), 
          null, false);
      
      /*
       * Fetches the Staff Member's share of the Tip Sum, that was displayed in the Label and adds 
       * this value to the Sheet. Also, the share will be added to tipSum.
       */
      Label labelTipShare = (Label)smallGrid.getChildren().get(2);
      String txt = labelTipShare.getText().replace("= ", "");
      tipSum = tipSum.add(new BigDecimal(txt.replace(',', '.').replace("€", "")));
      ExportUtils.createCell(row, 3, txt, styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
      
      /*
       * Increases the index, so a new Row can be created below this one.
       */
      newIndex++;
    }
    /*
     * Creates the last row of the Area, where the Sums are displayed.
     */
    row = sheet.createRow(newIndex);
    
    /*
     * Creates the Area, where the total Sum of Hours worked is displayed and the total Sum of 
     * Tips to pay.
     */
    ExportUtils.createCell(row, 1, "Gesamt:", styles.get(ExportUtils.STANDARD_RED), null, false);
    ExportUtils.createCell(row, 2, primary.getLbHoursTotal().getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 3, tipSum.toString() + "€", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    
    /*
     * Increases the index, so that the Calculation can use it to reference the Row below this Area.
     */
    newIndex++;
    
    return newIndex;
  }
  
  /**
   * Returns a String, that describes the Date the User entered in the pattern DDxMMxYYYY, so the 
   * Calculation has a consistent Value for the Date.
   * @return  The Date, that the User entered in the pattern DDxMMxYYYY
   * @since 1.0
   */
  private String getDate() {
    /*
     * Adds the selected Day to the String, that will be returned at the end of the Program. 
     * If the Day is < 10 then res.length() will be 2 and a '0' has to be placed in front of the 
     * Day, so the pattern of DDxMMxYYYY will be ensured. 
     */
    String res = dayBox.getSelectionModel().getSelectedItem().replace('.', 'x');
    if (res.length() == 2) {
      res = "0".concat(res);
    }
    /*
     * Concats the numerical value of the selected Month to the String to be returned.
     */
    switch (monthBox.getSelectionModel().getSelectedItem()) {
      case "Januar": 
        res = res.concat("01");
        break;
      case "Februar": 
        res = res.concat("02");
        break;
      case "März": 
        res = res.concat("03");
        break;
      case "April": 
        res = res.concat("04");
        break;
      case "Mai": 
        res = res.concat("05");
        break;
      case "Juni": 
        res = res.concat("06");
        break;
      case "Juli": 
        res = res.concat("07");
        break;
      case "August": 
        res = res.concat("08");
        break;
      case "September": 
        res = res.concat("09");
        break;
      case "Oktober": 
        res = res.concat("10");
        break;
      case "November": 
        res = res.concat("11");
        break;
      case "Dezember": 
        res = res.concat("12");
        break;
      default: 
        System.err.println("Error while parsing the Month...");
        res = res.concat("00");
        break;
    }
    res = res.concat("x");
    /*
     * Concats the Year to the String to be returned.
     */
    if (tfYear.getText().length() > 4) {
      res = res.concat(tfYear.getText().substring(0, 4));
    } else if (tfYear.getText().length() <= 0) {
      System.err.println("Error while parsing the Year...");
      res = res.concat("0000");
    } else {
      res = res.concat(tfYear.getText());
    }
    return res;
  }
}
