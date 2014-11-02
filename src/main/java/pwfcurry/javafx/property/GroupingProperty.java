package pwfcurry.javafx.property;

import pwfcurry.javafx.treevalue.TreeValue;

import java.util.function.Function;

public interface GroupingProperty extends Function<TreeValue,TableCellValue> {}
