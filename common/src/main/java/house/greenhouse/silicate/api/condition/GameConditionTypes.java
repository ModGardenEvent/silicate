package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import house.greenhouse.silicate.api.condition.builtin.BlockStateCondition;
import house.greenhouse.silicate.api.condition.builtin.EntityTypeCondition;
import house.greenhouse.silicate.api.condition.builtin.Vec3Condition;
import net.minecraft.core.Registry;

import static house.greenhouse.silicate.Silicate.id;

/**
 * Built-in {@link GameConditionType}s.
 */
public final class GameConditionTypes {
	public static final GameConditionType<CompoundCondition> COMPOUND = register("compound", CompoundCondition.CODEC);
	public static final GameConditionType<EntityTypeCondition> ENTITY_TYPE = register("entity_type", EntityTypeCondition.CODEC);
	public static final GameConditionType<BlockStateCondition> BLOCK_STATE = register("block_state", BlockStateCondition.CODEC);
	public static final GameConditionType<Vec3Condition> VEC3 = register("vec3", Vec3Condition.CODEC);
	
	private GameConditionTypes() {}
	
	private static <T extends GameCondition<T>> GameConditionType<T> register(
			String name,
			MapCodec<T> codec
	) {
		return Registry.register(
				GameConditionType.REGISTRY,
				id(name),
				new GameConditionType<>(codec)
		);
	}
}