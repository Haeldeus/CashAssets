package kasse.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import kasse.ComponentStorer;

/**
 * A Class which Objects will handle the Way, the Reset Button behaves whenever it is pressed.
 * This Handler will reset all modified Components in the Application to their original value.
 * @author Haeldeus
 * @version 1.0
 */
public class ResetButtonHandler implements EventHandler<MouseEvent> {
  
  /**
   * The ComponentStorer, that stores all Components, that can be altered by this Button.
   */
  private ComponentStorer cs;
  
  /**
   * Creates a new Handler with the given ComponentStorer. The ComponentStorer has to be the 
   * Storer, that contains all modifiable Components of this Application.
   * @param cs  The ComponentStorer, where all modifiable Components are stored.
   * @since 1.0
   * @throws NullPointerException If a Component wasn't stored in the given Storer or the Storer 
   *     is {@code null}.
   */
  public ResetButtonHandler(ComponentStorer cs) {
    this.cs = cs;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    /*
     * Sets the Text of all editable TextFields to 0.
     */
    for (TextField tf : cs.getBillsTextFields()) {
      tf.setText("0");
    }
    
    for (TextField tf : cs.getCoinTextFields()) {
      tf.setText("0");
    }

    cs.getCashNecessityEuroTextField().setText("0");
    cs.getPurseTextField().setText("0");
    
    for (Label l : cs.getCoinResults()) {
      if (l != null) {
        l.setText("=");
      }
    }
    
    for (Label l : cs.getBillsResults()) {
      l.setText("=");
    }
  }

}
