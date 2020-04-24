package externalregister.handlers;

import externalregister.ExternalWindow;

import java.math.BigDecimal;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.ExportUtils;

/**
 * The Handler for the Export Button.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The ExternalWindow, that opened the Export Stage, where this Button was clicked.
   */
  private final ExternalWindow primary;
  
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
   * @param primary The ExternalWindow, that opened the Export Stage, where this Button was clicked.
   *      Used to get all needed Components.
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
  public ExportButtonHandler(ExternalWindow primary, boolean open, ComboBox<String> dayBox, 
      ComboBox<String> monthBox, TextField tfYear, Stage dialog) {
    this.primary = primary;
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
    ExportUtils.createCell(row, 2, "Zählprotokoll Außentheke", 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Creates the Date Cells.
     */
    ExportUtils.createDateCells(row, 4, styles.get(ExportUtils.STANDARD_BLACK), 
        getDate().replaceAll("x", "\\."));
    
    /*
     * Creates the Cells, where the Header for the table is displayed. In this Table, the Data 
     * will be inserted. 
     */
    row = sheet.createRow(6);
    ExportUtils.createCell(row, 1, "Einheit", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 2, "Stück", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 3, "Betrag", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 4, "Einheit", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 5, "Stück", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 6, "Betrag", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    
    /*
     * Adds the table and the calculated result to the Sheet. Since there is an empty Row after the 
     * Table, the returned index will be incremented by 1.
     */
    int index = createData(sheet, row, styles, 7) + 1;
    
    /*
     * Creates the table, where the amount of Money, that is in the register per default, is 
     * displayed.
     */
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 1));
    ExportUtils.createCell(row, 0, "Ath Bestand", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    
    /*
     * Creates the table. Since the Signature Area is next to this table, this Area will be added 
     * to the Sheet as well.
     */
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "5 x 20€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 1, "100€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "12 x 10€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 1, "120€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 4));
    ExportUtils.createCell(row, 3, "Wichtige Bemerkungen:", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    for (int i = 5; i <= 7; i++) {
      ExportUtils.createCell(row, i, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), 
          null, false);
    }
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "10 x 5€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 1, "50€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    for (int i = 4; i <= 7; i ++) {
      ExportUtils.createCell(row, i, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
          false);
    }
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "50 x 2€", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "100€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    for (int i = 4; i <= 7; i ++) {
      ExportUtils.createCell(row, i, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
          false);
    }
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "50 x 1€", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "50€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "50 x 50ct", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "25€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "60 x 20ct", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "12€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 4));
    ExportUtils.createCell(row, 3, "Gezählt von Franz Weyher:", 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), 
        null, false);
    ExportUtils.createCell(row, 6, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), 
        null, false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "50 x 10ct", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
        null, false);
    ExportUtils.createCell(row, 1, "5€", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 0, "", styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 1, "", styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
    sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 1));
    ExportUtils.createCell(row, 0, "Kassenbestand insgesamt: 462€", 
        styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 3, "Mitarbeiter:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    
    index++;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 3, "Name, Vorname:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
        false);
    ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
        false);
    
    index += 2;
    row = sheet.createRow(index);
    ExportUtils.createCell(row, 3, "Unterschrift:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
        false);
    ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BOTTOM_BORDER), null, 
        false);
    
    /*
     * Exporting the created Excel File.
     */
    String date = getDate();
    String year = date.substring(date.lastIndexOf('x') + 1);
    String month = monthBox.getSelectionModel().getSelectedItem();
    ExportUtils.export(workbook, "Außentheke", date, year, month, open);
    dialog.close();
  }
  
  /**
   * Creates the Data Area, where the result of the calculation is displayed in the Excel Sheet. 
   * Returns the index of the Row below the created area.
   * @param sheet The Sheet, the area will be created in.
   * @param row The XSSFRow. This is used to prevent creating a new Object for each Row to be 
   *     created.
   * @param styles  All possible CellStyles in an HashMap of Integer and XSSFCellStyle.
   * @param index The index, where to start the creation of the new Area.
   * @return  An Integer value, that represents the index of the row below the created area.
   * @since 1.0
   */
  private int createData(XSSFSheet sheet, XSSFRow row, HashMap<Integer, XSSFCellStyle> styles, 
      int index) {
    int newIndex = index;
    
    String[] billData = {"500€", "200€", "100€", "50€", "20€", "10€", "5€", "2€", "1€"};
    String[] coinData = {"50ct", "20ct", "10ct", "5ct", "2ct", "1ct"};
    
    for (int i = 0; i <= 8; i++) {
      /*
       * Creates a new Row.
       */
      row = sheet.createRow(newIndex);
      
      /*
       * While i < 7, we can just grab each values from the billTfs.
       */
      if (i < 7) {
        ExportUtils.createCell(row, 1, billData[i], styles.get(ExportUtils.BLACK_WITH_BORDERS), 
            null, false);
        ExportUtils.createCell(row, 2, primary.getBillTfs()[6 - i].getText(), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 3, primary.getResultLabelBill()[6 - i].getText().substring(2), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        /*
         * As long as i < 5, the Cells display a value for the coinage Money, if it's greater, then 
         * the Cells will be empty.
         */
        String inputData = "";
        String resultData = "";
        if (i <= 5) {
          inputData = primary.getCoinTfs()[5 - i].getText();
          resultData = primary.getResultLabelCoin()[5 - i].getText().substring(2);
          ExportUtils.createCell(row, 4, coinData[i], styles.get(ExportUtils.BLACK_WITH_BORDERS), 
              null, false);
        } else {
          ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), 
              null, false);
        }
        ExportUtils.createCell(row, 5, inputData, styles.get(ExportUtils.BLACK_WITH_BORDERS), 
            null, false);
        ExportUtils.createCell(row, 6, resultData, styles.get(ExportUtils.BLACK_WITH_BORDERS), 
            null, false);
        
        /*
         * if i >= 7, the left side of the table displays the values for the 2€ and 1€ coins.
         */
      } else {
        int j = i == 7 ? 7 : 6;
        ExportUtils.createCell(row, 1, billData[i], styles.get(ExportUtils.BLACK_WITH_BORDERS), 
            null, false);
        ExportUtils.createCell(row, 2, primary.getCoinTfs()[j].getText(), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 3, primary.getResultLabelCoin()[j].getText().substring(2), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 6, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
      }
      /*
       * Increments newIndex, so a new Row can be created in the next iteration.
       */
      newIndex++;
    }
    
    /*
     * Creates the area, where the Sums are displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 1, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 2, "Summe 1:", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 3, primary.getEuroSum().toString() + "€", 
        styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 5, "Summe 2:", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
        false);
    BigDecimal centSum = primary.getTotalSum().subtract(primary.getEuroSum());
    ExportUtils.createCell(row, 6, centSum.toString() + "€", 
        styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    newIndex++;
    /*
     * Creates the empty Row below the Table.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    ExportUtils.createCell(row, 6, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    newIndex++;
    /*
     * Creates the Area, where the total Sum is displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 5, "Gesamtsumme:", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 6, primary.getTotalSum().toString(), 
        styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
    newIndex++;
    /*
     * Creates the Area, where the Cash Assets to be subtracted is displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 5, "Minus Kassenbestand:", 
        styles.get(ExportUtils.RED_WITH_BORDERS_WRAPPING), null, false);
    ExportUtils.createCell(row, 6, "- 462,00€", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    newIndex++;
    /*
     * Creates the Area where the revenue is displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 5, "Einnahmen:", styles.get(ExportUtils.RED_WITH_BORDERS), null, 
        false);
    ExportUtils.createCell(row, 6, primary.getRevenueSum().getText(), 
        styles.get(ExportUtils.BLACK_WITH_BORDERS), styles.get(ExportUtils.RED_WITH_BORDERS), true);
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
