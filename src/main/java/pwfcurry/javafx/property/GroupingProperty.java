package pwfcurry.javafx.property;

import static pwfcurry.javafx.Utils.capitaliseEnum;

import lombok.Getter;
import pwfcurry.javafx.treevalue.TreeValue;

@Getter
public enum GroupingProperty /*implements EnumValue*/ {
	
	COLOUR(TreeValue::getColour),
	
	TYPE(TreeValue::getType),
	
	COST(TreeValue::getCost);

	public String getValue() {
		return capitaliseEnum(this);
	}
	
	private final GroupingFunction groupingFunction;
	
	private GroupingProperty(GroupingFunction groupingFunction) {
		this.groupingFunction = groupingFunction;
	}
	
	public Value apply(TreeValue treeValue) {
		return groupingFunction.apply(treeValue);
	}
	
}
