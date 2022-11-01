package entity.visitor;

import entity.composite.ItemComponent;

public abstract class ItemComponentVisitor {

  public abstract void visit(ItemComponent itemComponent);
}
