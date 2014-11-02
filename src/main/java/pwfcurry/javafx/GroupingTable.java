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
import pwfcurry.javafx.property.GroupingFunction;
import pwfcurry.javafx.property.GroupingProperty;
import pwfcurry.javafx.property.Value;
import pwfcurry.javafx.property.TypeProperty;
import pwfcurry.javafx.treevalue.Leaf;
import pwfcurry.javafx.treevalue.Node;
import pwfcurry.javafx.treevalue.TreeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

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

		getColumns().add(nameColumn);
		getColumns().addAll(Stream.of(GroupingProperty.values()).map(this::createAttributeColumn).collect(toList()));
	}

	private TreeTableColumn<TreeValue,String> createAttributeColumn(GroupingProperty groupingProperty) {
		TreeTableColumn<TreeValue,String> valueColumn = new TreeTableColumn<>(groupingProperty.getValue());
		valueColumn.setPrefWidth(85);
		valueColumn.setCellValueFactory(cellDataFeatures -> StringConstant.valueOf(groupingProperty.apply(
				cellDataFeatures.getValue().getValue()).getValue()));
		return valueColumn;
	}

	private List<TreeItem<TreeValue>> createChildren(int childCount) {
		List<GroupingProperty> groupingProperties = Arrays.asList(GroupingProperty.values());
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
	
	// TODO recursive
	private List<TreeItem<TreeValue>> group(
			List<TreeItem<TreeValue>> list,
			GroupingProperty groupingProperty,
			List<GroupingProperty> remainingGroupingProperties)
	{
		Map<Value,List<TreeItem<TreeValue>>> grouped = groupTreeItems(list, groupingProperty);
		grouped.forEach((value, treeItems) -> {
			group(treeItems, PROPERTY, remainingProperties)
		});
		return null;
	}
	
	private Map<Value,List<TreeItem<TreeValue>>> groupTreeItems(
			List<TreeItem<TreeValue>> list,
			GroupingProperty groupingProperty)
	{
		return groupByFunction(list, compose(TreeItem::getValue, groupingProperty.getGroupingFunction()));
	}

	private static <B,A> Map<B,List<A>> groupByFunction(List<A> list, Function<A,B> groupingFunction) {
		return list.stream().collect(
				HashMap::new,
				(map, item) -> map.computeIfAbsent(groupingFunction.apply(item), key -> new ArrayList<>()).add(item),
				(firstMap, secondMap) -> firstMap.forEach((key, value) -> value.addAll(secondMap.get(key)))
		);
	}
	
	private List<TreeItem<TreeValue>> createNodesForGroups(
			Map<Value,List<TreeItem<TreeValue>>> groups,
			List<GroupingFunction> remainingGroupingProperties)
	{
		return groups.entrySet().stream().map(entry -> {
			Value groupingProperty = entry.getKey();
			List<TreeItem<TreeValue>> treeItems = entry.getValue();
			TreeItem<TreeValue> propertyNode = new TreeItem<>(new Node(groupingProperty));
			propertyNode.getChildren().addAll(treeItems);
			return propertyNode;
		}).collect(toList());
	}
	
}
