package cashassets.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class KeyEventHandler implements EventHandler<KeyEvent>{
  
  private TextField enter;
  
  private TextField up;
  
  private TextField down;
  
  private TextField left;
  
  private TextField right;
  
  /**
   * @param enter
   * @param up
   * @param down
   * @param left
   * @param right
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
