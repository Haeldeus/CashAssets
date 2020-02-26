package tipcalculator.handler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;

public class AddToTipHandler implements EventHandler<MouseEvent> {

  private final TipWindow primary;
  
  private GridPane gridPane;
  
  public AddToTipHandler(TipWindow primary, GridPane grid) {
    this.primary = primary;
    this.gridPane = grid;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    System.out.println("Has to be handled");
  }
}
