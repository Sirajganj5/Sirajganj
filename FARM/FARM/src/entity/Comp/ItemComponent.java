package entity.composite;

import constants.Constants;
import entity.visitor.ItemComponentVisitor;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public abstract class ItemComponent {
  protected String name = "Unnamed";
  protected int locationX = 0; // feet
  protected int locationY = 0; // feet
  protected int length = Constants.REAL_ITEM_LENGTH_MIN; // feet
  protected int width = Constants.REAL_ITEM_WIDTH_MIN; // feet
  protected int height = Constants.REAL_ITEM_HEIGHT_MIN; // feet
  protected Rectangle rectangle = new Rectangle(
    0,
    0,
    Constants.SCREEN_ITEM_LENGTH_MIN,
    Constants.SCREEN_ITEM_WIDTH_MIN
  ); // 2D representation
  protected int purchasePrice = 0; // dollars

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setLocationX(int x) {
    if (x < 0) x = 0;
    if (x > Constants.REAL_ITEM_X_BOUND) x = Constants.REAL_ITEM_X_BOUND;
    locationX = x;
    rectangle.setX(x * Constants.PIXELS_PER_FOOT);
  }

  public int getLocationX() {
    return locationX;
  }

  public void setLocationY(int y) {
    if (y < 0) y = 0;
    if (y > Constants.REAL_ITEM_Y_BOUND) y = Constants.REAL_ITEM_Y_BOUND;
    locationY = y;
    rectangle.setY(y * Constants.PIXELS_PER_FOOT);
  }

  public int getLocationY() {
    return locationY;
  }

  public void setLength(int length) {
    if (length < Constants.REAL_ITEM_LENGTH_MIN) length =
      Constants.REAL_ITEM_LENGTH_MIN;
    if (length > Constants.REAL_ITEM_LENGTH_MAX) length =
      Constants.REAL_ITEM_LENGTH_MAX;
    this.length = length;
    rectangle.setHeight(length * Constants.PIXELS_PER_FOOT);
  }

  public int getLength() {
    return length;
  }

  public void setWidth(int width) {
    if (width < Constants.REAL_ITEM_WIDTH_MIN) width =
      Constants.REAL_ITEM_WIDTH_MIN;
    if (width > Constants.REAL_ITEM_WIDTH_MAX) width =
      Constants.REAL_ITEM_WIDTH_MAX;
    this.width = width;
    rectangle.setWidth(width * Constants.PIXELS_PER_FOOT);
  }

  public int getWidth() {
    return width;
  }

  public void setHeight(int height) {
    if (height < Constants.REAL_ITEM_HEIGHT_MIN) height =
      Constants.REAL_ITEM_HEIGHT_MIN;
    if (height > Constants.REAL_ITEM_HEIGHT_MAX) height =
      Constants.REAL_ITEM_HEIGHT_MAX;
    this.height = height;
  }

  public int getHeight() {
    return height;
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public abstract ArrayList<Rectangle> getRectangles();

  public void setPurchasePrice(int purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public int getPurchasePrice() {
    return purchasePrice;
  }

  public abstract void setMarketValue(int marketValue)
    throws UnsupportedOperationException;

  public abstract int getMarketValue() throws UnsupportedOperationException;

  public abstract void addItemComponent(ItemComponent itemComponent)
    throws UnsupportedOperationException;

  public abstract void deleteItemComponent(ItemComponent itemComponent)
    throws UnsupportedOperationException;

  public abstract ArrayList<ItemComponent> getComponents()
    throws UnsupportedOperationException;

  public String toString() {
    return String.format("%s (%s)", getName(), getClass().getSimpleName());
  }

  public void acceptVisitor(ItemComponentVisitor visitor) {
    visitor.visit(this);
  }
}
