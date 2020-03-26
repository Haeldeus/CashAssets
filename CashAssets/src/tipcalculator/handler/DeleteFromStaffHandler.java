package tipcalculator.handler;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The EventHandler, that handles the MouseEvents for all Delete-Buttons in the Staff Member 
 * Options Stage.
 * @author Haeldeus
 * @version 1.0
 */
public class DeleteFromStaffHandler implements EventHandler<MouseEvent> {

  /**
   * The index, where in the List the Button is, which has this Handler-Object attached to it.
   */
  private int index;
  
  /**
   * The ArrayList of Strings, that stores all Staff Members.
   */
  private ArrayList<String> staff;
  
  /**
   * The GridPane, where the Button with this Handler-Object attached was added to.
   */
  private GridPane staffMemberPane;
  
  /**
   * The StaffHandler-Object, which is the parent Object. This Object is the Stage that contains 
   * {@link #staffMemberPane} and every other content of that Pane.
   */
  private StaffHandler primary;
  
  /**
   * The Constructor for this Handler.
   * @param primary The StaffHandler, this Handler was called from.
   * @param index The Index of the Delete Button.
   * @param staff All Staff Members in an ArrayList of Strings.
   * @param staffMemberPane The GridPane, the Delete Button was added to.
   * @since 1.0
   */
  public DeleteFromStaffHandler(StaffHandler primary, int index, ArrayList<String> staff, 
      GridPane staffMemberPane) {
    this.primary = primary;
    this.index = index;
    this.staff = staff;
    this.staffMemberPane = staffMemberPane;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    /*
     * Removes the Object from the staff-List with the given Index. This is the Object, that is 
     * on the left side of the Button with this Handler.
     */
    staff.remove(index);
    /*
     * Removes the Row with the stored index from the Pane.
     */
    staffMemberPane.getChildren().remove(2 * index);
    staffMemberPane.getChildren().remove(2 * index);
    /*
     * Iterates over the Staff-Member List to assign each Delete-Button a new Handler with their 
     * updated indices.
     */
    for (int i = 0; i < staff.size(); i++) {
      /*
       * Removes the onMouseClicked-Handler from the Button.
       */
      staffMemberPane.getChildren().get(2 * i + 1).removeEventHandler(
          MouseEvent.MOUSE_CLICKED, staffMemberPane.getChildren().get(2 * i + 1)
          .getOnMouseClicked());
      /*
       * Adds a new Handler with 'i' as the updated index.
       */
      staffMemberPane.getChildren().get(2 * i + 1)
          .setOnMouseClicked(new DeleteFromStaffHandler(primary, i, staff, staffMemberPane));
    }
    /*
     * Updates the Staff-List in the StaffHandler-Window.
     */
    primary.updateStaff(staff);
    
    /*
     * Deletes and re-adds all children from the Pane, so the Insets, that will be left after 
     * deleting a row, will be deleted as well.
     */
    ObservableList<Node> tmpChildren = staffMemberPane.getChildren();
    ArrayList<Node> children = new ArrayList<Node>();
    for (Node n : tmpChildren) {
      children.add(n);
    }
    staffMemberPane.getChildren().clear();
    for (int i = 0; i < children.size(); i++) {
      staffMemberPane.add(children.get(i), i % 2, i / 2);
    }
  }
}
