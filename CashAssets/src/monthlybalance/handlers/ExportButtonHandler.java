package monthlybalance.handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import monthlybalance.MBalanceWindow;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.ExportUtils;

/**
 * The Handler for the Export Button. Will export the Data into a new Excel File.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The MBalanceWindow, that opened the Export Stage, where this Button was clicked.
   */
  private final MBalanceWindow primary;
  
  /**
   * The boolean value that determines, if the Directory should be opened after exporting.
   */
  private final boolean open;
  
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
   * The Constructor for this Handler. Sets all parameters to their dedicated fields. 
   * @param primary The {@link MBalanceWindow}, this Handler was called from.
   * @param checkOpen The boolean value, if the Explorer should be opened after exporting.
   * @param monthBox  The ComboBox, where the User entered the Month which should be calculated.
   * @param tfYear  The ComboBox, where the User entered the Year of the Month to be calculated.
   * @param dialog  The Stage, the Button for this Handler is part of. Will be closed after 
   *                exporting.
   */
  public ExportButtonHandler(MBalanceWindow primary, boolean checkOpen, ComboBox<String> monthBox, 
      TextField tfYear, Stage dialog) {
    this.primary = primary;
    this.open = checkOpen;
    this.monthBox = monthBox;
    this.tfYear = tfYear;
    this.dialog = dialog;
  }

  @SuppressWarnings("unchecked")
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
    sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
    ExportUtils.createCell(row, 2, "Monatsabrechnung " + getDate().replaceAll("x", "/"), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Creates the Date Cells.
     */
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    Date dateobj = new Date();
    ExportUtils.createDateCells(row, 4, styles.get(ExportUtils.STANDARD_BLACK), 
        df.format(dateobj));
    
    
    //Creates the Data Area, where all the User inputs are displayed in the Sheet.
    /*
     * Creates the Values, that are displayed in the first Table. These are stored as String in 
     * ArrayLists for each Column and in total, as an ArrayList of ArrayLists for each Row.
     */
    ArrayList<ArrayList<String>> tableValues = new ArrayList<ArrayList<String>>();
    ArrayList<String> tmp = new ArrayList<String>();
    Collections.addAll(tmp, new String[]{"Geldbörsen & Kassen", "Bestand", "Anzahl:", "Summe:"});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"Geldbörsen", "325€", 
        primary.getRegisterFields().get(0).getText(), 
        primary.getRegisterLabels().get(0).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"Ath Kassen", "462€", 
        primary.getRegisterFields().get(1).getText(), 
        primary.getRegisterLabels().get(1).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    /*
     * Creates the Table itself.
     */
    createTable(sheet, styles, 4, 2, 3, tableValues, primary.getSumLabels().get(0).getText());
    tableValues.clear();
    
    /*
     * Creates the Values, that are displayed in the second Table. These are stored as String in 
     * ArrayLists for each Column and in total, as an ArrayList of ArrayLists for each Row.
     */
    tmp.clear();
    Collections.addAll(tmp, new String[]{"Wechselgeld Rollen", "Bestand", "Anzahl:", "Summe:"});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"0,10€", "4€", 
        primary.getRouleauFields().get(0).getText(), 
        primary.getRouleauLabels().get(0).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"0,20€", "8€", 
        primary.getRouleauFields().get(1).getText(), 
        primary.getRouleauLabels().get(1).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"0,50€", "20€", 
        primary.getRouleauFields().get(2).getText(), 
        primary.getRouleauLabels().get(2).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"1€", "25€", 
        primary.getRouleauFields().get(3).getText(), 
        primary.getRouleauLabels().get(3).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp, new String[]{"2€", "50€", 
        primary.getRouleauFields().get(4).getText(), 
        primary.getRouleauLabels().get(4).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    /*
     * Creates the Table itself.
     */
    createTable(sheet, styles, 9, 2, 6, tableValues, primary.getSumLabels().get(1).getText());
    tableValues.clear();
    
    /*
     * Creates the Values, that are displayed in the third Table. These are stored as String in 
     * ArrayLists for each Column and in total, as an ArrayList of ArrayLists for each Row.
     */
    tmp.clear();
    Collections.addAll(tmp, new String[]{"Wechselgeld Lose", "Anzahl:", "Summe:"});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,01€", primary.getCoinageFields().get(0).getText(), 
        primary.getCoinageLabels().get(0).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,02€", primary.getCoinageFields().get(1).getText(), 
        primary.getCoinageLabels().get(1).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,05€", primary.getCoinageFields().get(2).getText(), 
        primary.getCoinageLabels().get(2).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,10€", primary.getCoinageFields().get(3).getText(), 
        primary.getCoinageLabels().get(3).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,20€", primary.getCoinageFields().get(4).getText(), 
        primary.getCoinageLabels().get(4).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"0,50€", primary.getCoinageFields().get(5).getText(), 
        primary.getCoinageLabels().get(5).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"1€", primary.getCoinageFields().get(6).getText(), 
        primary.getCoinageLabels().get(6).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"2€", primary.getCoinageFields().get(7).getText(), 
        primary.getCoinageLabels().get(7).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    /*
     * Creates the Table itself.
     */
    createTable(sheet, styles, 17, 3, 7, tableValues, primary.getSumLabels().get(2).getText());
    tableValues.clear();
       
    /*
     * Creates the Values, that are displayed in the fourth Table. These are stored as String in 
     * ArrayLists for each Column and in total, as an ArrayList of ArrayLists for each Row.
     */
    tmp.clear();
    Collections.addAll(tmp, new String[]{"Scheine", "Anzahl:", "Summe:"});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"5€", primary.getBillFields().get(0).getText(), 
        primary.getBillLabels().get(0).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"10€", primary.getBillFields().get(1).getText(), 
        primary.getBillLabels().get(1).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"20€", primary.getBillFields().get(2).getText(), 
        primary.getBillLabels().get(2).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"50€", primary.getBillFields().get(3).getText(), 
        primary.getBillLabels().get(3).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"100€", primary.getBillFields().get(4).getText(), 
        primary.getBillLabels().get(4).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"200€", primary.getBillFields().get(5).getText(), 
        primary.getBillLabels().get(5).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    tmp.clear();
    Collections.addAll(tmp,  new String[]{"500€", primary.getBillFields().get(6).getText(), 
        primary.getBillLabels().get(6).getText()});
    tableValues.add((ArrayList<String>) tmp.clone());
    
    /*
     * Creates the Table itself.
     */
    createTable(sheet, styles, 28, 3, 6, tableValues, primary.getSumLabels().get(3).getText());
    tableValues.clear();
    
    /*
     * Creates the Last Row, where the total Sum is displayed.
     */
    row = sheet.createRow(38);
    sheet.addMergedRegion(new CellRangeAddress(38, 38, 2, 3));
    ExportUtils.createCell(row, 2, "Kassenbestand Gesamt:", styles.get(ExportUtils.STANDARD_RED), 
        null, false);
    ExportUtils.createCell(row, 4, primary.getSumLabels().get(4).getText(), 
        styles.get(ExportUtils.STANDARD_BLACK), null, false);
    
    /*
     * Exporting the created Excel File.
     */
    String date = getDate();
    String year = date.substring(date.lastIndexOf('x') + 1);
    String month = monthBox.getSelectionModel().getSelectedItem();
    ExportUtils.export(workbook, "Monatsabrechnung", date, year, month, open);
    dialog.close();
  }

  /**
   * Creates a Table in the given sheet. The top left Cell is at (row:index, column:1) and top 
   * right is at column 5. The length is given by tableLength.
   * @param sheet The Sheet, in which the Table will be created in.
   * @param styles All Styles, that are available. Used to design Cells accordingly.
   * @param index The rowIndex of the Table to be created.
   * @param headerLength The Length of the top left Cell. This can vary between 2 and 3.
   * @param tableLength The total length of the Cell, without the Row for ([Komplettsumme:][xxxxx]).
   *     This means, the last row created by this Method will be index + lablelength.
   * @param values The Values for each Cell as an ArrayList of ArrayList of Strings. The inner 
   *      ArrayList stores the values for each row and the outer ArrayList stores all Rows.
   * @param totalSum The Text, that will be displayed as total Sum.
   * @since 1.0
   */
  private void createTable(XSSFSheet sheet, 
      HashMap<Integer, XSSFCellStyle> styles, int index, int headerLength, int tableLength, 
      ArrayList<ArrayList<String>> values, String totalSum) {
    /*
     * Iterates over the the values-ArrayList. tableLength is used as parameter to reveal wrong 
     * inputs.
     */
    for (int i = 0; i < tableLength; i++) {
      /*
       * Creates a new Row below the last Row.
       */
      XSSFRow row = sheet.createRow(index + i);
      /*
       * Adds a merged Region for the leftmost Cells of the Table.
       */
      sheet.addMergedRegion(new CellRangeAddress(index + i, index + i, 1, headerLength));
      /*
       * If i == 0 is true, then this Row is the Header of the Table and will be created in red 
       * font.
       */
      if (i == 0) {
        /*
         * Creates the header from the given ArrayList at values.get(i).
         */
        ExportUtils.createCell(row, 1, values.get(i).get(0), 
            styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, headerLength + 1, values.get(i).get(1), 
            styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, headerLength + 2, values.get(i).get(2), 
            styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
        /*
         * If headerLength is 3, then there are only 3 Cells per Row.
         */
        if (headerLength < 3) {
          ExportUtils.createCell(row, headerLength + 3, values.get(i).get(3), 
              styles.get(ExportUtils.RED_WITH_BORDERS), null, false);
        }
      } else {
        /*
         * Since i is greater than 0, a data Row has to be created in standard black font.
         * These calls will create the Row from the given ArrayList at values.get(i).
         */
        ExportUtils.createCell(row, 1, values.get(i).get(0), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, headerLength + 1, values.get(i).get(1), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        ExportUtils.createCell(row, headerLength + 2, values.get(i).get(2), 
            styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        /*
         * If headerLength is 3, then there are only 3 Cells per Row.
         */
        if (headerLength < 3) {
          ExportUtils.createCell(row, headerLength + 3, values.get(i).get(3), 
              styles.get(ExportUtils.BLACK_WITH_BORDERS), null, false);
        }
      }
    }
    /*
     * Creates a last new Row to display the total Sum of this Tab.
     */
    XSSFRow row = sheet.createRow(index + tableLength);
    /*
     * Creates the 2 Cells, that are needed to display the Sum.
     */
    ExportUtils.createCell(row, 4, "Komplettsumme:", styles.get(ExportUtils.STANDARD_RED), null, 
        false);
    ExportUtils.createCell(row, 5, totalSum, styles.get(ExportUtils.STANDARD_BLACK), null, false);
  }
  
  /**
   * Returns a String, that describes the Date the User entered in the pattern MMxYYYY, so the 
   * Calculation has a consistent Value for the Date.
   * @return  The Date, that the User entered in the pattern MMxYYYY
   * @since 1.0
   */
  private String getDate() {
    String res = "";
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
