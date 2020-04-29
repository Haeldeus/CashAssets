package oldca.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import oldca.ComponentStorer;

/**
 * A Class which Objects will handle the Way, the Edit Button behaves whenever it is pressed.
 * This Handler will make all Fields Editable again and resets the Labels of the Application, 
 * that displayed the result of (coinageFactor * coinageAmount).
 * @author Haeldeus
 * @version 1.0
 */
public class EditButtonHandler implements EventHandler<MouseEvent> {

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
  public EditButtonHandler(ComponentStorer cs) {
    this.cs = cs;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    cs.getCoinDifferenceLabel().getStyleClass().clear();
    cs.getCoinDifferenceLabel().getStyleClass().addAll("label");
    cs.getTipSumLabel().getStyleClass().clear();
    cs.getTipSumLabel().getStyleClass().addAll("label");
    cs.setFieldsEditable(true);
    cs.getExportButton().setDisable(true);
    cs.getResetButton().setDisable(false);
    for (Label l : cs.getAlterableLabels()) {
      l.setText("0,00€");
    }
  }

}
