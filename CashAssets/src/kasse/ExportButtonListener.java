package kasse;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
 * The ActionListener for the Export-Button.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonListener implements ActionListener {

  /**
   * The Container, that holds the Button, this ActionListener was added to.
   * In this case, it's the contentPane of the JFrame.
   */
  private final Container container;
  
  /**
   * The Constructor for the ActionListener of the Export Button. This will set the given Container 
   * as the contentPane of the Frame, as far as this Class knows. This is needed to get all Label 
   * Values and other inputs of the User.
   * @param c The Container, that holds the contentPane of the Frame, this Button was added to.
   * @since 1.0
   */
  public ExportButtonListener(Container c) {
    this.container = c;
  }
  

  @Override
  public void actionPerformed(ActionEvent arg0) {
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
    createCell(row, 4, getCellValueFromTextField(KassenLayout.purseTF), csStandard, null, false);
    
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
    createCell(row, 2, getCellValueFromTextField(KassenLayout.oneCentTF), csStandardBorder, null, 
        false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.oneCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "5€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.fiveEuroTF), csStandardBorder, null, 
        false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.fiveEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the eighth Row
    row = sheet.createRow(7);
    
    createCell(row, 1, "2ct", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.twoCentTF), csStandardBorder, null, 
        false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.twoCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "10€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.tenEuroTF), csStandardBorder, null, 
        false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.tenEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the ninth Row
    row = sheet.createRow(8);
    
    createCell(row, 1, "5ct", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.fiveCentTF), csStandardBorder, null, 
        false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.fiveCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "20€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.twentyEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.twentyEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the temth Row
    row = sheet.createRow(9);
    
    createCell(row, 1, "10ct", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.tenCentTF), csStandardBorder, null, 
        false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.tenCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "50€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.fiftyEuroTF), csStandardBorder, null, 
        false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.fiftyEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the eleventh Row
    row = sheet.createRow(10);
    
    createCell(row, 1, "20ct", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.twentyCentTF), csStandardBorder, 
        null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.twentyCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "100€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.hundredEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.hundredEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the twelfth Row
    row = sheet.createRow(11);
    
    createCell(row, 1, "50ct", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.fiftyCentTF), csStandardBorder, 
        null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.fiftyCentSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "200€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.twohundredEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.twohundredEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the thirteenth Row
    row = sheet.createRow(12);
    
    createCell(row, 1, "1€", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.oneEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.oneEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    createCell(row, 4, "500€", csStandardBorder, null, false);
    createCell(row, 5, getCellValueFromTextField(KassenLayout.fivehundredEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.fivehundredEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    
    //Create the fourteenth Row
    row = sheet.createRow(13);
    
    createCell(row, 1, "2€", csStandardBorder, null, false);
    createCell(row, 2, getCellValueFromTextField(KassenLayout.twoEuroTF), csStandardBorder, 
        null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.twoEuroSumLabel).substring(2), 
        csStandardBorder, null, false);
    for (int i = 4; i <= 6; i++) {
      createCell(row, i, null, csStandardBorder, null, false);
    }
    
    
    //Create the fifteenth Row
    row = sheet.createRow(14);
    
    createCell(row, 2, "Kleingeld:", csStandard, null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.coinageSumLabel), csStandard, null, 
        false);
    createCell(row, 5, "Scheine:", csStandard, null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.billsSumLabel), csStandard, null, false);
    
    //Create the seventeenth Row
    row = sheet.createRow(16);
    
    createCell(row, 2, "Muss Kleingeld:", csStandard, null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.coinageNecessitySumLabel), csStandard, 
        null, false);
    createCell(row, 5, "Gesamtsumme:", csRed, null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.revenuesWithTipSumLabel), csStandard, 
        null, false);
    
    //Create the nineteenth Row
    row = sheet.createRow(18);
    
    createCell(row, 2, "Differenz Kleingeld:", csStandard, null, false);
    createCell(row, 3, getCellValueFromLabel(KassenLayout.coinageDifferenceSumLabel), csStandard, 
        csRed, true);    
    createCell(row, 5, "Kassenschnitt Bar:", csStandard, null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.totalCashNecessitySumLabel), csStandard, 
        null, false);
    
    // Create the twenty-first Row
    row = sheet.createRow(20);
    
    createCell(row, 5, "Rest Tip:", csStandard, null, false);
    createCell(row, 6, getCellValueFromLabel(KassenLayout.tipSumLabel), csStandard, csRed, true);
    
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
    
    //Create the twenty-eigth Row
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
    
    try {
      String s = getDate().replaceAll("\\.", "x") + ".xlsx";
      workbook.write(new FileOutputStream(s));
      workbook.close();
      Runtime.getRuntime().exec("explorer.exe /select," + s);   //TODO: add setting for this
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Returns the Date, the User has entered in the Frame.
   * @return A Single String of the Form "dd.mm.yyyy"
   * @since 1.0
   */
  @SuppressWarnings("unchecked")
  private String getDate() {
    String res = "";
    KassenLayout layout = (KassenLayout)container.getLayout();
    JComboBox<String> box = (JComboBox<String>)layout.getComp(KassenLayout.dateDayDropdown);
    if (Integer.parseInt(box.getSelectedItem().toString().replaceAll("\\.", "")) < 10) {
      res = res.concat("" + box.getSelectedItem().toString());
    } else {
      res = res.concat("0" + box.getSelectedItem().toString());
    }
    box = (JComboBox<String>)layout.getComp(KassenLayout.dateMonthDropdown);
    if (Integer.parseInt(box.getSelectedItem().toString().replaceAll("\\.", "")) < 10) {
      res = res.concat("" + box.getSelectedItem().toString());
    } else {
      res = res.concat("0" + box.getSelectedItem().toString());
    }
    JTextField year = (JTextField)layout.getComp(KassenLayout.dateYearTextField);
    res = res.concat("" + year.getText());
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
      cell.setCellValue(cell.getStringCellValue().replace("<html><font color='red'>", ""));
      cell.setCellValue(cell.getStringCellValue().replaceAll("</font></html>", ""));
      if (cell.toString().charAt(0) == '-') {
        cell.setCellStyle(style2);
      } else {
        cell.setCellStyle(style);
      }
    } else {
      cell.setCellStyle(style);
    }
  }
  
  /**
   * Returns the Text of the JTextField, that is described by the given String.
   * @param compNameTextField The String describing the JTextField. These can be found by 
   *      calling {@code KassenLayout.<CompName>}.
   * @return  The Text, that was written in the given JTextField by the User.
   * @see KassenLayout
   * @since 1.0
   */
  private String getCellValueFromTextField(String compNameTextField) {
    KassenLayout layout = (KassenLayout)container.getLayout();
    JTextField field = (JTextField)layout.getComp(compNameTextField);
    return field.getText();
  }
  
  /**
   * Returns the Text of the JLabel, that is described by the given String.
   * @param compNameLabel The String describing the JLabel. These can be found by 
   *      calling {@code KassenLayout.<CompName>}.
   * @return  The Text, that was written in the given JLabel by the application.
   * @see KassenLayout
   * @since 1.0
   */
  private String getCellValueFromLabel(String compNameLabel) {
    KassenLayout layout = (KassenLayout)container.getLayout();
    JLabel field = (JLabel)layout.getComp(compNameLabel);
    return field.getText();
  }
}
