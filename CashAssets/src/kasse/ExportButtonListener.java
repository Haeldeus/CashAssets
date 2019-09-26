package kasse;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
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
  
  public ExportButtonListener(Container c) {
    this.container = c;
  }
  

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Create a new Excel File from Scratch.
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Current Date"); //TODO add current Date to Sheet-Title
    XSSFRow row = sheet.createRow(0);
    XSSFCell cell = row.createCell(0);
    workbook.setPrintArea(0, 0, 0, 0, 0);
    cell.setCellValue("Gezählt von:");
    try {
      workbook.write(new FileOutputStream("Try.xlsx"));
      workbook.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
