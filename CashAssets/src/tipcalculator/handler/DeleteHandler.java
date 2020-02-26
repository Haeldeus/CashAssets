package tipcalculator.handler;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class DeleteHandler implements EventHandler<MouseEvent> {

  private int index;
  
  private ArrayList<String> staff;
  
  private GridPane staffMemberPane;
  
  private StaffHandler primary;
  
  /**
   * The Constructor for this Handler.
   * @param primary The StaffHandler, this Handler was called from.
   * @param index The Index of the Delete Button.
   * @param staff All Staff Members in an ArrayList of Strings.
   * @param staffMemberPane The GridPane, the Delete Button was added to.
   * @since 1.0
   */
  public DeleteHandler(StaffHandler primary, int index, ArrayList<String> staff, 
      GridPane staffMemberPane) {
    this.primary = primary;
    this.index = index;
    this.staff = staff;
    this.staffMemberPane = staffMemberPane;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    staff.remove(index);
    staffMemberPane.getChildren().remove(2 * index);
    staffMemberPane.getChildren().remove(2 * index);
    for (int i = 0; i < staff.size(); i++) {
      staffMemberPane.getChildren().get(2 * i + 1).removeEventHandler(
          MouseEvent.MOUSE_CLICKED, staffMemberPane.getChildren().get(2 * i + 1)
          .getOnMouseClicked());
      staffMemberPane.getChildren().get(2 * i + 1)
          .setOnMouseClicked(new DeleteHandler(primary, i, staff, staffMemberPane));
    }
    primary.updateStaff(staff);
  }
}
