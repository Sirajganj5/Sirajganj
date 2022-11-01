package entity.composite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

public class ItemContainer extends ItemComponent {
  private String type = "ItemContainer"; // for deserialization
  private ArrayList<ItemComponent> components = new ArrayList<ItemComponent>();

  public ItemContainer() {
    rectangle.setOpacity(0.5);
  }

  public ItemContainer(String name) {
    rectangle.setOpacity(0.5);
    setName(name);
  }

  public ArrayList<Rectangle> getRectangles() {
    ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    rectangles.add(getRectangle());
    for (ItemComponent c : components) rectangles.addAll(c.getRectangles());
    return rectangles;
  }

  public void setMarketValue(int marketValue)
    throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
      "Cannot set the marketValue of an ItemContainer!"
    );
  }

  public int getMarketValue() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
      "Cannot get the marketValue of an ItemContainer!"
    );
  }

  public void addItemComponent(ItemComponent itemComponent) {
    this.components.add(itemComponent);
  }

  public void deleteItemComponent(ItemComponent itemComponent) {
    this.components.remove(itemComponent);
  }

  public ArrayList<ItemComponent> getComponents()
    throws UnsupportedOperationException {
    return components;
  }

  public static void saveJSON(ItemContainer itemContainer, String filePath) {
    // allow Rectangle objects to be serialized
    JsonSerializer<Rectangle> rectangleSerializer = new JsonSerializer<Rectangle>() {

      public JsonElement serialize(
        Rectangle src,
        Type typeOfSrc,
        JsonSerializationContext context
      ) {
        JsonObject jsonRectangle = new JsonObject();
        jsonRectangle.addProperty("x", src.getX());
        jsonRectangle.addProperty("y", src.getY());
        jsonRectangle.addProperty("width", src.getWidth());
        jsonRectangle.addProperty("height", src.getHeight());
        jsonRectangle.addProperty("opacity", src.getOpacity());
        return jsonRectangle;
      }
    };

    // create a GsonBuilder with our custom settings and use it to serialize the ItemContainer
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Rectangle.class, rectangleSerializer);
    Gson gson = gsonBuilder.create();
    String json = gson.toJson(itemContainer);

    // write the serialized ItemContainer string to a file at filePath
    FileWriter writer = null;
    try {
      writer = new FileWriter(filePath);
      writer.write(json);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.flush();
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static ItemContainer loadJSON(String filePath) {
    // allow Rectangle objects to be deserialized
    JsonDeserializer<Rectangle> rectangleDeserializer = new JsonDeserializer<Rectangle>() {

      public Rectangle deserialize(
        JsonElement json,
        Type typeOfT,
        JsonDeserializationContext context
      )
        throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Rectangle rectangle = new Rectangle(
          jsonObject.get("x").getAsDouble(),
          jsonObject.get("y").getAsDouble(),
          jsonObject.get("width").getAsDouble(),
          jsonObject.get("height").getAsDouble()
        );
        rectangle.setOpacity(jsonObject.get("opacity").getAsDouble());
        return rectangle;
      }
    };

    // allow ItemComponent components to be deserialized as Items and ItemContainers
    RuntimeTypeAdapterFactory<ItemComponent> itemComponentAdapterFactory = RuntimeTypeAdapterFactory
      .of(ItemComponent.class, "type")
      .registerSubtype(Item.class, "Item")
      .registerSubtype(ItemContainer.class, "ItemContainer");

    // open the file that contains a serialized ItemContainer
    Reader reader = null;
    try {
      reader = Files.newBufferedReader(Paths.get(filePath));
    } catch (NoSuchFileException e) {
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }

    // create a GsonBuilder with our custom settings and use it to deserialize the file
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Rectangle.class, rectangleDeserializer);
    gsonBuilder.registerTypeAdapterFactory(itemComponentAdapterFactory);
    Gson gson = gsonBuilder.create();
    ItemContainer itemContainer = gson.fromJson(reader, ItemContainer.class);

    // close the file
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // return the deserialized ItemContainer
    return itemContainer;
  }
}
