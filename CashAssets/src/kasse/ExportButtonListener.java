package kasse;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

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
    XSSFCell cell = row.createCell(2);
    cell.setCellValue("Gaststätte Weyher GmbH Haxtergrund 8 33100 Paderborn");
    cell.setCellStyle(csStandard);
    
    //Create the second Row
    row = sheet.createRow(1);
    cell = row.createCell(2);
    cell.setCellValue("Zählprotokoll Bar");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(4);
    cell.setCellValue("Datum:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(5);
    cell.setCellValue(getDate());
    cell.setCellStyle(csStandard);
    
    //Create the fourth Row
    row = sheet.createRow(3);
    sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
    cell = row.createCell(2);
    cell.setCellValue("Gezählte Geldbörsen:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(4);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.purseTF));
    cell.setCellStyle(csStandard);
    
    //Create the sixth Row
    row = sheet.createRow(5);
    cell = row.createCell(1);
    cell.setCellValue("Einheit");
    cell.setCellStyle(csRedBorder);
    
    cell = row.createCell(2);
    cell.setCellValue("Stück");
    cell.setCellStyle(csRedBorder);
    
    cell = row.createCell(3);
    cell.setCellValue("Betrag");
    cell.setCellStyle(csRedBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("Einheit");
    cell.setCellStyle(csRedBorder);
    
    cell = row.createCell(5);
    cell.setCellValue("Stück");
    cell.setCellStyle(csRedBorder);
    
    cell = row.createCell(6);
    cell.setCellValue("Betrag");
    cell.setCellStyle(csRedBorder);
    
    //Create the seventh Row
    row = sheet.createRow(6);
    cell = row.createCell(1);
    cell.setCellValue("1ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.oneCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.oneCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("5€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.fiveEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.fiveEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the eighth Row
    row = sheet.createRow(7);
    cell = row.createCell(1);
    cell.setCellValue("2ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.twoCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.twoCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("10€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.tenEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.tenEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the ninth Row
    row = sheet.createRow(8);
    cell = row.createCell(1);
    cell.setCellValue("5ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.fiveCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.fiveCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("20€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.twentyEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.twentyEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the temth Row
    row = sheet.createRow(9);
    cell = row.createCell(1);
    cell.setCellValue("10ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.tenCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.tenCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("50€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.fiftyEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.fiftyEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    
    //Create the eleventh Row
    row = sheet.createRow(10);
    cell = row.createCell(1);
    cell.setCellValue("20ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.twentyCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.twentyCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("100€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.hundredEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.hundredEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the twelfth Row
    row = sheet.createRow(11);
    cell = row.createCell(1);
    cell.setCellValue("50ct");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.fiftyCentTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.fiftyCentSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("200€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.twohundredEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.twohundredEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the thirteenth Row
    row = sheet.createRow(12);
    cell = row.createCell(1);
    cell.setCellValue("1€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.oneEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.oneEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("500€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.fivehundredEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.fivehundredEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    //Create the fourteenth Row
    row = sheet.createRow(13);
    cell = row.createCell(1);
    cell.setCellValue("2€");
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(2);
    cell.setCellValue(getCellValueFromTextField(KassenLayout.twoEuroTF));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.twoEuroSumLabel).substring(2));
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(4);
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(5);
    cell.setCellStyle(csStandardBorder);
    
    cell = row.createCell(6);
    cell.setCellStyle(csStandardBorder);
    
    
    //Create the fifteenth Row
    row = sheet.createRow(14);
    cell = row.createCell(2);
    cell.setCellValue("Kleingeld:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.coinageSumLabel));
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(5);
    cell.setCellValue("Scheine:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.billsSumLabel));
    cell.setCellStyle(csStandard);
    
    //Create the seventeenth Row
    row = sheet.createRow(16);
    cell = row.createCell(2);
    cell.setCellValue("Muss Kleingeld:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.coinageNecessitySumLabel));
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(5);
    cell.setCellValue("Gesamtsumme:");
    cell.setCellStyle(csRed);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.revenuesWithTipSumLabel));
    cell.setCellStyle(csStandard);
    
    //Create the nineteenth Row
    row = sheet.createRow(18);
    cell = row.createCell(2);
    cell.setCellValue("Differenz Kleingeld:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(3);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.coinageDifferenceSumLabel));
    cell.setCellValue(cell.getStringCellValue().replace("<html><font color='red'>", ""));
    cell.setCellValue(cell.getStringCellValue().replaceAll("</font></html>", ""));
    if (cell.toString().charAt(0) == '-') {
      cell.setCellStyle(csRed);
    } else {
      cell.setCellStyle(csStandard);
    }
    
    cell = row.createCell(5);
    cell.setCellValue("Kassenschnitt Bar:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.totalCashNecessitySumLabel));
    cell.setCellStyle(csStandard);
    
    // Create the twenty-first Row
    row = sheet.createRow(20);
    cell = row.createCell(5);
    cell.setCellValue("Rest Tip:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(6);
    cell.setCellValue(getCellValueFromLabel(KassenLayout.tipSumLabel));
    cell.setCellValue(cell.getStringCellValue().replace("<html><font color='red'>", ""));
    cell.setCellValue(cell.getStringCellValue().replaceAll("</font></html>", ""));
    if (cell.toString().charAt(0) == '-') {
      cell.setCellStyle(csRed);
    } else {
      cell.setCellStyle(csStandard);
    }
    
    //Create the twenty-third Row
    row = sheet.createRow(22);
    sheet.addMergedRegion(new CellRangeAddress(22, 22, 1, 2));
    cell = row.createCell(1);
    cell.setCellValue("Wichtige Bemerkungen:");
    cell.setCellStyle(csStandard);
    
    for (int i = 3; i <= 6; i++) {
      cell = row.createCell(i);
      cell.setCellStyle(csUnderscoreBorder);
    }
    
    //Create the twenty-fourth Row
    row = sheet.createRow(23);
    for (int i = 2; i <= 6; i++) {
      cell = row.createCell(i);
      cell.setCellStyle(csUnderscoreBorder);
    }
    
    //Create the twenty-fifth Row
    row = sheet.createRow(24);
    for (int i = 2; i <= 6; i++) {
      cell = row.createCell(i);
      cell.setCellStyle(csUnderscoreBorder);
    }
    
    //Create the twenty-eigth Row
    row = sheet.createRow(27);
    sheet.addMergedRegion(new CellRangeAddress(27, 27, 1, 2));
    cell = row.createCell(1);
    cell.setCellValue("Gezählt von Franz Weyher:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(3);
    cell.setCellStyle(csUnderscoreBorder);
    
    cell = row.createCell(4);
    cell.setCellStyle(csUnderscoreBorder);
    
    //Create the thirtieth Row
    row = sheet.createRow(29);
    cell = row.createCell(1);
    cell.setCellValue("Mitarbeiter:");
    cell.setCellStyle(csStandard);
    
    //Create the thirty-first Row
    row = sheet.createRow(30);
    cell = row.createCell(1);
    cell.setCellValue("Name/Vorname:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(2);
    cell.setCellStyle(csUnderscoreBorder);
    
    cell = row.createCell(3);
    cell.setCellStyle(csUnderscoreBorder);
    
    cell = row.createCell(4);
    cell.setCellValue("Unterschrift:");
    cell.setCellStyle(csStandard);
    
    cell = row.createCell(5);
    cell.setCellStyle(csUnderscoreBorder);
    
    cell = row.createCell(6);
    cell.setCellStyle(csUnderscoreBorder);
    
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

  private String getCellValueFromTextField(String compNameTextField) {
    KassenLayout layout = (KassenLayout)container.getLayout();
    JTextField field = (JTextField)layout.getComp(compNameTextField);
    return field.getText();
  }
  
  private String getCellValueFromLabel(String compNameLabel) {
    KassenLayout layout = (KassenLayout)container.getLayout();
    JLabel field = (JLabel)layout.getComp(compNameLabel);
    return field.getText();
  }
}
