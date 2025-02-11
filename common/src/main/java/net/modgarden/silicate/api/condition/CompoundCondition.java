package net.modgarden.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.api.context.GameContext;

import java.util.List;

/**
 * A condition composed of other conditions, all checked together.
 */
public class CompoundCondition implements GameCondition<CompoundCondition> {
	public static final MapCodec<CompoundCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL
					.optionalFieldOf("or", false)
					.forGetter(CompoundCondition::isOr),
			GameCondition.CODEC
					.listOf()
					.fieldOf("conditions")
					.forGetter(CompoundCondition::getConditions)
	).apply(instance, CompoundCondition::new));
	private final boolean or;
	private final List<GameCondition<?>> conditions;

	private CompoundCondition(boolean or, List<GameCondition<?>> conditions) {
		this.or = or;
		this.conditions = List.copyOf(conditions);
	}

	public static CompoundCondition of(boolean or, List<GameCondition<?>> conditions) {
		return new CompoundCondition(or, conditions);
	}

	public static CompoundCondition of(boolean or, GameCondition<?>... conditions) {
		return of(or, List.of(conditions));
	}

	public static CompoundCondition of(GameCondition<?>... conditions) {
		return of(false, List.of(conditions));
	}

	@Override
	public boolean test(GameContext context) {
		if (isOr()) {
			return conditions.stream().anyMatch(condition -> condition.test(context));
		} else {
			return conditions.stream().allMatch(condition -> condition.test(context));
		}
	}

	@Override
	public MapCodec<CompoundCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<CompoundCondition> getType() {
		return GameConditionTypes.COMPOUND;
	}

	/**
	 * @return Whether the compound is an {@code or} type or an {@code and} type.
	 */
	public boolean isOr() {
		return or;
	}

	/**
	 * @return {@link GameCondition}s that in the compound.
	 */
	public List<GameCondition<?>> getConditions() {
		return conditions;
	}
}
