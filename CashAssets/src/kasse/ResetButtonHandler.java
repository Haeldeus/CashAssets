package kasse;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ResetButtonHandler implements EventHandler<MouseEvent> {

  private ComponentStorer cs;
  
  public ResetButtonHandler(ComponentStorer cs) {
    this.cs = cs;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    
    for (TextField tf : cs.getBillsTextFields()) {
      tf.setText("0");
    }
    
    for (TextField tf : cs.getCoinTextFields()) {
      tf.setText("0");
    }

    cs.getCashNecessityCentTextField().setText("0");
    cs.getCashNecessityEuroTextField().setText("0");
    cs.getPurseTextField().setText("0");
  }

}
