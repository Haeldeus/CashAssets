package ui;

import java.awt.Container;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import layouts.MainLayout;

/**
 * The Class which Objects display the User Interface in a JFrame.
 * @author Haeldeus
 * @version 1.0
 */
public class Interface {

  /**
   * A Method to build the Frame and display it.
   * @since 1.0
   */
  public void build() {
    //System.out.println(new Date(System.currentTimeMillis()));
    JFrame frame = new JFrame();
    frame.setSize(new Dimension(600, 500));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new MainLayout());
    Container c = frame.getContentPane();
    addMenu(frame);
    addTextFields(c);
    addLabels(c);
    addButtons(c);
    frame.setVisible(true);
  }

  /**
   * Adds the Menu to the Frame.
   * @param c The Container, that holds the ContentPane of the Frame.
   * @since 1.0
   * @see JFrame#getContentPane()
   */
  private void addMenu(Container c) {
    JMenuBar menuBar = new JMenuBar();
    
    JMenu edit = new JMenu(getLang("Edit"));
    menuBar.add(edit);
    
    JMenuItem server = new JMenuItem(getLang("Server"));
    edit.add(server);
    
    JMenuItem settings = new JMenuItem(getLang("Settings"));
    edit.add(settings);
    
    c.add(MainLayout.MENU, menuBar);
  }
  
  /**
   * Adds all JLabels to the Frame.
   * @param c The Container, that holds the ContentPane of the Frame.
   * @since 1.0
   * @see JFrame#getContentPane()
   */
  private void addLabels(Container c) {
    JLabel lblTmp = new JLabel("1ct");
    c.add(MainLayout.ONECENTLABEL, lblTmp);
  }
  
  /**
   * Adds all JTextFields to the Frame.
   * @param c The Container, that holds the ContentPane of the Frame.
   * @since 1.0
   * @see JFrame#getContentPane()
   */
  private void addTextFields(Container c) {
    JTextField tfTmp = new JTextField();
  }

  /**
   * Adds all JButtons to the Frame.
   * @param c The Container, that holds the ContentPane of the Frame.
   * @since 1.0
   * @see JFrame#getContentPane()
   */
  private void addButtons(Container c) {
    // TODO Auto-generated method stub
    
  }
  
  private String getLang(String foo) {
    //TODO: Localization.
    return foo;
  }
}
