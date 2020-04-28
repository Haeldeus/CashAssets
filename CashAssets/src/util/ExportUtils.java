package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A Utility Class to provide Methods used in multiple packages and Objects, whenever a File will 
 * be exported. This way, the Methods don't have to be implemented multiple times nor a reference 
 * has to be passed to every Object.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportUtils {

  /**
   * The static Field, that indexes the CellStyle for Standard black text with no Borders in the 
   * HashMap returned by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int STANDARD_BLACK = 0;
  
  /**
   * The static Field, that indexes the CellStyle for Standard red text with no Borders in the 
   * HashMap returned by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int STANDARD_RED = 1;
  
  /**
   * The static Field, that indexes the CellStyle for red text with Borders in the HashMap returned 
   * by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int RED_WITH_BORDERS = 2;
  
  /**
   * The static Field, that indexes the CellStyle for black text with Borders in the HashMap 
   * returned by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int BLACK_WITH_BORDERS = 3;
  
  /**
   * The static Field, that indexes the CellStyle for black text with a Border at the Bottom in the 
   * HashMap returned by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int BLACK_WITH_BOTTOM_BORDER = 4;
  
  /**
   * The static Field, that indexes the CellStyle for red text with Borders and wrapping text in 
   * the HashMap returned by {@link #getStyles(XSSFWorkbook)}.
   */
  public static final int RED_WITH_BORDERS_WRAPPING = 5;
  
  /**
   * The static Field, that stores the Information displayed in the Header of each Exported File.
   */
  public static final String EXPORT_HEADER = "Gaststätte Weyher GmbH Haxtergrund 8 33100 Paderborn";
  
  /**
   * Creates all CellStyles necessary for the Excel Files.
   * @param workbook  The Workbook, the Styles should be created from.
   * @return  A HashMap with Integers as Key and all CellStyles as Elements. To get each Style, the 
   *     use of the static Fields of this Class is recommended.
   * @since 1.0
   */
  public static HashMap<Integer, XSSFCellStyle> getStyles(XSSFWorkbook workbook) {    
    //Creates the Fonts, that will be used in the CellStyles.
    /*
     * Creates a Standard Black font.
     */
    XSSFFont fontBlack = workbook.createFont();
    fontBlack.setFontName("Arial");
    fontBlack.setFontHeightInPoints((short)10);
    fontBlack.setColor(IndexedColors.BLACK.getIndex());
    
    /*
     * Creates a Red font.
     */
    XSSFFont fontRed = workbook.createFont();
    fontRed.setFontName("Arial");
    fontRed.setFontHeightInPoints((short)10);
    fontRed.setColor(IndexedColors.RED.getIndex());
    
    /*
     * Creates the CellStyle, that will display the Cell as standard black text with no Borders and 
     * central horizontal alignment.
     */
    final XSSFCellStyle csStandard = workbook.createCellStyle();
    csStandard.setAlignment(HorizontalAlignment.CENTER);
    csStandard.setVerticalAlignment(VerticalAlignment.CENTER);
    csStandard.setFont(fontBlack);
    
    /*
     * Creates a HashMap, that will be returned with all Styles as Elements and the static fields 
     * of this File as Keys.
     */
    HashMap<Integer, XSSFCellStyle> res = new HashMap<Integer, XSSFCellStyle>();
    res.put(STANDARD_BLACK, csStandard);
    
    /*
     * Creates the CellStyle, that will display the Cell as red Text with no Borders and central 
     * horizontal alignment.
     */
    final XSSFCellStyle csRed = workbook.createCellStyle();
    csRed.setAlignment(HorizontalAlignment.CENTER);
    csRed.setVerticalAlignment(VerticalAlignment.CENTER);
    csRed.setFont(fontRed);
    res.put(STANDARD_RED, csRed);
    
    /*
     * Creates the CellStyle, that will display the Cell as red Text with Borders on all sides and 
     * central horizontal alignment.
     */
    final XSSFCellStyle csRedBorder = workbook.createCellStyle();
    csRedBorder.setAlignment(HorizontalAlignment.CENTER);
    csRedBorder.setVerticalAlignment(VerticalAlignment.CENTER);
    csRedBorder.setFont(fontRed);
    csRedBorder.setBorderBottom(BorderStyle.HAIR);
    csRedBorder.setBorderLeft(BorderStyle.HAIR);
    csRedBorder.setBorderRight(BorderStyle.HAIR);
    csRedBorder.setBorderTop(BorderStyle.HAIR);
    res.put(RED_WITH_BORDERS, csRedBorder);
    
    /*
     * Creates the CellStyle, that will display the Cell as standard black Text with Borders on all 
     * sides and central horizontal alignment.
     */
    final XSSFCellStyle csStandardBorder = workbook.createCellStyle();
    csStandardBorder.setAlignment(HorizontalAlignment.CENTER);
    csStandardBorder.setVerticalAlignment(VerticalAlignment.CENTER);
    csStandardBorder.setBorderBottom(BorderStyle.HAIR);
    csStandardBorder.setBorderLeft(BorderStyle.HAIR);
    csStandardBorder.setBorderRight(BorderStyle.HAIR);
    csStandardBorder.setBorderTop(BorderStyle.HAIR);
    csStandardBorder.setFont(fontBlack);
    res.put(BLACK_WITH_BORDERS, csStandardBorder);
    
    /*
     * Creates the CellStyle, that will display the Cell as standard black Text with a Border at 
     * the Bottom and central horizontal alignment.
     */
    final XSSFCellStyle csUnderscoreBorder = workbook.createCellStyle();
    csUnderscoreBorder.setAlignment(HorizontalAlignment.CENTER);
    csUnderscoreBorder.setVerticalAlignment(VerticalAlignment.CENTER);
    csUnderscoreBorder.setBorderBottom(BorderStyle.HAIR);
    csUnderscoreBorder.setFont(fontBlack);
    res.put(BLACK_WITH_BOTTOM_BORDER, csUnderscoreBorder);
    
    /*
     * Creates the CellStyle, that will display the Cell as red Text with Borders on all sides and 
     * central horizontal alignment.
     */
    final XSSFCellStyle csRedBorderWrapping = workbook.createCellStyle();
    csRedBorderWrapping.setAlignment(HorizontalAlignment.CENTER);
    csRedBorderWrapping.setVerticalAlignment(VerticalAlignment.CENTER);
    csRedBorderWrapping.setFont(fontRed);
    csRedBorderWrapping.setBorderBottom(BorderStyle.HAIR);
    csRedBorderWrapping.setBorderLeft(BorderStyle.HAIR);
    csRedBorderWrapping.setBorderRight(BorderStyle.HAIR);
    csRedBorderWrapping.setBorderTop(BorderStyle.HAIR);
    csRedBorderWrapping.setWrapText(true);
    res.put(RED_WITH_BORDERS_WRAPPING, csRedBorderWrapping);
    
    return res;
  }
  
  
  /**
   * Creates the Area, where the User can enter his Signature and enter some important Notes to 
   * this File.
   * @param sheet The Sheet, this Area will be created in.
   * @param row The Row, that will be used as placeholder for every Row created. The Rows will be 
   *     saved temporary in this XSSFRow to reduce memory usage.
   * @param styles  All CellStyles, that are available as an HashMap of Integer and XSSFCellStyle. 
   *     Ideally, this HashMap contains every Style created by {@link #getStyles(XSSFWorkbook)}, 
   *     since this Method doesn't check, if the Style is available. 
   * @param index The index of the row, where to start the creation of this Area.
   * @since 1.0
   */
  public static void createSignatureArea(XSSFSheet sheet, XSSFRow row, 
      HashMap<Integer, XSSFCellStyle> styles, int index) {
    /*
     * Creates a new rowIndex since the index has to be altered and in case index is used for other 
     * methods in the calculation that called this Method as well, this isn't altered. 
     */
    int rowIndex = index;
    
    /*
     * Creates a new Row with the given index. This Row will have an info-Cell and a place where 
     * the user can enter important notes.
     */
    row = sheet.createRow(rowIndex);
    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 1, 2));
    ExportUtils.createCell(row, 1, "Wichtige Bemerkungen", styles.get(STANDARD_BLACK), 
        null, false);
    for (int i = 3; i <= 6; i++) {
      createCell(row, i, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    }
    rowIndex++;
    
    /*
     * Creates a new Row. This Row will only have a place to enter important notes.
     */
    row = sheet.createRow(rowIndex);    
    for (int i = 2; i <= 6; i++) {
      createCell(row, i, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    }
    rowIndex++;
    
    /*
     * Creates a new Row. This Row as well will only have a place to enter important notes.
     */
    row = sheet.createRow(rowIndex);    
    for (int i = 2; i <= 6; i++) {
      createCell(row, i, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    }
    rowIndex += 3;
    
    /*
     * Creates a new Row. This Row will have an Info-Cell and a place, where the User can enter 
     * his signature.
     */
    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 1, 2)); 
    row = sheet.createRow(rowIndex);   
    createCell(row, 1, "Gezählt von Franz Weyher:", styles.get(STANDARD_BLACK), null, false);
    createCell(row, 3, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    createCell(row, 4, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    rowIndex += 2;
    

    /*
     * Creates a new Row with an Info-Cell.
     */
    row = sheet.createRow(rowIndex);
    createCell(row, 1, "Mitarbeiter:", styles.get(STANDARD_BLACK), null, false);
    rowIndex++;
    
    /*
     * Creates a new Row where the Name of a Staff Member, that should put his signature below 
     * this Sheet, can be entered as well as the signature itself.
     */
    row = sheet.createRow(rowIndex);    
    createCell(row, 1, "Name,Vorname:", styles.get(STANDARD_BLACK), null, false);
    createCell(row, 2, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    createCell(row, 3, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    createCell(row, 4, "Unterschrift:", styles.get(STANDARD_BLACK), null, false);
    createCell(row, 5, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
    createCell(row, 6, null, styles.get(BLACK_WITH_BOTTOM_BORDER), null, false);
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
   * @param alternativeStyling  {@code true}, if the Cell has an alternative Styling, 
   *     {@code false} else.
   * @see XSSFRow
   * @see XSSFCellStyle
   * @since 1.0
   */
  public static void createCell(XSSFRow row, int column, String data, XSSFCellStyle style, 
      XSSFCellStyle style2, boolean alternativeStyling) {
    /*
     * Creates a new Cell at the given column in the given row.
     */
    XSSFCell cell = row.createCell(column);
    /*
     * Checks, if there is data to be displayed in the Cell. If yes, this adds it to the created 
     * Cell.
     */
    if (data != null) {
      cell.setCellValue(data);
    }
    
    /*
     * Checks if there is alternative Styling to be done. If yes, this part checks, if the first 
     * character of the data is a '-'. In this case the alternative Style should be used. If the 
     * first Character isn't a '-' or alternative styling isn't enabled, the first Style is used 
     * instead.
     */
    if (alternativeStyling) {
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
   * Creates a Cell in the given row and column with a Formula as content. The Formula itself will 
   * not be checked, if it is correct.
   * @param row The Row, this Cell will be created in.
   * @param column  The Column, this Cell will be created in.
   * @param formula The Formula, that will be the Content for this Cell.
   * @param style The CellStyle, that will be used for this Cell.
   * @see XSSFRow
   * @see XSSFCellStyle
   */
  public static void createFormulaCell(XSSFRow row, int column, String formula, 
      XSSFCellStyle style) {
    
    /*
     * Creates a new Cell at the given column in the given row.
     */
    XSSFCell cell = row.createCell(column);
    
    /*
     * Sets the Cell Type to Formula, so it doesn't display it's Data as a String, but instead 
     * treads it as a formula.
     */
    cell.setCellType(CellType.FORMULA);
    
    /*
     * Checks, if there is a formula to be calculated in the Cell. If yes, this adds it to the 
     * created Cell.
     */
    if (formula != null) {
      cell.setCellFormula(formula);
    }
    
    /*
     * Sets the Styling for the Cell.
     */
    cell.setCellStyle(style);
  }
  
  /**
   * Creates the Cells, where the Date will be added into.
   * @param row The Row, where the Cells will be added to.
   * @param startCol  The Index of the first Column. The Cells will be created in the Columns 
   *      startCol and startCol + 1
   * @param style The CellStyle, that will be used to create the Cells.
   * @param date  The Date for this Calculation as a String in the pattern DD.MM.YYYY
   * @since 1.0
   */
  public static void createDateCells(XSSFRow row, int startCol, XSSFCellStyle style, String date) {
    createCell(row, startCol, "Datum:", style, null, false);
    createCell(row, startCol + 1, date, style, null, false);
  }
  
  /**
   * Exports the Excel-File, that was created with the given Workbook.
   * @param workbook  The Workbook, that was used to create the Excel File.
   * @param directoryName The Name of the directory, the File will be saved in. This only has to be 
   *     "Tip" etc. and not "Tip/year/month/", since the total path will be set inside this Method.
   * @param date The date, this Calculation was made for. Has to match the pattern DDxMMxYYYY.
   * @param year The year of the date, this Calculation was made for. Is passed to this method for 
   *     simplicity-Issues instead of calculating it again.
   * @param month The month of the date, this Calculation was made for. Is passed to this method 
   *     for simplicity-Issues instead of calculating it again.
   * @param open  The boolean value, that determines if the Directory should be opened after 
   *     exporting the File.
   * @since 1.0
   */
  public static void export(XSSFWorkbook workbook, String directoryName, String date, String year, 
      String month, boolean open) {
    try {
      /*
       * Checks, if the Directory already exists. If not, this creates all necessary Folders.
       */
      File directory = new File(directoryName + File.separator + year + File.separator + month);
      if (!directory.exists()) {
        directory.mkdirs();
      }
      
      /*
       * The Excel-Sheet will be called the same as the Date replacing the Dots in the "official" 
       * representation with an x to match OS-naming patterns.
       */
      String s = directoryName + File.separator + year + File.separator + month + File.separator;
      s = s.concat(date + ".xlsx");
      
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
      
      /*
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
  
}
