package pwfcurry.javafx;

import static java.util.stream.Collectors.toList;

import javafx.scene.control.TreeItem;
import pwfcurry.javafx.property.GroupingProperty;
import pwfcurry.javafx.property.Value;
import pwfcurry.javafx.treevalue.Node;
import pwfcurry.javafx.treevalue.TreeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Grouper {

	public List<TreeItem<TreeValue>> group(
			List<TreeValue> treeValues,
			List<GroupingProperty> groupingProperties)
	{
		return group1(treeValues, new ArrayList<>(groupingProperties));
	}
	
	private static List<TreeItem<TreeValue>> group1(
			List<TreeValue> treeValues,
			List<GroupingProperty> groupingProperties)
	{
		if (groupingProperties.isEmpty()) {
			return createLeaves(treeValues);
		}
		else {
			GroupingProperty groupingProperty = groupingProperties.remove(0);
			Map<Value,List<TreeValue>> groupedLeaves = groupTreeItems(treeValues, groupingProperty);
			return createNodes(groupedLeaves, groupingProperties);
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
			treeNode.getChildren().addAll(group1(treeValues, new ArrayList<>(remaining)));
			treeNodes.add(treeNode);
		});
		return treeNodes;
	}

	private static List<TreeItem<TreeValue>> createLeaves(List<TreeValue> treeValues) {
		return treeValues.stream().map(TreeItem::new).collect(toList());
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
