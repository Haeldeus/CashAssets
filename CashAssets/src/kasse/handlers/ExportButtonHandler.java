package kasse.handlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import kasse.ComponentStorer;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    //TODO add a Password to check permission before exporting?
    //Create a new Workbook
    XSSFWorkbook workbook = new XSSFWorkbook();
    
    //Create a new Sheet for the Excel File.
    XSSFSheet sheet = workbook.createSheet(getDate().replaceAll("\\.", " "));
    
    //Basic Sheet Settings
    for (int i = 0; i <= 7; i++) {
      sheet.setColumnWidth(i, 3900);  //Setting Column Width
    }
    sheet.getPrintSetup().setLandscape(true); //Setting Landscape View

    //Creates new CellStyles, for the Standard Cells as well as the red ones.
    final XSSFCellStyle csStandard = workbook.createCellStyle();
    csStandard.setAlignment(HorizontalAlignment.CENTER);
    
    final XSSFCellStyle csRed = workbook.createCellStyle();
    csRed.setAlignment(HorizontalAlignment.CENTER);
    XSSFFont font = workbook.createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short)10);
    font.setColor(IndexedColors.RED.getIndex());
    csRed.setFont(font);
    
    final XSSFCellStyle csRedBorder = workbook.createCellStyle();
    csRedBorder.setAlignment(HorizontalAlignment.CENTER);
    csRedBorder.setFont(font);
    csRedBorder.setBorderBottom(BorderStyle.HAIR);
    csRedBorder.setBorderLeft(BorderStyle.HAIR);
    csRedBorder.setBorderRight(BorderStyle.HAIR);
    csRedBorder.setBorderTop(BorderStyle.HAIR);
    
    final XSSFCellStyle csStandardBorder = workbook.createCellStyle();
    csStandardBorder.setAlignment(HorizontalAlignment.CENTER);
    csStandardBorder.setBorderBottom(BorderStyle.HAIR);
    csStandardBorder.setBorderLeft(BorderStyle.HAIR);
    csStandardBorder.setBorderRight(BorderStyle.HAIR);
    csStandardBorder.setBorderTop(BorderStyle.HAIR);
    
    final XSSFCellStyle csUnderscoreBorder = workbook.createCellStyle();
    csUnderscoreBorder.setAlignment(HorizontalAlignment.CENTER);
    csUnderscoreBorder.setBorderBottom(BorderStyle.HAIR);
    
    //Create the Header
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
    XSSFRow row = sheet.createRow(0);
    createCell(row, 2, "Gaststätte Weyher GmbH Haxtergrund 8 33100 Paderborn", csStandard, null, 
        false);
    
    //Create the second Row
    row = sheet.createRow(1);
    
    createCell(row, 2, "Zählprotokoll Bar", csStandard, null, false);
    createCell(row, 4, "Datum:", csStandard, null, false);
    createCell(row, 5, getDate(), csStandard, null, false);
    
    //Create the fourth Row
    row = sheet.createRow(3);
    sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
    
    createCell(row, 2, "Gezählte Geldbörsen:", csStandard, null, false);
    createCell(row, 4, componentStorer.getPurseTextField().getText(), csStandard, null, false);
    
    //Create the sixth Row
    row = sheet.createRow(5);
    
    createCell(row, 1, "Einheit", csRedBorder, null, false);
    createCell(row, 2, "Stück", csRedBorder, null, false);
    createCell(row, 3, "Betrag", csRedBorder, null, false);
    createCell(row, 4, "Einheit", csRedBorder, null, false);
    createCell(row, 5, "Stück", csRedBorder, null, false);
    createCell(row, 6, "Betrag", csRedBorder, null, false);
    
    //Create the seventh Row
    row = sheet.createRow(6);
    
    createCell(row, 1, "1ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[0].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[0].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "5€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[0].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[0].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the eighth Row
    row = sheet.createRow(7);
    
    createCell(row, 1, "2ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[1].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[1].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "10€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[1].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[1].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the ninth Row
    row = sheet.createRow(8);
    
    createCell(row, 1, "5ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[2].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[2].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "20€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[2].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[2].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the tenth Row
    row = sheet.createRow(9);
    
    createCell(row, 1, "10ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[3].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[3].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "50€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[3].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[3].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the eleventh Row
    row = sheet.createRow(10);
    
    createCell(row, 1, "20ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[4].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[4].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "100€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[4].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[4].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the twelfth Row
    row = sheet.createRow(11);
    
    createCell(row, 1, "50ct", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[5].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[5].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "200€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[5].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[5].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the thirteenth Row
    row = sheet.createRow(12);
    
    createCell(row, 1, "1€", csStandardBorder, null, false);
    createCell(row, 2, componentStorer.getCoinTextFields()[6].getText(), csStandardBorder, null, 
        false);
    createCell(row, 3, componentStorer.getCoinResults()[6].getText().substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "500€", csStandardBorder, null, false);
    createCell(row, 5, componentStorer.getBillsTextFields()[6].getText(), csStandardBorder, null, 
        false);
    createCell(row, 6, componentStorer.getBillsResults()[6].getText().substring(2), 
        csStandardBorder, null, false);
    
    //Create the fourteenth Row
    row = sheet.createRow(13);
    
    createCell(row, 1, "2€", csStandardBorder, null, false);
    if (!simple) {
      createCell(row, 2, componentStorer.getCoinTextFields()[7].getText(), csStandardBorder, null, 
          false);
      createCell(row, 3, componentStorer.getCoinResults()[7].getText().substring(2), 
          csStandardBorder, null, false);
    } else {
      createCell(row, 2, "0", csStandardBorder, null, false);
      createCell(row, 3, "0,00€", csStandardBorder, null, false);
    }
    for (int i = 4; i <= 6; i++) {
      createCell(row, i, null, csStandardBorder, null, false);
    }
    
    //Create the fifteenth Row
    row = sheet.createRow(14);
    
    createCell(row, 2, "Kleingeld:", csStandard, null, false);
    createCell(row, 3, componentStorer.getCoinSumLabel().getText(), csStandard, null, 
        false);
    createCell(row, 5, "Scheine:", csStandard, null, false);
    createCell(row, 6, componentStorer.getBillSumLabel().getText(), csStandard, null, false);
    
    //Create the seventeenth Row
    row = sheet.createRow(16);
    
    createCell(row, 2, "Muss Kleingeld:", csStandard, null, false);
    createCell(row, 3, componentStorer.getCoinNecessityLabel().getText(), csStandard, 
        null, false);
    createCell(row, 5, "Gesamtsumme:", csRed, null, false);
    createCell(row, 6, componentStorer.getCoinCleanedLabel().getText(), csStandard, 
        null, false);
    
    //Create the nineteenth Row
    row = sheet.createRow(18);
    
    createCell(row, 2, "Differenz Kleingeld:", csStandard, null, false);
    createCell(row, 3, componentStorer.getCoinDifferenceLabel().getText(), csStandard, 
        csRed, true);
    createCell(row, 5, "Kassenschnitt Bar:", csStandard, null, false);
    createCell(row, 6, componentStorer.getSumLabel().getText(), csStandard, 
        null, false);
    
    // Create the twenty-first Row
    row = sheet.createRow(20);
    
    createCell(row, 5, "Rest Tip:", csStandard, null, false);
    createCell(row, 6, componentStorer.getTipSumLabel().getText(), csStandard, csRed, true);
    
    //Create the twenty-third Row
    row = sheet.createRow(22);
    sheet.addMergedRegion(new CellRangeAddress(22, 22, 1, 2));
    
    createCell(row, 1, "Wichtige Bemerkungen:", csStandard, null, false);
    for (int i = 3; i <= 6; i++) {
      createCell(row, i, null, csUnderscoreBorder, null, false);
    }
    
    //Create the twenty-fourth Row
    row = sheet.createRow(23);
    
    for (int i = 2; i <= 6; i++) {
      createCell(row, i, null, csUnderscoreBorder, null, false);
    }
    
    //Create the twenty-fifth Row
    row = sheet.createRow(24);
    
    for (int i = 2; i <= 6; i++) {
      createCell(row, i, null, csUnderscoreBorder, null, false);
    }
    
    //Create the twenty-eighth Row
    row = sheet.createRow(27);
    sheet.addMergedRegion(new CellRangeAddress(27, 27, 1, 2));
    
    createCell(row, 1, "Gezählt von Franz Weyher:", csStandard, null, false);
    createCell(row, 3, null, csUnderscoreBorder, null, false);
    createCell(row, 4, null, csUnderscoreBorder, null, false);
    
    //Create the thirtieth Row
    row = sheet.createRow(29);
    
    createCell(row, 1, "Mitarbeiter:", csStandard, null, false);
    
    //Create the thirty-first Row
    row = sheet.createRow(30);
    
    createCell(row, 1, "Name/Vorname:", csStandard, null, false);
    createCell(row, 2, null, csUnderscoreBorder, null, false);
    createCell(row, 3, null, csUnderscoreBorder, null, false);
    createCell(row, 4, "Unterschrift:", csStandard, null, false);
    createCell(row, 5, null, csUnderscoreBorder, null, false);
    createCell(row, 6, null, csUnderscoreBorder, null, false);
    
    //Creating the Excel-Sheet.
    try {
      /*
       * The Excel-Sheet will be called the same as the Date replacing the Dots in the "official" 
       * representation with an x to match OS-naming patterns.
       */
      String s = getDate().replaceAll("\\.", "x") + ".xlsx";
      
      /*
       * Creates a new Path to the potential new File.
       */
      Path p = Paths.get(s);
      
      /*
       * If the File already exists, a new file called DDxMMxYYYY(i).xlsx will be created with i 
       * being the lowest natural number that describes a path that isn't a File already.
       */
      if (Files.exists(p)) {
        /*
         * Creates a Counter to count up existing Files.
         */
        int counter = 1;
        /*
         * Replaces the File-Ending .xlsx with (1).xlsx to check, if a single iteration would 
         * already exit the loop.
         */
        s = s.replace(".xlsx", "(1).xlsx");
        p = Paths.get(s);
        /*
         * For as long as there are already existing files with the given name 
         * DDxNNxYYYY(>counter<).xlsx, the counter will be raised.
         */
        while (Files.exists(p)) {
          /*
           * Replaces (>counter<) with (>counter + 1<) in the File Name.
           */
          s = s.replace("(" + counter + ").xlsx", "(" + (counter + 1) + ").xlsx");
          counter++;
          p = Paths.get(s);
        }
      }
      
      /**
       * Let the Workbook writes its content into the new File.
       */
      workbook.write(new FileOutputStream(s));
      
      /*
       * Closes the Workbook.
       */
      workbook.close();
      
      /*
       * Opens the Folder that contains the newly created File and selects it to let the User 
       * open it immediately after creation.
       */
      if (open) {
        Runtime.getRuntime().exec("explorer.exe /select," + s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    box = componentStorer.getMonthBox();
    if (Integer.parseInt(box.getSelectionModel().getSelectedItem().replaceAll("\\.", "")) < 10) {
      res = res.concat("0" + box.getSelectionModel().getSelectedItem());
    } else {
      res = res.concat("" + box.getSelectionModel().getSelectedItem());
    }
    res = res.concat("" + componentStorer.getYear().getText().replaceAll("\\D+", "")
        .substring(0, 4));
    return res;
  }

  /**
   * Creates a Cell in the Excel Sheet. This will be created in the given {@code row} at the given 
   * {@code column}. The value of the Cell will be set to {@code data} with the given CellStyle 
   * {@code style}.
   * @param row The Row, in which the new Cell will be created.
   * @param column  The Column, in which the new Cell will be placed in.
   * @param data  The Value of the new Cell.
   * @param style The Style of the Cell.
   * @param style2  The alternative Style of the Cell. Can be set to null, if there is no 
   *      alternative Style.
   * @param bool  {@code true}, if the Cell has an alternative Styling, {@code false} else.
   * @see XSSFRow
   * @see XSSFCellStyle
   * @since 1.0
   */
  private void createCell(XSSFRow row, int column, String data, XSSFCellStyle style, 
      XSSFCellStyle style2, boolean bool) {
    XSSFCell cell = row.createCell(column);
    if (data != null) {
      cell.setCellValue(data);
    }
    if (bool) {
      //TODO Testing purposes. This shouldn't be used anymore if I'm correct. 
      //cell.setCellValue(cell.getStringCellValue().replace("<html><font color='red'>", ""));
      //cell.setCellValue(cell.getStringCellValue().replaceAll("</font></html>", ""));
      if (cell.toString().charAt(0) == '-') {
        cell.setCellStyle(style2);
      } else {
        cell.setCellStyle(style);
      }
    } else {
      cell.setCellStyle(style);
    }
  }
}
