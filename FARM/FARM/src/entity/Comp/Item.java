package entity.composite;

import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public class Item extends ItemComponent {
  private String type = "Item"; // for ItemContainer deserialization
  protected int marketValue = 0; // dollars

  public Item() {
    rectangle.setOpacity(0.5);
  }

  public Item(String name) {
    rectangle.setOpacity(0.5);
    setName(name);
  }

  public ArrayList<Rectangle> getRectangles() {
    ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    rectangles.add(getRectangle());
    return rectangles;
  }

  public void setMarketValue(int marketValue)
    throws UnsupportedOperationException {
    this.marketValue = marketValue;
  }

  public int getMarketValue() throws UnsupportedOperationException {
    return marketValue;
  }

  public void addItemComponent(ItemComponent itemComponent)
    throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
      "Cannot add an ItemComponent to an Item!"
    );
  }

  public void deleteItemComponent(ItemComponent itemComponent)
    throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
      "Cannot delete an ItemComponent from an Item!"
    );
  }

  public ArrayList<ItemComponent> getComponents()
    throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
      "Cannot get components of an Item!"
    );
  }
}
