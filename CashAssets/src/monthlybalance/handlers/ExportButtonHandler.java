package monthlybalance.handlers;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import monthlybalance.MBalanceWindow;

/**
 * The Handler for the Export Button. Will export the Data into a new Excel File.
 * @author Haeldeus
 * @version 1.0
 */
public class ExportButtonHandler implements EventHandler<MouseEvent> {

  /**
   * The Constructor for this Handler. Sets all parameters to their dedicated fields. 
   * @param primary The {@link MBalanceWindow}, this Handler was called from.
   * @param checkOpen The boolean value, if the Explorer should be opened after exporting.
   * @param monthBox  The ComboBox, where the User entered the Month which should be calculated.
   * @param tfYear  The ComboBox, where the User entered the Year of the Month to be calculated.
   * @param dialog  The Stage, the Button for this Handler is part of. Will be closed after 
   *                exporting.
   */
  public ExportButtonHandler(MBalanceWindow primary, boolean checkOpen, ComboBox<String> monthBox, 
      TextField tfYear, Stage dialog) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void handle(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

}
