package tipcalculator.handler;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tipcalculator.TipWindow;

/**
 * The Handler for the Delete Button in the Tip Window.
 * @author Haeldeus
 * @version 1.0
 */
public class DeleteFromTipHandler implements EventHandler<MouseEvent> {

  /**
   * The Index of the Row, the Delete Button is in.
   */
  private int rowIndex;
  
  /**
   * The GridPane, that contains the Delete Button.
   */
  private GridPane grid;
  
  /**
   * The TipWindow, that contains the GridPane, the Delete Button is contained in.
   */
  private TipWindow primary;
  
  /**
   * The Constructor for this Handler. Sets all fields to the given values.
   * @param rowIndex  The Index of the Row, the Delete Button is in.
   * @param grid  The GridPane, that contains the Delete Button.
   * @param primary The TipWindow, that contains the Delete Button.
   * @since 1.0
   */
  public DeleteFromTipHandler(int rowIndex, GridPane grid, TipWindow primary) {
    this.rowIndex = rowIndex;
    this.grid = grid;
    this.primary = primary;
  }
  
  @Override
  public void handle(MouseEvent arg0) {
    /*
     * Removes the Row with the stored index.
     */
    for (int i = 0; i < 3; i++) {
      grid.getChildren().remove(3 * (rowIndex - 1));
    }
    /*
     * Removes and Re-Adds all children of the gridPane to get rid of the Insets, that are 
     * still there after deleting the Row.
     */
    ObservableList<Node> tmpChildren = grid.getChildren();
    ArrayList<Node> children = new ArrayList<Node>();
    for (Node n : tmpChildren) {
      children.add(n);
    }
    grid.getChildren().clear();
    /*
     * Rebuilds the GridPane.
     */
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        /*
         * Iterates over all Nodes, that are part of the GridPane.
         */
        for (int j = 0; j < children.size(); j++) {
          /*
           * If j mod 3 = 2, the Node is a Delete Button and a new Handler has to be added.
           */
          if (j % 3 == 2) {
            /*
             * Creates a new Delete Button with a new Listener and an updated rowIndex and 
             * adds it to the grid.
             */
            Image img = new Image(getClass().getResourceAsStream("/res/delete.png"));
            ImageView iw = new ImageView(img);
            Button btDelete = new Button("", iw);
            btDelete.setOnMouseClicked(new DeleteFromTipHandler((j / 3) + 1, grid, primary));
            grid.add(btDelete, 2, (j / 3));
          /*
           * If j is the biggest possible value (size-1), a new Add Button will be added to the 
           * grid. 
           */
          } else if (j == children.size() - 1) {
            /*
             * Stores the size of the Children-List. This is used to determine the rowIndex of the 
             * AddButton.
             */
            int size = grid.getChildren().size();
            /*
             * Adds a new Add Button and it's Listener to the Grid 
             */
            Image img = new Image(getClass().getResourceAsStream("/res/add.png"));
            ImageView iw = new ImageView(img);
            Button btAdd = new Button("", iw);
            btAdd.setOnMouseClicked(new AddToTipHandler(primary, grid, size / 3 + 1));
            grid.add(btAdd, 0, (size / 3 + 1));
          } else {
            /*
             * Adds the children at index j in the children-List to the grid.
             * Since each row is 3 Nodes wide, j mod 3 equals the Column Index,
             * while j / 3 equals the Row index.
             */
            grid.add(children.get(j), (j % 3), (j / 3));
          }
        }
        /*
         * Sets primary.deleted to true, since a row was deleted.
         */
        primary.setDeleted(true);
        /*
         * Requests focus for the Tip TextField to update the Labels, that display the Tip Share of 
         * each Staff Member.
         */
        primary.getTfTip().requestFocus();
      }     
    });
  }

}
