package pwfcurry.javafx;

import static java.util.stream.Collectors.toList;
import static pwfcurry.javafx.Utils.random;

import com.google.common.collect.Lists;
import com.sun.javafx.binding.StringConstant;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import pwfcurry.javafx.property.ColourProperty;
import pwfcurry.javafx.property.CostProperty;
import pwfcurry.javafx.property.GroupingProperty;
import pwfcurry.javafx.property.TypeProperty;
import pwfcurry.javafx.property.Value;
import pwfcurry.javafx.treevalue.Leaf;
import pwfcurry.javafx.treevalue.Node;
import pwfcurry.javafx.treevalue.TreeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GroupingTable extends TreeTableView<TreeValue> {

	public GroupingTable() {
		setupTable();
	}

	private static Leaf createLeaf(Integer integer) {
		return new Leaf(
				"val " + integer,
				random(ColourProperty.class),
				random(TypeProperty.class),
				random(CostProperty.class));
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
		List<GroupingProperty> groupingProperties = Lists.newArrayList(GroupingProperty.COLOUR, GroupingProperty.TYPE);
		List<TreeValue> leaves = createRandomLeaves(childCount);
		return createChildren(leaves, groupingProperties);
	}

	private static List<TreeItem<TreeValue>> createChildren(
			List<TreeValue> treeValues,
			List<GroupingProperty> remaining)
	{
		if (remaining.isEmpty()) {
			return createLeaves(treeValues);
		}
		else {
			GroupingProperty groupingProperty = remaining.remove(0);
			Map<Value,List<TreeValue>> groupedLeaves = groupTreeItems(treeValues, groupingProperty);
			return createNodes(groupedLeaves, remaining);
		}
	}

	private static List<TreeItem<TreeValue>> createNodes(
			Map<Value,List<TreeValue>> groupedLeaves,
			List<GroupingProperty> remaining)
	{
		List<TreeItem<TreeValue>> treeNodes = new ArrayList<>();
		groupedLeaves.forEach((value, treeValues) -> {
			Node node = new Node(value);
			TreeItem<TreeValue> treeNode = new TreeItem<>(node);
			treeNode.getChildren().addAll(createChildren(treeValues, new ArrayList<>(remaining)));
			treeNodes.add(treeNode);
		});
		return treeNodes;
	}

	private static List<TreeItem<TreeValue>> createLeaves(List<TreeValue> treeValues) {
		return treeValues.stream().map(TreeItem::new).collect(toList());
	}

	private List<TreeValue> createRandomLeaves(int childrenCount) {
		return Utils.range(1, childrenCount).stream().
				map(GroupingTable::createLeaf).
				collect(toList());
	}

	private static Map<Value,List<TreeValue>> groupTreeItems(List<TreeValue> list, GroupingProperty groupingProperty) {
		return groupByFunction(groupingProperty.getGroupingFunction(), list);
	}

	private static <A, B> Map<A,List<B>> groupByFunction(Function<B,A> groupingFunction, List<B> list) {
		return list.stream().collect(
				HashMap::new,
				(map, item) -> map.computeIfAbsent(groupingFunction.apply(item), key -> new ArrayList<>()).add(item),
				(firstMap, secondMap) -> firstMap.forEach((key, value) -> value.addAll(secondMap.get(key)))
		);
	}

}
