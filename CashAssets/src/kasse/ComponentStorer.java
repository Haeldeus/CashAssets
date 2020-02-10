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
   * Sets the {@link #dayBox} to the given ComboBox for other Objects to access.
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
   * Sets the {@link #monthBox} to the given ComboBox for other Objects to access.
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
   * Sets the {@link #year} to the given TextField for other Objects to access.
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
   * Sets {@link #billsTextFields} to the given Array of TextFields for other Objects to access.
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
   * Sets {@link #coinTextFields} to the given Array of TextFields for other Objects to access.
   * @param coinTextFields The TextFields, where the User can enter the amount of Coins counted 
   *     for each type.
   * @since 1.0
   */
  public void setCoinTextFields(TextField[] coinTextFields) {
    this.coinTextFields = coinTextFields;
  }

  /**
   * Returns {@link #billsResults}, The Labels where the Result of the Multiplication for each 
   * Bill is stored.
   * @return the billsResults
   * @throws NullPointerException If the Bills-Results-Labels weren't set before.
   * @since 1.0
   */
  public Label[] getBillsResults() {
    return billsResults;
  }

  /**
   * Sets {@link #billsResults} to the given Array of Labels for other Objects to access.
   * @param billsResults the billsResults to set
   * @since 1.0
   */
  public void setBillsResults(Label[] billsResults) {
    this.billsResults = billsResults;
  }

  /**
   * @return the coinResults
   */
  public Label[] getCoinResults() {
    return coinResults;
  }

  /**
   * @param coinResults the coinResults to set
   */
  public void setCoinResults(Label[] coinResults) {
    this.coinResults = coinResults;
  }

  /**
   * @return the purseTextField
   */
  public TextField getPurseTextField() {
    return purseTextField;
  }

  /**
   * @param purseTextField the purseTextField to set
   */
  public void setPurseTextField(TextField purseTextField) {
    this.purseTextField = purseTextField;
  }

  /**
   * @return the cashNecessityTextFields
   */
  public TextField getCashNecessityCentTextField() {
    return cashNecessityCentTextField;
  }

  /**
   * @param cashNecessityTextFields the cashNecessityTextFields to set
   */
  public void setCashNecessityCentTextField(TextField cashNecessityTextFields) {
    this.cashNecessityCentTextField = cashNecessityTextFields;
  }

  /**
   * @return the cashNecessityEuroTextField
   */
  public TextField getCashNecessityEuroTextField() {
    return cashNecessityEuroTextField;
  }

  /**
   * @param cashNecessityEuroTextField the cashNecessityEuroTextField to set
   */
  public void setCashNecessityEuroTextField(TextField cashNecessityEuroTextField) {
    this.cashNecessityEuroTextField = cashNecessityEuroTextField;
  }

  /**
   * @return the coinLabel
   */
  public Label getCoinSumLabel() {
    return coinSumLabel;
  }

  /**
   * @param coinSumLabel the coinSumLabel to set
   */
  public void setCoinSumLabel(Label coinSumLabel) {
    this.coinSumLabel = coinSumLabel;
  }

  /**
   * @return the billLabel
   */
  public Label getBillSumLabel() {
    return billSumLabel;
  }

  /**
   * @param billSumLabel the billSumLabel to set
   */
  public void setBillSumLabel(Label billSumLabel) {
    this.billSumLabel = billSumLabel;
  }

  /**
   * @return the sumLabel
   */
  public Label getSumLabel() {
    return sumLabel;
  }

  /**
   * @param sumLabel the sumLabel to set
   */
  public void setSumLabel(Label sumLabel) {
    this.sumLabel = sumLabel;
  }

  /**
   * @return the coinNecessityLabel
   */
  public Label getCoinNecessityLabel() {
    return coinNecessityLabel;
  }

  /**
   * @param coinNecessityLabel the coinNecessityLabel to set
   */
  public void setCoinNecessityLabel(Label coinNecessityLabel) {
    this.coinNecessityLabel = coinNecessityLabel;
  }

  /**
   * @return the coinCleanedLabel
   */
  public Label getCoinCleanedLabel() {
    return coinCleanedLabel;
  }

  /**
   * @param coinCleanedLabel the coinCleanedLabel to set
   */
  public void setCoinCleanedLabel(Label coinCleanedLabel) {
    this.coinCleanedLabel = coinCleanedLabel;
  }

  /**
   * @return the coinDifferenceLabel
   */
  public Label getCoinDifferenceLabel() {
    return coinDifferenceLabel;
  }

  /**
   * @param coinDifferenceLabel the coinDifferenceLabel to set
   */
  public void setCoinDifferenceLabel(Label coinDifferenceLabel) {
    this.coinDifferenceLabel = coinDifferenceLabel;
  }

  /**
   * @return the cashNecessityLabel
   */
  public Label getCashNecessityLabel() {
    return cashNecessityLabel;
  }

  /**
   * @param cashNecessityLabel the cashNecessityLabel to set
   */
  public void setCashNecessityLabel(Label cashNecessityLabel) {
    this.cashNecessityLabel = cashNecessityLabel;
  }

  /**
   * @return the tipSumLabel
   */
  public Label getTipSumLabel() {
    return tipSumLabel;
  }

  /**
   * @param tipSumLabel the tipSumLabel to set
   */
  public void setTipSumLabel(Label tipSumLabel) {
    this.tipSumLabel = tipSumLabel; 
  }

  /**
   * @return the exportButton
   */
  public Button getExportButton() {
    return exportButton;
  }

  /**
   * @param exportButton the exportButton to set
   */
  public void setExportButton(Button exportButton) {
    this.exportButton = exportButton;   
  }

  /**
   * @return
   */
  public Button getResetButton() {
    return resetButton;
  }

  /**
   * @param resetButton
   */
  public void setResetButton(Button resetButton) {
    this.resetButton = resetButton;
  }

  /**
   * @return
   */
  public Label[] getAlterableLabels() {
    return new Label[] {coinSumLabel, billSumLabel, sumLabel, coinNecessityLabel, 
        coinCleanedLabel, coinDifferenceLabel, cashNecessityLabel, tipSumLabel};
  }

  /**
   * Updates all ToolTips to show their Value as a Tooltip. This is used to maintain 
   * usability when entered big values.
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
