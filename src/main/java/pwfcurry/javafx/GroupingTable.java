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
import pwfcurry.javafx.treevalue.Leaf;
import pwfcurry.javafx.treevalue.Node;
import pwfcurry.javafx.treevalue.TreeValue;

import java.util.List;
import java.util.stream.Stream;

public class GroupingTable extends TreeTableView<TreeValue> {

	private final Grouper grouper;
	
	public GroupingTable() {
		grouper = new Grouper();
		setupTable();
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
		return grouper.group(leaves, groupingProperties);
	}

	private List<TreeValue> createRandomLeaves(int childrenCount) {
		return Utils.range(1, childrenCount).stream().
				map(GroupingTable::createLeaf).
				collect(toList());
	}

	private static Leaf createLeaf(Integer integer) {
		return createLeaf("val " + integer);
	}
	
	public static Leaf createLeaf(String name) {
		return new Leaf(
				name,
				random(ColourProperty.class),
				random(TypeProperty.class),
				random(CostProperty.class));
	}

}
