package pwfcurry.javafx.property;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public enum CostProperty implements Value {

	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);

	private final Integer value;
	
	@Override
	public String getValue() {
		return value.toString();
	}

}
