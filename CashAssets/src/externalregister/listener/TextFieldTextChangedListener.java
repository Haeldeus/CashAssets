package externalregister.listener;

import externalregister.ExternalWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * The Listener, that listens to all Text changes for all TextFields.
 * @author Haeldeus
 * @version 1.0
 */
public class TextFieldTextChangedListener implements ChangeListener<String> {

  /**
   * The ExternalWindow, the TextField this Listener was added to is part of.
   */
  private ExternalWindow primary;
  
  /**
   * The Constructor for this Listener.
   * @param primary The ExternalWindow, the TextField this Listener was added to is part of.
   * @since 1.0
   */
  public TextFieldTextChangedListener(ExternalWindow primary) {
    this.primary = primary;
  }
  
  @Override
  public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    primary.calc();
  }

}
