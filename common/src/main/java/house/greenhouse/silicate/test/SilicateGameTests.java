package house.greenhouse.silicate.test;

import house.greenhouse.silicate.Silicate;
import house.greenhouse.silicate.api.condition.CompoundCondition;
import house.greenhouse.silicate.api.condition.InvertedCondition;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import house.greenhouse.silicate.api.condition.builtin.*;
import house.greenhouse.silicate.api.condition.builtin.math.Comparison;
import house.greenhouse.silicate.api.condition.builtin.math.Vec3Comparison;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.*;
import house.greenhouse.silicate.api.exception.InvalidContextParameterException;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
public class SilicateGameTests {
	@GameTest(template = "silicate:test_template")
	public static void contextParam(GameTestHelper helper) {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.ORIGIN)
				.required(ContextParamTypes.BLOCK_STATE)
				.optional(ContextParamTypes.BLOCK_ENTITY)
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
		helper.assertTrue(
			paramSet.hasParam(ContextParamTypes.BLOCK_ENTITY),
			"ContextParamSet.hasParam(ContextParamTypes.BLOCK_ENTITY) != true"
		);
		helper.assertFalse(
			paramSet.hasParam(ContextParamTypes.UNIT),
			"ContextParamSet.hasParam(ContextParamTypes.UNIT) != false"
		);
		helper.succeed();
	}
	
	@GameTest(template = "silicate:test_template")
	public static void contextParamMap(GameTestHelper helper) throws InvalidContextParameterException {
		ContextParamSet paramSet = createParamSet();
		ContextParamMap paramMap = createParamMap(createState(), createOrigin(), null, null, null, null, helper);
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
		helper.assertFalse(
			paramMap.has(ContextParamTypes.BLOCK_ENTITY),
			"ContextParamMap.has(ContextParamTypes.BLOCK_ENTITY) != false"
		);
		helper.assertTrue(
			paramMap.has(ContextParamTypes.ORIGIN),
			"ContextParamMap.has(ContextParamTypes.ORIGIN) != true"
		);
		ContextParamMap.Mutable mutableParamMap = ContextParamMap.Mutable.of(paramMap);
		Vec3 newOrigin = createOrigin().getBottomCenter();
		helper.assertTrue(
			mutableParamMap.get(ContextParamTypes.ORIGIN)
				.equals(paramMap.get(ContextParamTypes.ORIGIN)),
			"ContextParamMap.Mutable.get(ContextParamTypes.ORIGIN) != oldOrigin"
		);
		ContextParam<Vec3> oldOrigin = mutableParamMap.set(ContextParamTypes.ORIGIN, newOrigin);
		helper.assertTrue(
			paramMap.get(ContextParamTypes.ORIGIN).equals(oldOrigin),
			"ContextParamMap.get(ContextParamTypes.ORIGIN) != oldOrigin"
		);
		helper.assertTrue(
			mutableParamMap.get(ContextParamTypes.ORIGIN)
				.value()
				.equals(newOrigin),
			"ContextParamMap.Mutable.get(ContextParamTypes.ORIGIN) != newOrigin"
		);
		try {
			createInvalidParamMap();
			helper.fail("Invalid parameter type allowed in ContextParamMap");
		} catch (InvalidContextParameterException ignored) {
		}
		try {
			createMissingParamMap();
			helper.fail("Missing parameter allowed in ContextParamMap");
		} catch (InvalidContextParameterException ignored) {
		}
		helper.succeed();
	}
	
	@GameTest(template = "silicate:test_template")
	public static void gameContext(GameTestHelper helper) throws InvalidContextParameterException {
		ContextParamMap paramMap = createParamMap(createState(), createOrigin(), null, null, null, null, helper);
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
	public static void conditions(GameTestHelper helper) throws InvalidContextParameterException {
		Chicken chicken = helper.getEntities(EntityType.CHICKEN).getFirst();
		Zombie zombie = helper.getEntities(EntityType.ZOMBIE).getFirst();
		ServerPlayer player = createFakePlayer(helper);
		ContextParamMap paramMap = createParamMap(
			createState(),
			createOrigin(),
			chicken,
			zombie,
			createEntityBlock(),
			player,
			helper
		);
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
		BlockEntityTypeCondition blockEntityTypeCondition = new BlockEntityTypeCondition(
			ContextParamTypes.BLOCK_ENTITY,
			BlockEntityType.FURNACE
		);
		helper.assertTrue(
			blockEntityTypeCondition.test(context),
			"BlockEntityTypeCondition test failed"
		);
		PlayerGameTypeCondition gameTypeCondition = new PlayerGameTypeCondition(
			ContextParamTypes.ATTACKING_ENTITY,
			PlayerGameTypeCondition.SURVIVAL_LIKE
		);
		helper.assertTrue(
			gameTypeCondition.test(context),
			"PlayerGameTypeCondition test failed"
		);
		EntityPassengerCondition passengerCondition = new EntityPassengerCondition(
				ContextParamTypes.THIS_ENTITY,
				EntityTypeCondition.of(
						ContextParamTypes.PASSENGER_ENTITY,
						EntityType.ZOMBIE
				),
				false
		);
		helper.assertFalse(
			passengerCondition.test(context),
			"EntityPassengerCondition test unexpectedly succeeded"
		);
		EntityVehicleCondition vehicleCondition = new EntityVehicleCondition(
				ContextParamTypes.VICTIM_ENTITY,
				EntityTypeCondition.of(
						ContextParamTypes.VEHICLE_ENTITY,
						EntityType.CHICKEN
				)
		);
		helper.assertFalse(
				vehicleCondition.test(context),
				"EntityVehicleCondition test unexpectedly succeeded"
		);
		zombie.startRiding(chicken);
		helper.assertTrue(
				passengerCondition.test(context),
				"EntityPassengerCondition test failed"
		);
		helper.assertTrue(
				vehicleCondition.test(context),
				"EntityVehicleCondition test failed"
		);
		InvertedCondition invertedCondition = new InvertedCondition(
				EntityTypeCondition.of(
						ContextParamTypes.VICTIM_ENTITY,
						EntityType.CHICKEN
				)
		);
		helper.assertTrue(
				invertedCondition.test(context), 
				"InvertedCondition test failed"
		);
		EntityVehicleCondition invertedVehicleCondition = new EntityVehicleCondition(
				ContextParamTypes.VICTIM_ENTITY,
				TypedGameCondition.fromUntyped(
						ContextParamTypes.VICTIM_ENTITY,
						invertedCondition
				)
		);
		helper.assertFalse(
				invertedVehicleCondition.test(context),
				"TypedGameCondition.fromUntyped test failed"
		);
		CompoundCondition compoundCondition = CompoundCondition.of(
				stateCondition,
				entityTypeCondition,
				entityTagCondition,
				vec3Condition,
				blockEntityTypeCondition,
				gameTypeCondition,
				passengerCondition,
				vehicleCondition
		);
		helper.assertTrue(
				compoundCondition.test(context),
				"CompoundCondition test failed"
		);
		helper.succeed();
	}
	
	private static void createInvalidParamMap() throws InvalidContextParameterException {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
			.required(ContextParamTypes.BLOCK_STATE)
			.required(ContextParamTypes.ORIGIN)
			.build();
		ContextParamMap.Builder.of(paramSet)
			.withParameter(ContextParamTypes.THIS_ENTITY, new ContextParam<>(null))
			.build();
	}
	
	private static void createMissingParamMap() throws InvalidContextParameterException {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
			.required(ContextParamTypes.BLOCK_STATE)
			.required(ContextParamTypes.ORIGIN)
			.build();
		ContextParamMap.Builder.of(paramSet)
			.build();
	}
	
	private static ContextParamMap createParamMap(BlockState state, BlockPos origin, Entity entity, Entity entity2, @Nullable BlockState entityBlock, @Nullable ServerPlayer fakePlayer, GameTestHelper helper) throws InvalidContextParameterException {
		ContextParamSet paramSet = createParamSet();
		helper.setBlock(origin, state);
		ContextParamMap.Builder builder = ContextParamMap.Builder.of(paramSet)
				.withParameter(ContextParamTypes.BLOCK_STATE, state)
				.withParameter(ContextParamTypes.ORIGIN, origin.getCenter())
				.withParameter(ContextParamTypes.THIS_ENTITY, entity)
				.withParameter(ContextParamTypes.VICTIM_ENTITY, entity2);
		if (entityBlock != null) {
			BlockPos entityBlockPos = origin.east();
			helper.setBlock(entityBlockPos, entityBlock);
			builder
					.withParameter(ContextParamTypes.BLOCK_ENTITY, helper.getBlockEntity(entityBlockPos));
		}
		if (fakePlayer != null) {
			builder
					.withParameter(ContextParamTypes.ATTACKING_ENTITY, fakePlayer);
		}
		return builder
				.build();
	}
	
	private static @NotNull ContextParamSet createParamSet() {
		return ContextParamSet.Builder.of()
			.required(ContextParamTypes.BLOCK_STATE)
			.required(ContextParamTypes.ORIGIN)
			.required(ContextParamTypes.THIS_ENTITY)
			.optional(ContextParamTypes.BLOCK_ENTITY)
			.optional(ContextParamTypes.ATTACKING_ENTITY)
			.optional(ContextParamTypes.VICTIM_ENTITY)
			.build();
	}
	
	private static BlockState createState() {
		return Blocks.NOTE_BLOCK.defaultBlockState()
			.setValue(NoteBlock.NOTE, 2)
			.setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.BANJO);
	}
	
	private static BlockState createEntityBlock() {
		return Blocks.FURNACE
			.defaultBlockState()
			.setValue(FurnaceBlock.LIT, true);
	}
	
	private static ServerPlayer createFakePlayer(GameTestHelper helper) {
		ServerPlayer fakePlayer = Silicate.getHelper().createFakePlayer(helper.getLevel());
		fakePlayer.setGameMode(GameType.ADVENTURE);
		fakePlayer.setPos(createOrigin().getCenter());
		return fakePlayer;
	}
	
	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}