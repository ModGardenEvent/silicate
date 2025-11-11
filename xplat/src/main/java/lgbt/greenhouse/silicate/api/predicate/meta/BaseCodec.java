package lgbt.greenhouse.silicate.api.predicate.meta;

import lgbt.greenhouse.silicate.api.predicate.GamePredicate;

import java.util.function.Function;

/**
 * A codec that appends fields to a {@link PredicateCodecBuilder}.
 */
@FunctionalInterface
public
interface BaseCodec<T extends GamePredicate<T>> extends Function<PredicateCodecBuilder<T>, PredicateCodecBuilder<T>> {
	@Override
	PredicateCodecBuilder<T> apply(PredicateCodecBuilder<T> builder);

	/**
	 * Applies the child {@link BaseCodec} to the parent's.
	 *
	 * @param child the current class's {@link BaseCodec}
	 * @return a chained {@link BaseCodec}
	 */
	default BaseCodec<T> andThen(BaseCodec<T> child) {
		return Function.super.andThen(child)::apply;
	}
}
