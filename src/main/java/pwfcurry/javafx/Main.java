package pwfcurry.javafx;

import static java.util.stream.Collectors.toList;
import static pwfcurry.javafx.Utils.compose;

import com.sun.javafx.binding.StringConstant;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pwfcurry.javafx.property.ColourProperty;
import pwfcurry.javafx.property.CostProperty;
import pwfcurry.javafx.property.TypeProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();

		StackPane tableContainer = new StackPane();
		TreeTableView<TreeValue> treeTableView = createTable();
		tableContainer.getChildren().add(treeTableView);

		Label label = new Label("Expanded row count...");
		label.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> label.setText("Expanded row count: " + treeTableView.getExpandedItemCount()));

		root.add(label, 0, 0);
		root.add(tableContainer, 0, 2);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private TreeTableView<TreeValue> createTable() {
		TreeItem<TreeValue> root = new TreeItem<>(new Node(() -> "ROOT"));
		root.getChildren().addAll(createChildren(100));

		TreeTableView<TreeValue> tableView = new TreeTableView<>(root);

		TreeTableColumn<TreeValue,String> nameColumn = new TreeTableColumn<>("Name");
		nameColumn.setCellValueFactory(
				cellDataFeatures -> StringConstant.valueOf(cellDataFeatures.getValue().getValue().getName()));

		tableView.getColumns().addAll(
				nameColumn,
				createAttributeColumn("Colour", TreeValue::getColour),
				createAttributeColumn("Type", TreeValue::getType),
				createAttributeColumn("Cost", TreeValue::getCost)
		);

		return tableView;
	}

	private TreeTableColumn<TreeValue,String> createAttributeColumn(
			String name,
			Function<TreeValue,TableCellValue> attribute)
	{
		TreeTableColumn<TreeValue,String> valueColumn = new TreeTableColumn<>(name);
		valueColumn.setCellValueFactory(cellDataFeatures -> getStringConstant(cellDataFeatures, attribute));
		return valueColumn;
	}

	private StringConstant getStringConstant(
			CellDataFeatures<TreeValue,String> cellDataFeatures,
			Function<TreeValue,TableCellValue> attribute)
	{
		return StringConstant.valueOf(attribute.apply(cellDataFeatures.getValue().getValue()).getValue());
	}


	private List<TreeItem<TreeValue>> createChildren(int childrenCount) {
		List<TreeItem<TreeValue>> allTreeItems = Utils.range(1, childrenCount).stream().
				map(this::createTreeItem).
				collect(toList());
		return group(allTreeItems, compose(TreeItem::getValue, TreeValue::getColour));
	}

	private List<TreeItem<TreeValue>> group(
			List<TreeItem<TreeValue>> allTreeItems,
			Function<TreeItem<TreeValue>,TableCellValue> groupingAttribute)
	{
		Map<TableCellValue,List<TreeItem<TreeValue>>> map = allTreeItems.stream().collect(
				HashMap::new,
				(map1, treeItem) -> map1.get(groupingAttribute.apply(treeItem)).add(treeItem),
				(firstMap, secondMap) -> firstMap.forEach((key, value) -> value.addAll(secondMap.get(key)))
		);

		return map.entrySet().stream().map(entry -> {
			TableCellValue property = entry.getKey();
			List<TreeItem<TreeValue>> treeItems = entry.getValue();
			TreeItem<TreeValue> propertyNode = new TreeItem<>(new Node(property));
			propertyNode.getChildren().addAll(treeItems);
			return propertyNode;
		}).collect(toList());
	}

	private TreeItem<TreeValue> createTreeItem(Integer integer) {
		return new TreeItem<>(new Leaf(
				"val " + integer,
				Utils.random(ColourProperty.class),
				Utils.random(TypeProperty.class),
				Utils.random(CostProperty.class)));
	}

}
