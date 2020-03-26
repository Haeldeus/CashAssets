package tipcalculator.handler;

import java.util.Timer;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.task.MyTimerTask;

/**
 * The KeyHandler for the ComboBox in the TipWindow, where the User can enter Staff Members.
 * @author Haeldeus
 * @version 1.0
 */
public class ComboBoxKeyHandler implements EventHandler<KeyEvent> {

  /**
   * The ComboBox, this Handler is attached to.
   */
  private ComboBox<String> staffMember;
  
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
   * The GridPane, the ComboBox was added to. Used to change the ComboBox Focus when the User 
   * presed Enter.
   */
  private GridPane gridPane;
  
  /**
   * The Constructor for this Handler. Will set the Fields to the given values.
   * @param staffMember The ComboBox, this Handler will be attached to.
   * @param gridPane  The GridPane, the ComboBox was added to.
   * @since 1.0
   */
  public ComboBoxKeyHandler(ComboBox<String> staffMember, GridPane gridPane) {
    this.staffMember = staffMember;
    this.multiple = false;
    this.gridPane = gridPane;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handle(KeyEvent arg0) {
    /*
     * Checks, if the current input is a alphabetical Key.
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
      for (String member : staffMember.getItems()) {
        /*
         * Sets length to input.length(). If this is greater than the Length of the current String 
         * from the List, length will be set to this instead to prevent errors.
         */
        int length = input.length();
        if (length > member.length()) {
          length = member.length();
        }
        /*
         * Stores the substring from the beginning of the current String from the List to length in 
         * a new variable.
         */
        String c = member.toUpperCase().substring(0, length);
        /*
         * Compares, if the current input equals the saved substring. If yes, this Item will be 
         * selected and the Method will be stopped.
         */
        if (c.equals(input)) {
          staffMember.getSelectionModel().select(member);
          return;
        }
      }
      /*
       * If the User pressed Enter, the next ComboBox will be selected. If this was the last 
       * ComboBox in the Pane, a new Entry will be added at the Bottom.
       */
    } else if (arg0.getCode() == KeyCode.ENTER) {
      /*
       * Iterates over the List of ComboBoxes to get the index, where this ComboBox is stored at 
       * in the List.
       */
      for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
        ComboBox<String> tmpBox = (ComboBox<String>)gridPane.getChildren().get(i);
        /*
         * Compares the ComboBox, that this Handler is attached to to the ComboBox, that is stored 
         * at the current index.
         */
        if (tmpBox == staffMember) {
          /*
           * If i + 4 is greater than getChildren.size(), a new Line has to be created.
           */
          if (i + 4 >= gridPane.getChildren().size()) {
            /*
             * Saves the add Button, that is at the Bottom of the Pane in a new variable.
             */
            Button tmp = (Button)gridPane.getChildren().get(gridPane.getChildren().size() - 1);
            /*
             * Fires an event, that clicks the Add-Button.
             */
            tmp.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, 
                false, false, false, true, false, false, false, false, false, null));
          }
          /*
           * Selects the Box below the ComboBox this Handler was attached to and requests Focus for 
           * it.
           */
          tmpBox = (ComboBox<String>)gridPane.getChildren().get(i + 3);
          tmpBox.requestFocus();
        }
      }
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
      if ((input < 'A' || input > 'Z')) {
        return false;
      }
      return true;
    } catch (StringIndexOutOfBoundsException exc) {
      //If a key was pressed, that isn't alphanumerical
      return false;
    }
  }
  
}