package house.greenhouse.silicate.test;

import house.greenhouse.silicate.api.condition.builtin.BlockStateCondition;
import house.greenhouse.silicate.api.condition.builtin.EntityTypeCondition;
import house.greenhouse.silicate.api.condition.builtin.Vec3Condition;
import house.greenhouse.silicate.api.condition.builtin.math.Comparison;
import house.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class SilicateGameTests {
	@GameTest(template = "silicate:test_template")
	public static void contextParam(GameTestHelper helper) {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.ORIGIN)
				.required(ContextParamTypes.BLOCK_STATE)
				.optional(ContextParamTypes.THIS_ENTITY)
				.build();
		helper.assertFalse(
				paramSet.isRequired(ContextParamTypes.THIS_ENTITY),
				"ContextParamSet.isRequired(ContextParamTypes.THIS_ENTITY) != false"
		);
		helper.assertTrue(
				paramSet.getRequired()
						.containsAll(List.of(
								ContextParamTypes.ORIGIN,
								ContextParamTypes.BLOCK_STATE
						)),
				"ContextParamSet.getRequired() does not contain required"
		);
		helper.assertTrue(
				paramSet.getAll()
						.containsAll(List.of(
								ContextParamTypes.ORIGIN,
								ContextParamTypes.BLOCK_STATE,
								ContextParamTypes.THIS_ENTITY
						)),
				"ContextParamSet.getAll() does not contain all"
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.ORIGIN),
				"ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true"
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.BLOCK_STATE),
				"ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true"
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.THIS_ENTITY),
				"ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true"
		);
		helper.assertFalse(
				paramSet.hasParam(ContextParamTypes.BLOCK_ENTITY),
				"ContextParamSet.hasParam(ContextParamTypes.BLOCK_ENTITY) != false"
		);
		helper.succeed();
	}
	
	@GameTest(template = "silicate:test_template")
	public static void contextParamMap(GameTestHelper helper) {
		ContextParamSet paramSet = createParamSet();
		ContextParamMap paramMap = createParamMap(createState(), createOrigin(), null, helper);
		helper.assertTrue(
				paramMap.getParamSet().equals(paramSet),
				"ContextParamMap.getParamSet() does not equal paramSet"
		);
		helper.assertTrue(
				paramMap.get(ContextParamTypes.BLOCK_ENTITY) == null,
				"ContextParamMap.get(ContextParamTypes.BLOCK_ENTITY) != null"
		);
		helper.assertTrue(
				paramMap.get(ContextParamTypes.ORIGIN)
						.value()
						.equals(createOrigin().getCenter()),
				"ContextParamTypes.ORIGIN is not equal to origin"
		);
		try {
			createInvalidParamMap();
			helper.fail("Invalid parameter type allowed in ContextParamMap");
		} catch (IllegalArgumentException ignored) {
		}
		try {
			createMissingParamMap();
			helper.fail("Missing parameter allowed in ContextParamMap");
		} catch (IllegalArgumentException ignored) {
		}
		helper.succeed();
	}
	
	@GameTest(template = "silicate:test_template")
	public static void gameContext(GameTestHelper helper) {
		ContextParamMap paramMap = createParamMap(createState(), createOrigin(), null, helper);
		GameContext context = GameContext.of(helper.getLevel(), paramMap);
		helper.assertTrue(
				context.getLevel() != null,
				"GameContext.getLevel() == null"
		);
		helper.assertTrue(
				context.getLevel().equals(helper.getLevel()),
				"GameContext.getLevel() is not equal to level"
		);
		helper.assertTrue(
				context.getParams().equals(paramMap),
				"GameContext.getParams() is not equal to paramMap"
		);
		helper.succeed();
	}
	
	@GameTest(template = "silicate:test_template")
	public static void conditions(GameTestHelper helper) {
		ContextParamMap paramMap = createParamMap(createState(), createOrigin(), helper.getEntities(EntityType.CHICKEN).getFirst(), helper);
		GameContext context = GameContext.of(helper.getLevel(), paramMap);
		BlockStateCondition stateCondition = new BlockStateCondition(
				ContextParamTypes.BLOCK_STATE,
				createState()
		);
		helper.assertTrue(
				stateCondition.test(context),
				"BlockStateCondition test failed"
		);
		EntityTypeCondition entityTypeCondition = EntityTypeCondition.of(
				ContextParamTypes.THIS_ENTITY,
				EntityType.CHICKEN
		);
		helper.assertTrue(
				entityTypeCondition.test(context),
				"EntityTypeCondition EntityType test failed"
		);
		EntityTypeCondition entityTagCondition = EntityTypeCondition.of(
				ContextParamTypes.THIS_ENTITY,
				EntityTypeTags.FALL_DAMAGE_IMMUNE
		);
		helper.assertTrue(
				entityTagCondition.test(context),
				"EntityTypeCondition TagKey test failed"
		);
		Vec3Condition vec3Condition = new Vec3Condition(
				ContextParamTypes.ORIGIN,
				new Vec3Comparison(
						Comparison.EQUALS,
						Comparison.GREATER_THAN,
						Comparison.LESS_THAN_EQUALS
				),
				createOrigin().getBottomCenter()
		);
		helper.assertTrue(
				vec3Condition.test(context),
				"Vec3Condition test failed"
		);
		helper.succeed();
	}
	
	private static void createInvalidParamMap() throws IllegalArgumentException {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.BLOCK_STATE)
				.required(ContextParamTypes.ORIGIN)
				.build();
		ContextParamMap.Builder.of(paramSet)
				.withParameter(ContextParamTypes.THIS_ENTITY, new ContextParam<>(null))
				.build();
	}
	
	private static void createMissingParamMap() throws IllegalArgumentException {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.BLOCK_STATE)
				.required(ContextParamTypes.ORIGIN)
				.build();
		ContextParamMap.Builder.of(paramSet)
				.build();
	}
	
	private static ContextParamMap createParamMap(BlockState state, BlockPos origin, Entity entity, GameTestHelper helper) {
		ContextParamSet paramSet = createParamSet();
		helper.setBlock(origin, state);
		return ContextParamMap.Builder.of(paramSet)
				.withParameter(ContextParamTypes.BLOCK_STATE, state)
				.withParameter(ContextParamTypes.ORIGIN, origin.getCenter())
				.withParameter(ContextParamTypes.THIS_ENTITY, entity)
				.build();
	}
	
	private static @NotNull ContextParamSet createParamSet() {
		return ContextParamSet.Builder.of()
				.required(ContextParamTypes.BLOCK_STATE)
				.required(ContextParamTypes.ORIGIN)
				.required(ContextParamTypes.THIS_ENTITY)
				.build();
	}
	
	private static BlockState createState() {
		return Blocks.NOTE_BLOCK.defaultBlockState()
				.setValue(NoteBlock.NOTE, 2)
				.setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.BANJO);
	}
	
	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}
