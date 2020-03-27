package util;

import java.util.Timer;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import tipcalculator.task.MyTimerTask;

/**
 * The EventHandler for KeyEvents for the ComboBox in the Export Stage or CashAssets Stage, where 
 * the Month can be entered.
 * @author Haeldeus
 * @version 1.0
 */
public class MonthComboBoxKeyHandler implements EventHandler<KeyEvent> {

  /**
   * The ComboBox, this Handler is attached to.
   */
  private ComboBox<String> monthBox;
  
  /**
   * The boolean value, if the User has entered multiple Letters on their Keyboard in a short time. 
   * This is used to make selecting Members by entering their name possible.
   */
  private volatile boolean multiple;
  
  /**
   * The current Text, that was entered by the User on their Keyboard. This will be reset to "" 
   * after a short time. Used to make inputs by the Keyboard possible.
   */
  private volatile String text;
  
  /**
   * The Constructor for this Handler. Will set the Fields to the given values.
   * @param monthBox The ComboBox, this Handler will be attached to.
   * @since 1.0
   */
  public MonthComboBoxKeyHandler(ComboBox<String> monthBox) {
    this.monthBox = monthBox;
    this.multiple = false;
  }
  
  @Override
  public void handle(KeyEvent arg0) {
    /*
     * Checks, if the current input is an alphabetical Key.
     */
    if (checkTextKey(arg0)) {
      /*
       * Saves the input in a String variable.
       */
      String input = arg0.getText().toUpperCase();
      /*
       * Checks, if the User has entered multiple Keys in short succession. If yes, the 
       * current Char is added at the end of the current input.
       */
      if (checkMultiple()) {
        input = getText().concat(input);
      }
      /*
       * Starts a new Task to reset the multiple-Field, if no new input was entered after a short 
       * time.
       */
      MyTimerTask timerTask = new MyTimerTask(this, input);
      Timer timer = new Timer(true);
      timer.schedule(timerTask, 0);
      /*
       * Sets the current text to input.
       */
      setText(input);
      
      /*
       * Iterates over the List of Members, that are selectable in the ComboBox.
       */
      for (String month : monthBox.getItems()) {
        /*
         * Sets length to input.length(). If this is greater than the Length of the current String 
         * from the List, length will be set to this instead to prevent errors.
         */
        int length = input.length();
        if (length > month.length()) {
          length = month.length();
        }
        /*
         * Stores the substring from the beginning of the current String from the List to length in 
         * a new variable.
         */
        String c = month.toUpperCase().substring(0, length);
        /*
         * Compares, if the current input equals the saved substring. If yes, this Item will be 
         * selected and the Method will be stopped.
         */
        if (c.equals(input)) {
          monthBox.getSelectionModel().select(month);
          return;
        }
      }
      /*
       * If the User pressed Enter, the next ComboBox will be selected. If this was the last 
       * ComboBox in the Pane, a new Entry will be added at the Bottom.
       */
    } 
  }

  /**
   * Checks, if there were multiple Inputs in the last second. The value for this will be 
   * altered in {@link MyTimerTask}.
   * @return  {@code true}, if there were inputs, {@code false} if not.
   * @since 1.0
   */
  private boolean checkMultiple() {
    return this.multiple;
  }
  
  /**
   * Sets the current {@link #text} to the given String. This String length can be greater than 1, 
   * if there were multiple inputs in succession.
   * @param str The String, text will be set to.
   * @since 1.0
   */
  private void setText(String str) {
    this.text = str;
  }
  
  /**
   * Returns the current Text, that is stored in {@link #text}.
   * @return  The String, that is stored.
   * @since 1.0
   */
  public String getText() {
    return this.text;
  }
  
  /**
   * Sets {@link #multiple} to true to enable longer inputs.
   */
  public void startMult() {
    this.multiple = true;
  }
  
  /**
   * Resets the current input. This will call {@link #setText(String)} with an empty String and 
   * sets {@link #multiple} to false.
   * @since 1.0
   */
  public void reset() {
    this.setText("");
    this.multiple = false;
  }
  
  /**
   * Checks, if the given KeyEvent encodes an alphabetical input.
   * @param event The KeyEvent, that was triggered.
   * @return  {@code true}, if the KeyEvent encodes an alphabetical input, {@code false} else.
   * @since 1.0
   * @see KeyEvent
   */
  private boolean checkTextKey(KeyEvent event) {
    try {
      char input = event.getText().toUpperCase().charAt(0);
      if (input == 'Ä' || input == 'Ö' || input == 'Ü') {
        return true;
      } else if (input < 'A' || input > 'Z') {
        return false;
      }
      return true;
    } catch (StringIndexOutOfBoundsException exc) {
      //If a key was pressed, that isn't alphanumerical
      return false;
    }
  }
}
