package cashassets.listener;

import cashassets.CashAssetsWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * The Listener, that listens to focus changes for all TextFields.
 * @author Haeldeus
 * @version 1.0
 */
public class TextFieldFocusChangedListener implements ChangeListener<Boolean> {

  /**
   * The TextField, this Listener was added to.
   */
  private TextField tf;
  
  /**
   * The NewCashAssetsWindow, the TextField this Listener was added to is part of.
   */
  private CashAssetsWindow primary;
  
  /**
   * Determines, if a separator like ',' or '.' is allowed in the TextField.
   */
  private boolean allowSeparator;
  
  /**
   * The Constructor for this Listener.
   * @param tf  The TextField, this Listener was added to.
   * @param primary The NewCashAssetsWindow, the TextField is part of.
   * @param allowSeparator Determines, if a ',' or '.' is allowed in the TextField.
   * @since 1.0
   */
  public TextFieldFocusChangedListener(TextField tf, CashAssetsWindow primary, 
      boolean allowSeparator) {
    this.tf = tf;
    this.primary = primary;
    this.allowSeparator = allowSeparator;
  }
  
  @Override
  public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
    /*
     * Calculates the current input, if the Focus is lost.
     */
    if (oldP && !newP) {
      
      /*
       * Replaces all non numerical characters from the Text in the TextField except for '.' 
       * and ',', in case they are allowed.
       */
      if (!allowSeparator) {
        tf.setText(tf.getText().replaceAll("[\\D]", "").trim());
      } else {
        tf.setText(tf.getText().replaceAll("[\\D&&[^,]&&[^\\.]]","").trim());
        BigDecimal tmp = new BigDecimal(tf.getText().replace(',', '.'));
        tmp = tmp.setScale(2, RoundingMode.DOWN);
        tf.setText(tmp.toString());
      }
      
      if (tf.getText().equals("") || tf.getText() == null) {
        tf.setText("0");
      }
      /*
       * Calculates the current input.
       */
      primary.calc();
      /*
       * Selects the Text in this TextField, if the Focus is gained.
       */
    } else if (!oldP && newP) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          tf.selectAll();
        }        
      });
    }
  }

}