package cashassets.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * The Handler for KeyEvents in the TextFields of this Stage.
 * @author Haeldeus
 * @version 1.0
 */
public class KeyEventHandler implements EventHandler<KeyEvent> {
  
  /**
   * The TextField, that will gain Focus, when the Enter Key was pressed.
   */
  private TextField enter;
  
  /**
   * The TextField, that will gain Focus, when the KeyCombination for Ctrl + Up was pressed.
   */
  private TextField up;
  
  /**
   * The TextField, that will gain Focus, when the KeyCombination for Ctrl + Down was pressed.
   */
  private TextField down;
  
  /**
   * The TextField, that will gain Focus, when the KeyCombination for Ctrl + Left was pressed.
   */
  private TextField left;
  
  /**
   * The TextField, that will gain Focus, when the KeyCombination for Ctrl + Right was pressed.
   */
  private TextField right;
  
  /**
   * The Constructor for this Handler. Sets all Fields to their belonging values.
   * @param enter The TextField, that will gain Focus, when the Enter Key was pressed. Might be 
   *     {@code null}.
   * @param up The TextField, that will gain Focus, when the KeyCombination for Ctrl + Up was 
   *     pressed. Might be {@code null}.
   * @param down  The TextField, that will gain Focus, when the KeyCombination for Ctrl + Down was 
   *     pressed. Might be {@code null}.
   * @param left  The TextField, that will gain Focus, when the KeyCombination for Ctrl + Left was 
   *     pressed. Might be {@code null}.
   * @param right The TextField, that will gain Focus, when the KeyCombination for Ctrl + Right was 
   *     pressed. Might be {@code null}.
   * @since 1.0
   */
  public KeyEventHandler(TextField enter, TextField up, TextField down, TextField left, 
      TextField right) {
    this.enter = enter;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }
  
  @Override
  public void handle(KeyEvent event) {
    /*
     * The KeyCombinations for switching the TextFields with Control + Arrow
     */
    final KeyCombination kcUp = new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_DOWN);
    final KeyCombination kcDown = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);
    final KeyCombination kcRight = new KeyCodeCombination(KeyCode.RIGHT, 
        KeyCombination.CONTROL_DOWN);
    final KeyCombination kcLeft = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);
    
    /*
     * Sets the Configuration for this Handler, so the corresponding field will be focus, when the 
     * Key(s) were pressed.
     */
    if (event.getCode() == KeyCode.ENTER && enter != null) {
      enter.requestFocus();
    } else if (kcUp.match(event) && up != null) {
      up.requestFocus();
    } else if (kcDown.match(event) && down != null) {
      down.requestFocus();
    } else if (kcLeft.match(event) && left != null) {
      left.requestFocus();
    } else if (kcRight.match(event) && right != null) {
      right.requestFocus();
    }
  }

}
