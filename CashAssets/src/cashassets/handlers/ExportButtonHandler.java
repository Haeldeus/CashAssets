package cashassets.handlers;

import cashassets.CashAssetsWindow;

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
 * The Handler for the ExportButton in the Export Window.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The CashAssetsWindow, that opened the Export Stage, where this Button was clicked.
   */
  private final CashAssetsWindow primary;
  
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
   * @param primary The NewCashAssetsWindow, that opened the Export Stage, where this Button was 
   *      clicked.
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
  public ExportButtonHandler(CashAssetsWindow primary, boolean open, 
      ComboBox<String> dayBox, ComboBox<String> monthBox, TextField tfYear, Stage dialog) {
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
    ExportUtils.createCell(row, 2, "Z�hlprotokoll Bar", 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Creates the Date Cells.
     */
    ExportUtils.createDateCells(row, 4, styles.get(ExportUtils.STANDARD_BLACK), 
        getDate().replaceAll("x", "\\."));
    
    
    /*
     * Creates the Row, where the amount of counted purses is displayed.
     */
    sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));
    row = sheet.createRow(4);
    
    ExportUtils.createCell(row, 2, "Gez�hlte Geldb�rsen:", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    ExportUtils.createCell(row, 4, primary.getPurses().getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Creates the Area for the Data, that was given by the User.
     */
    row = sheet.createRow(6);
    int index = createData(sheet, row, styles, 6) + 1;
    row = sheet.createRow(index);
    
    /*
     * Creates the Signature Area.
     */
    ExportUtils.createSignatureArea(sheet, row, styles, index);
    
    /*
     * Exporting the created Excel File.
     */
    String date = getDate();
    String year = date.substring(date.lastIndexOf('x') + 1);
    String month = monthBox.getSelectionModel().getSelectedItem();
    ExportUtils.export(workbook, "Kassenbestand", date, year, month, open);
    dialog.close();
  }
  
  /**
   * Creates the Data Area, where the result of the calculation is displayed in the Excel Sheet. 
   * Returns the index of the Row below the created area.
   * @param sheet The Sheet, the area will be created in.
   * @param row The XSSFRow. This is used to prevent creating a new Object for each Row to be 
   *     created. Has to be set to represent the given index beforehand.
   * @param styles  All possible CellStyles in an HashMap of Integer and XSSFCellStyle.
   * @param index The index, where to start the creation of the new Area.
   * @return  An Integer value, that represents the index of the row below the created area.
   * @since 1.0
   */
  private int createData(XSSFSheet sheet, XSSFRow row, 
      HashMap<Integer, XSSFCellStyle> styles, int index) {
    /*
     * Storing the Index to be returned in a new Integer. Also checks, if the simple layout was 
     * used in the primary Window.
     */
    int newIndex = index;
    boolean simple = primary.isSimple();
    
    /*
     * The header Data for the Table.
     */
    String[] headerData = {"Einheit", "St�ck", "Betrag"};
    /*
     * Creates the header.
     */
    for (int i = 0; i < 6; i++) {
      ExportUtils.createCell(row, i + 1, headerData[i % 3], 
          styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
    }
    
    /*
     * Creates the Table. Also stores the id of each possible coin and Bill. The factors Array is 
     * used, when the simple Design was used to create Formulas, so the User can enter the values 
     * of the coins afterwards. 
     */
    newIndex++;
    String[] billIds = {"5�:", "10�:", "20�:", "50�:", "100�:", "200�:", "500�:"};
    String[] coinIds = {"1ct:", "2ct:", "5ct:", "10ct:", "20ct:", "50ct:", "1�:", "2�:"};
    double[] factors = {0.01, 0.02, 0.05, 0.1, 0.2, 0.5, 1, 2};
    for (int i = 0; i <= 7; i++) {
      /*
       * Creates the Row in the Sheet for the current row in the table.
       */
      row = sheet.createRow(newIndex);
      /*
       * Creates the first Cell. This is always the ID of the coin only.
       */
      ExportUtils.createCell(row, 1, coinIds[i], styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
          false);
      /*
       * If the simple design wasn't used, we can fetch the data from the Fields in the primary 
       * Window.
       */
      if (!simple) {
        ExportUtils.createCell(row, 2, primary.getCoinFields().get(i).getText(), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 3, primary.getCoinFieldRes().get(i).getText().substring(2), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        /*
         * If the simple design was used, default values will be printed in the second column of 
         * the table. The third column will contain a formula, so the values can be entered 
         * afterwards and this column shows the correct amount of money with that coin.
         */
      } else {
        ExportUtils.createCell(row, 2, "0", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, 
            false);
        ExportUtils.createFormulaCell(row, 3, "C" + (index + 2 + i) + "*" + factors[i]
            + "&\"�\"" , styles.get(ExportUtils.BLACK_WITH_BORDERS));
      }
      /*
       * Creates the Cells for the Bills. These will be entered in both designs, so we can 
       * simply fetch them from the primary Window. Since primary.getBillFields().size() == 7, 
       * we have to check, if i is < 7 to prevent an ArrayIndexOutOfBoundsException. If i == 7,
       * empty Cells with Border will be created.
       */
      if (i < 7) {
        ExportUtils.createCell(row, 4, billIds[i], styles.get(ExportUtils.BLACK_WITH_BORDERS), 
            null, false);
        ExportUtils.createCell(row, 5, primary.getBillFields().get(i).getText(), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 6, primary.getBillFieldRes().get(i).getText().substring(2), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
      } else {
        ExportUtils.createCell(row, 4, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 5, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, 6, "", styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
      }
      
      newIndex++;
    }
    /*
     * Creates the Row where the Sums are displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 2, "Kleingeld:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 3, primary.getLabels().get(primary.coinageSumId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 5, "Scheine:", styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 6, primary.getLabels().get(primary.billsSumId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    newIndex += 2;
    
    /*
     * Creates the Row where the amount of coinage needed is displayed as well as the total Sum.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 2, "Muss Kleingeld:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 3, primary.getLabels().get(primary.coinageNeededId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    ExportUtils.createCell(row, 5, "Gesamtsumme:", styles.get(ExportUtils.STANDARD_RED), null, 
        false);
    ExportUtils.createCell(row, 6, primary.getLabels().get(primary.totalSumId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    newIndex += 2;
    
    /*
     * Creates the Row where the difference in coinage is displayed as well as the cash needed, 
     * that was calculated by the register's system.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 2, "Differenz Kleingeld:", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    ExportUtils.createCell(row, 3, primary.getLabels().get(primary.diffCoinageId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), styles.get(ExportUtils.STANDARD_RED), true);
    ExportUtils.createCell(row, 5, "Kassenschnitt Bar:", styles.get(ExportUtils.STANDARD_BLACK), 
        null, false);
    ExportUtils.createCell(row, 6, primary.getLabels().get(primary.cashNeededId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    newIndex += 2;
    
    /*
     * Creates the Row, where the Tip sum is displayed.
     */
    row = sheet.createRow(newIndex);
    ExportUtils.createCell(row, 5, "Rest Tip:", styles.get(ExportUtils.STANDARD_BLACK), null, 
        false);
    ExportUtils.createCell(row, 6, primary.getLabels().get(primary.tipSumId).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), styles.get(ExportUtils.STANDARD_RED), true);
    
    /*
     * Returns the index of the Row below the last entry in the table.
     */
    return newIndex + 1;
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
      case "M�rz": 
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
