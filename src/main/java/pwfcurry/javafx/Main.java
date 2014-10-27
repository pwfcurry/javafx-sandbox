package pwfcurry.javafx;

import static com.google.common.base.Preconditions.checkArgument;
import static pwfcurry.javafx.Main.Utils.capitaliseEnum;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
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
import lombok.Data;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
		label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> label.setText("Expanded row count: " + treeTableView.getExpandedItemCount()));
		
		root.add(label, 0,0);
		root.add(tableContainer, 0,2);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private TreeTableView<TreeValue> createTable() {
		TreeItem<TreeValue> root = new TreeItem<>(new TreeValue("Root", ColourProperty.ROOT, TypeProperty.ROOT, CostProperty.ROOT));
		root.getChildren().addAll(createChildren(100));
		
		TreeTableView<TreeValue> tableView = new TreeTableView<>(root);
		
		TreeTableColumn<TreeValue,String> nameColumn = new TreeTableColumn<>("Name");
		nameColumn.setCellValueFactory(cellDataFeatures -> StringConstant.valueOf(cellDataFeatures.getValue().getValue().getName()));
		
		tableView.getColumns().addAll(
				nameColumn,
				createAttributeColumn("Colour", TreeValue::getColour),
				createAttributeColumn("Type", TreeValue::getType),
				createAttributeColumn("Cost", TreeValue::getCost)
		);
		
		return tableView;
	}
	
	private TreeTableColumn<TreeValue,String> createAttributeColumn(String name, Function<TreeValue,TableCellValue> attribute) {
		TreeTableColumn<TreeValue,String> valueColumn = new TreeTableColumn<>(name);
		valueColumn.setCellValueFactory(cellDataFeatures -> getStringConstant(cellDataFeatures, attribute));
		return valueColumn;
	}
	
	private StringConstant getStringConstant(CellDataFeatures<TreeValue,String> cellDataFeatures, Function<TreeValue,TableCellValue> attribute) {
		return StringConstant.valueOf(attribute.apply(cellDataFeatures.getValue().getValue()).getValue());
	}
	
	
	
	private List<TreeItem<TreeValue>> createChildren(int childrenCount) {
		List<TreeItem<TreeValue>> allTreeItems = range(1, childrenCount).stream().map(this::createTreeItem).collect(toList());
		return group(allTreeItems, TreeValue::getColour);
	}
	
	private List<TreeItem<TreeValue>> group(List<TreeItem<TreeValue>> allTreeItems, Function<TreeValue,TableCellValue> attribute) {
		return group1(allTreeItems, TreeItem::getValue, attribute.andThen(TableCellValue::getValue));
	}
	
	private List<TreeItem<TreeValue>> group1(List<TreeItem<TreeValue>> allTreeItems, Function<TreeItem<TreeValue>,TreeValue> valFromItem, Function<TreeValue,String> attribute) {
		return group2(allTreeItems, valFromItem.andThen(attribute));
	}
	
	private List<TreeItem<TreeValue>> group2(List<TreeItem<TreeValue>> allTreeItems, Function<TreeItem<TreeValue>,String> attribute) {
		Map<String,TreeItem<TreeValue>> map = allTreeItems.stream().collect(toMap(attribute::apply, Function.identity()));
		return collect;
	}
	
	private TreeItem<TreeValue> createTreeItem(Integer integer) {
		return new TreeItem<>(new TreeValue("val "+ integer, random(ColourProperty.class), random(TypeProperty.class), random(CostProperty.class)));
	}
	
	private static <T extends Enum> T random(Class<T> clazz) {
		T[] enumConstants = clazz.getEnumConstants();
		return enumConstants[new Random().nextInt(enumConstants.length)];
	} 
	
//	private List<TreeItem<TreeValue>> createChildren(int childrenCount) { // max 13
//		checkArgument(childrenCount > 0);
//		return range(1, childrenCount).stream().map(integer -> {
//			TreeValue treeValue = new TreeValue("ROOT", null, null);
//			TreeItem<TreeValue> item = new TreeItem<>(treeValue);
//			if (integer > 1) {
//				item.getChildren().addAll(createChildren(integer-1));
//			}
//			return item;
//		}).collect(toList());
//	}
	
	private static ContiguousSet<Integer> range(int from, int to) {
		return ContiguousSet.create(Range.closed(from, to), DiscreteDomain.integers());
	}
	
	
	@Data
	private static class TreeValue {
		private final String name;
		private final ColourProperty colour;
		private final TypeProperty type;
		private final CostProperty cost;
	}
	
	
	private interface TableCellValue {
		String getValue();
	}
	
	
	private enum ColourProperty implements TableCellValue {
		
		ROOT, WHITE, BLUE, BLACK, RED, GREEN;
		
		@Override
		public String getValue() {
			return capitaliseEnum(this);
		}
		
	}
	
	
	private enum TypeProperty implements TableCellValue {
		
		ROOT, Instant, Sorcery, Creature, Enchantment, Land;
		
		@Override
		public String getValue() {
			return capitaliseEnum(this);
		}
		
	}
	
	
	private enum CostProperty implements TableCellValue {
		
		ROOT,ONE,TWO,THREE,FOUR,FIVE;
		
		@Override
		public String getValue() {
			return capitaliseEnum(this);
		}
		
	}
	
	
	public static class Utils {
		
		public static String capitaliseEnum(Enum<?> e) {
			return WordUtils.capitalizeFully(e.name().replace('_', ' '));
		}
		
	}
	
	
	// todo need to do better
	
	private static interface MyTreeValue {
		String getName();
		TableCellValue getColour();
		TableCellValue getType();
		TableCellValue getCost();
	}
	
	private static class Node implements MyTreeValue {
		
		@Override
		public String getName() {
			return "node";
		}
		
		@Override
		public TableCellValue getColour() {
			return () -> "colour";
		}
		
		@Override
		public TableCellValue getType() {
			return () -> "type";
		}
		
		@Override
		public TableCellValue getCost() {
			return () -> "cost";
		}
	}
	
	@Data
	private static class Leaf implements MyTreeValue {
		@Delegate
		private final TreeValue treeValue;
	}
	
	
	
}
