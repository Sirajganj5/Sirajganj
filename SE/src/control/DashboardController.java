
package control;

import constants.Constants;
import entity.Adapt.DRONE.Real.TelloDrone;
import entity.Adapt.DRONE.Real.TelloDroneAdapter;
import entity.Adapt.DRONE.Vitualization.AnimatedDrone;
import entity.Comp.Item;
import entity.Comp.ItemComponent;
import entity.Comp.ItemContainer;
import entity.visitor.AggregateMarketValueVisitor;
import entity.visitor.AggregatePurchasePriceVisitor;
import java.io.IOException;
import java.lang.Integer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class DashboardController {
  private ItemContainer rootItemContainer;
  private AnimatedDrone animatedDrone;
  private TelloDroneAdapter telloDroneAdapter;

  @FXML
  private URL location;

  @FXML
  private ResourceBundle resources;

  @FXML
  private TextArea infoLog = new TextArea();

  private TreeItem<ItemComponent> rootTreeItem;
  private TreeItem<ItemComponent> commandCenterTreeItem;
  private TreeItem<ItemComponent> droneTreeItem;

  @FXML
  private TreeView<ItemComponent> farmTreeView = new TreeView<ItemComponent>();

  @FXML
  private TextField selectionName = new TextField();

  @FXML
  private TextField selectionLocationX = new TextField();

  @FXML
  private TextField selectionLocationY = new TextField();

  @FXML
  private TextField selectionLength = new TextField();

  @FXML
  private TextField selectionWidth = new TextField();

  @FXML
  private TextField selectionHeight = new TextField();

  @FXML
  private TextField selectionPurchasePrice = new TextField();

  @FXML
  private TextField selectionAggregatePurchasePrice = new TextField();

  @FXML
  private TextField selectionMarketValue = new TextField();

  @FXML
  private TextField selectionAggregateMarketValue = new TextField();

  private UnaryOperator<TextFormatter.Change> intFilter = new UnaryOperator<TextFormatter.Change>() {

    public TextFormatter.Change apply(TextFormatter.Change textField) {
      textField.setText(textField.getText().replaceAll("[^0-9]", ""));
      return textField;
    }
  };

  @FXML
  private Group farmMap = new Group();

  @FXML
  private RadioButton simulationModeButton = new RadioButton();

  @FXML
  private RadioButton droneModeButton = new RadioButton();

  private ToggleGroup modeToggleGroup = new ToggleGroup();

  private void initializeInputsAndOutputs() {
    infoLog.setEditable(false);
    selectionLocationX.setTextFormatter(new TextFormatter<>(intFilter));
    selectionLocationY.setTextFormatter(new TextFormatter<>(intFilter));
    selectionLength.setTextFormatter(new TextFormatter<>(intFilter));
    selectionWidth.setTextFormatter(new TextFormatter<>(intFilter));
    selectionHeight.setTextFormatter(new TextFormatter<>(intFilter));
    selectionPurchasePrice.setTextFormatter(new TextFormatter<>(intFilter));
    selectionMarketValue.setTextFormatter(new TextFormatter<>(intFilter));
    selectionAggregatePurchasePrice.setEditable(false);
    selectionAggregateMarketValue.setEditable(false);
    simulationModeButton.setSelected(true);
    simulationModeButton.setToggleGroup(modeToggleGroup);
    droneModeButton.setToggleGroup(modeToggleGroup);
  }

  private void initializeRootItemContainer() {
    // attempt loading previous rootItemContainer from file
     rootItemContainer = ItemContainer.loadJSON("rootItemContainer.json");
    if (rootItemContainer == null) {
      // create new rootItemContainer with default components
      rootItemContainer = new ItemContainer("Root");
      rootItemContainer.setLength(Constants.REAL_FARM_LENGTH);
      rootItemContainer.setWidth(Constants.REAL_FARM_WIDTH);

      ItemContainer commandCenter = new ItemContainer("Command Center");
      commandCenter.setLength(Constants.REAL_DRONE_SIZE);
      commandCenter.setWidth(Constants.REAL_DRONE_SIZE);

      Item droneItem = new Item("Drone");
      droneItem.setLength(Constants.REAL_DRONE_SIZE);
      droneItem.setWidth(Constants.REAL_DRONE_SIZE);
      droneItem.setPurchasePrice(1000);
      droneItem.setMarketValue(900);

      commandCenter.addItemComponent(droneItem);
      rootItemContainer.addItemComponent(commandCenter);
    }
  }

  private TreeItem<ItemComponent> createTreeItem(ItemComponent ic) {
    if (ic instanceof Item || ic.getComponents().size() == 0) {
      return new TreeItem<ItemComponent>(ic);
    } else {
      TreeItem<ItemComponent> ti = new TreeItem<ItemComponent>(ic);
      for (ItemComponent child : ic.getComponents()) {
        TreeItem<ItemComponent> cti = createTreeItem(child);
        String name = cti.getValue().getName();
        if (name == "Command Center") {
          commandCenterTreeItem = cti;
        } else if (name == "Drone") {
          droneTreeItem = cti;
        }
        ti.getChildren().add(cti);
      }
      ti.setExpanded(true);
      return ti;
    }
  }

  private void initializeFarmTreeView(ItemComponent root) {
    rootTreeItem = createTreeItem(root);
    farmTreeView.setRoot(rootTreeItem);
    farmTreeView.setEditable(false);
  }

  private void addRectanglesToFarmMap(ItemComponent ic) {
    String name = ic.getName();
    if (
      !name.equals("Root") &&
      !name.equals("Command Center") &&
      !name.equals("Drone")
    ) {
      farmMap.getChildren().add(ic.getRectangle());
    }
    if (ic instanceof ItemContainer) {
      for (ItemComponent child : ic.getComponents()) {
        addRectanglesToFarmMap(child);
      }
    }
  }

  private void initializeAnimatedDrone() {
    animatedDrone = new AnimatedDrone("drone.png");
    farmMap.getChildren().add(animatedDrone);
  }

  private void initializeTelloDroneAdapter() {
    TelloDrone telloDrone = null;
    try {
      telloDrone = new TelloDrone();
    } catch (IOException e) {
      e.printStackTrace();
    }
//    telloDroneAdapter = new TelloDroneAdapter(telloDrone);
    updateTelloDroneAdapterFlightFloor();
  }

  @FXML
  public void initialize() {
    initializeInputsAndOutputs();
    initializeRootItemContainer();
    initializeFarmTreeView(rootItemContainer);
    addRectanglesToFarmMap(rootItemContainer);
    initializeAnimatedDrone();
//    initializeTelloDroneAdapter();
  }

  private void addToInfoLog(String message) {
    infoLog.appendText(String.format("%s\n", message));
  }

 private void saveRootItemContainer() {
    ItemContainer.saveJSON(rootItemContainer, "rootItemContainer.json");
    addToInfoLog("Changes saved");
  }

  private int getTalletItemHeight(ItemComponent itemComponent) {
    int h = itemComponent.getHeight();
    if (itemComponent instanceof ItemContainer) {
      for (ItemComponent c : itemComponent.getComponents()) {
        int ch = getTalletItemHeight(c);
        if (ch > h) h = ch;
      }
    }
    return h;
  }

  private void updateTelloDroneAdapterFlightFloor() {
    telloDroneAdapter.setFlightFloor(getTalletItemHeight(rootItemContainer));
    addToInfoLog(
      String.format(
        "Updated drone flight floor with: %d feet",
        telloDroneAdapter.getFlightFloor()
      )
    );
  }

  private TreeItem<ItemComponent> getCurrentSelection() {
    return farmTreeView.getSelectionModel().getSelectedItem();
  }

  private void addToFarmTreeViewAndFarmMap(ItemComponent component) {
    TreeItem<ItemComponent> selection = getCurrentSelection();
    if (selection == null) {
      addToInfoLog("Failed to add; nothing is selected");
      return;
    }
    ItemComponent selectionValue = selection.getValue();
    if (selectionValue instanceof Item) addToInfoLog(
      "Failed to add; Item is selected"
    ); else { // selection is an ItemContainer
      selection.getValue().addItemComponent(component);
      TreeItem<ItemComponent> treeItem = new TreeItem<ItemComponent>(component);
      treeItem.setExpanded(true);
      selection.getChildren().add(treeItem);
      farmMap.getChildren().add(component.getRectangle());
      animatedDrone.toFront();
      updateTelloDroneAdapterFlightFloor();
      addToInfoLog(
        String.format("%s added", component.getClass().getSimpleName())
      );
      saveRootItemContainer();
    }
  }

  @FXML
  /*
   * Called when the "Add Item" button is clicked
   */
  public void addItem(ActionEvent event) {
    addToFarmTreeViewAndFarmMap(new Item());
  }

  @FXML
  /*
   * Called when the "Add ItemContainer" button is clicked
   */
  public void addItemContainer(ActionEvent event) {
    addToFarmTreeViewAndFarmMap(new ItemContainer());
  }

  @FXML
  /*
   * Called when the "Delete Selection" button is clicked
   */
  public void deleteSelection(ActionEvent event) {
    TreeItem<ItemComponent> selection = getCurrentSelection();
    if (selection == null) addToInfoLog(
      "Failed to delete; nothing is selected"
    ); else if (selection == rootTreeItem) addToInfoLog(
      "Failed to delete; Root is selected"
    ); else if (selection == commandCenterTreeItem) addToInfoLog(
      "Failed to delete; Command Center is selected"
    ); else if (selection == droneTreeItem) addToInfoLog(
      "Failed to delete; Drone is selected"
    ); else {
      ItemComponent component = selection.getValue();
      TreeItem<ItemComponent> parent = selection.getParent();
      parent.getValue().deleteItemComponent(selection.getValue());
      parent.getChildren().remove(selection);
      farmMap.getChildren().removeAll(component.getRectangles());
      updateTelloDroneAdapterFlightFloor();
      addToInfoLog("Selection deleted");
      saveRootItemContainer();
      loadSelectionDetails();
    }
  }

  private void refreshSelectionAggregatePurchasePrice(ItemComponent component) {
    AggregatePurchasePriceVisitor visitor = new AggregatePurchasePriceVisitor();
    component.acceptVisitor(visitor);
    selectionAggregatePurchasePrice.setText(
      String.format("%d", visitor.value())
    );
  }

  private void refreshSelectionAggregateMarketValue(ItemComponent component) {
    AggregateMarketValueVisitor visitor = new AggregateMarketValueVisitor();
    component.acceptVisitor(visitor);
    selectionAggregateMarketValue.setText(String.format("%d", visitor.value()));
  }

  @FXML
  /*
   * Called when the "Farm TreeView" is interacted with
   */
  public void loadSelectionDetails() {
    TreeItem<ItemComponent> selection = getCurrentSelection();
    if (selection == null) return;
    ItemComponent component = selection.getValue();
    selectionName.setText(component.getName());
    selectionLocationX.setText(String.format("%d", component.getLocationX()));
    selectionLocationY.setText(String.format("%d", component.getLocationY()));
    selectionLength.setText(String.format("%d", component.getLength()));
    selectionWidth.setText(String.format("%d", component.getWidth()));
    selectionHeight.setText(String.format("%d", component.getHeight()));
    selectionPurchasePrice.setText(
      String.format("%d", component.getPurchasePrice())
    );
    if (component instanceof Item) {
      selectionMarketValue.setText(
        String.format("%d", component.getMarketValue())
      );
      selectionMarketValue.setDisable(false);
    } else {
      selectionMarketValue.setText("");
      selectionMarketValue.setDisable(true);
    }
    refreshSelectionAggregatePurchasePrice(component);
    refreshSelectionAggregateMarketValue(component);
    addToInfoLog("Selection details loaded");
  }

  private int parseIntFromTextField(TextField textField) {
    String text = textField.getText();
    if (text.length() == 0) text = "0";
    return Integer.parseInt(text);
  }

  @FXML
  /*
   * Called when the "Update Selection" button is clicked
   */
  public void updateSelection(ActionEvent event) {
    TreeItem<ItemComponent> selection = getCurrentSelection();
    if (selection == null) addToInfoLog(
      "Failed to update; nothing is selected"
    ); else if (selection == rootTreeItem) addToInfoLog(
      "Failed to update; Root is selected"
    ); else if (selection == commandCenterTreeItem) addToInfoLog(
      "Failed to update; Command Center is selected"
    ); else if (selection == droneTreeItem) addToInfoLog(
      "Failed to update; Drone is selected"
    ); else {
      ItemComponent component = selection.getValue();
      component.setName(selectionName.getText());
      component.setLocationX(parseIntFromTextField(selectionLocationX));
      component.setLocationY(parseIntFromTextField(selectionLocationY));
      component.setLength(parseIntFromTextField(selectionLength));
      component.setWidth(parseIntFromTextField(selectionWidth));
      component.setHeight(parseIntFromTextField(selectionHeight));
      component.setPurchasePrice(parseIntFromTextField(selectionPurchasePrice));
      if (component instanceof Item) component.setMarketValue(
        parseIntFromTextField(selectionMarketValue)
      );
      selection.setValue(component);
      farmTreeView.refresh();
      farmMap.getChildren().remove(component.getRectangle());
      farmMap.getChildren().add(component.getRectangle());
      animatedDrone.toFront();
//      updateTelloDroneAdapterFlightFloor();
      addToInfoLog("Selection updated");
      saveRootItemContainer();
      loadSelectionDetails();
    }
  }

  @FXML
  /*
   * Called when the "Visit Selection button is clicked
   */
  public void visitSelection() {
    TreeItem<ItemComponent> selection = getCurrentSelection();
    if (selection == null) addToInfoLog(
      "Failed to visit; nothing is selected"
    ); else if (selection == rootTreeItem) addToInfoLog(
      "Failed to visit; Root is selected"
    ); else if (selection == commandCenterTreeItem) addToInfoLog(
      "Failed to visit; Command Center is selected"
    ); else if (selection == droneTreeItem) addToInfoLog(
      "Failed to visit; Drone is selected"
    ); else {
      if (droneModeButton.isSelected()) {
    	  logger.addHandler(handler);
  		logger.setUseParentHandlers(false);
  		
  		// Set default logging level to a bit more detailed than INFO. Logging
  		// statements at or above the set logging level will be output to
  		// the console window. INFO level is typically used to show high
  		// level informational messages. FINE is for logging of more detail, FINER
  		// for logging a lot of detail. Due to a quirk in the design of the Java
  		// logger, we have to set the desired log level in both the logger and 
  		// handler instances. The logger is used by the calling program to create
  		// log messages and the handler is used by the logger to send log messages
  		// to a destination (like the console or a file).
  		
  	  	logger.setLevel(Level.FINE);
  		handler.setLevel(Level.FINE);

  	    logger.info("start");
  	    TelloDroneAdapter demo =new TelloDroneAdapter();
  	    demo.visitor();
//        if (telloDroneAdapter.isDeployed()) addToInfoLog(
//          "Failed to visit; drone is already deployed"
//        ); else {
//          ItemComponent component = selection.getValue();
//          addToInfoLog("Deploying drone");
//          telloDroneAdapter.visitLocation(
//            component.getLocationX(),
//            component.getLocationY()
//          );
//        }
      } else {
        if (animatedDrone.isDeployed()) addToInfoLog(
          "Failed to visit; simulation is already running"
        ); else {
          ItemComponent component = selection.getValue();
          animatedDrone.visitLocation(
            component.getLocationX(),
            component.getLocationY()
          );
          addToInfoLog("Simulation started");
        }
      }
    }
  }
  private static final Logger 		logger = Logger.getGlobal();
	private static final ConsoleHandler handler = new ConsoleHandler();
  @FXML
  /*
   * Called when the "Scan Farm" button is clicked
   */
  public void scanFarm() {
    if (droneModeButton.isSelected()) {
    	logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		
		// Set default logging level to a bit more detailed than INFO. Logging
		// statements at or above the set logging level will be output to
		// the console window. INFO level is typically used to show high
		// level informational messages. FINE is for logging of more detail, FINER
		// for logging a lot of detail. Due to a quirk in the design of the Java
		// logger, we have to set the desired log level in both the logger and 
		// handler instances. The logger is used by the calling program to create
		// log messages and the handler is used by the logger to send log messages
		// to a destination (like the console or a file).
		
	  	logger.setLevel(Level.FINE);
		handler.setLevel(Level.FINE);

	    logger.info("start");
	    TelloDroneAdapter demo =new TelloDroneAdapter();
	    demo.execute();
//      if (telloDroneAdapter.isDeployed()) addToInfoLog(
//        "Failed to scan; drone is already deployed"
//      ); else {
//    	  
//        addToInfoLog("Deploying drone");
////        telloDroneAdapter.scanFarm();
//      }
    } else {
      if (animatedDrone.isDeployed()) addToInfoLog(
        "Failed to scan; simulation is already running"
      ); else {
        animatedDrone.scanFarm();
        addToInfoLog("Simulation started");
      }
    }
    
  }
}
