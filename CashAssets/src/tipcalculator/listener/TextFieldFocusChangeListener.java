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
 * @author Haeldeus
 * @version 1.0
 */
public class TextFieldFocusChangeListener implements ChangeListener<Boolean> {

  private TextField tfHours;
  
  private boolean exact;
  
  private GridPane gridPane;
  
  private TipWindow primary;
  
  private boolean isTotal;
  
  /**
   * The Constructor for this Listener, where all needed variables will be passed to the newly 
   * created Object.
   * @param tfHours The TextField, where the amount of Hours can be stored in and this Listener 
   *     is listening to.
   * @param exact The boolean value, if the calculation should be exact or if hours alone are 
   *     sufficient
   * @param grid  The GridPane, where the TextField was added to.
   * @param primary The TipWindow, the TextField is a component of.
   * @param isTotal The boolean value, if this Listener is listening to the total TipSum TextField.
   * @since 1.0   
   */
  public TextFieldFocusChangeListener(TextField tfHours, boolean exact, GridPane grid, 
      TipWindow primary, boolean isTotal) {
    this.tfHours = tfHours;
    this.exact = exact;
    this.gridPane = grid;
    this.primary = primary;
    this.isTotal = isTotal;
  }

  @Override
  public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
      Boolean newProperty) {
    String str = tfHours.getText();
    str = str.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
    str = str.replaceAll(",", "\\.");
    if (!exact && !isTotal) {
      if (str.indexOf('.') != - 1) {
        str = str.substring(0, str.indexOf('.'));
      }
    }
    tfHours.setText(str.replace('.', ','));
    BigDecimal total = new BigDecimal("0.00");
    for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
      GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
      TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
      try {
        if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
          total = total.add(new BigDecimal(tmpTf.getText().replace(',', '.')));
        }
      } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
        System.err.println("Skipping this Error....");
      }
    }
    BigDecimal totalHours = new BigDecimal("0.00");
    totalHours = totalHours.add(total);
    
    totalHours = totalHours.setScale(2, RoundingMode.DOWN);
    primary.getLbHoursTotal().setText(totalHours.toString().replace('.', ',') + "h");
    
    if (primary.getTfTip().getText() != null && !primary.getTfTip().getText().equals("")) {
      String txt = primary.getTfTip().getText();
      txt = txt.replaceAll("[\\D&&[^,]&&[^\\.]]", "");
      txt = txt.replaceAll(",", "\\.");
      BigDecimal totalTip = new BigDecimal(txt);
      totalTip = totalTip.setScale(2, RoundingMode.DOWN);
      
      MathContext mc = new MathContext(10);
      
      BigDecimal tipPerHour;
      if (totalHours.compareTo(new BigDecimal("0.00")) != 0) {
        tipPerHour = totalTip.divide(totalHours, mc);
      } else {
        tipPerHour = new BigDecimal("0.00");
      }
      for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
        GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
        TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
        try {
          if (tmpTf.getText() != null && !tmpTf.getText().equals("")) {
            Label lbTmp = (Label)smallGrid.getChildren().get(2);
            BigDecimal tip = new BigDecimal("0.00");
            tip = tipPerHour.multiply(new BigDecimal(tmpTf.getText()));
            tip = tip.setScale(2, RoundingMode.HALF_DOWN);
            lbTmp.setText("= " + tip.toString() + "€");
          } else {
            Label lbTmp = (Label)smallGrid.getChildren().get(2);
            lbTmp.setText("= 0.00€");
          }
        } catch (NumberFormatException nfe) {
          nfe.printStackTrace();
          System.err.println("Skipping this Error....");
        }
      }
    }
    
    if (!oldProperty && newProperty) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          tfHours.selectAll();
        }              
      });
    }
  }  

}
