package externalregister.listener;

import externalregister.ExternalWindow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextFieldFocusChangedListener implements ChangeListener<Boolean> {

  private TextField tf;
  
  private ExternalWindow primary;
  
  public TextFieldFocusChangedListener(TextField tf, ExternalWindow primary) {
    this.tf = tf;
    this.primary = primary;
  }
  
  @Override
  public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldP, Boolean newP) {
    if (oldP && !newP) {
      /*
       * Replaces all non numerical characters from the Text in the TextField except for '.' 
       * and ','.
       */
      tf.setText(tf.getText().replaceAll("[\\D]", "").trim());
      if (tf.getText().equals("") || tf.getText() == null) {
        tf.setText("0");
      }
      primary.calc();
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
