package entity.visitor;

import entity.composite.Item;
import entity.composite.ItemComponent;

public class AggregateMarketValueVisitor extends ItemComponentVisitor {
  private int total = 0; // dollars

  public void visit(ItemComponent itemComponent) {
    if (itemComponent instanceof Item) {
      total += itemComponent.getMarketValue();
    } else {
      for (ItemComponent c : itemComponent.getComponents()) visit(c);
    }
  }

  public int value() {
    return total;
  }
}
