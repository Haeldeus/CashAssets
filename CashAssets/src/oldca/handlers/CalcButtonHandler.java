package oldca.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import oldca.ComponentStorer;

/**
 * The Handler for the Calculate-Button. This Handler will calculate the Money in the Purses and 
 * update the Labels in the Window.
 * @author Haeldeus
 * @version 1.0
 */
public class CalcButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The ComponentStorer, that handles all modifiable Components, that will be altered by this 
   * Handler.
   */
  private ComponentStorer cs;
  
  /**
   * The boolean value that determines, if the simple design is used in the Application.
   */
  private final boolean simple;
  
  /**
   * Creates a new Handler with the given ComponentStorer. The ComponentStorer has to be the 
   * Storer, that contains all modifiable Components of this Application.
   * @param cs  The ComponentStorer, where all modifiable Components are stored.
   * @param simple  The boolean value, that determines, if the simple design is used.
   * @since 1.0
   * @throws NullPointerException If a Component wasn't stored in the given Storer or the Storer 
   *     is {@code null}.
   */
  public CalcButtonHandler(ComponentStorer cs, boolean simple) {
    this.cs = cs;
    this.simple = simple;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    if (simple) {
      handleSimple();
    } else {
      handleBig();
    }
  }

  /**
   * Handles the Event if the simple design is used.
   * @since 1.0
   */
  private void handleSimple() {
    /*
     * Make all Fields uneditable, so they cannot be altered after calculation. This is needed to 
     * make sure, that the export will be correct. 
     */
    cs.setFieldsEditable(false);
    cs.getExportButton().setDisable(false);
    cs.getResetButton().setDisable(true);
    
    /*
     *Gets the Text of the coinage Field and replaces all Non-Digits. 
     */
    String str = cs.getCoinTextFields()[7].getText();
    str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
    /*
     * If there are more than 1 chars that equal '.' or ',' then make the TextField red to show 
     * that there was an Error in the input. This will also cancel the operation.
     */
    int count = 0;
    for (char c : str.toCharArray()) {
      if (c == '.' || c == ',') {
        count++;
      }
      if (count > 1) {
        cs.getCoinTextFields()[7].getStyleClass().add("wrongTF");
        return;
      }
    }
    /*
     * If there was no input or no digits in the Input, set the input to '0'.
     */
    if (str == null || str.length() == 0) {
      str = "0";
    }
    cs.getCoinTextFields()[7].setText(str);
    
    /*
     *Gets the Text of the Cash Necessity Field and replaces all Non-Digits. 
     */
    str = cs.getCashNecessityEuroTextField().getText();
    str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
    /*
     * If there are more than 1 chars that equal '.' or ',' then make the TextField red to show 
     * that there was an Error in the input. This will also cancel the operation.
     */
    count = 0;
    for (char c : str.toCharArray()) {
      if (c == '.' || c == ',') {
        count++;
      }
      if (count > 1) {
        cs.getCashNecessityEuroTextField().getStyleClass().add("wrongTF");
        return;
      }
    }
    /*
     * If there was no input or no digits in the Input, set the input to '0'.
     */
    if (str == null || str.length() == 0) {
      str = "0";
    }
    cs.getCashNecessityEuroTextField().setText(str);
    
    /*
     * Creates the BigDecimals that are needed for this Handler.
     */
    BigDecimal coinageMoney = new BigDecimal("0.00");
    coinageMoney = coinageMoney.add(new BigDecimal(cs.getCoinTextFields()[7].getText()
        .replaceAll(",", "\\.")));
    coinageMoney = coinageMoney.setScale(2, RoundingMode.DOWN);
    cs.getCoinTextFields()[7].setText(coinageMoney.toString().replaceAll("\\.", ","));
    BigDecimal billMoney = new BigDecimal("0.00");
    
    /*
     * Gets all Sums of bills.
     */
    for (int i = 8; i <= 14; i++) {
      billMoney = addToDecimal(billMoney, i);
    }
    
    /*
     * Sets the Text of the coinage Result Label.
     */
    cs.getCoinResults()[7].setText("= " + coinageMoney.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * A new BigDecimal for the total amount of Money in the purses.
     */
    BigDecimal totalEuros = coinageMoney.add(billMoney);
    
    /*
     * Sets the Text of the Labels, that display each of the BigDecimals value.
     */
    cs.getCoinSumLabel().setText(("" + coinageMoney + "€").replaceAll("\\.", ","));
    cs.getBillSumLabel().setText(("" + billMoney + "€").replaceAll("\\.", ","));
    cs.getSumLabel().setText(("" + totalEuros + "€").replaceAll("\\.", ","));

    /*
     * Calculates the amount of coinage, that has to be in each purse and was in it beforehand.
     */
    int coinNecessity = Integer.parseInt(setField(cs.getPurseTextField())) * 25;
    /*
     * Sets the Text of the Label, that displays the amount of coinage needed.
     */
    cs.getCoinNecessityLabel().setText(coinNecessity + ",00€");
    
    /*
     * Creates a new BigDecimal to calculate the difference between the coinage in the purses and 
     * the amount of needed coinage.
     */
    BigDecimal coinageDiff = coinageMoney.subtract(new BigDecimal(coinNecessity));
    /*
     * Sets the Text of the Label, that displays the BigDecimal for the coinage difference.
     */
    cs.getCoinDifferenceLabel().setText(coinageDiff.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * Compares the coinage difference BigDecimal to 0 to check if it's negative or positive. If 
     * it's negative, the Label will display it's Text in red, else in normal black.
     */
    if (coinageDiff.compareTo(new BigDecimal("0.0")) == -1) {
      cs.getCoinDifferenceLabel().getStyleClass().clear();
      cs.getCoinDifferenceLabel().getStyleClass().addAll("minusLabel");
    } else {
      cs.getCoinDifferenceLabel().getStyleClass().clear();
      cs.getCoinDifferenceLabel().getStyleClass().addAll("label");
    }
    
    /*
     * There is a given discrepancy of coinage of 25€. When counting the purses, this discrepancy 
     * will be fixed, by taking the amount of Euro from the Bill Money. So we have to subtract the 
     * coin Difference from the billMoney. The result will be displayed in the belonging Label.
     */
    BigDecimal revenueWithTips = billMoney.add(coinageDiff);
    cs.getCoinCleanedLabel().setText(revenueWithTips.toString().replaceAll("\\.", ",") + "€");
    
    
    /*
     * Calculates the total CashNecessity by adding cashNecessityEuro with the Input of the 
     * cent-Field after cleaning it from potential Input-Errors (input >= 100, input contains 
     * non-Digits).
     */
    BigDecimal cashNecessity = new BigDecimal("0.00");
    cashNecessity = cashNecessity.add(new BigDecimal(cs.getCashNecessityEuroTextField().getText()
        .replaceAll(",", "\\.")));
    cashNecessity = cashNecessity.setScale(2, RoundingMode.DOWN);
    cs.getCashNecessityEuroTextField().setText(cashNecessity.toString().replaceAll("\\.", ","));
    cs.getCashNecessityLabel().setText(cashNecessity.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * Calculates the tip in the Purses by subtracting Cash Necessity from the revenue calculated 
     * before.
     */
    BigDecimal tips = revenueWithTips.subtract(cashNecessity);
    cs.getTipSumLabel().setText(tips.toString().replaceAll("\\.", ",") + "€");
    if (tips.compareTo(new BigDecimal("0.0")) < 0) {
      cs.getTipSumLabel().getStyleClass().clear();
      cs.getTipSumLabel().getStyleClass().addAll("minusLabel");
    } else {
      cs.getTipSumLabel().getStyleClass().clear();
      cs.getTipSumLabel().getStyleClass().addAll("label");
    }
    cs.updateToolTips();
  }
  
  /**
   * Handles the Event if the standard design is used.
   * @since 1.0
   */
  private void handleBig() {
    /*
     * Make all Fields uneditable, so they cannot be altered after calculation. This is needed to 
     * make sure, that the export will be correct. 
     */
    cs.setFieldsEditable(false);
    cs.getExportButton().setDisable(false);
    cs.getResetButton().setDisable(true);
    
    /*
     * Create new BigDecimals to store the amount of Money that was counted. This is divided into 
     * Bills and Coins, since there is a need to display the total Coinage in the purses.
     */
    BigDecimal coinageMoney = new BigDecimal("0.00");
    BigDecimal billMoney = new BigDecimal("0.00");

    /*
     * Adds each (TextField * factor) to the dedicated BigDecimal.
     */
    for (int i = 0; i <= 14; i++) {
      if (i <= 7) {
        coinageMoney = addToDecimal(coinageMoney, i);
      } else {
        billMoney = addToDecimal(billMoney, i);
      }
    }
    
    /*
     * A new BigDecimal for the total amount of Money in the purses.
     */
    BigDecimal totalEuros = coinageMoney.add(billMoney);
    
    /*
     * Sets the Text of the Labels, that display each of the BigDecimals value.
     */
    cs.getCoinSumLabel().setText(("" + coinageMoney + "€").replaceAll("\\.", ","));
    cs.getBillSumLabel().setText(("" + billMoney + "€").replaceAll("\\.", ","));
    cs.getSumLabel().setText(("" + totalEuros + "€").replaceAll("\\.", ","));
    
    /*
     * Calculates the amount of coinage, that has to be in each purse and was in it beforehand.
     */
    int coinNecessity = Integer.parseInt(setField(cs.getPurseTextField())) * 25;
    /*
     * Sets the Text of the Label, that displays the amount of coinage needed.
     */
    cs.getCoinNecessityLabel().setText(coinNecessity + ",00€");
    
    /*
     * Creates a new BigDecimal to calculate the difference between the coinage in the purses and 
     * the amount of needed coinage.
     */
    BigDecimal coinageDiff = coinageMoney.subtract(new BigDecimal(coinNecessity));
    /*
     * Sets the Text of the Label, that displays the BigDecimal for the coinage difference.
     */
    cs.getCoinDifferenceLabel().setText(coinageDiff.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * Compares the coinage difference BigDecimal to 0 to check if it's negative or positive. If 
     * it's negative, the Label will display it's Text in red, else in normal black.
     */
    if (coinageDiff.compareTo(new BigDecimal("0.0")) == -1) {
      cs.getCoinDifferenceLabel().getStyleClass().clear();
      cs.getCoinDifferenceLabel().getStyleClass().addAll("minusLabel");
    } else {
      cs.getCoinDifferenceLabel().getStyleClass().clear();
      cs.getCoinDifferenceLabel().getStyleClass().addAll("label");
    }
    
    /*
     * There is a given discrepancy of coinage of 25€. When counting the purses, this discrepancy 
     * will be fixed, by taking the amount of Euro from the Bill Money. So we have to subtract the 
     * coin Difference from the billMoney. The result will be displayed in the belonging Label.
     */
    BigDecimal revenueWithTips = billMoney.add(coinageDiff);
    cs.getCoinCleanedLabel().setText(revenueWithTips.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * Calculates the total CashNecessity by adding cashNecessityEuro with the Input of the 
     * cent-Field after cleaning it from potential Input-Errors (input >= 100, input contains 
     * non-Digits).
     */
    BigDecimal cashNecessity = new BigDecimal("0.00");
    cashNecessity = cashNecessity.add(new BigDecimal(cs.getCashNecessityEuroTextField().getText()
        .replaceAll(",", "\\.")));
    cashNecessity = cashNecessity.setScale(2, RoundingMode.DOWN);
    cs.getCashNecessityLabel().setText(cashNecessity.toString().replaceAll("\\.", ",") + "€");
    
    /*
     * Calculates the tip in the Purses by subtracting Cash Necessity from the revenue calculated 
     * before.
     */
    BigDecimal tips = revenueWithTips.subtract(cashNecessity);
    cs.getTipSumLabel().setText(tips.toString().replaceAll("\\.", ",") + "€");
    if (tips.compareTo(new BigDecimal("0.0")) < 0) {
      cs.getTipSumLabel().getStyleClass().clear();
      cs.getTipSumLabel().getStyleClass().addAll("minusLabel");
    } else {
      cs.getTipSumLabel().getStyleClass().clear();
      cs.getTipSumLabel().getStyleClass().addAll("label");
    }
    cs.updateToolTips();
  }
  
  /**
   * Adds a new BigDecimal to the given {@code decimal}. The new BigDecimal is created by 
   * multiplying a factor depending on {@code index} with the Text of the TextField at 
   * {@link ComponentStorer#getCoinTextFields()}[index]. <br>
   * If the index is out of the bonds specified below, an Error Message will be printed 
   * into the Console and the factor will be set to {@code 0.00} and the index will be set to 0.<br>
   * The Indices imply the following factors: <br>
   * 0 <=> 0.01 (1ct Coins) <br>
   * 1 <=> 0.02 (2ct Coins) <br>
   * 2 <=> 0.05 (5ct Coins) <br>
   * 3 <=> 0.10 (10ct Coins) <br>
   * 4 <=> 0.20 (20ct Coins) <br>
   * 5 <=> 0.50 (50ct Coins) <br>
   * 6 <=> 1.00 (1€ Coins) <br>
   * 7 <=> 2.00 (2€ Coins) <br>
   * 8 <=> 5.00 (5€ Bills) <br>
   * 9 <=> 10.00 (10€ Bills) <br>
   * 10 <=> 20.00 (20€ Bills) <br>
   * 11 <=> 50.00 (50€ Bills) <br>
   * 12 <=> 100.00 (100€ Bills) <br>
   * 13 <=> 200.00 (200€ Bills) <br>
   * 14 <=> 500.00 (500€ Bills) <br>
   * @param decimal The BigDecimal, the new BigDecimal is added to.
   * @param index The Index, that identifies the BigDecimal to be created.
   * @return  The Result of {@link BigDecimal#add(BigDecimal)} with the new BigDecimal.
   * @since 1.0
   */
  private BigDecimal addToDecimal(BigDecimal decimal, int index) {
    /*
     * The Factor, indicated be the index, that will be multiplied with the given decimal.
     */
    BigDecimal factor;
    /*
     * Determines, if the index is pointed at a bill factor instead of a coin factor.
     */
    boolean bill;
    
    /*
     * If the index is 8 or greater, it indicates a bill factor, since there are only 8 coin-types 
     * (starting to count at 0).
     */
    bill = index > 7;
    
    /*
     * Switches the index, since each index points at a single factor indirectly by indexing their 
     * spot in the Arrays in ComponentStorer.
     */
    switch (index) {
      case 0: 
        factor = new BigDecimal("0.01");
        break;
      case 1:
        factor = new BigDecimal("0.02");
        break;
      case 2:
        factor = new BigDecimal("0.05");
        break;
      case 3:
        factor = new BigDecimal("0.10");
        break;
      case 4:
        factor = new BigDecimal("0.20");
        break;
      case 5:
        factor = new BigDecimal("0.50");
        break;
      case 6:
        factor = new BigDecimal("1.00");
        break;
      case 7:
        factor = new BigDecimal("2.00");
        break;
      case 8:
        factor = new BigDecimal("5.00");
        index = index % 8;
        break;
      case 9:
        factor = new BigDecimal("10.00");
        index = index % 8;
        break;
      case 10:
        factor = new BigDecimal("20.00");
        index = index % 8;
        break;
      case 11:
        factor = new BigDecimal("50.00");
        index = index % 8;
        break;
      case 12:
        factor = new BigDecimal("100.00");
        index = index % 8;
        break;
      case 13:
        factor = new BigDecimal("200.00");
        index = index % 8;
        break;
      case 14:
        factor = new BigDecimal("500.00");
        index = index % 8;
        break;
      default:
        System.err.println("An Error occured when parsing the TextFields, please report this!");
        factor = new BigDecimal("0.00");
        index = 0;
        break;      
    }
    
    /*
     * If the index pointed at a bill-Factor, the application has to get the factor from 
     * billTextField-Array in ComponentStorer, else from the coinTextField-Array.
     */
    if (!bill) {
      return decimal.add(calculateSetComponents(cs.getCoinTextFields()[index], 
          cs.getCoinResults()[index], factor));
    } else {
      return decimal.add(calculateSetComponents(cs.getBillsTextFields()[index], 
          cs.getBillsResults()[index], factor));
    }
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
  private BigDecimal calculateSetComponents(TextField field, Label label, BigDecimal factor) {
    /*
     * Creates a new BigDecimal, that will be returned in the End.
     */
    BigDecimal res = new BigDecimal("0.0");
    
    /*
     * Calls setField(TextField) to get a digit-only String and parses this String to an Integer.
     */
    int parsed = Integer.parseInt(setField(field));
    
    /*
     * Multiplies the given factor with the parsed User-input and adds it to res (0.0) to get 
     * "Input*Factor".
     */
    res = res.add(factor.multiply(new BigDecimal(parsed)));
    
    /*
     * Sets the Text for the Label.
     */
    String strRes = "= " + res;
    
    /*
     * Sets the Text of the given Label.
     */
    label.setText(strRes.concat("€").replaceAll("\\.", ","));
    return res;
  }
  
  /**
   * Sets the content of the given Field to a digit-only String, so it can be processed by 
   * {@link Integer#parseInt(String)} and returns the new Text as a String.
   * @param field The TextField to be altered.
   * @return  The altered content of the Field as a String.
   * @since 1.0
   */
  private String setField(TextField field) {
    String txt = field.getText();
    /*
     * Calls removeNonDigits(String) to remove all non-digits from the String.
     */
    txt = removeNonDigits(txt);
    
    /*
     * Sets the text to the digit only String.
     */
    field.setText(txt);
    return txt;
  }
  
  /**
   * Removes all Non-Digits from the given String
   * @param str The String, that will be edited
   * @return  The given String without all Characters that are Non-Digits.
   * @since 1.0
   */
  private String removeNonDigits(String str) {
    /*
     * If the Input was an empty String or the given String wasn't yet initialized (usually 
     * these are always initialized, but doing this for error-prevention), the Field will be set 
     * to 0.
     */
    if (str == null || str.length() == 0) {
      return "0";
    }
    /*
     * Calls replaceAll(String, String) from the String-Class with the regular expression \\D+, 
     * which stands for any Character but [0-9] and "" to replace these occurences.
     */
    return str.replaceAll("\\D+", "");
  }
  
}
