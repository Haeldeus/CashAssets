package monthlybalance.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import monthlybalance.MBalanceWindow;

/**
 * The Listener, that listens to all Text changes for all TextFields.
 * @author Haeldeus
 * @version 1.0
 */
public class TextFieldTextChangedListener implements ChangeListener<String> {

  /**
   * The MBalanceWindow, the TextField this Listener was added to is part of.
   */
  private MBalanceWindow primary;
  
  /**
   * The Constructor for this Listener.
   * @param primary The MBalanceWindow, the TextField this Listener was added to is part of.
   * @since 1.0
   */
  public TextFieldTextChangedListener(MBalanceWindow primary) {
    this.primary = primary;
  }
  
  @Override
  public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    primary.calc();
  }

}
