package kasse;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.HashMap;

/**
 * A Layout for the GUI that will display all Components.
 * @author Haeldeus
 * @version 1.0
 */
public class KassenLayout implements LayoutManager {

  /**
   * The Label, that displays the Value "Datum:".
   */
  public static final String dateLabel = "Date Label";
  
  public static final String dateDayDropdown = "Date Day Dropdown";
  public static final String dateMonthDropdown = "Date Month Dropdown";
  public static final String dateYearTextField = "Date Year TextField";
  
  /**
   * The Label, that displays the Value "1ct".
   */
  public static final String oneCentLabel = "One Cent";
  
  /**
   * The Label, that displays the Value "2ct".
   */
  public static final String twoCentLabel = "Two Cent";
  
  /**
   * The Label, that displays the Value "5ct".
   */
  public static final String fiveCentLabel = "Five Cent";
  /**
   * The Label, that displays the Value "10ct".
   */
  public static final String tenCentLabel = "Ten Cent";
  
  /**
   * The Label, that displays the Value "20ct".
   */
  public static final String twentyCentLabel = "Twenty Cent";
  
  /**
   * The Label, that displays the Value "50ct".
   */
  public static final String fiftyCentLabel = "Fifty Cent";
  
  /**
   * The Label, that displays the Value "1€".
   */
  public static final String oneEuroLabel = "One Euro";
  
  /**
   * The Label, that displays the Value "2€".
   */
  public static final String twoEuroLabel = "Two Euro";
 
  
  /**
   * The Label, that displays the Value "5€".
   */
  public static final String fiveEuroLabel = "Five Euro";
  
  /**
   * The Label, that displays the Value "10€".
   */
  public static final String tenEuroLabel = "Ten Euro";
  
  /**
   * The Label, that displays the Value "20€".
   */
  public static final String twentyEuroLabel = "Twenty Euro";
  
  /**
   * The Label, that displays the Value "50€".
   */
  public static final String fiftyEuroLabel = "Fifty Euro";
  
  /**
   * The Label, that displays the Value "100€".
   */
  public static final String hundredEuroLabel = "Hundred Euro";
  
  /**
   * The Label, that displays the Value "200€".
   */
  public static final String twohundredEuroLabel = "Twohundred Euro";
  
  /**
   * The Label, that displays the Value "500€".
   */
  public static final String fivehundredEuroLabel = "Fivehundred Euro";
  
  public static final String oneCentSumLabel = "One Cent Sum";
  public static final String twoCentSumLabel = "Two Cent Sum";
  public static final String fiveCentSumLabel = "Five Cent Sum";
  public static final String tenCentSumLabel = "Ten Cent Sum";
  public static final String twentyCentSumLabel = "Twenty Cent Sum";
  public static final String fiftyCentSumLabel = "Fifty Cent Sum";
  public static final String oneEuroSumLabel = "One Euro Sum";
  public static final String twoEuroSumLabel = "Two Euro Sum";
  
  public static final String fiveEuroSumLabel = "Five Euro Sum";
  public static final String tenEuroSumLabel = "Ten Euro Sum";
  public static final String twentyEuroSumLabel = "Twenty Euro Sum";
  public static final String fiftyEuroSumLabel = "Fifty Euro Sum";
  public static final String hundredEuroSumLabel = "Hundred Euro Sum";
  public static final String twohundredEuroSumLabel = "Twohundred Euro Sum";
  public static final String fivehundredEuroSumLabel = "Fivehundred Euro Sum";
  
  public static final String purseLabel = "Purse";
  
  public static final String oneCentTF = "One Cent TF";
  public static final String twoCentTF = "Two Cent TF";
  public static final String fiveCentTF = "Five Cent TF";
  public static final String tenCentTF = "Ten Cent TF";
  public static final String twentyCentTF = "Twenty Cent TF";
  public static final String fiftyCentTF = "Fifty Cent TF";
  public static final String oneEuroTF = "One Euro TF";
  public static final String twoEuroTF = "Two Euro TF";
  
  public static final String fiveEuroTF = "Five Euro TF";
  public static final String tenEuroTF = "Ten Euro TF";
  public static final String twentyEuroTF = "Twenty Euro TF";
  public static final String fiftyEuroTF = "Fifty Euro TF";
  public static final String hundredEuroTF = "Hundred Euro TF";
  public static final String twohundredEuroTF = "Twohundred Euro TF";
  public static final String fivehundredEuroTF = "Fivehundred Euro TF";
  
  public static final String purseTF = "Purse TF";
  
  public static final String seperatorOne = "Seperator One";
  public static final String seperatorTwo = "Seperator Two";
  public static final String seperatorThree = "Seperator Three";
  
  public static final String cashNecessityLabel = "Cash Necessity Label";
  public static final String cashNecessityEuroLabel = "Cash Necessity Euro Label";
  public static final String cashNecessityCentLabel = "Cash Necessity Cent Label";
  
  public static final String cashNecessityEuroTF = "Cash Necessity Euro TF";
  public static final String cashNecessityCentTF = "Cash Necessity Cent TF";
  
  public static final String calcButton = "Calc Button";
  public static final String resetButton = "Reset Button";
  public static final String exportButton = "Export Button";
  
  public static final String coinageLabel = "Coinage Label";
  public static final String coinageSumLabel = "Coinage Sum Label";
  public static final String billsLabel = "Bills Label";
  public static final String billsSumLabel = "Bills Sum Label";
  public static final String totalLabel = "Total Label";
  public static final String totalSumLabel = "Total Sum Label";
  public static final String coinageNecessityLabel = "Coinage Necessity Label";
  public static final String coinageNecessitySumLabel = "Coinage Necessity Sum Label";
  public static final String coinageDifferenceLabel = "Coinage Difference Label";
  public static final String coinageDifferenceSumLabel = "Coinage Difference Sum Label";
  public static final String totalCashNecessityLabel = "Total Cash Necessity Label";
  public static final String totalCashNecessitySumLabel = "Total Cash Necessity Sum Label";
  public static final String revenuesWithTipLabel = "Revenues With Tip Label";
  public static final String revenuesWithTipSumLabel = "Revenues With Tip Sum Label";
  public static final String tipLabel = "Tip Label";
  public static final String tipSumLabel = "Tip Sum Label";
  
  public static final Dimension dim = new Dimension(600, 700);
  private HashMap<String, Component> comps;
  
  public KassenLayout() {
    comps = new HashMap<String, Component>();
  }
  
  @Override
  public void addLayoutComponent(String arg0, Component arg1) {
    comps.put(arg0, arg1);
  }

  @Override
  public void layoutContainer(Container arg0) {
    if (comps.get(dateLabel) != null) {
      comps.get(dateLabel).setBounds(20, 10, 60, 20);
    }
    
    if (comps.get(dateDayDropdown) != null) {
      comps.get(dateDayDropdown).setBounds(90, 10, 50, 20);
    }
    
    if (comps.get(dateMonthDropdown) != null) {
      comps.get(dateMonthDropdown).setBounds(160, 10, 50, 20);
    }
    
    if (comps.get(dateYearTextField) != null) {
      comps.get(dateYearTextField).setBounds(230, 10, 50, 20);
    }
    
    if (comps.get(exportButton) != null) {
      comps.get(exportButton).setBounds(480, 10, 100, 20);
    }
    
    if (comps.get(oneCentLabel) != null) {
      comps.get(oneCentLabel).setBounds(20, 40, 50, 20);
    }
    if (comps.get(twoCentLabel) != null) {
      comps.get(twoCentLabel).setBounds(20, 70, 50, 20);
    }
    if (comps.get(fiveCentLabel) != null) {
      comps.get(fiveCentLabel).setBounds(20, 100, 50, 20);
    }
    if (comps.get(tenCentLabel) != null) {
      comps.get(tenCentLabel).setBounds(20, 130, 50, 20);
    }
    if (comps.get(twentyCentLabel) != null) {
      comps.get(twentyCentLabel).setBounds(20, 160, 50, 20);
    }
    if (comps.get(fiftyCentLabel) != null) {
      comps.get(fiftyCentLabel).setBounds(20, 190, 50, 20);
    }
    if (comps.get(oneEuroLabel) != null) {
      comps.get(oneEuroLabel).setBounds(20, 220, 50, 20);
    }
    if (comps.get(twoEuroLabel) != null) {
      comps.get(twoEuroLabel).setBounds(20, 250, 50, 20);
    }
    
    
    if (comps.get(oneCentSumLabel) != null) {
      comps.get(oneCentSumLabel).setBounds(160, 40, 150, 20);
    }
    
    if (comps.get(twoCentSumLabel) != null) {
      comps.get(twoCentSumLabel).setBounds(160, 70, 150, 20);
    }
    
    if (comps.get(fiveCentSumLabel) != null) {
      comps.get(fiveCentSumLabel).setBounds(160, 100, 150, 20);
    }
    
    if (comps.get(tenCentSumLabel) != null) {
      comps.get(tenCentSumLabel).setBounds(160, 130, 150, 20);
    }
    
    if (comps.get(twentyCentSumLabel) != null) {
      comps.get(twentyCentSumLabel).setBounds(160, 160, 150, 20);
    }
    
    if (comps.get(fiftyCentSumLabel) != null) {
      comps.get(fiftyCentSumLabel).setBounds(160, 190, 150, 20);
    }
    
    if (comps.get(oneEuroSumLabel) != null) {
      comps.get(oneEuroSumLabel).setBounds(160, 220, 150, 20);
    }
    
    if (comps.get(twoEuroSumLabel) != null) {
      comps.get(twoEuroSumLabel).setBounds(160, 250, 150, 20);
    }
    
    
    if (comps.get(fiveEuroLabel) != null) {
      comps.get(fiveEuroLabel).setBounds(300, 40, 50, 20);
    }
    if (comps.get(tenEuroLabel) != null) {
      comps.get(tenEuroLabel).setBounds(300, 70, 50, 20);
    }
    if (comps.get(twentyEuroLabel) != null) {
      comps.get(twentyEuroLabel).setBounds(300, 100, 50, 20);
    }
    if (comps.get(fiftyEuroLabel) != null) {
      comps.get(fiftyEuroLabel).setBounds(300, 130, 50, 20);
    }
    if (comps.get(hundredEuroLabel) != null) {
      comps.get(hundredEuroLabel).setBounds(300, 160, 50, 20);
    }
    if (comps.get(twohundredEuroLabel) != null) {
      comps.get(twohundredEuroLabel).setBounds(300, 190, 50, 20);
    }
    if (comps.get(fivehundredEuroLabel) != null) {
      comps.get(fivehundredEuroLabel).setBounds(300, 220, 50, 20);
    }
    
    
    if (comps.get(fiveEuroSumLabel) != null) {
      comps.get(fiveEuroSumLabel).setBounds(440, 40, 150, 20);
    }
    if (comps.get(tenEuroSumLabel) != null) {
      comps.get(tenEuroSumLabel).setBounds(440, 70, 150, 20);
    }
    if (comps.get(twentyEuroSumLabel) != null) {
      comps.get(twentyEuroSumLabel).setBounds(440, 100, 150, 20);
    }
    if (comps.get(fiftyEuroSumLabel) != null) {
      comps.get(fiftyEuroSumLabel).setBounds(440, 130, 150, 20);
    }
    if (comps.get(hundredEuroSumLabel) != null) {
      comps.get(hundredEuroSumLabel).setBounds(440, 160, 150, 20);
    }
    if (comps.get(twohundredEuroSumLabel) != null) {
      comps.get(twohundredEuroSumLabel).setBounds(440, 190, 150, 20);
    }
    if (comps.get(fivehundredEuroSumLabel) != null) {
      comps.get(fivehundredEuroSumLabel).setBounds(440, 220, 150, 20);
    }
    
    
    if (comps.get(purseLabel) != null) {
      comps.get(purseLabel).setBounds(300, 250, 50, 20);
    }
    
    
    if (comps.get(oneCentTF) != null) {
      comps.get(oneCentTF).setBounds(90, 40, 50, 20);
    }
    if (comps.get(twoCentTF) != null) {
      comps.get(twoCentTF).setBounds(90, 70, 50, 20);
    }
    if (comps.get(fiveCentTF) != null) {
      comps.get(fiveCentTF).setBounds(90, 100, 50, 20);
    }
    if (comps.get(tenCentTF) != null) {
      comps.get(tenCentTF).setBounds(90, 130, 50, 20);
    }
    if (comps.get(twentyCentTF) != null) {
      comps.get(twentyCentTF).setBounds(90, 160, 50, 20);
    }
    if (comps.get(fiftyCentTF) != null) {
      comps.get(fiftyCentTF).setBounds(90, 190, 50, 20);
    }
    if (comps.get(oneEuroTF) != null) {
      comps.get(oneEuroTF).setBounds(90, 220, 50, 20);
    }
    if (comps.get(twoEuroTF) != null) {
      comps.get(twoEuroTF).setBounds(90, 250, 50, 20);
    }
    
    
    if (comps.get(fiveEuroTF) != null) {
      comps.get(fiveEuroTF).setBounds(370, 40, 50, 20);
    }
    if (comps.get(tenEuroTF) != null) {
      comps.get(tenEuroTF).setBounds(370, 70, 50, 20);
    }
    if (comps.get(twentyEuroTF) != null) {
      comps.get(twentyEuroTF).setBounds(370, 100, 50, 20);
    }
    if (comps.get(fiftyEuroTF) != null) {
      comps.get(fiftyEuroTF).setBounds(370, 130, 50, 20);
    }
    if (comps.get(hundredEuroTF) != null) {
      comps.get(hundredEuroTF).setBounds(370, 160, 50, 20);
    }
    if (comps.get(twohundredEuroTF) != null) {
      comps.get(twohundredEuroTF).setBounds(370, 190, 50, 20);
    }
    if (comps.get(fivehundredEuroTF) != null) {
      comps.get(fivehundredEuroTF).setBounds(370, 220, 50, 20);
    }
    
    if (comps.get(purseTF) != null) {
      comps.get(purseTF).setBounds(370, 250, 50, 20);
      comps.get(purseTF).setBackground(Color.orange);
    }
    
    if (comps.get(seperatorOne) != null) {
      comps.get(seperatorOne).setBounds(20, 280, 600, 20);
    }
    
    if (comps.get(cashNecessityLabel) != null) {
      comps.get(cashNecessityLabel).setBounds(20, 310, 120, 20);
    }
    
    if (comps.get(cashNecessityEuroTF) != null) {
      comps.get(cashNecessityEuroTF).setBounds(160, 310, 50, 20);
    }
    
    if (comps.get(cashNecessityEuroLabel) != null) {
      comps.get(cashNecessityEuroLabel).setBounds(220, 310, 20, 20);
    }
    
    if (comps.get(cashNecessityCentTF) != null) {
      comps.get(cashNecessityCentTF).setBounds(260, 310, 50, 20);
    }
    
    if (comps.get(cashNecessityCentLabel) != null) {
      comps.get(cashNecessityCentLabel).setBounds(320, 310, 30, 20);
    }
    
    if (comps.get(seperatorTwo) != null) {
      comps.get(seperatorTwo).setBounds(20, 340, 600, 20);
    }
    
    if (comps.get(coinageLabel) != null) {
      comps.get(coinageLabel).setBounds(20, 370, 60, 20);
    }
    
    if (comps.get(coinageSumLabel) != null) {
      comps.get(coinageSumLabel).setBounds(160, 370, 120, 20);
    }
    
    if (comps.get(billsLabel) != null) {
      comps.get(billsLabel).setBounds(300, 370, 120, 20);
    }
    
    if (comps.get(billsSumLabel) != null) {
      comps.get(billsSumLabel).setBounds(440, 370, 120, 20);
    }
    
    if (comps.get(totalLabel) != null) {
      comps.get(totalLabel).setBounds(20, 400, 100, 20);
    }
    
    if (comps.get(totalSumLabel) != null) {
      comps.get(totalSumLabel).setBounds(160, 400, 120, 20);
    }
    
    if (comps.get(seperatorThree) != null) {
      comps.get(seperatorThree).setBounds(20, 430, 600, 20);
    }
    
    if (comps.get(coinageNecessityLabel) != null) {
      comps.get(coinageNecessityLabel).setBounds(20, 460, 100, 20);
    }
    
    if (comps.get(coinageNecessitySumLabel) != null) {
      comps.get(coinageNecessitySumLabel).setBounds(160, 460, 120, 20);
    }
    
    if (comps.get(coinageDifferenceLabel) != null) {
      comps.get(coinageDifferenceLabel).setBounds(20, 490, 120, 20);
    }
    
    if (comps.get(coinageDifferenceSumLabel) != null) {
      comps.get(coinageDifferenceSumLabel).setBounds(160, 490, 200, 20);
    }
    
    if (comps.get(revenuesWithTipLabel) != null) {
      comps.get(revenuesWithTipLabel).setBounds(300, 460, 120, 20);
    }
    
    if (comps.get(revenuesWithTipSumLabel) != null) {
      comps.get(revenuesWithTipSumLabel).setBounds(440, 460, 120, 20);
    }
    
    if (comps.get(totalCashNecessityLabel) != null) {
      comps.get(totalCashNecessityLabel).setBounds(300, 490, 120, 20);
    }
    
    if (comps.get(totalCashNecessitySumLabel) != null) {
      comps.get(totalCashNecessitySumLabel).setBounds(440, 490, 120, 20);
    }
    
    if (comps.get(tipLabel) != null) {
      comps.get(tipLabel).setBounds(300, 520, 120, 20);
    }
    
    if (comps.get(tipSumLabel) != null) {
      comps.get(tipSumLabel).setBounds(440, 520, 120, 20);
    }
    
    if (comps.get(calcButton) != null) {
      comps.get(calcButton).setBounds(20, 600, 260, 50);
    }
    
    if (comps.get(resetButton) != null) {
      comps.get(resetButton).setBounds(320, 600, 260, 50);
    }
  }

  @Override
  public Dimension minimumLayoutSize(Container arg0) {
    return new Dimension(600, 700);
  }

  @Override
  public Dimension preferredLayoutSize(Container arg0) {
    return new Dimension(600, 700);
  }

  @Override
  public void removeLayoutComponent(Component arg0) {
    if (comps.containsValue(arg0)) {
      comps.remove(arg0);
    } else {
      System.out.println("No such Component!");
    }
  }
  
  /**
   * Removes the Component, that is saved with the given {@code comp} as key.
   * @param comp The String for the Component to be removed.
   * @since 1.0
   */
  public void removeLayoutComponent(String comp) {
    if (comps.get(comp) != null) {
      removeLayoutComponent(comps.get(comp));
    } else {
      System.out.println("No such Component!");
    }
  }
  
  /**
   * Returns the Component that is defined by the given String. Returns {@code null}, if this 
   * Component isn't yet implemented or if it is a Component that wasn't specified.
   * @param c The String, that defines the Component to be returned.
   * @return The Component, that is defined by {@code c}, {@code null} if it isn't an 
   *      implemented Component
   * @since 1.0
   */
  public Component getComp(String c) {
    if (comps.get(c) != null) {
      return comps.get(c);
    } else {
      System.out.println("This Component is not implemented yet.");
      return null;
    }
  }
}
