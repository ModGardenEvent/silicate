package net.modgarden.silicate.test;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Arrow;
import net.modgarden.silicate.Silicate;
import net.modgarden.silicate.api.SilicateRegistries;
import net.modgarden.silicate.api.condition.AlwaysCondition;
import net.modgarden.silicate.api.condition.CompoundCondition;
import net.modgarden.silicate.api.condition.InvertedCondition;
import net.modgarden.silicate.api.condition.builtin.*;
import net.modgarden.silicate.api.condition.builtin.math.Comparison;
import net.modgarden.silicate.api.condition.builtin.math.Vec3Comparison;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParam;
import net.modgarden.silicate.api.context.param.ContextParamMap;
import net.modgarden.silicate.api.context.param.ContextParamSet;
import net.modgarden.silicate.api.context.param.ContextParamTypes;
import net.modgarden.silicate.api.exception.InvalidContextParameterException;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.Vec3;
import net.modgarden.silicate.api.condition.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
	@SuppressWarnings("unchecked")
	public static void conditions(GameTestHelper helper) throws InvalidContextParameterException {
		SkeletonHorse skeletonHorse = helper.getEntities(EntityType.SKELETON_HORSE).getFirst();
		Skeleton skeleton = helper.getEntities(EntityType.SKELETON).getFirst();
		ServerPlayer player = createFakePlayer(helper);
		// Fake players need to be added to the player list manually
		// This is necessary for OwnableEntity#getOwner(UUID)
		helper.getLevel().players().add(player);
		ContextParamMap paramMap = createParamMap(
			createState(),
			createOrigin(),
			skeletonHorse,
			skeleton,
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
			EntityType.SKELETON_HORSE
		);
		helper.assertTrue(
			entityTypeCondition.test(context),
			"EntityTypeCondition EntityType test failed"
		);
		EntityTypeCondition entityTagCondition = EntityTypeCondition.of(
			ContextParamTypes.THIS_ENTITY,
			EntityTypeTags.SKELETONS
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

		Arrow arrow = helper.getEntities(EntityType.ARROW).getFirst();
		ContextParamMap projectileParamMap = createParamMap(
				createState(),
				createOrigin(),
				arrow,
				player,
				null,
				null,
				helper
		);
		GameContext projectileContext = GameContext.of(helper.getLevel(), projectileParamMap);

		EntityTameOwnerCondition tameOwnerCondition = new EntityTameOwnerCondition(
				ContextParamTypes.THIS_ENTITY,
				Holder.direct(
						EntityTypeCondition.of(
								ContextParamTypes.OWNER_ENTITY,
								EntityType.PLAYER
						)
				)
		);
		helper.assertFalse(
				tameOwnerCondition.test(context),
				"EntityTameOwnerCondition test unexpectedly succeeded"
		);
		EntityProjectileOwnerCondition projectileOwnerCondition = new EntityProjectileOwnerCondition(
				ContextParamTypes.THIS_ENTITY,
				Holder.direct(
					EntityTypeCondition.of(
						ContextParamTypes.OWNER_ENTITY,
						EntityType.SKELETON
					)
				)
		);
		helper.assertFalse(
				projectileOwnerCondition.test(projectileContext),
				"EntityProjectileOwnerCondition test unexpectedly succeeded"
		);
		EntityPassengerCondition passengerCondition = new EntityPassengerCondition(
				ContextParamTypes.THIS_ENTITY,
				MaybeTypedCondition.of(
						EntityTypeCondition.of(
								ContextParamTypes.PASSENGER_ENTITY,
								EntityType.SKELETON
						)
				),
				false
		);
		helper.assertFalse(
			passengerCondition.test(context),
			"EntityPassengerCondition test unexpectedly succeeded"
		);
		EntityVehicleCondition vehicleCondition = new EntityVehicleCondition(
				ContextParamTypes.VICTIM_ENTITY,
				MaybeTypedCondition.of(
						EntityTypeCondition.of(
								ContextParamTypes.VEHICLE_ENTITY,
								EntityType.SKELETON_HORSE
						)
				)
		);
		helper.assertFalse(
				vehicleCondition.test(context),
				"EntityVehicleCondition test unexpectedly succeeded"
		);
		skeletonHorse.setOwnerUUID(player.getUUID());
		arrow.setOwner(skeleton);
		skeleton.startRiding(skeletonHorse);
		helper.assertTrue(
				tameOwnerCondition.test(context),
				"EntityTameOwnerCondition test failed"
		);
		helper.assertTrue(
				projectileOwnerCondition.test(projectileContext),
				"EntityProjectileOwnerCondition test failed"
		);
		helper.assertTrue(
				passengerCondition.test(context),
				"EntityPassengerCondition test failed"
		);
		helper.assertTrue(
				vehicleCondition.test(context),
				"EntityVehicleCondition test failed"
		);
		InvertedCondition invertedCondition = new InvertedCondition(
				Holder.direct(
						EntityTypeCondition.of(
								ContextParamTypes.VICTIM_ENTITY,
								EntityType.SKELETON_HORSE
						)
				)
		);
		helper.assertTrue(
				invertedCondition.test(context),
				"InvertedCondition test failed"
		);
		EntityVehicleCondition invertedVehicleCondition = new EntityVehicleCondition(
				ContextParamTypes.VICTIM_ENTITY,
				MaybeTypedCondition.of(invertedCondition)
		);
		helper.assertFalse(
				invertedVehicleCondition.test(context),
				"TypedGameCondition.fromUntyped test failed"
		);
		AlwaysCondition trueCondition = new AlwaysCondition(true);
		helper.assertTrue(
				trueCondition.test(context),
				"AlwaysCondition(true) test failed"
		);
		AlwaysCondition falseCondition = new AlwaysCondition(false);
		helper.assertFalse(
				falseCondition.test(context),
				"AlwaysCondition(false) test succeeded unexpectedly"
		);
		CompoundCondition compoundCondition = CompoundCondition.of(
				Holder.direct(stateCondition),
				Holder.direct(entityTypeCondition),
				Holder.direct(entityTagCondition),
				Holder.direct(vec3Condition),
				Holder.direct(blockEntityTypeCondition),
				Holder.direct(gameTypeCondition),
				Holder.direct(passengerCondition),
				Holder.direct(vehicleCondition),
				Holder.direct(trueCondition),
				Holder.direct(new InvertedCondition(Holder.direct(falseCondition)))
		);
		helper.assertTrue(
				compoundCondition.test(context),
				"CompoundCondition test failed"
		);
		helper.succeed();
	}

	@GameTest(template = "silicate:test_template")
	public static void conditionTemplates(GameTestHelper helper) throws InvalidContextParameterException {
		SkeletonHorse skeletonHorse = helper.getEntities(EntityType.SKELETON_HORSE).getFirst();
		Skeleton skeleton = helper.getEntities(EntityType.SKELETON).getFirst();
		ServerPlayer player = createFakePlayer(helper);
		ContextParamMap paramMap = createParamMapWithPassenger(
				createState(),
				createOrigin(),
				skeletonHorse,
				skeleton,
				createEntityBlock(),
				player,
				helper
		);
		skeleton.startRiding(skeletonHorse);
		GameContext context = GameContext.of(helper.getLevel(), paramMap);
		Holder<GameCondition<?>> always = GameCondition.getTemplate(ResourceLocation.fromNamespaceAndPath("test", "true"));
		helper.assertTrue(always.value().test(context), "ConditionTemplate test:true failed");
		Holder<GameCondition<?>> ridingOnly = GameCondition.getTemplate(ResourceLocation.fromNamespaceAndPath("test", "riding_only"));
		helper.assertTrue(ridingOnly.value().test(context), "ConditionTemplate test:riding_only failed");
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
		return checkAndBuildParamMap(origin, entityBlock, fakePlayer, helper, builder);
	}

	private static ContextParamMap createParamMapWithPassenger(BlockState state, BlockPos origin, Entity entity, Entity entity2, @Nullable BlockState entityBlock, @Nullable ServerPlayer fakePlayer, GameTestHelper helper) throws InvalidContextParameterException {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.BLOCK_STATE)
				.required(ContextParamTypes.ORIGIN)
				.optional(ContextParamTypes.BLOCK_ENTITY)
				.optional(ContextParamTypes.ATTACKING_ENTITY)
				.required(ContextParamTypes.VEHICLE_ENTITY)
				.required(ContextParamTypes.THIS_ENTITY)
				.build();
		helper.setBlock(origin, state);
		ContextParamMap.Builder builder = ContextParamMap.Builder.of(paramSet)
				.withParameter(ContextParamTypes.BLOCK_STATE, state)
				.withParameter(ContextParamTypes.ORIGIN, origin.getCenter())
				.withParameter(ContextParamTypes.VEHICLE_ENTITY, entity)
				.withParameter(ContextParamTypes.THIS_ENTITY, entity2);
		return checkAndBuildParamMap(origin, entityBlock, fakePlayer, helper, builder);
	}

	@NotNull
	private static ContextParamMap checkAndBuildParamMap(BlockPos origin, @Nullable BlockState entityBlock, @Nullable ServerPlayer fakePlayer, GameTestHelper helper, ContextParamMap.Builder builder) throws InvalidContextParameterException {
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
