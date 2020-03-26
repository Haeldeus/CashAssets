package tipcalculator.task;

import java.util.TimerTask;

import tipcalculator.handler.ComboBoxKeyHandler;

public class MyTimerTask extends TimerTask {

  private ComboBoxKeyHandler primary;
  
  private String text;
  
  public MyTimerTask(ComboBoxKeyHandler primary, String str) {
    this.primary = primary;
    this.text = str;
  }
  
  @Override
  public void run() {
    try {
      primary.startMult();
      Thread.sleep(1000);
      if (this.text.equals(primary.getText())) {
        primary.reset();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
