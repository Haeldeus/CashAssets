package externalregister.listener;

import externalregister.ExternalWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TextFieldTextChangedListener implements ChangeListener<String> {

  private ExternalWindow primary;
  
  public TextFieldTextChangedListener(ExternalWindow primary) {
    this.primary = primary;
  }
  
  @Override
  public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    primary.calc();
  }

}
