package tipcalculator.listener;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;

/**
 * The Listener, that listens to focus Changes in all TextFields of the TipCalculator.
 * @author Haeldeus
 * @version 1.0
 */
public class TextFieldFocusChangeListener implements ChangeListener<Boolean> {

  /**
   * The TextField, this Listener was added to.
   */
  private TextField textField;
  
  /**
   * The boolean value, if the calculation should be done with exact values or if the hours alone 
   * are sufficient.
   */
  private boolean exact;
  
  /**
   * The GridPane where the TextFields for the Staff Members hours were added to.
   */
  private GridPane gridPane;
  
  /**
   * The TipWindow, that contains the GridPane and all TextFields, this Listener was added to.
   */
  private TipWindow primary;
  
  /**
   * The boolean value, if this Listener was added to the total TipSum TextField.
   */
  private boolean isTotal;
  
  /**
   * The Constructor for this Listener, where all needed variables will be passed to the newly 
   * created Object.
   * @param textField The TextField, where the amount of Hours can be stored in and this Listener 
   *     is listening to.
   * @param exact The boolean value, if the calculation should be exact or if hours alone are 
   *     sufficient.
   * @param grid  The GridPane, where the TextFields for the Staff Members hours were added to.
   * @param primary The TipWindow, the TextField is a component of.
   * @param isTotal The boolean value, if this Listener is listening to the total TipSum TextField.
   * @since 1.0   
   */
  public TextFieldFocusChangeListener(TextField textField, boolean exact, GridPane grid, 
      TipWindow primary, boolean isTotal) {
    this.textField = textField;
    this.exact = exact;
    this.gridPane = grid;
    this.primary = primary;
    this.isTotal = isTotal;
  }

  @Override
  public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
      Boolean newProperty) {
    /*
     * Replaces all non numerical characters from the Text in the TextField except for '.' and ','.
     */
    String str = textField.getText();
    str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
    str = str.replaceAll(",", "\\.");
    /*
     * If the hours are sufficient for the calculation and this Listener isn't listening to the 
     * TipSum TextField, all decimal places will be deleted from the Text.
     */
    if (!exact && !isTotal) {
      if (str.indexOf('.') != - 1) {
        str = str.substring(0, str.indexOf('.'));
      }
    }
    /*
     * Formats the TextField to show ##,## in case it is the total Tip Sum TextField.
     */
    if (isTotal && str.length() > 0) {
      if (str.charAt(str.length() - 1) == '.') {
        str = str.concat("00");
        /*
         * If this condition is met, the input was ##,#
         */
      } else if (str.length() - str.indexOf(".") == 2) {
        str = str.concat("0");
      }
    }
    /*
     * Sets the Text of the TextField to the adjusted Text. Since german Numbers are displayed 
     * as ##,## instead of ##.## the '.' in the Display will be replaced by a ','.
     */
    textField.setText(str.replace('.', ','));
    
    /*
     * Creates a new BigDecimal, where the total amount of hours worked will be stored in.
     */
    BigDecimal total = new BigDecimal("0.00");
    /*
     * Iterates over all TextFields in the GridPane to calculate the total amount of hours worked.
     */
    for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
      /*
       * Since the TextFields are part of a GridPane inside the GridPane, this step is necessary 
       * to get the TextField.
       */
      GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
      TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
      /*
       * In case there was a formation Error, this step is surrounded with a try/catch Block.
       */
      try {
        /*
         * If there is an input in the TextField, this input will be added to total.
         */
        if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
          total = total.add(new BigDecimal(tmpTf.getText().replace(',', '.')));
        }
      } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
        System.err.println("Skipping this Error....");
      }
    }
    
    /*
     * Sets the Scale of the BigDecimal to 2, since there is no need for a more precise calulation.
     */
    total = total.setScale(2, RoundingMode.DOWN);
    /*
     * Updates the Text of the Label, that displays the total amount of Hours.
     */
    primary.getLbHoursTotal().setText(total.toString().replace('.', ',') + "h");
    
    /*
     * If there was a total Tip Sum entered, this condition is met and the calculation will use 
     * that value.
     */
    if (primary.getTfTip().getText() != null && !primary.getTfTip().getText().equals("")) {
      /*
       * Removes all non numerical characters from the total Tip Sum TextField except for '.' 
       * and ','.
       */
      String txt = primary.getTfTip().getText();
      txt = txt.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
      txt = txt.replaceAll(",", "\\.");
      /*
       * Creates a new BigDecimal with the Tip Sum as Value and sets it's scale to 2.
       */
      BigDecimal totalTip = new BigDecimal(txt);
      totalTip = totalTip.setScale(2, RoundingMode.DOWN);
      /*
       * Sets the Text of the TextField to the adjusted Text. Since german Numbers are displayed 
       * as ##,## instead of ##.## the '.' in the Display will be replaced by a ','.
       */
      primary.getTfTip().setText(totalTip.toString().replace('.', ','));
      
      /*
       * Creates a new MathContext for precise calculations.
       */
      MathContext mc = new MathContext(10);
      
      /*
       * Creates a new BigDecimal, that stores the amount of tip-money, everyone gets per hour of 
       * work.
       */
      BigDecimal tipPerHour;
      /*
       * Checks if the total amount of hours worked is 0. If yes, a default value of 0€ per Hours 
       * worked is set. Else, the result of totalTipSum / totalHoursOfWork.
       */
      if (total.compareTo(new BigDecimal("0.00")) != 0) {
        tipPerHour = totalTip.divide(total, mc);
      } else {
        tipPerHour = new BigDecimal("0.00");
      }
      /*
       * Iterates over all TextFields in the GridPane to calculate their Share of the Tip for each 
       * Staff Member.
       */
      for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
        /*
         * Since the TextFields are part of a GridPane inside the GridPane, this step is necessary 
         * to get the TextField.
         */
        GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
        TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
        /*
         * In case there was a formation Error, this step is surrounded with a try/catch Block.
         */
        try {
          /*
           * If there is Text in the TextField, this Text will be used for calculation.
           */
          if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
            /*
             * Fetches the Label, in which the Share of the Staff Member can be displayed in.
             */
            Label lbTmp = (Label)smallGrid.getChildren().get(2);
            /*
             * Creates a BigDecimal, which calculates to tip for this Staff Member.
             */
            BigDecimal tip = new BigDecimal("0.00");
            /*
             * Multiplies the Hours, this Staff Member has worked with the Tip per Hour value to 
             * calculate their Share of the Tip. Also, scales this result down to 2 decimal places.
             */
            tip = tipPerHour.multiply(new BigDecimal(tmpTf.getText()));
            tip = tip.setScale(2, RoundingMode.HALF_DOWN);
            /*
             * Displays the result in the Label.
             */
            lbTmp.setText("= " + tip.toString().replaceAll("\\.", ",") + "€");
          } else {
            /*
             * Fetches the Label, in which the Share of the Staff Member can be displayed in.
             */
            Label lbTmp = (Label)smallGrid.getChildren().get(2);
            /*
             * Since there was no input, the default value of 0,00€ will be displayed in the Label.
             */
            lbTmp.setText("= 0.00€");
          }
        } catch (NumberFormatException nfe) {
          nfe.printStackTrace();
          System.err.println("Skipping this Error....");
        }
      }
      /*
       * If there was no Input in TfTip, the Labels will be reset to a default value.
       */
    } else {
      for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
        /*
         * Fetches the Label, in which the Share of the Staff Member can be displayed in.
         */
        GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
        Label lbTmp = (Label)smallGrid.getChildren().get(2);
        /*
         * Since there was no input, the default value of 0,00€ will be displayed in the Label.
         */
        lbTmp.setText("= 0.00€");
      }
    }
    
    /*
     * Selects all Text, if this TextField gains Focus.
     */
    if (!oldProperty && newProperty) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          textField.selectAll();
        }              
      });
    }
  }  

}
