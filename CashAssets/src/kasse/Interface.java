package kasse;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * A Class to create the User Interface and all Fields, that need to be set. This one also does 
 * all the Calculations.
 * @author Haeldeus
 * @version 1.0
 */
public class Interface {
  
  /**
   * Creates a new Interface. 
   */
  public Interface() {
    runGui();
  }
  
  /**
   * Creates the GUI for the User, where he can input all variables for the Program to count 
   * the amount of Money made on that day.
   */
  @SuppressWarnings("static-access")
  private void runGui() {
    JFrame frame = new JFrame();
    frame.setSize(KassenLayout.getDimension());
    frame.setTitle("Kassenbestand - v1.0");
    frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new KassenLayout());
    addLabels(frame.getContentPane());
    addTextFields(frame.getContentPane());
    addComboBoxes(frame.getContentPane());
    addButtons(frame.getContentPane());
    frame.setResizable(false);
    frame.revalidate();
    frame.setVisible(true);
  }

  /**
   * Adds all Labels to the GUI.
   * @param c The ContentPane of the Frame.
   * @since 1.0
   */
  private void addLabels(Container c) {
    c.add(KassenLayout.dateLabel, new JLabel("Datum:"));
    c.add(KassenLayout.oneCentLabel, new JLabel("1ct"));
    c.add(KassenLayout.twoCentLabel, new JLabel("2ct"));
    c.add(KassenLayout.fiveCentLabel, new JLabel("5ct"));
    c.add(KassenLayout.tenCentLabel, new JLabel("10ct"));
    c.add(KassenLayout.twentyCentLabel, new JLabel("20ct"));
    c.add(KassenLayout.fiftyCentLabel, new JLabel("50ct"));
    c.add(KassenLayout.oneEuroLabel, new JLabel("1€"));
    c.add(KassenLayout.twoEuroLabel, new JLabel("2€"));
    
    c.add(KassenLayout.fiveEuroLabel, new JLabel("5€"));
    c.add(KassenLayout.tenEuroLabel, new JLabel("10€"));
    c.add(KassenLayout.twentyEuroLabel, new JLabel("20€"));
    c.add(KassenLayout.fiftyEuroLabel, new JLabel("50€"));
    c.add(KassenLayout.hundredEuroLabel, new JLabel("100€"));
    c.add(KassenLayout.twohundredEuroLabel, new JLabel("200€"));
    c.add(KassenLayout.fivehundredEuroLabel, new JLabel("500€"));
    
    c.add(KassenLayout.oneCentSumLabel, new JLabel("="));
    c.add(KassenLayout.twoCentSumLabel, new JLabel("="));
    c.add(KassenLayout.fiveCentSumLabel, new JLabel("="));
    c.add(KassenLayout.tenCentSumLabel, new JLabel("="));
    c.add(KassenLayout.twentyCentSumLabel, new JLabel("="));
    c.add(KassenLayout.fiftyCentSumLabel, new JLabel("="));
    c.add(KassenLayout.oneEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.twoEuroSumLabel, new JLabel("="));
    
    c.add(KassenLayout.fiveEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.tenEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.twentyEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.fiftyEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.hundredEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.twohundredEuroSumLabel, new JLabel("="));
    c.add(KassenLayout.fivehundredEuroSumLabel, new JLabel("="));
    
    c.add(KassenLayout.purseLabel, new JLabel("Gezählt:"));
    
    c.add(KassenLayout.separatorOne, new JLabel("--------------------------------------------------"
        + "-------------------------------------------------------------------------------------"));
    
    c.add(KassenLayout.cashNecessityLabel, new JLabel("Kassenschnitt Bar: "));
    c.add(KassenLayout.cashNecessityEuroLabel, new JLabel("€"));
    c.add(KassenLayout.cashNecessityCentLabel, new JLabel("ct"));
    
    c.add(KassenLayout.separatorTwo, new JLabel("--------------------------------------------------"
        + "-------------------------------------------------------------------------------------"));
    
    c.add(KassenLayout.coinageLabel, new JLabel("Kleingeld: "));
    c.add(KassenLayout.coinageSumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.billsLabel, new JLabel("Scheine: "));
    c.add(KassenLayout.billsSumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.totalLabel, new JLabel("Gesamt: "));
    c.add(KassenLayout.totalSumLabel, new JLabel("0,00€"));
    
    c.add(KassenLayout.separatorThree, 
        new JLabel("------------------------------------------------------------------------------"
            + "---------------------------------------------------------"));
    
    c.add(KassenLayout.coinageNecessityLabel, new JLabel("Muss Kleingeld: "));
    c.add(KassenLayout.coinageNecessitySumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.coinageDifferenceLabel, new JLabel("Differenz Kleingeld: "));
    c.add(KassenLayout.coinageDifferenceSumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.revenuesWithTipLabel, new JLabel("Einnahmen incl Tip:"));
    c.add(KassenLayout.revenuesWithTipSumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.totalCashNecessityLabel, new JLabel("Kassenschnitt:"));
    c.add(KassenLayout.totalCashNecessitySumLabel, new JLabel("0,00€"));
    c.add(KassenLayout.tipLabel, new JLabel("Rest Tip:"));
    c.add(KassenLayout.tipSumLabel, new JLabel("0,00€"));
  }
  
  /**
   * Adds all TextFields to the GUI.
   * @param c The ContentPane of the Frame.
   * @since 1.0
   */
  private void addTextFields(Container c) {
    c.add(KassenLayout.dateYearTextField, new JTextField("20"));
    c.add(KassenLayout.oneCentTF, new JTextField("0"));
    c.add(KassenLayout.twoCentTF, new JTextField("0"));
    c.add(KassenLayout.fiveCentTF, new JTextField("0"));
    c.add(KassenLayout.tenCentTF, new JTextField("0"));
    c.add(KassenLayout.twentyCentTF, new JTextField("0"));
    c.add(KassenLayout.fiftyCentTF, new JTextField("0"));
    c.add(KassenLayout.oneEuroTF, new JTextField("0"));
    c.add(KassenLayout.twoEuroTF, new JTextField("0"));
    
    c.add(KassenLayout.fiveEuroTF, new JTextField("0"));
    c.add(KassenLayout.tenEuroTF, new JTextField("0"));
    c.add(KassenLayout.twentyEuroTF, new JTextField("0"));
    c.add(KassenLayout.fiftyEuroTF, new JTextField("0"));
    c.add(KassenLayout.hundredEuroTF, new JTextField("0"));
    c.add(KassenLayout.twohundredEuroTF, new JTextField("0"));
    c.add(KassenLayout.fivehundredEuroTF, new JTextField("0"));
    
    c.add(KassenLayout.purseTF, new JTextField("0"));
    
    c.add(KassenLayout.cashNecessityEuroTF, new JTextField("0"));
    c.add(KassenLayout.cashNecessityCentTF, new JTextField("0"));
  }
  
  /**
   * Adds all ComboBoxes to the Frame.
   * @param c The ContentPane of the Frame.
   * @since 1.0
   */
  private void addComboBoxes(Container c) {    
    JComboBox<String> intBox = new JComboBox<String>();
    for (int i = 1; i <= 31; i++) {
      intBox.addItem(i + ".");
    }
    c.add(KassenLayout.dateDayDropdown, intBox);
    
    intBox = new JComboBox<String>();
    for (int i = 1; i <= 12; i++) {
      intBox.addItem(i + ".");
    }
    c.add(KassenLayout.dateMonthDropdown, intBox);
  }
  
  /**
   * Adds all Buttons to the GUI.
   * @param c The ContentPane of the Frame.
   * @since 1.0
   */
  private void addButtons(Container c) {
    JButton btCalc = new JButton("Berechne");
    btCalc.addActionListener(new ButtonListener(c, btCalc));
    c.add(KassenLayout.calcButton, btCalc);
    
    JButton btReset = new JButton("Reset");
    btReset.addActionListener(new ButtonListener(c, btReset));
    c.add(KassenLayout.resetButton, btReset);
    
    JButton btExport = new JButton("Export");
    btExport.setEnabled(false);
    btExport.addActionListener(new ExportButtonListener(c));
    c.add(KassenLayout.exportButton, btExport);
  }
  
  /**
   * Used to run the GUI for the User.
   * @param args  Unused.
   * @since 1.0
   */
  public static void main(String[] args) {
    new Interface();
  }

}
