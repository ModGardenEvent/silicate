package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.context.GameContext;

import java.util.List;

/**
 * A condition composed of other conditions, all checked together.
 */
public class CompoundCondition implements GameCondition<CompoundCondition> {
	public static final MapCodec<CompoundCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GameCondition.CODEC
					.listOf()
					.fieldOf("conditions")
					.forGetter(CompoundCondition::getConditions)
	).apply(instance, CompoundCondition::new));
	private final List<GameCondition<?>> conditions;
	
	private CompoundCondition(List<GameCondition<?>> conditions) {
		this.conditions = List.copyOf(conditions);
	}
	
	public static CompoundCondition of(List<GameCondition<?>> conditions) {
		return new CompoundCondition(conditions);
	}
	
	public static CompoundCondition of(GameCondition<?>... conditions) {
		return of(List.of(conditions));
	}
	
	@Override
	public boolean test(GameContext context) {
		return conditions.stream().allMatch(condition -> condition.test(context));
	}
	
	@Override
	public MapCodec<CompoundCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<CompoundCondition> getType() {
		return GameConditionTypes.COMPOUND_CONDITION;
	}

	public List<GameCondition<?>> getConditions() {
		return conditions;
	}
}