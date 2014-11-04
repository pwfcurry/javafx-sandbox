package pwfcurry.javafx;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public interface Utils {

	public static String capitaliseEnum(Enum<?> e) {
		return capitalizeFully(e.name().replace('_', ' '));
	}

	public static <A,B> Function<A,B> $(Function<A,B> f) {
		return f;
	}
	
	public static <A, B, C> Function<A,C> compose(Function<A,B> first, Function<B,C> second) {
		return first.andThen(second);
	}

	public static <A, B, C, D> Function<A,D> compose(Function<A,B> first, Function<B,C> second, Function<C,D> third) {
		return compose(compose(first, second), third);
	}

	public static List<Integer> range(int from, int to) {
		return IntStream.range(from, ++to).collect(ArrayList::new, List::add, (first, second) -> first.addAll(second));
	}

	public static <T extends Enum> T random(Class<T> clazz) {
		T[] enumConstants = clazz.getEnumConstants();
		return enumConstants[new Random().nextInt(enumConstants.length)];
	}

}
