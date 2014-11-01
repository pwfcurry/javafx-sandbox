package pwfcurry.javafx;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.util.Random;
import java.util.function.Function;

public interface Utils {

	public static String capitaliseEnum(Enum<?> e) {
		return capitalizeFully(e.name().replace('_', ' '));
	}

	public static <A, B, C> Function<A,C> compose(Function<A,B> first, Function<B,C> second) {
		return first.andThen(second);
	}

	public static <A, B, C, D> Function<A,D> compose(Function<A,B> first, Function<B,C> second, Function<C,D> third) {
		return compose(compose(first, second), third);
	}

	public static ContiguousSet<Integer> range(int from, int to) {
		return ContiguousSet.create(Range.closed(from, to), DiscreteDomain.integers());
	}

	public static <T extends Enum> T random(Class<T> clazz) {
		T[] enumConstants = clazz.getEnumConstants();
		return enumConstants[new Random().nextInt(enumConstants.length)];
	}
	
}
