package tipcalculator.handler;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;
import tipcalculator.listener.TextFieldFocusChangeListener;
import tipcalculator.listener.TextFieldTextChangeListener;
import util.Util;

public class AddToTipHandler implements EventHandler<MouseEvent> {

  private final TipWindow primary;
  
  private GridPane gridPane;
  
  private int rowIndex;
  
  private boolean exact;
  
  /**
   * 
   * @param primary
   * @param grid
   * @param rowIndex
   */
  public AddToTipHandler(TipWindow primary, GridPane grid, int rowIndex) {
    this.primary = primary;
    this.gridPane = grid;
    this.rowIndex = rowIndex;
    exact = false;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handle(MouseEvent arg0) {
    gridPane.getChildren().remove(gridPane.getChildren().size() - 1);
    ArrayList<String> usedItems = new ArrayList<String>();
    for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
      usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
          .getSelectedItem());
    }
    ArrayList<String> tmpStaff = Util.getStaffMembers();
    ArrayList<String> res = new ArrayList<String>();
    for (String s : tmpStaff) {
      if (!usedItems.contains(s)) {
        res.add(s);
      }
    }
    ComboBox<String> staffMember = new ComboBox<String>(FXCollections
        .observableArrayList(res));
    staffMember.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        String selected = staffMember.getSelectionModel().getSelectedItem();
        ArrayList<String> usedItems = new ArrayList<String>();
        for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
          usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
              .getSelectedItem());
        }
        ArrayList<String> tmpStaff = Util.getStaffMembers();
        ArrayList<String> res = new ArrayList<String>();
        for (String s : tmpStaff) {
          if (!usedItems.contains(s)) {
            res.add(s);
          }
        }
        if (selected != null) {
          res.add(selected);
        }
        staffMember.setItems(FXCollections.observableArrayList(res));
      }      
    });
    gridPane.add(staffMember, 0, rowIndex);
    
    GridPane gridTemp = new GridPane();
    gridTemp.setHgap(5);
    
    TextField tfHours = new TextField();
    tfHours.setMaxWidth(50);
    tfHours.focusedProperty().addListener(new TextFieldFocusChangeListener(tfHours, exact, 
        gridPane, primary, false));
    tfHours.textProperty().addListener(new TextFieldTextChangeListener(tfHours, 
        exact, gridPane, primary, false));
    tfHours.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent arg0) {
        if (arg0.getCode() == KeyCode.ENTER) {
          for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
            GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
            TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
            if (tmpTf == tfHours) {
              if (i + 3 >= gridPane.getChildren().size()) {
                primary.getTfTip().requestFocus();
              } else {
                smallGrid = (GridPane)gridPane.getChildren().get(i + 3);
                tmpTf = (TextField)smallGrid.getChildren().get(0);
                tmpTf.requestFocus();
              }
            }
          }
        }
      }      
    });
    gridTemp.add(tfHours, 0, 0);
    
    Label labelH = new Label("h");
    gridTemp.add(labelH, 1, 0);
    
    Label lbTip = new Label("=");
    lbTip.setMinWidth(50);
    gridTemp.add(lbTip, 2, 0);
    gridPane.add(gridTemp, 1, rowIndex);
    
    Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
    ImageView imageview = new ImageView(img);
    Button remove = new Button("", imageview);
    remove.setTooltip(new Tooltip("Aus Liste entfernen"));
    remove.setOnMouseClicked(new DeleteFromTipHandler(rowIndex, gridPane, primary));
    gridPane.add(remove, 2, rowIndex);

    img = new Image(getClass().getResourceAsStream("/res/add.png"));
    imageview = new ImageView(img);
    Button add = new Button("", imageview);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(primary, gridPane, rowIndex + 1));
    gridPane.add(add, 0, rowIndex + 1);
  }
  
  protected void reduceIndex() {
    rowIndex--;
  }
}
