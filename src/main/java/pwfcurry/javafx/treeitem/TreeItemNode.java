package pwfcurry.javafx.treeitem;

import javafx.scene.control.TreeItem;
import pwfcurry.javafx.treevalue.Node;

import java.util.List;

public class TreeItemNode extends TreeItem<Node> {
	
	public TreeItemNode(Node node, List<TreeItem<Node>> children) {
		super(node);
		getChildren().addAll(children);
	}
	
}
