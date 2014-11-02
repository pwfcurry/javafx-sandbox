package pwfcurry.javafx;

import static java.util.stream.Collectors.toList;
import static pwfcurry.javafx.Utils.compose;
import static pwfcurry.javafx.Utils.random;

import com.sun.javafx.binding.StringConstant;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import pwfcurry.javafx.property.ColourProperty;
import pwfcurry.javafx.property.CostProperty;
import pwfcurry.javafx.property.GroupingProperty;
import pwfcurry.javafx.property.TableCellValue;
import pwfcurry.javafx.property.TypeProperty;
import pwfcurry.javafx.treevalue.Leaf;
import pwfcurry.javafx.treevalue.Node;
import pwfcurry.javafx.treevalue.TreeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GroupingTable extends TreeTableView<TreeValue> {

	public GroupingTable() {
		setupTable();
	}

	private static TreeItem<TreeValue> createTreeItem(Integer integer) {
		return new TreeItem<>(new Leaf(
				"val " + integer,
				random(ColourProperty.class),
				random(TypeProperty.class),
				random(CostProperty.class)
		));
	}

	private void setupTable() {
		TreeItem<TreeValue> root = new TreeItem<>(new Node(() -> "ROOT"));
		root.getChildren().addAll(createChildren(100));
		setRoot(root);

		TreeTableColumn<TreeValue,String> nameColumn = new TreeTableColumn<>("Name");
		nameColumn.setCellValueFactory(
				cellDataFeatures -> StringConstant.valueOf(cellDataFeatures.getValue().getValue().getName()));
		nameColumn.setPrefWidth(150);

		getColumns().addAll(
				nameColumn,
				createAttributeColumn("Colour", TreeValue::getColour),
				createAttributeColumn("Type", TreeValue::getType),
				createAttributeColumn("Cost", TreeValue::getCost)
		);
	}

	private TreeTableColumn<TreeValue,String> createAttributeColumn(
			String name,
			GroupingProperty attribute)
	{
		TreeTableColumn<TreeValue,String> valueColumn = new TreeTableColumn<>(name);
		valueColumn.setPrefWidth(85);
		valueColumn.setCellValueFactory(cellDataFeatures -> getStringConstant(cellDataFeatures, attribute));
		return valueColumn;
	}

	private StringConstant getStringConstant(
			CellDataFeatures<TreeValue,String> cellDataFeatures,
			GroupingProperty attribute)
	{
		return StringConstant.valueOf(attribute.apply(cellDataFeatures.getValue().getValue()).getValue());
	}

	private List<TreeItem<TreeValue>> createChildren(int childCount) {
		List<GroupingProperty> groupingProperties = Arrays.asList(
				TreeValue::getType,
				TreeValue::getCost,
				TreeValue::getColour
		);

		GroupingProperty firstProperty = groupingProperties.remove(0);

		return group(
				createRandomTreeItems(childCount),
				firstProperty,
				groupingProperties
		);
	}
	
	private List<TreeItem<TreeValue>> createRandomTreeItems(int childrenCount) {
		return Utils.range(1, childrenCount).stream().
				map(GroupingTable::createTreeItem).
				collect(toList());
	}
	
	private List<TreeItem<TreeValue>> group(
			List<TreeItem<TreeValue>> list,
			GroupingProperty groupingProperty,
			List<GroupingProperty> remainingGroupingProperties)
	{
		Map<TableCellValue,List<TreeItem<TreeValue>>> grouped = groupTreeItems(list, groupingProperty);
		grouped.forEach((tableCellValue, treeItems) -> {
//			group(treeItems, PROPERTY, remainingProperties)
		});
	}
	
	private Map<TableCellValue,List<TreeItem<TreeValue>>> groupTreeItems(
			List<TreeItem<TreeValue>> list,
			GroupingProperty groupingProperty)
	{
		return groupByFunction(list, compose(TreeItem::getValue, groupingProperty));
	}

	private static <B,A> Map<B,List<A>> groupByFunction(List<A> list, Function<A,B> groupingFunction) {
		return list.stream().collect(
				HashMap::new,
				(map, item) -> map.computeIfAbsent(groupingFunction.apply(item), key -> new ArrayList<>()).add(item),
				(firstMap, secondMap) -> firstMap.forEach((key, value) -> value.addAll(secondMap.get(key)))
		);
	}
	
	private List<TreeItem<TreeValue>> createNodesForGroups(
			Map<TableCellValue,List<TreeItem<TreeValue>>> groups,
			List<GroupingProperty> remainingGroupingProperties)
	{
		return groups.entrySet().stream().map(entry -> {
			TableCellValue groupingProperty = entry.getKey();
			List<TreeItem<TreeValue>> treeItems = entry.getValue();
			TreeItem<TreeValue> propertyNode = new TreeItem<>(new Node(groupingProperty));
			propertyNode.getChildren().addAll(treeItems);
			return propertyNode;
		}).collect(toList());
	}
	
}
