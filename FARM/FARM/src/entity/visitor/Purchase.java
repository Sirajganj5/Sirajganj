package entity.visitor;

import entity.composite.ItemComponent;
import entity.composite.ItemContainer;

public class AggregatePurchasePriceVisitor extends ItemComponentVisitor {
  private int total = 0; // dollars

  public void visit(ItemComponent itemComponent) {
    total += itemComponent.getPurchasePrice();
    if (itemComponent instanceof ItemContainer) {
      for (ItemComponent c : itemComponent.getComponents()) visit(c);
    }
  }

  public int value() {
    return total;
  }
}
