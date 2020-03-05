package tipcalculator.handler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DeleteFromTipHandler implements EventHandler<MouseEvent> {

  private int rowIndex;
  
  public DeleteFromTipHandler() {
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    System.out.println("Handle this");
  }

}
