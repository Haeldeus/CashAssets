package kasse;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxListCell;

public class ComponentStorer {
  
  private ComboBox<String> dayBox;
  
  private ComboBox<String> monthBox;
  
  private TextField year;

  private TextField[] billsTextFields;
  
  private TextField[] coinTextFields;
  
  private Label[] billsResults;
  
  private Label[] coinResults;
  
  private TextField purseTextField;
  
  private TextField cashNecessityCentTextField;
  
  private TextField cashNecessityEuroTextField;
  
  private Label coinSumLabel;
  
  private Label billSumLabel;
  
  private Label sumLabel;
  
  private Label coinNecessityLabel;
  
  private Label coinCleanedLabel;
  
  private Label coinDifferenceLabel;
  
  private Label cashNecessityLabel;
  
  private Label tipSumLabel;
  
  private Button exportButton;
  
  private Button resetButton;
  
  public ComponentStorer() {
  }
  
  /**
   * 
   * @param bool  The boolean value, if the fields should be editable or not.
   */
  public void setFieldsEditable(boolean bool) {
    for (TextField tf : billsTextFields) {
      tf.setEditable(bool);
      if (bool) {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      } else {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      }
    }
    for (TextField tf : coinTextFields) {
      tf.setEditable(bool);
      if (bool) {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "enabledTF");
      } else {
        tf.getStyleClass().clear();
        tf.getStyleClass().addAll("text-field", "text-input", "disabledTF");
      }
    }
    dayBox.setDisable(!bool);
    monthBox.setDisable(!bool);
    year.setEditable(bool);
    purseTextField.setEditable(bool);
    cashNecessityCentTextField.setEditable(bool);
    cashNecessityEuroTextField.setEditable(bool);
    if (bool) {
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
   * @return the dayBox
   */
  public ComboBox<String> getDayBox() {
    return dayBox;
  }

  /**
   * @param dayBox the dayBox to set
   */
  public void setDayBox(ComboBox<String> dayBox) {
    this.dayBox = dayBox;
  }

  /**
   * @return the monthBox
   */
  public ComboBox<String> getMonthBox() {
    return monthBox;
  }

  /**
   * @param monthBox the monthBox to set
   */
  public void setMonthBox(ComboBox<String> monthBox) {
    this.monthBox = monthBox;
  }

  /**
   * @return the year
   */
  public TextField getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(TextField year) {
    this.year = year;
  }

  /**
   * @return the billsTextFields
   */
  public TextField[] getBillsTextFields() {
    return billsTextFields;
  }

  /**
   * @param billsTextFields the billsTextFields to set
   */
  public void setBillsTextFields(TextField[] billsTextFields) {
    this.billsTextFields = billsTextFields;
  }

  /**
   * @return the coinTextFields
   */
  public TextField[] getCoinTextFields() {
    return coinTextFields;
  }

  /**
   * @param coinTextFields the coinTextFields to set
   */
  public void setCoinTextFields(TextField[] coinTextFields) {
    this.coinTextFields = coinTextFields;
  }

  /**
   * @return the billsResults
   */
  public Label[] getBillsResults() {
    return billsResults;
  }

  /**
   * @param billsResults the billsResults to set
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
   * 
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
