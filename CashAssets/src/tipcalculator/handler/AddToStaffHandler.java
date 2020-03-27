package tipcalculator.handler;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The Handler, that handles Event for the Add-Button in the Staff Options Scene.
 * @author Haeldeus
 * @version 1.0
 */
public class AddToStaffHandler implements EventHandler<MouseEvent> {

  /**
   * The GridPane, the new Line will be added to.
   */
  private GridPane staffMemberPane;
  
  /**
   * The Scene, from where this Handler was called.
   */
  private StaffHandler primary;
  
  /**
   * The ScrollPane, that contains the GridPane. Used to automatically scroll to the 
   * Bottom when a new Line was added.
   */
  private ScrollPane sp;
  
  /**
   * The Constructor for this Handler. Sets all Fields to the given values.
   * @param primary The Scene, from where this Handler was called.
   * @param staffMemberPane The GridPane, the new Line will be added to.
   * @param sp  The ScrollPane, that contains {@link #staffMemberPane}.
   * @since 1.0
   */
  public AddToStaffHandler(StaffHandler primary, GridPane staffMemberPane, ScrollPane sp) {
    this.primary = primary;
    this.staffMemberPane = staffMemberPane;
    this.sp = sp;
  }
  
  @Override
  public void handle(MouseEvent event) {
    /*
     * Removes the Add-Button from it's current position in the Grid.
     */
    int size = staffMemberPane.getChildren().size();
    staffMemberPane.getChildren().remove(size - 1);
    
    /*
     * Creates a new TextField with it's Listeners.
     */
    TextField tf = new TextField("Infos im Tooltip");
    tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
          Boolean newProperty) {
        /*
         * Checks, if the TextField has lost the Focus.
         */
        if (!newProperty) {
          /*
           * Removes this TextField, in case it has lost it's focus.
           */
          int size = staffMemberPane.getChildren().size();
          staffMemberPane.getChildren().remove(size - 1);
          /*
           * Adds a new Add-Button at the position, this TextField was before.
           */
          Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
          ImageView imageview = new ImageView(img);
          Button add = new Button("", imageview);
          add.setOnMouseClicked(new AddToStaffHandler(primary, staffMemberPane, sp));
          staffMemberPane.add(add, 0, size / 2);
        }
        /*
         * Scrolls to the Bottom of the ScrollPane.
         */
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            sp.setVvalue(sp.getVmax());
          }          
        });
      }      
    });
    tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        /*
         * If the Enter-Key was pressed, the current text in the TextField will be added to the 
         * Staff Member-List.
         */
        if (event.getCode() == KeyCode.ENTER) {
          /*
           * Removes the TextField from the GridPane.
           */
          staffMemberPane.getChildren().remove(staffMemberPane.getChildren().size() - 1);
          /*
           * Updates the Staff-List in the parent StaffHandler.
           */
          ArrayList<String> staff = primary.getStaff();
          staff.add(tf.getText());
          primary.updateStaff(staff);
          /*
           * Creates a new Label with the Text, that was entered in the TextField and adds it to 
           * the Pane.
           */
          Label l = new Label(tf.getText());
          staffMemberPane.add(l, 0, staff.size() - 1);
          /*
           * Adds a new Delete Button after the newly created Label to be able to delete this entry.
           */
          Image image = new Image(getClass().getResourceAsStream("/res/delete.png"));
          ImageView imageview = new ImageView(image);
          Button del = new Button("", imageview);
          del.setOnMouseClicked(new DeleteFromStaffHandler(primary, staff.size() - 1, staff, 
              staffMemberPane));
          staffMemberPane.add(del, 1, staff.size() - 1);
          
          /*
           * Adds a new Add-Button at the Bottom of the List to add more Members if needed.
           */
          Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
          ImageView imgview = new ImageView(img);
          Button add = new Button("", imgview);
          add.setOnMouseClicked(new AddToStaffHandler(primary, staffMemberPane, sp));
          staffMemberPane.add(add, 0, staff.size());
          GridPane.setColumnSpan(add, 2);
          /*
           * If the Escape-Key was pressed, the TextField will be set as disabled and by this, 
           * removed from the Pane. This is done, since it loses Focus this way and in this case 
           * the FocusChanged Listener above will receive an Event.
           */
        } else if (event.getCode() == KeyCode.ESCAPE) {
          tf.setDisable(true);
        }      
      }
    });
    /*
     * Sets the ToolTip with additional Info.
     */
    tf.setTooltip(new Tooltip("Enter drücken zum Hinzufügen, Escape zum Abbrechen"));
    /*
     * Adds the new TextField at Row 'size / 2', which is the lowest Row in the Pane.
     */
    staffMemberPane.add(tf, 0, size / 2);
    /*
     * Scrolls to the Bottom of the ScrollPane, where this TextField was created.
     */
    sp.vvalueProperty().bind(staffMemberPane.heightProperty());
    sp.vvalueProperty().unbind();
  }

}
