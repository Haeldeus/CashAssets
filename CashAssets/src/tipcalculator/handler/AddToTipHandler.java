package tipcalculator.handler;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

/**
 * The Event Handler, that is attached to the Add-Button in the Tip Scene. This Handler will add a 
 * new Row to the GridPane when it is called.
 * @author Haeldeus
 * @version 1.0
 */
public class AddToTipHandler implements EventHandler<MouseEvent> {

  /**
   * The TipWindow, the Button this Handler is attached to was added to.
   */
  private final TipWindow primary;
  
  /**
   * The GridPane, that contains the Button with this Handler.
   */
  private GridPane gridPane;
  
  /**
   * The Index of the Row, this Button is in.
   */
  private int rowIndex;
  
  /**
   * Determines, if the calculation should be done exactly. If not, only total hours will be used, 
   * if yes, the input won't be altered.
   */
  private boolean exact;
  
  /**
   * The Constructor for this Handler. This will set all Fields to the given values.
   * @param primary The TipWindow, that contains the Button with this Handler.
   * @param grid  The GridPane, that contains the Button with this Handler.
   * @param rowIndex  The Index of the Row, the Button with this Handler is in (First row = 0).
   * @since 1.0
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
    /*
     * Removes the Add-Button from the GridPane to make space for a new Row.
     */
    gridPane.getChildren().remove(gridPane.getChildren().size() - 1);
    /*
     * Creates a new List, where all Items that are selected in already existing ComboBoxes are 
     * stored in.
     */
    ArrayList<String> usedItems = new ArrayList<String>();
    for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
      usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
          .getSelectedItem());
    }
    /*
     * Creates a new List, where all Staff Members are stored in.
     */
    ArrayList<String> tmpStaff = Util.getStaffMembers();
    ArrayList<String> res = new ArrayList<String>();
    /*
     * Checks for every Staff Member, if it was selected in another ComboBox. If not, it will be 
     * added to the Items of the new ComboBox.
     */
    for (String s : tmpStaff) {
      if (!usedItems.contains(s)) {
        res.add(s);
      }
    }
    /*
     * Creates a new ComboBox with the previously created List as Items. Also adds the 
     * EventHandlers for this ComboBox.
     */
    ComboBox<String> staffMember = new ComboBox<String>(FXCollections
        .observableArrayList(res));
    staffMember.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent arg0) {
        /*
         * Stores the currently selected Item in a new variable.
         */
        String selected = staffMember.getSelectionModel().getSelectedItem();
        /*
         * Creates a new List, where all Items that are selected in already existing ComboBoxes are 
         * stored in.
         */
        ArrayList<String> usedItems = new ArrayList<String>();
        for (int i = 0; i < gridPane.getChildren().size() - 1; i += 3) {
          usedItems.add(((ComboBox<String>)gridPane.getChildren().get(i)).getSelectionModel()
              .getSelectedItem());
        }
        /*
         * Creates a new List, where all Staff Members are stored in.
         */
        ArrayList<String> tmpStaff = Util.getStaffMembers();
        ArrayList<String> res = new ArrayList<String>();
        /*
         * Checks for every Staff Member, if it was selected in another ComboBox. If not, it will 
         * be added to the Items of the new ComboBox.
         */
        for (String s : tmpStaff) {
          if (!usedItems.contains(s)) {
            res.add(s);
          }
        }
        /*
         * If there was a selected Item, it will be added to the List as well.
         */
        if (selected != null) {
          res.add(selected);
        }
        /*
         * Sets the Items of the ComboBox to the created List.
         */
        staffMember.setItems(FXCollections.observableArrayList(res));
      }      
    });
    staffMember.setOnKeyPressed(new ComboBoxKeyHandler(staffMember, gridPane));
    gridPane.add(staffMember, 0, rowIndex);
    
    /*
     * Creates a new GridPane, where the TextField and the Label is stored in, where the User 
     * can enter the Hours for this Staff Member and the letter 'h' is displayed. The 'h' is 
     * displayed to ensure understandability what to put into the TextField.
     */
    GridPane gridTemp = new GridPane();
    gridTemp.setHgap(5);
    
    /*
     * Creates a new TextField with the given Listeners.
     */
    TextField tfHours = new TextField();
    tfHours.setMaxWidth(50);
    tfHours.focusedProperty().addListener(new TextFieldFocusChangeListener(tfHours, exact, 
        gridPane, primary, false));
    tfHours.textProperty().addListener(new TextFieldTextChangeListener(tfHours, 
        exact, gridPane, primary, false));
    tfHours.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent arg0) {
        /*
         * If the User presses Enter, the next TextField will be selected.
         */
        if (arg0.getCode() == KeyCode.ENTER) {
          /*
           * Iterates over all TextFields to get Field, where the Key was pressed in..
           */
          for (int i = 1; i < gridPane.getChildren().size(); i += 3) {
            /*
             * Creates a Copy of the TextField that is stored at the given Index in the GridPane.
             */
            GridPane smallGrid = (GridPane)gridPane.getChildren().get(i);
            TextField tmpTf = (TextField)smallGrid.getChildren().get(0);
            /*
             * Checks if the Copy is the Field, where the Enter-Key was pressed in.
             */
            if (tmpTf == tfHours) {
              /*
               * If i + 3 is greater than the size of the GridPanes content, this Field is the 
               * last in the List.
               */
              if (i + 3 >= gridPane.getChildren().size()) {
                /*
                 * Jumps back to the Top to select the Tip-TextField.
                 */
                primary.getTfTip().requestFocus();
              } else {
                /*
                 * Selects the next TextField in the List.
                 */
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
    
    /*
     * Creates the Label with the 'h' as Text.
     */
    Label labelH = new Label("h");
    gridTemp.add(labelH, 1, 0);
    
    /*
     * Creates a Label, where the Share of the Tip for this Staff Member can be displayed in.
     */
    Label lbTip = new Label("=");
    lbTip.setMinWidth(50);
    gridTemp.add(lbTip, 2, 0);
    gridPane.add(gridTemp, 1, rowIndex);
    
    /*
     * Adds a Delete-Button at the End of the Row to remove this Row from the GridPane.
     */
    Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
    ImageView imageview = new ImageView(img);
    Button remove = new Button("", imageview);
    remove.setTooltip(new Tooltip("Aus Liste entfernen"));
    remove.setOnMouseClicked(new DeleteFromTipHandler(rowIndex, gridPane, primary));
    gridPane.add(remove, 2, rowIndex);

    /*
     * Adds a Add-Button below the current Row so the User can add more Rows to the GridPane.
     */
    img = new Image(getClass().getResourceAsStream("/res/add.png"));
    imageview = new ImageView(img);
    Button add = new Button("", imageview);
    add.setTooltip(new Tooltip("Hinzufügen"));
    add.setOnMouseClicked(new AddToTipHandler(primary, gridPane, rowIndex + 1));
    gridPane.add(add, 0, rowIndex + 1);
    
    /*
     * If a row was deleted as the last action, this part is used to get rid of the Inset, that was 
     * left afterwards.
     */
    if (primary.getDeleted()) {
      ObservableList<Node> tmpChildren = gridPane.getChildren();
      ArrayList<Node> children = new ArrayList<Node>();
      for (Node n : tmpChildren) {
        children.add(n);
      }
      gridPane.getChildren().clear();
      for (int i = 0; i < children.size(); i++) {
        gridPane.add(children.get(i), i % 3, i / 3);
      }
      primary.setDeleted(false);
    }
  }
}
