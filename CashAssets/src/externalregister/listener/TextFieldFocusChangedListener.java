package externalregister.listener;

import externalregister.ExternalWindow;
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
   * The ExternalWindow, the TextField this Listener was added to is part of.
   */
  private ExternalWindow primary;
  
  /**
   * The Constructor for this Listener.
   * @param tf  The TextField, this Listener was added to.
   * @param primary The ExternalWindow, the TextField is part of.
   * @since 1.0
   */
  public TextFieldFocusChangedListener(TextField tf, ExternalWindow primary) {
    this.tf = tf;
    this.primary = primary;
  }
  
  @Override
  public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
    /*
     * Calculates the current input, if the Focus is lost.
     */
    if (oldP && !newP) {
      /*
       * Replaces all non numerical characters from the Text in the TextField except for '.' 
       * and ','.
       */
      tf.setText(tf.getText().replaceAll("[\\D]", "").trim());
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
