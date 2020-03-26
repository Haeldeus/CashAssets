package tipcalculator.task;

import java.util.TimerTask;

import tipcalculator.handler.ComboBoxKeyHandler;
import tipcalculator.handler.ExportDayComboBoxKeyHandler;

/**
 * A Task, that is used to determine if the User enters multiple Letters while the ComboBox is 
 * selected to enable searching by names to select the Item that matches the Input.
 * @author Haeldeus
 * @version 1.0
 */
public class MyTimerTask extends TimerTask {

  /**
   * The ComboBoxKeyHandler, that started this thread.
   */
  private ComboBoxKeyHandler primary;
  
  /**
   * The ExportDayComboBoxKeyHandler, that started this Thread.
   */
  private ExportDayComboBoxKeyHandler secondary;
  
  /**
   * The Text, that was entered by the User when this Task was started.
   */
  private String text;
  
  /**
   * The Constructor for this Task. This will set all Fields to the given values.
   * @param primary The ComboBoxKeyHandler, that started this Thread.
   * @param str The Text, that was entered by the User when this Task was started.
   * @since 1.0
   */
  public MyTimerTask(ComboBoxKeyHandler primary, String str) {
    this.primary = primary;
    this.text = str;
  }
  
  /**
   * The Constructor for this Task. This will set all Fields to the given values.
   * @param primary The ExportDayComboBoxKeyHandler, that started this Thread.
   * @param str The Text, that was entered by the User when this Task was started.
   * @since 1.0
   */
  public MyTimerTask(ExportDayComboBoxKeyHandler primary, String str) {
    this.secondary = primary;
    this.text = str;
  }
  
  @Override
  public void run() {
    /*
     * If this Task was started from a ComboBoxKeyHandler, primary will be set to a value and this 
     * Part is used to run the Task.
     */
    if (primary != null) {
      try {
        /*
         * Sets primary.multiple to true, so the Application knows, that the User is able to enter 
         * multiple Letters to search for this Text in the Items of the given ComboBox.
         */
        primary.startMult();
        /*
         * The Timeout, after which the input is reset to "", if no new Keys were pressed.
         */
        Thread.sleep(1000);
        /*
         * Checks, if the Text of primary is still the same as it was, when this Thread started. 
         * If yes, there was no new Input and the search-Term is reset. If no, this Task finishes 
         * without changes.
         */
        if (this.text.equals(primary.getText())) {
          primary.reset();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      /*
       * If this Task was started from a ExportDayComboBoxKeyHandler, primary will be set to a 
       * value while primary is null. This Part is used to run the Task.
       */
    } else if (secondary != null) {
      try {
        /*
         * Sets primary.multiple to true, so the Application knows, that the User is able to enter 
         * multiple Letters to search for this Text in the Items of the given ComboBox.
         */
        secondary.startMult();
        /*
         * The Timeout, after which the input is reset to "", if no new Keys were pressed.
         */
        Thread.sleep(1000);
        /*
         * Checks, if the Text of primary is still the same as it was, when this Thread started. 
         * If yes, there was no new Input and the search-Term is reset. If no, this Task finishes 
         * without changes.
         */
        if (this.text.equals(secondary.getText())) {
          secondary.reset();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
