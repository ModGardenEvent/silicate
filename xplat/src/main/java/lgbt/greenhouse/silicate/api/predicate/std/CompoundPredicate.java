package lgbt.greenhouse.silicate.api.predicate.std;

import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.BaseCodec;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;

import java.util.List;

/**
 * A predicate composed of other conditions, all checked together.
 */
public abstract sealed class CompoundPredicate<T extends CompoundPredicate<T>>
		implements GamePredicate<T>
		permits AllPredicate, AnyPredicate {
	private final Deferred<List<Holder<GamePredicate<?>>>> conditions;

	protected CompoundPredicate(Deferred<List<Holder<GamePredicate<?>>>> conditions) {
		this.conditions = conditions;
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
	public Deferred<List<Holder<GamePredicate<?>>>> getConditions() {
		return this.conditions;
	}
}
