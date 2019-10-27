package starter;

import ui.Interface;

/**
 * A Class, that is able to start the Application via a static main Method.
 * @author Haeldeus
 * @version 1.0
 */
public class ClientStarter {

  /**
   * The Main Method that can be used to start the Application.
   * @param args Unused.
   * @since 1.0
   */
  public static void main(String[] args) {
    new Interface().build();
  }
}
