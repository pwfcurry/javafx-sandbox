package pwfcurry.javafx.treeitem;

import javafx.scene.control.TreeItem;
import pwfcurry.javafx.treevalue.Leaf;

import java.util.List;

public class ItemLeaf extends TreeItem<Leaf> {
	
	public ItemLeaf(Leaf leaf, List<TreeItem<Leaf>> children) {
		super(leaf);
		getChildren().addAll(children);
	}
	
}
