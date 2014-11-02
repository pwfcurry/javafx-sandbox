package pwfcurry.javafx.treeitem;

import javafx.scene.control.TreeItem;
import pwfcurry.javafx.treevalue.Node;

import java.util.List;

public class ItemNode extends TreeItem<Node> {
	
	public ItemNode(Node node, List<TreeItem<Node>> children) {
		super(node);
		getChildren().addAll(children);
	}
	
}
