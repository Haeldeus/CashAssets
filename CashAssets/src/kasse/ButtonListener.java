package kasse;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * The ActionListener, that performs the Action, whenever a Button in the Frame is pressed.
 * @author Haeldeus
 * @version 1.0
 */
public class ButtonListener implements ActionListener {

  /**
   * The Layout, that is used in the Frame, the Button with this ActionListener was added to.
   */
  private final KassenLayout layout;
  
  /**
   * The Button this ActionListener was added to.
   */
  private final JButton button;
  
  /**
   * The Container, which holds the Frame with this Button in it. Needed to revalidate it.
   */
  private final Container container;
  
  /**
   * Instantiates a new Listener for the Button.
   * @param c The ContentPane of the Frame, this Button was added to.
   * @param button The Button, this listener was added to.
   * @since 1.0
   */
  public ButtonListener(Container c, JButton button) {
    layout = (KassenLayout) c.getLayout();
    this.button = button;
    container = c;
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (button.getText().equals("Berechne")) {
      setFieldsEditable(false);
      calculate();
    } else if (button.getText().equals("Reset")) {
      setFieldsEditable(true);
      reset();
    }
    container.revalidate();
  }

  /**
   * Sets all Editable Fields of the Layout editable or non editable, depending on the given 
   * boolean.
   * @param b The boolean, that sets the Fields editable if it is {@code true}, 
   *      non editable if it is {@code false}.
   *      @since 1.0
   */
  private void setFieldsEditable(boolean b) {
    JTextField field = (JTextField)layout.getComp(KassenLayout.oneCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.twoCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.fiveCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.tenCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.twentyCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.fiftyCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.oneEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.twoEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.fiveEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.tenEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.twentyEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.fiftyEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.hundredEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.twohundredEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.fivehundredEuroTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.purseTF);
    field.setOpaque(b);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.cashNecessityCentTF);
    field.setEditable(b);
    field = (JTextField)layout.getComp(KassenLayout.cashNecessityEuroTF);
    field.setEditable(b);
    
    @SuppressWarnings("unchecked") //can be done, since it will always be an String-ComboBox
    JComboBox<String> intBox = (JComboBox<String>)layout.getComp(KassenLayout.dateDayDropdown);
    intBox.setEnabled(b);
    
    @SuppressWarnings("unchecked") //see above
    JComboBox<String> stringBox = (JComboBox<String>)layout.getComp(KassenLayout.dateMonthDropdown);
    stringBox.setEnabled(b);
    field = (JTextField)layout.getComp(KassenLayout.dateYearTextField);
    field.setEditable(b);
    
    JButton button = (JButton)layout.getComp(KassenLayout.exportButton);
    button.setEnabled(!b);
  }

  /**
   * Calculates the total amount of money in the counted purses. Updates all Labels in the Frame as 
   * well.
   * @since 1.0
   */
  private void calculate() {
    JLabel label = (JLabel)layout.getComp(KassenLayout.oneCentSumLabel);
    BigDecimal coinageMoney = new BigDecimal("0.00");
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.oneCentTF), label, new BigDecimal("0.01")));
    
    label = (JLabel)layout.getComp(KassenLayout.twoCentSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.twoCentTF), label, new BigDecimal("0.02")));
    
    label = (JLabel)layout.getComp(KassenLayout.fiveCentSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.fiveCentTF), label, new BigDecimal("0.05")));

    label = (JLabel)layout.getComp(KassenLayout.tenCentSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.tenCentTF), label, new BigDecimal("0.1")));
    
    label = (JLabel)layout.getComp(KassenLayout.twentyCentSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.twentyCentTF), label, new BigDecimal("0.2")));

    label = (JLabel)layout.getComp(KassenLayout.fiftyCentSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.fiftyCentTF), label, new BigDecimal("0.5")));
    
    label = (JLabel)layout.getComp(KassenLayout.oneEuroSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.oneEuroTF), label, new BigDecimal("1.0")));

    label = (JLabel)layout.getComp(KassenLayout.twoEuroSumLabel);
    coinageMoney = coinageMoney.add(calculateSetComponents(
        (JTextField)layout.getComp(KassenLayout.twoEuroTF), label, new BigDecimal("2.0")));
    
    String txt = setField((JTextField)layout.getComp(KassenLayout.fiveEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.fiveEuroSumLabel);
    //TODO: Change billMoney to BigDecimal to ensure correct calculation.
    int billMoney = 0;
    billMoney += getSumEuro(txt, label, 5);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.tenEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.tenEuroSumLabel);
    billMoney += getSumEuro(txt, label, 10);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.twentyEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.twentyEuroSumLabel);
    billMoney += getSumEuro(txt, label, 20);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.fiftyEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.fiftyEuroSumLabel);
    billMoney += getSumEuro(txt, label, 50);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.hundredEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.hundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 100);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.twohundredEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.twohundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 200);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.fivehundredEuroTF));
    label = (JLabel)layout.getComp(KassenLayout.fivehundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 500);
    
    label = (JLabel)layout.getComp(KassenLayout.coinageSumLabel);
    label.setText(("" + coinageMoney + "€").replaceAll("\\.", ","));
    
    
    label = (JLabel)layout.getComp(KassenLayout.billsSumLabel);
    label.setText(billMoney + ",00€");
    
    
    BigDecimal totalEuros = coinageMoney.add(new BigDecimal(billMoney));
    
    label = (JLabel)layout.getComp(KassenLayout.totalSumLabel);

    label.setText((totalEuros + "€").replaceAll("\\.", ","));
    
    txt = setField((JTextField)layout.getComp(KassenLayout.purseTF));
    int coinNecessity = Integer.parseInt(txt) * 25;
    label = (JLabel)layout.getComp(KassenLayout.coinageNecessitySumLabel);
    label.setText(coinNecessity + ",00€");
    
    BigDecimal coinageDiff = coinageMoney.subtract(new BigDecimal(coinNecessity));
    label = (JLabel)layout.getComp(KassenLayout.coinageDifferenceSumLabel);
    
    if (coinageDiff.compareTo(new BigDecimal("0.0")) == -1) {
      label.setText("<html><font color='red'>" + coinageDiff.toString()
          .replaceAll("\\.", ",") + "€" + "</font></html>");
    } else {
      label.setText(coinageDiff.toString().replaceAll("\\.", ",") + "€");
    }
    
    label = (JLabel)layout.getComp(KassenLayout.afterCoinageFixSumLabel);
    
    BigDecimal revenueWithTips = totalEuros.subtract(coinageDiff);
    label.setText(revenueWithTips.toString().replaceAll("\\.", ",") + "€");
    
    label = (JLabel)layout.getComp(KassenLayout.totalCashNecessitySumLabel);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.cashNecessityEuroTF));
    
    final int cashNecessityEuro = Integer.parseInt(txt);
    
    txt = setField((JTextField)layout.getComp(KassenLayout.cashNecessityCentTF));
    BigDecimal cashNecessity;
    int i = Integer.parseInt(txt);
    if (Integer.parseInt(txt) >= 100) {
      while (i >= 100) {
        i /= 10;
      }
      cashNecessity = new BigDecimal(cashNecessityEuro + "." + i);
    } else if (i < 10) {
      cashNecessity = new BigDecimal(cashNecessityEuro + ".0" + i);
    } else {
      cashNecessity = new BigDecimal(cashNecessityEuro + "." + Integer.parseInt(txt));
    }
    label.setText(cashNecessity.toString().replaceAll("\\.", ",") + "€");
    
    label = (JLabel)layout.getComp(KassenLayout.tipSumLabel);
    BigDecimal tips = revenueWithTips.subtract(cashNecessity);
    if (tips.compareTo(new BigDecimal("0.0")) < 0) {
      label.setText("<html><font color='red'>" + tips.toString().replaceAll("\\.", ",") + "€" 
          + "</font></html>");
    } else {
      label.setText(tips.toString().replaceAll("\\.", ",") + "€");
    }
  }
  
  /**
   * Resets all Label-Texts to their default value, in case they were altered by the 
   * Calculate-Button.
   * @since 1.0
   */
  public void reset() {
    JLabel label = (JLabel)layout.getComp(KassenLayout.coinageSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.billsSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.totalSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.coinageNecessitySumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.coinageDifferenceSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.afterCoinageFixSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.totalCashNecessitySumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.tipSumLabel);
    label.setText("0,00€");
  }
  
  /**
   * Calculates, how much money the given Coin-TextField multiplied by the given factor is. The 
   * result will be printed in the given JLabel and returned as a BigDecimal. Also, the given 
   * JTextField will be, if necessary,  altered to display a correct Integer Value (e.g.: "1a" 
   * will be altered to "1").
   * @param field   The JTextField, which contains the amount of coins or bills counted. Will be 
   *      altered if necessary.
   * @param label The JLabel, that will display the result.
   * @param factor  The Factor, the given amount will be multiplied by.
   * @return The amount of money all coins of this type result in. Also, the Label will be set to 
   *      display this result.
   * @since 1.0
   */
  private BigDecimal calculateSetComponents(JTextField field, JLabel label, BigDecimal factor) {
    BigDecimal res = new BigDecimal("0.0");
    int parsed = Integer.parseInt(setField(field));
    res = res.add(factor.multiply(new BigDecimal(parsed)));
    
    String strRes = "= " + res;  
    if (factor.compareTo(new BigDecimal("0.1")) == 0 || factor.compareTo(new BigDecimal("0.2")) == 0
        || factor.compareTo(new BigDecimal("0.5")) == 0 
        || factor.compareTo(new BigDecimal("1.0")) == 0 
        || factor.compareTo(new BigDecimal("2.0")) == 0 
        || ((parsed != 0) && (parsed % 10 == 0) && (factor.compareTo(new BigDecimal("0.01")) == 0)) 
        || ((parsed != 0) && (parsed % 5 == 0) && (factor.compareTo(new BigDecimal("0.02")) == 0)) 
        || ((parsed != 0) && (parsed % 2 == 0) && (factor.compareTo(new BigDecimal("0.05")) == 0))
        ) {
      label.setText(strRes.concat("0€").replaceAll("\\.", ","));
    } else {
      label.setText(strRes.concat("€").replaceAll("\\.", ","));
    }
    return res;
  }
  
  /**
   * Sets the content of the given Field to a digit only String, so it can be processed by 
   *    {@link Integer#parseInt(String)}.
   * @param field The JTextField to be altered.
   * @return  The altered content of the Field as a String.
   * @since 1.0
   */
  private String setField(JTextField field) {
    String txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    return txt;
  }
  
  /**
   * Calculates, how much money in Euro the given String with the given factor is. The result will 
   * be printed in the given JLabel and returned as an Integer.
   * @param txt The String, that represents the amount of times, a coin or bill of this type was 
   *      counted in the machine.
   * @param label The JLabel, that will display the result.
   * @param factor The factor, the given Text will be multiplied with. In practical terms: The 
   *      value of the coin or bill in Euro.
   * @return  The amount of Money, all coins or bills of this type has made.
   * @since 1.0
   */
  private int getSumEuro(String txt, JLabel label, int factor) {
    int euro = factor * Integer.parseInt(txt);
    label.setText("= " + euro + ",00€");
    return euro;
  }
  
  /**
   * Removes all Non-Digits from the given String
   * @param str The String, that will be edited
   * @return  The given String without all Characters that are Non-Digits.
   * @since 1.0
   */
  private String removeNonDigits(String str) {
    if (str == null || str.length() == 0) {
      return "0";
    }
    return str.replaceAll("\\D+", "");
  }
}
