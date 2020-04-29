package oldca.handlers;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import oldca.ComponentStorer;

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
   * The Container, that holds the Button, this ActionListener was added to.
   * In this case, it's the contentPane of the JFrame.
   */
  private final ComponentStorer componentStorer;
  
  /**
   * The boolean value that determines, if the simple Design is used. In this case, 
   * there are a few Cells that have to be created differently.
   */
  private final boolean simple;
  
  /**
   * The boolean value that determines, if the Directory should be opened after exporting.
   */
  private final boolean open;
  
  /**
   * Creates a new Handler for the Export Button. The componentStorer is used to 
   * access the TextFields and Labels in the primaryStage of the Application.
   * @param componentStorer The ComponentStorer of the Application, which handles all 
   *     Components, that are of use for this Handler.
   * @param simple  The boolean value, if the simple design is used.
   * @param open  The boolean value, if the directory should be opened.
   * @since 1.0
   */
  public ExportButtonHandler(ComponentStorer componentStorer, boolean simple, boolean open) {
    this.componentStorer = componentStorer;
    this.simple = simple;
    this.open = open;
  }

  @Override
  public void handle(MouseEvent arg0) {
    //TODO Shorten this Method with for-loops for the actual input of the User to the Excel Sheet.

    //Create a new Workbook
    XSSFWorkbook workbook = new XSSFWorkbook();
    
    //Create a new Sheet for the Excel File.
    XSSFSheet sheet = workbook.createSheet(getDate().replaceAll("\\.", " "));
    
    //Basic Sheet Settings
    for (int i = 0; i <= 7; i++) {
      sheet.setColumnWidth(i, 3900);  //Setting Column Width
    }
    sheet.getPrintSetup().setLandscape(true); //Setting Landscape View

    /*
     * Creates the Styles for this Sheet.
     */
    final HashMap<Integer, XSSFCellStyle> styles = ExportUtils.getStyles(workbook);
    final XSSFCellStyle csStandard = styles.get(ExportUtils.STANDARD_BLACK);
    final XSSFCellStyle csRed = styles.get(ExportUtils.STANDARD_RED);
    final XSSFCellStyle csRedBorder = styles.get(ExportUtils.RED_WITH_BORDERS);
    final XSSFCellStyle csStandardBorder = styles.get(ExportUtils.BLACK_WITH_BORDERS);
    
    //Create the Header
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
    XSSFRow row = sheet.createRow(0);
    ExportUtils.createCell(row, 2, ExportUtils.EXPORT_HEADER, csStandard, null, false);
    
    //Create the second Row
    row = sheet.createRow(1);
    
    ExportUtils.createCell(row, 2, "Zählprotokoll Bar", csStandard, null, false);
    ExportUtils.createCell(row, 4, "Datum:", csStandard, null, false);
    ExportUtils.createCell(row, 5, getDate(), csStandard, null, false);
    
    //Create the fourth Row
    row = sheet.createRow(3);
    sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
    
    ExportUtils.createCell(row, 2, "Gezählte Geldbörsen:", csStandard, null, false);
    ExportUtils.createCell(row, 4, componentStorer.getPurseTextField().getText(), csStandard, 
        null, false);
    
    //Create the sixth Row
    row = sheet.createRow(5);
    
    ExportUtils.createCell(row, 1, "Einheit", csRedBorder, null, false);
    ExportUtils.createCell(row, 2, "Stück", csRedBorder, null, false);
    ExportUtils.createCell(row, 3, "Betrag", csRedBorder, null, false);
    ExportUtils.createCell(row, 4, "Einheit", csRedBorder, null, false);
    ExportUtils.createCell(row, 5, "Stück", csRedBorder, null, false);
    ExportUtils.createCell(row, 6, "Betrag", csRedBorder, null, false);
    
    //Create the seventh Row
    row = sheet.createRow(6);
    
    ExportUtils.createCell(row, 1, "1ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[0].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[0].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "5€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[0].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[0].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the eighth Row
    row = sheet.createRow(7);
    
    ExportUtils.createCell(row, 1, "2ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[1].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[1].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "10€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[1].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[1].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the ninth Row
    row = sheet.createRow(8);
    
    ExportUtils.createCell(row, 1, "5ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[2].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[2].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "20€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[2].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[2].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the tenth Row
    row = sheet.createRow(9);
    
    ExportUtils.createCell(row, 1, "10ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[3].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[3].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "50€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[3].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[3].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the eleventh Row
    row = sheet.createRow(10);
    
    ExportUtils.createCell(row, 1, "20ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[4].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[4].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "100€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[4].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[4].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the twelfth Row
    row = sheet.createRow(11);
    
    ExportUtils.createCell(row, 1, "50ct", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[5].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[5].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "200€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[5].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[5].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the thirteenth Row
    row = sheet.createRow(12);
    
    ExportUtils.createCell(row, 1, "1€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[6].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[6].getText().substring(2), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 4, "500€", csStandardBorder, null, false);
    ExportUtils.createCell(row, 5, componentStorer.getBillsTextFields()[6].getText(), 
        csStandardBorder, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillsResults()[6].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the fourteenth Row
    row = sheet.createRow(13);
    
    ExportUtils.createCell(row, 1, "2€", csStandardBorder, null, false);
    if (!simple) {
      ExportUtils.createCell(row, 2, componentStorer.getCoinTextFields()[7].getText(), 
          csStandardBorder, null, false);
      ExportUtils.createCell(row, 3, componentStorer.getCoinResults()[7].getText().substring(2), 
          csStandardBorder, null, false);
    } else {
      ExportUtils.createCell(row, 2, "0", csStandardBorder, null, false);
      ExportUtils.createCell(row, 3, "0,00€", csStandardBorder, null, false);
    }
    for (int i = 4; i <= 6; i++) {
      ExportUtils.createCell(row, i, null, csStandardBorder, null, false);
    }
    
    //Create the fifteenth Row
    row = sheet.createRow(14);
    
    ExportUtils.createCell(row, 2, "Kleingeld:", csStandard, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinSumLabel().getText(), csStandard, null, 
        false);
    ExportUtils.createCell(row, 5, "Scheine:", csStandard, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getBillSumLabel().getText(), csStandard, null, 
        false);
    
    //Create the seventeenth Row
    row = sheet.createRow(16);
    
    ExportUtils.createCell(row, 2, "Muss Kleingeld:", csStandard, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinNecessityLabel().getText(), csStandard, 
        null, false);
    ExportUtils.createCell(row, 5, "Gesamtsumme:", csRed, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getCoinCleanedLabel().getText(), csStandard, 
        null, false);
    
    //Create the nineteenth Row
    row = sheet.createRow(18);
    
    ExportUtils.createCell(row, 2, "Differenz Kleingeld:", csStandard, null, false);
    ExportUtils.createCell(row, 3, componentStorer.getCoinDifferenceLabel().getText(), csStandard, 
        csRed, true);
    ExportUtils.createCell(row, 5, "Kassenschnitt Bar:", csStandard, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getSumLabel().getText(), csStandard, 
        null, false);
    
    // Create the twenty-first Row
    row = sheet.createRow(20);
    
    ExportUtils.createCell(row, 5, "Rest Tip:", csStandard, null, false);
    ExportUtils.createCell(row, 6, componentStorer.getTipSumLabel().getText(), csStandard, csRed, 
        true);
    
    ExportUtils.createSignatureArea(sheet, row, styles, 22);
    
    //Creating the Excel-Sheet.
    String year = componentStorer.getYear().getText().replaceAll("\\D+", "").substring(0, 4);
    String month = getMonth();
    String date = getDate().replaceAll("\\.", "x");
    ExportUtils.export(workbook, "Kassenbestand", date, year, month, open);
  }

  /**
   * Returns a String with the Name of the Month that is selected in the MonthBox.
   * @return  A String with the Name of the Month that was selected in the Month-Box.
   * @since 1.0
   */
  private String getMonth() {
    return componentStorer.getMonthBox().getSelectionModel().getSelectedItem();
  }
  

  /**
   * Returns the Date, the User has entered in the Stage.
   * @return A Single String of the Form "dd.mm.yyyy"
   * @since 1.0
   */
  private String getDate() {
    String res = "";
    ComboBox<String> box = componentStorer.getDayBox();
    if (Integer.parseInt(box.getSelectionModel().getSelectedItem().replaceAll("\\.", "")) < 10) {
      res = res.concat("0" + box.getSelectionModel().getSelectedItem());
    } else {
      res = res.concat("" + box.getSelectionModel().getSelectedItem());
    }
    res = res.concat(getStringForMonth() + ".");
    res = res.concat("" + componentStorer.getYear().getText().replaceAll("\\D+", "")
        .substring(0, 4));
    return res;
  }

  /**
   * Returns a String, that resembles the numerical value of the selected Month in the 
   * MonthBox. <br>
   * E.g. Januar equals 01, Februar equals 02, etc.
   * @return  The numerical value of the selected Month as a String.
   * @since 1.0
   */
  private String getStringForMonth() {
    ComboBox<String> box = componentStorer.getMonthBox();
    String month = box.getSelectionModel().getSelectedItem();
    switch (month) {
      case "Januar": return "01";
      case "Februar": return "02";
      case "März": return "03";
      case "April": return "04";
      case "Mai": return "05";
      case "Juni": return "06";
      case "Juli": return "07";
      case "August": return "08";
      case "September": return "09";
      case "Oktober": return "10";
      case "November": return "11";
      case "Dezember": return "12";
      default: 
        System.err.println("Error while parsing the Month...");
        return "00";
    }
  }
}
