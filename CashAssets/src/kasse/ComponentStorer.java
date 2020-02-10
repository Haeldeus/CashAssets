package kasse;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * A Class which Objects stores all editable Components of the Application.
 * With the given getter- and setter-Methods, the Button Handlers can manipulate these 
 * Components to show desired texts.
 * @author Haeldeus
 * @version 1.0
 */
public class ComponentStorer {
  
  /**
   * The ComboBox, that is used as the Box to select the Day in the Application.
   */
  private ComboBox<String> dayBox;
  
  /**
   * The ComboBox, that is used as the Box to select the Month in the Application.
   */
  private ComboBox<String> monthBox;
  
  /**
   * The TextField, where the User can enter the Year in the Application.
   */
  private TextField year;

  /**
   * All TextFields, where the User can enter the values of counted Bills of each type.
   */
  private TextField[] billsTextFields;
  
  /**
   * All TextFields, where the User can enter the values of counted coins of each type.
   */
  private TextField[] coinTextFields;
  
  /**
   * All Labels, that show the total sum of all Bills of this type for each type.
   */
  private Label[] billsResults;
  
  /**
   * All Labels, that show the total sum of all Coins of this type for each type.
   */
  private Label[] coinResults;
  
  /**
   * The TextField, where the User enters the amount of counted purses.
   */
  private TextField purseTextField;
  
  /**
   * The TextField, where the User enters the cent-part of the Cash Necessity.
   */
  private TextField cashNecessityCentTextField;
  
  /**
   * The TextField, where the User enters the Euro-Part of the Cash Necessity.
   */
  private TextField cashNecessityEuroTextField;
  
  /**
   * The Label, that displays the total amount of coinage Money in all purses.
   */
  private Label coinSumLabel;
  
  /**
   * The Label that displays the total amount of bill Money in all purses.
   */
  private Label billSumLabel;
  
  /**
   * The Label that displays the total amount of Money in all Purses (coinSum + billSum).
   */
  private Label sumLabel;
  
  /**
   * The Label, that displays the amount of coinage that has to be in the purses.
   */
  private Label coinNecessityLabel;
  
  /**
   * The Label, that displays the amount of Money, that should be in the purses after subtracting 
   * the coin difference.
   */
  private Label coinCleanedLabel;
  
  /**
   * The Label, that displays the difference between the coinage Money that should be in the purses 
   * and the actual amount of coinage Money, that was in the purses.
   */
  private Label coinDifferenceLabel;
  
  /**
   * The Label, that displays the amount of Money, that should be in the purses (excluding tips) 
   * according to the electronic system.
   */
  private Label cashNecessityLabel;
  
  /**
   * The Label, that displays the total Sum of Tips left in the purses after subtracting the Cash 
   * Necessity from the total Money in the Purses.
   */
  private Label tipSumLabel;
  
  /**
   * The Button to export the Data to an Excel Sheet when pressed.
   */
  private Button exportButton;
  
  /**
   * The Button, which resets all User input to default values.
   */
  private Button resetButton;
  
  /**
   * The default Constructor for this Class. Creates a new ComponentStorer.
   * @since 1.0
   */
  public ComponentStorer() {
  }
  
  /**
   * Disables or enables all fields, where the User can enter values, depending on the given 
   * Boolean.
   * @param editable  The boolean value, if the fields should be editable or not.
   * @since 1.0
   */
  public void setFieldsEditable(boolean editable) {
    /*
     * Sets all TextFields editability to the given Boolean and sets their Styles accordingly.
     */
    for (TextField tf : billsTextFields) {
      tf.setEditable(editable);
      if (editable) {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      } else {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      }
    }
    for (TextField tf : coinTextFields) {
      tf.setEditable(editable);
      if (editable) {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      } else {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      }
    }
    /*
     * Sets the ComboBoxes editability to the given Boolean.
     */
    dayBox.setDisable(!editable);
    monthBox.setDisable(!editable);
    
    /*
     * Sets the TextFields editability to the given Boolean.
     */
    year.setEditable(editable);
    purseTextField.setEditable(editable);
    cashNecessityCentTextField.setEditable(editable);
    cashNecessityEuroTextField.setEditable(editable);
    
    /*
     * Sets the Styles of all TextFields accordingly to the Boolean.
     */
    if (editable) {
      year.getStyleClass().clear();
      year.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      purseTextField.getStyleClass().clear();
      purseTextField.getStyleClass().addAll("text-field", "text-input", "purseTF");
      cashNecessityCentTextField.getStyleClass().clear();
      cashNecessityCentTextField.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      cashNecessityEuroTextField.getStyleClass().clear();
      cashNecessityEuroTextField.getStyleClass().addAll("text-field", "text-input", "enabledTF");
    } else {
      year.getStyleClass().clear();
      year.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      purseTextField.getStyleClass().clear();
      purseTextField.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      cashNecessityCentTextField.getStyleClass().clear();
      cashNecessityCentTextField.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      cashNecessityEuroTextField.getStyleClass().clear();
      cashNecessityEuroTextField.getStyleClass().addAll("text-field", "text-input", "disabledTF");
    }
  }

  /**
   * Returns the {@link #dayBox}, the ComboBox, where the User can enter the Day of the Date, a 
   * new File should be created of.
   * @return The DayBox that has to be set beforehand.
   * @throws NullPointerException If dayBox wasn't set before.
   * @since 1.0
   */
  public ComboBox<String> getDayBox() {
    return dayBox;
  }

  /**
   * Sets the {@link #dayBox}, the ComboBox where the User can enter the Day of the Date to create 
   * a File of, to the given ComboBox to be accessible by other Objects.
   * @param dayBox The ComboBox, where the User can enter the Day of the Date to create a Sheet for.
   * @since 1.0
   */
  public void setDayBox(ComboBox<String> dayBox) {
    this.dayBox = dayBox;
  }

  /**
   * Returns the {@link #monthBox}, the ComboBox, where the User can enter the Month of the Date, a 
   * new Sheet should be created of.
   * @return The MonthBox that has to be set beforehand.
   * @throws NullPointerException If monthBox wasn't set before.
   * @since 1.0
   */
  public ComboBox<String> getMonthBox() {
    return monthBox;
  }

  /**
   * Sets the {@link #monthBox}, the ComboBox where the User can enter the Month of the Date to 
   * create a File of, to the given ComboBox to be accessible by other Objects.
   * @param monthBox The ComboBox, where the User can enter the Month of the Date to create a Sheet 
   *     for.
   * @since 1.0
   */
  public void setMonthBox(ComboBox<String> monthBox) {
    this.monthBox = monthBox;
  }

  /**
   * Returns the {@link #year}, the TextField, where the User can enter the Year of the Date, a new 
   * Sheet should be created of.
   * @return The Year-TextField that has to be set beforehand
   * @throws NullPointerException If the Year-TextField wasn't set before.
   * @since 1.0
   */
  public TextField getYear() {
    return year;
  }

  /**
   * Sets the {@link #year}, the TextField where the User can enter the Year of the Date to create 
   * a new File of, to the given TextField to be accessible by other Objects.
   * @param year The TextField, where the User can enter the Year of the Date to create a Sheet for.
   * @since 1.0
   */
  public void setYear(TextField year) {
    this.year = year;
  }

  /**
   * Returns {@link #billsTextFields}, all TextFields, where the User can enter the amount of Bills 
   * counted. Each Bill has an unique index to access it's TextField: <br>
   * 0 <=> 5€ Bills <br>
   * 1 <=> 10€ Bills <br>
   * 2 <=> 20€ Bills <br>
   * 3 <=> 50€ Bills <br>
   * 4 <=> 100€ Bills <br>
   * 5 <=> 200€ Bills <br>
   * 6 <=> 500€ Bills <br>
   * @return The Array {@link #billsTextFields} that has to be set beforehand.
   * @throws NullPointerException If the Array wasn't set before.
   * @since 1.0
   */
  public TextField[] getBillsTextFields() {
    return billsTextFields;
  }

  /**
   * Sets {@link #billsTextFields}, the TextFields where the User can enter the amount of Bills 
   * counted, to the given Array of TextFields to be accessible by other Objects.
   * @param billsTextFields The TextFields, where the User can enter the amount of Bills counted 
   *     for each type.
   * @since 1.0
   */
  public void setBillsTextFields(TextField[] billsTextFields) {
    this.billsTextFields = billsTextFields;
  }

  /**
   * Returns the {@link #coinTextFields}, the Array of TextFields, where the User can enter the 
   * amount of Coins counted. Each Coin has an unique index to access it's TextField: <br>
   * 0 <=> 1ct Coins <br>
   * 1 <=> 2ct Coins <br>
   * 2 <=> 5ct Coins <br>
   * 3 <=> 10ct Coins <br>
   * 4 <=> 20ct Coins <br>
   * 5 <=> 50ct Coins <br>
   * 6 <=> 1€ Coins <br>
   * 7 <=> 2€ Coins <br>
   * @return The Array {@link #coinTextFields}, that has to be set beforehand.
   * @throws NullPointerException If the Coin-TextFields weren't set before.
   * @since 1.0
   */
  public TextField[] getCoinTextFields() {
    return coinTextFields;
  }

  /**
   * Sets {@link #coinTextFields}, the TextFields where the User can enter the amount of each coin, 
   * to the given Array of TextFields to be accessible by other Objects.
   * @param coinTextFields The TextFields, where the User can enter the amount of Coins counted 
   *     for each type.
   * @since 1.0
   */
  public void setCoinTextFields(TextField[] coinTextFields) {
    this.coinTextFields = coinTextFields;
  }

  /**
   * Returns {@link #billsResults}, The Labels where the Result of the Multiplication for each 
   * Bill is stored. Each Bill has an unique index to access it's Label, see 
   * {@link #getBillsTextFields()} for these indices.
   * @return The Labels, where the results of the Multiplication are stored, which have to be set 
   *     beforehand.
   * @throws NullPointerException If the Bills-Results-Labels weren't set before.
   * @since 1.0
   */
  public Label[] getBillsResults() {
    return billsResults;
  }

  /**
   * Sets {@link #billsResults}, the Labels where the Results of the Multiplication are stored, to 
   * the given Array of Labels to be accessible by other Objects.
   * @param billsResults The Labels, where the Results of the Multiplication are stored.
   * @since 1.0
   */
  public void setBillsResults(Label[] billsResults) {
    this.billsResults = billsResults;
  }

  /**
   * Returns {@link #coinResults}, the Labels where the Result of the Multiplication for each coin 
   * is stored.
   * @return The Labels, where the results of the Multiplication are stored, which have to be set 
   *     beforehand.
   * @throws NullPointerException If the Coin-Results-Labels weren't set before.
   * @since 1.0
   */
  public Label[] getCoinResults() {
    return coinResults;
  }

  /**
   * Sets {@link #coinResults}, the Labels where the result of the Multiplication for each coin is 
   * stored, to the given Array of Labels to be accessible by other Objects.
   * @param coinResults The Labels, where the Results of the Multiplication are stored.
   * @since 1.0
   */
  public void setCoinResults(Label[] coinResults) {
    this.coinResults = coinResults;
  }

  /**
   * Returns {@link #purseTextField}, the TextField where the User can enter the amount of purses 
   * counted.
   * @return The {@link #purseTextField}, that has to be set beforehand.
   * @throws NullPointerException If the purse-TextField wasn't set before.
   * @since 1.0
   */
  public TextField getPurseTextField() {
    return purseTextField;
  }

  /**
   * Sets {@link #purseTextField}, the TextField where the User can enter the amount of purses 
   * counted, to the given TextField to be accessible by other Objects.
   * @param purseTextField The TextField, that contains the amount of purses counted.
   * @since 1.0
   */
  public void setPurseTextField(TextField purseTextField) {
    this.purseTextField = purseTextField;
  }

  /**
   * Returns {@link #cashNecessityCentTextField}, the TextField where the User can enter the 
   * cent-Part of the Cash-Necessity.
   * @return The {@link #cashNecessityCentTextField}, that has to be set beforehand.
   * @throws NullPointerException If the TextField wasn't set before.
   * @since 1.0
   */
  public TextField getCashNecessityCentTextField() {
    return cashNecessityCentTextField;
  }

  /**
   * Sets {@link #cashNecessityCentTextField}, the TextField where the User can enter the cent-Part 
   * of the Cash-Necessity, to the given TextField to be accessible by other Objects.
   * @param cashNecessityTextFields The TextField, that contains the cent-Part of the 
   *     Cash-Necessity.
   * @since 1.0
   */
  public void setCashNecessityCentTextField(TextField cashNecessityTextFields) {
    this.cashNecessityCentTextField = cashNecessityTextFields;
  }

  /**
   * Returns {@link #cashNecessityEuroTextField}, the TextField where the User can enter the 
   * Euro-Part of the Cash-Necessity.
   * @return The {@link #cashNecessityEuroTextField}, that has to be set beforehand.
   * @throws NullPointerException If the TextField wasn't set before.
   * @since 1.0
   */
  public TextField getCashNecessityEuroTextField() {
    return cashNecessityEuroTextField;
  }

  /**
   * Sets {@link #cashNecessityEuroTextField}, the TextField where the User can enter the 
   * Euro-Part of the Cash-Necessity, to the given TextField to be accessible by other Objects.
   * @param cashNecessityEuroTextField  The TextField, that contains the Euro-Part of the 
   *     Cash-Necessity.
   * @since 1.0
   */
  public void setCashNecessityEuroTextField(TextField cashNecessityEuroTextField) {
    this.cashNecessityEuroTextField = cashNecessityEuroTextField;
  }

  /**
   * Returns {@link #coinSumLabel}, the Label that contains the total Sum of Coinage in 
   * all Purses counted.
   * @return The {@link #coinSumLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getCoinSumLabel() {
    return coinSumLabel;
  }

  /**
   * Sets {@link #coinSumLabel}, the Label that displays the total Sum of Coinage in all 
   * purses counted, to the given Label to be accessible by other Objects.
   * @param coinSumLabel The Label, that displays the total Sum of Coinage in all purses.
   * @since 1.0
   */
  public void setCoinSumLabel(Label coinSumLabel) {
    this.coinSumLabel = coinSumLabel;
  }

  /**
   * Returns {@link #billSumLabel}, the Label that displays the total amount of Money in 
   * Bills in all purses.
   * @return The {@link #billSumLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getBillSumLabel() {
    return billSumLabel;
  }

  /**
   * Sets {@link #billSumLabel}, the Label that displays the total Sum of Bills in all 
   * purses counted, to the given Label to be accessible by other Objects.
   * @param billSumLabel The Label, that displays the total Sum of Bills in all purses.
   * @since 1.0
   */
  public void setBillSumLabel(Label billSumLabel) {
    this.billSumLabel = billSumLabel;
  }

  /**
   * Returns the {@link #sumLabel}, the Label that displays the total Sum (coinage + Bills) 
   * in all purses.
   * @return The {@link #sumLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getSumLabel() {
    return sumLabel;
  }

  /**
   * Sets {@link #sumLabel}, the Label that displays the total Sum (coinage + Bills) in all purses, 
   * to the given Label to be accessible by other Objects.
   * @param sumLabel The Label, that displays the total Sum in all purses.
   * @since 1.0
   */
  public void setSumLabel(Label sumLabel) {
    this.sumLabel = sumLabel;
  }

  /**
   * Returns the {@link #coinNecessityLabel}, the Label that displays the amount of coinage, that 
   * was in the purses before the business day.
   * @return The {@link #coinNecessityLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getCoinNecessityLabel() {
    return coinNecessityLabel;
  }

  /**
   * Sets {@link #coinNecessityLabel}, the Label that displays the amount of coinage that was in 
   * the purses before the business day, to the given Label to be accessible by other Objects.
   * @param coinNecessityLabel The Label, that displays the amount of coinage that was in the 
   *     purses before the business day.
   * @since 1.0
   */
  public void setCoinNecessityLabel(Label coinNecessityLabel) {
    this.coinNecessityLabel = coinNecessityLabel;
  }

  /**
   * Returns the {@link #coinCleanedLabel}, the Label that displays the amount of Money that have 
   * to be in the purses after subtracting the coin-Difference from the total Sum.
   * @return The {@link #coinCleanedLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getCoinCleanedLabel() {
    return coinCleanedLabel;
  }

  /**
   * Sets {@link #coinCleanedLabel}, the Label that displays the amount of Money that have to be in 
   * the purses after subtracting the coin-Difference from the total Sum, to the given Label to be 
   * accessible by other Objects.
   * @param coinCleanedLabel The Label, that displays the amount of Money that has to be in all 
   *     Purses after subtracting the coin-Difference from the total Sum.
   * @since 1.0
   */
  public void setCoinCleanedLabel(Label coinCleanedLabel) {
    this.coinCleanedLabel = coinCleanedLabel;
  }

  /**
   * Returns the {@link #coinDifferenceLabel}, the Label that displays the difference between the 
   * needed coinage Money and the coinage Money, that was in the purses.
   * @return The {@link #coinDifferenceLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getCoinDifferenceLabel() {
    return coinDifferenceLabel;
  }

  /**
   * Sets {@link #coinDifferenceLabel}, the Label that displays the difference between the needed 
   * coinage Money and the coinage Money that was in the purses, to the given Label to be 
   * accessible by other Objects.
   * @param coinDifferenceLabel The Label, that displays the difference between the needed coinage 
   *     Money and the coinage Money that was in the purses.
   * @since 1.0
   */
  public void setCoinDifferenceLabel(Label coinDifferenceLabel) {
    this.coinDifferenceLabel = coinDifferenceLabel;
  }

  /**
   * Returns the {@link #cashNecessityLabel}, the Label that displays the total Money needed after 
   * the business day.
   * @return The {@link #cashNecessityLabel}, that has to be set beforehand.
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getCashNecessityLabel() {
    return cashNecessityLabel;
  }

  /**
   * Sets {@link #cashNecessityLabel}, the Label that displays the total Money needed after the 
   * business day, to the given Label to be accessible by other Objects.
   * @param cashNecessityLabel The Label, that displays the total Money needed.
   * @since 1.0
   */
  public void setCashNecessityLabel(Label cashNecessityLabel) {
    this.cashNecessityLabel = cashNecessityLabel;
  }

  /**
   * Returns the {@link #tipSumLabel}, the Label that displays the excess Money, that was in the 
   * purses. This is calculated by subtracting the Cash Necessity from the total Sum of Money in 
   * the purses. This excess is the Tip of the business day.
   * @return The {@link #tipSumLabel}, that has to be set beforehand
   * @throws NullPointerException If the Label wasn't set before.
   * @since 1.0
   */
  public Label getTipSumLabel() {
    return tipSumLabel;
  }

  /**
   * Sets {@link #tipSumLabel}, the Label that displays the excess Money that was in the Purses 
   * (the Tip of the business day), to the given Label to be accessible by other Objects.
   * @param tipSumLabel The Label that displays the excess Money in the Purses (the Tip).
   * @since 1.0
   */
  public void setTipSumLabel(Label tipSumLabel) {
    this.tipSumLabel = tipSumLabel; 
  }

  /**
   * Returns the {@link #exportButton}, the Button used to export the calculated data to an 
   * Excel-Sheet.
   * @return The {@link #exportButton}, that has to be set beforehand.
   * @throws NullPointerException If the Button wasn't set before.
   * @since 1.0
   */
  public Button getExportButton() {
    return exportButton;
  }

  /**
   * Sets {@link #exportButton}, the Button used to export the calculated data to an Excel-Sheet, 
   * to the given Button to be accessible by other Objects.
   * @param exportButton The Button, that will export the data to an Excel-Sheet.
   * @since 1.0
   */
  public void setExportButton(Button exportButton) {
    this.exportButton = exportButton;   
  }

  /**
   * Returns the {@link #resetButton}, the Button used to reset all TextFields altered by User 
   * input to their default values.
   * @return The {@link #resetButton}, that has to be set beforehand.
   * @throws NullPointerException If the Button wasn't set before.
   * @since 1.0
   */
  public Button getResetButton() {
    return resetButton;
  }

  /**
   * Sets {@link #resetButton}, the Button used to reset all TextFields altered by User input to 
   * their default values, to the given Button to be accessible by other Objects.
   * @param resetButton The Button, that will reset the User input.
   * @since 1.0
   */
  public void setResetButton(Button resetButton) {
    this.resetButton = resetButton;
  }

  /**
   * Returns all Labels, that are alterable. These are: <br>
   * - {@link #coinSumLabel} <br>
   * - {@link #billSumLabel} <br>
   * - {@link #sumLabel} <br>
   * - {@link #coinNecessityLabel} <br>
   * - {@link #coinCleanedLabel} <br>
   * - {@link #coinDifferenceLabel} <br>
   * - {@link #cashNecessityLabel} <br>
   * - {@link #tipSumLabel} <br>
   * They have to be set beforehand.
   * @return An Array with the Size of 8, that contains all alterable Labels.
   * @throws NullPointerException If any of the Labels weren't set before.
   * @since 1.0
   */
  public Label[] getAlterableLabels() {
    return new Label[] {coinSumLabel, billSumLabel, sumLabel, coinNecessityLabel, 
        coinCleanedLabel, coinDifferenceLabel, cashNecessityLabel, tipSumLabel};
  }

  /**
   * Updates all ToolTips to show their Value as a Tooltip. This is used to maintain 
   * usability when big values were entered.
   * @since 1.0
   */
  public void updateToolTips() {
    for (Label l : getBillsResults()) {
      l.setTooltip(new Tooltip(l.getText()));
    }
    for (Label l : getCoinResults()) {
      l.setTooltip(new Tooltip(l.getText()));
    }
    
    for (Label l : getAlterableLabels()) {
      l.setTooltip(new Tooltip(l.getText()));
    }
  }
}
