package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;

import java.util.List;

/**
 * A predicate composed of other conditions, all checked together.
 */
public class CompoundPredicate implements GamePredicate<CompoundPredicate> {
	public static final MapCodec<CompoundPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL
					.optionalFieldOf("or", false)
					.forGetter(CompoundPredicate::isOr),
			GamePredicate.CODEC
					.listOf()
					.fieldOf("conditions")
					.forGetter(CompoundPredicate::getConditions)
	).apply(instance, CompoundPredicate::new));
	private final boolean or;
	private final List<Holder<GamePredicate<?>>> conditions;

	private CompoundPredicate(boolean or, List<Holder<GamePredicate<?>>> conditions) {
		this.or = or;
		this.conditions = List.copyOf(conditions);
	}

	public static CompoundPredicate of(boolean or, List<Holder<GamePredicate<?>>> conditions) {
		return new CompoundPredicate(or, conditions);
	}

	@SuppressWarnings("unchecked")
	public static CompoundPredicate of(boolean or, Holder<GamePredicate<?>>... conditions) {
		return of(or, List.of(conditions));
	}

	@SuppressWarnings("unchecked")
	public static CompoundPredicate of(Holder<GamePredicate<?>>... conditions) {
		return of(false, List.of(conditions));
	}

	@Override
	public boolean test(GameContext context) {
		if (isOr()) {
			return conditions.stream().anyMatch(condition -> condition.value().test(context));
		} else {
			return conditions.stream().allMatch(condition -> condition.value().test(context));
		}
	}

	@Override
	public MapCodec<CompoundPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<CompoundPredicate> getType() {
		return PredicateTypes.COMPOUND;
	}

	/**
	 * @return Whether the compound is an {@code or} type or an {@code and} type.
	 */
	public boolean isOr() {
		return or;
	}

	/**
	 * @return {@link GamePredicate}s that in the compound.
	 */
	public List<Holder<GamePredicate<?>>> getConditions() {
		return conditions;
	}
}
