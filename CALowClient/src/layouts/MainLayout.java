package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.HashMap;

/**
 * The Layout for the Main Window of the Application.
 * @author Haeldeus
 * @version 1.0
 */
public class MainLayout implements LayoutManager {

  public static final String MENU = "MENU";
  
  /**
   * The String, that defines the Component, that simply displays "1ct" to allow the User to 
   * identify, which TextField is used for every single type of money.
   */
  public static final String ONECENTLABEL = "ONE_CENT_LABEL";
  
  /**
   * The String, that defines the Component, used to describe how many 1 ct Coins were counted.
   */
  public static final String ONECENTTF = "ONE_CENT_TEXTFIELD";
  
  /**
   * The String, that defines the Component, used to describe how many 2 ct Coins were counted.
   */
  public static final String TWOCENTTF = "TWO_CENT_TEXTFIELD";

  /**
   * The HashMap, that contains all Components of this Layout with a String as their identifier.
   */
  private HashMap<String, Component> components;
  
  /**
   * The Constructor for this Layout Manager. Creates a new HashMap for the Components.
   * @since 1.0
   */
  public MainLayout() {
    components = new HashMap<String, Component>();
  }
  
  @Override
  public void addLayoutComponent(String id, Component comp) {
    components.put(id, comp);
  }

  @Override
  public void layoutContainer(Container arg0) {
    if (components.get(MENU) != null) {
      components.get(MENU).setBounds(0, 0, 600, 20);
    }
  }

  @Override
  public Dimension minimumLayoutSize(Container arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Dimension preferredLayoutSize(Container arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeLayoutComponent(Component arg0) {
    if (components.containsValue(arg0)) {
      components.remove(arg0);
    } else {
      System.err.println("There was an Error removing a Component from the Interface. "
          + "Please report this.");
    }
  }
  
  /**
   * Removes the Component, that is identified by the given String {@code id}, from the Interface.
   * @param id  The Component to be removed.
   * @since 1.0
   */
  public void removeLayoutComponent(String id) {
    if (components.containsKey(id)) {
      components.remove(id);
    } else {
      System.err.println("There was an Error removing a Component from the Interface. "
          + "Please report this.");
    }
  }
}
