package kasse;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    JTextField field = (JTextField)layout.getComp(KassenLayout.oneCentTF);
    String txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    JLabel label = (JLabel)layout.getComp(KassenLayout.oneCentSumLabel);
    int[] arr = getSumCent(txt, label, 1);
    int coinageCentMoney = 0;
    int coinageEuroMoney = 0;
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.twoCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.twoCentSumLabel);
    arr = getSumCent(txt, label, 2);
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.fiveCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.fiveCentSumLabel);
    arr = getSumCent(txt, label, 5);
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.tenCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.tenCentSumLabel);
    arr = getSumCent(txt, label, 10);
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.twentyCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.twentyCentSumLabel);
    arr = getSumCent(txt, label, 20);
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.fiftyCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.fiftyCentSumLabel);
    arr = getSumCent(txt, label, 50);
    coinageEuroMoney += arr[0];
    coinageCentMoney += arr[1];
    if (coinageCentMoney >= 100) {
      coinageEuroMoney++;
      coinageCentMoney -= 100;
    }
    
    field = (JTextField)layout.getComp(KassenLayout.oneEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.oneEuroSumLabel);
    coinageEuroMoney += getSumEuro(txt, label, 1);
    
    field = (JTextField)layout.getComp(KassenLayout.twoEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.twoEuroSumLabel);
    coinageEuroMoney += getSumEuro(txt, label, 2);
    
    field = (JTextField)layout.getComp(KassenLayout.fiveEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.fiveEuroSumLabel);
    int billMoney = 0;
    billMoney += getSumEuro(txt, label, 5);
    
    field = (JTextField)layout.getComp(KassenLayout.tenEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.tenEuroSumLabel);
    billMoney += getSumEuro(txt, label, 10);
    
    field = (JTextField)layout.getComp(KassenLayout.twentyEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.twentyEuroSumLabel);
    billMoney += getSumEuro(txt, label, 20);
    
    field = (JTextField)layout.getComp(KassenLayout.fiftyEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.fiftyEuroSumLabel);
    billMoney += getSumEuro(txt, label, 50);
    
    field = (JTextField)layout.getComp(KassenLayout.hundredEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.hundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 100);
    
    field = (JTextField)layout.getComp(KassenLayout.twohundredEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.twohundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 200);
    
    field = (JTextField)layout.getComp(KassenLayout.fivehundredEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    label = (JLabel)layout.getComp(KassenLayout.fivehundredEuroSumLabel);
    billMoney += getSumEuro(txt, label, 500);
    
    label = (JLabel)layout.getComp(KassenLayout.coinageSumLabel);
    if (coinageCentMoney >= 10) {
      label.setText(coinageEuroMoney + "," + coinageCentMoney + "€");
    } else {
      label.setText(coinageEuroMoney + ",0" + coinageCentMoney + "€");
    }
    
    label = (JLabel)layout.getComp(KassenLayout.billsSumLabel);
    label.setText(billMoney + ",00€");
    
    int totalEuros = billMoney + coinageEuroMoney;
    
    label = (JLabel)layout.getComp(KassenLayout.totalSumLabel);
    if (coinageCentMoney >= 10) {
      label.setText(totalEuros + "," + coinageCentMoney + "€");
    } else {
      label.setText(totalEuros + ",0" + coinageCentMoney + "€");
    }
    
    field = (JTextField)layout.getComp(KassenLayout.purseTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    int coinNecessity = Integer.parseInt(txt) * 25;
    label = (JLabel)layout.getComp(KassenLayout.coinageNecessitySumLabel);
    label.setText(coinNecessity + ",00€");
    
    int coinageDiffEuro = coinageEuroMoney - coinNecessity;
    label = (JLabel)layout.getComp(KassenLayout.coinageDifferenceSumLabel);
    
    if (coinageDiffEuro < 0) {
      int centDiff;
      if (coinageCentMoney != 0) {
        centDiff = 100 - coinageCentMoney;
        coinageDiffEuro++;
      } else {
        centDiff = coinageCentMoney;
      }
      if (centDiff >= 10) {
        if (coinageDiffEuro != 0) {
          label.setText("<html><font color='red'>" + coinageDiffEuro + "," + centDiff + "€" 
              + "</font></html>");
        } else {
          label.setText("<html><font color='red'>-" + coinageDiffEuro + "," + centDiff + "€" 
              + "</font></html>");
        }
      } else {
        if (coinageDiffEuro != 0) {
          label.setText("<html><font color='red'>" + coinageDiffEuro + ",0" + centDiff + "€" 
              + "</font></html>");
        } else {
          label.setText("<html><font color='red'>-" + coinageDiffEuro + ",0" + centDiff + "€" 
              + "</font></html>");
        }
      }
    } else {
      if (coinageCentMoney >= 10) {
        label.setText(coinageDiffEuro + "," + coinageCentMoney + "€");
      } else {
        label.setText(coinageDiffEuro + ",0" + coinageCentMoney + "€");
      }    
    }
    
    label = (JLabel)layout.getComp(KassenLayout.revenuesWithTipSumLabel);
    if (coinageCentMoney >= 10) {
      label.setText(totalEuros + "," + coinageCentMoney + "€");
    } else {
      label.setText(totalEuros + ",0" + coinageCentMoney + "€");
    }
    
    
    label = (JLabel)layout.getComp(KassenLayout.tipSumLabel);
    field = (JTextField)layout.getComp(KassenLayout.cashNecessityEuroTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    final int resEuros = Integer.parseInt(txt);
    
    field = (JTextField)layout.getComp(KassenLayout.cashNecessityCentTF);
    txt = field.getText();
    txt = removeNonDigits(txt);
    field.setText(txt);
    final int resCents = Integer.parseInt(txt);
    
    int x = totalEuros - resEuros;
    int y = coinageCentMoney - resCents;
    
    if (totalEuros > resEuros) {
      if (coinageCentMoney >= resCents) {
        if (y >= 10) {
          label.setText(x + "," + y + "€");
        } else {
          label.setText(x + ",0" + y + "€");
        }
      } else {
        if (y + 100 >= 10) {
          label.setText((x - 1) + "," + (y + 100) + "€");
        } else {
          label.setText((x - 1) + ",0" + (y + 100) + "€");
        }
      }
    } else if (totalEuros == resEuros) {
      if (coinageCentMoney >= resCents) {
        if (y >= 10) {
          label.setText(x + "," + y + "€");
        } else {
          label.setText(x + ",0" + y + "€");
        }
      } else {
        if ((y * (-1)) >= 10) {
          label.setText("<html><font color='red'>-" + x + "," + y * (-1) + "€" + "</font></html>");
        } else {
          label.setText("<html><font color='red'>-" + x + ",0" + y * (-1) + "€" + "</font></html>");
        }
      }
    } else {
      if (coinageCentMoney > resCents) {
        int tmpX = x + 1;
        if (tmpX == 0) {
          if (((y - 100) * (-1)) >= 10) {
            label.setText("<html><font color='red'>-" + tmpX + "," + ((y - 100) * (-1)) 
                + "€" + "</font></html>");
          } else {
            label.setText("<html><font color='red'>-" + tmpX + ",0" + ((y - 100) * (-1)) 
                + "€" + "</font></html>");
          }
        } else {
          if (((y - 100) * (-1)) >= 10) {
            label.setText("<html><font color='red'>" + tmpX + "," + ((y - 100) * (-1)) 
                + "€" + "</font></html>");
          } else {
            label.setText("<html><font color='red'>" + tmpX + ",0" + ((y - 100) * (-1)) 
                + "€" + "</font></html>");
          }
        }
      } else if (coinageCentMoney == resCents) {
        label.setText("<html><font color='red'>" + x + ",0" + y + "€" + "</font></html>");
      } else {
        if ((y * (-1)) >= 10) {
          label.setText("<html><font color='red'>" + x + "," + (y * (-1)) + "€" + "</font></html>");
        } else {
          label.setText("<html><font color='red'>" + x + ",0" + (y * (-1)) + "€</font></html>");
        }
      }
    }
    
    label = (JLabel)layout.getComp(KassenLayout.totalCashNecessitySumLabel);
    if (resCents >= 10) {
      label.setText(resEuros + "," + resCents + "€");
    } else {
      label.setText(resEuros + ",0" + resCents + "€");
    }
  }
  
  /**
   * Resets all Label-Texts to their default value, in case their were altered by the 
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
    
    label = (JLabel)layout.getComp(KassenLayout.revenuesWithTipSumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.totalCashNecessitySumLabel);
    label.setText("0,00€");
    
    label = (JLabel)layout.getComp(KassenLayout.tipSumLabel);
    label.setText("0,00€");
  }
  
  /**
   * Calculates, how much Money in Euro and cents the given String with the given factor is. The 
   * result will be printed in the given JLabel and returned as an Array of Integer.
   * @param txt The String, that represents the amount of times, a coin of this type was counted 
   *      in the machine.
   * @param label The JLabel, that will display the result.
   * @param factor  The factor, the given Text will be multiplied with. In practical terms: The 
   *      value of the coin in cents.
   * @return  The amount of Money, all coins or bills of this type has made in an Array with the 
   *      fixed length of 2, where the amount of Euro is at 0 and the amount of cents is at 1.
   * @since 1.0
   */
  private int[] getSumCent(String txt, JLabel label, int factor) {
    int euro = 0;
    int cent = factor * Integer.parseInt(txt);
    while (cent >= 100) {
      euro++;
      cent -= 100;
    }
    if (cent >= 10) {
      label.setText("= " + euro + "," + cent + "€");
    } else {
      label.setText("= " + euro + ",0" + cent + "€");
    }
    return new int[]{euro, cent};
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
