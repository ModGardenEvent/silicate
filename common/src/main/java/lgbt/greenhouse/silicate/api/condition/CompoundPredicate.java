package lgbt.greenhouse.silicate.api.condition;

import lgbt.greenhouse.silicate.api.condition.meta.BaseCodec;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;

import java.util.List;

/**
 * A predicate composed of other conditions, all checked together.
 */
public abstract sealed class CompoundPredicate<T extends CompoundPredicate<T>>
		implements GamePredicate<T>
		permits AllPredicate, AnyPredicate {
	private final List<Holder<GamePredicate<?>>> conditions;

	protected CompoundPredicate(List<Holder<GamePredicate<?>>> conditions) {
		this.conditions = List.copyOf(conditions);
	}

	protected sealed abstract static class Type<T extends CompoundPredicate<T>> extends GamePredicate.Type<T>
			permits AllPredicate.Type, AnyPredicate.Type {
		@Override
		protected BaseCodec<T> createBaseCodec() {
			return super.createBaseCodec()
					.andThen(builder -> builder.withValue(
							"conditions",
							SilicateValueTypes.LIST_CONDITION,
							CompoundPredicate::getConditions
					));
		}
	}

	/**
	 * @return {@link GamePredicate}s that in the compound.
	 */
	public List<Holder<GamePredicate<?>>> getConditions() {
		return conditions;
	}
}
