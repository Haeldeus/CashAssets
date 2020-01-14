package kasse;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class EditButtonHandler implements EventHandler<MouseEvent> {

  private ComponentStorer cs;
  
  public EditButtonHandler(ComponentStorer cs) {
    this.cs = cs;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    cs.setFieldsEditable(true);
    cs.getExportButton().setDisable(true);
    cs.getResetButton().setDisable(false);
    for (Label l : cs.getAlterableLabels()) {
      l.setText("0,00€");
    }
  }

}
