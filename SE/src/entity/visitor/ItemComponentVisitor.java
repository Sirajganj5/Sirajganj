package entity.visitor;

import entity.Comp.ItemComponent;

public abstract class ItemComponentVisitor {

  public abstract void visit(ItemComponent itemComponent);
}
