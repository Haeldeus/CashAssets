package tipcalculator.handler;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class AddHandler implements EventHandler<MouseEvent> {

  private GridPane staffMemberPane;
  
  private StaffHandler primary;
  
  public AddHandler(StaffHandler primary, GridPane staffMemberPane) {
    this.primary = primary;
    this.staffMemberPane = staffMemberPane;
  }
  
  @Override
  public void handle(MouseEvent event) {
    int size = staffMemberPane.getChildren().size();
    staffMemberPane.getChildren().remove(size - 1);
    TextField tf = new TextField("Infos im Tooltip");
    tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldProperty, 
          Boolean newProperty) {
        if (!newProperty) {
          int size = staffMemberPane.getChildren().size();
          staffMemberPane.getChildren().remove(size - 1);
          Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
          ImageView imageview = new ImageView(img);
          Button add = new Button("", imageview);
          add.setOnMouseClicked(new AddHandler(primary, staffMemberPane));
          staffMemberPane.add(add, 0, size / 2);
        }
      }      
    });
    tf.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
          staffMemberPane.getChildren().remove(staffMemberPane.getChildren().size() - 1);
          ArrayList<String> staff = primary.getStaff();
          staff.add(tf.getText());
          primary.updateStaff(staff);
          Label l = new Label(tf.getText());
          staffMemberPane.add(l, 0, staff.size() - 1);
          Image image = new Image(getClass().getResourceAsStream("/res/delete.png"));
          ImageView imageview = new ImageView(image);
          Button del = new Button("", imageview);
          del.setOnMouseClicked(new DeleteHandler(primary, staff.size() - 1, staff, 
              staffMemberPane));
          staffMemberPane.add(del, 1, staff.size() - 1);
          
          Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
          ImageView imgview = new ImageView(img);
          Button add = new Button("", imgview);
          add.setOnMouseClicked(new AddHandler(primary, staffMemberPane));
          staffMemberPane.add(add, 0, staff.size());
          GridPane.setColumnSpan(add, 2);
          
        } else if (event.getCode() == KeyCode.ESCAPE) {
          tf.setDisable(true);
        }
      }
    });
    tf.setTooltip(new Tooltip("Enter drücken zum Hinzufügen, Escape zum Abbrechen"));
    staffMemberPane.add(tf, 0, size / 2);
  }

}
