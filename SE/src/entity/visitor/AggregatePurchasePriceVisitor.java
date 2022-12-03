package entity.visitor;

import entity.Comp.ItemComponent;
import entity.Comp.ItemContainer;

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
