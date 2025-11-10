package lgbt.greenhouse.silicate.test.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import lgbt.greenhouse.silicate.impl.Silicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterSet;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;
import lgbt.greenhouse.silicate.api.exception.InvalidParameterException;
import lgbt.greenhouse.silicate.test.SilicateTestGlobalParameterKeys;
import lgbt.greenhouse.silicate.test.util.ExpectedResultCondition;

import java.util.List;

public class ConditionsTestInstance extends GameTestInstance {
	public static final MapCodec<ConditionsTestInstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			ExpectedResultCondition.CODEC.listOf()
					.fieldOf("conditions")
					.forGetter(ConditionsTestInstance::conditions),
			TestData.CODEC
					.forGetter(ConditionsTestInstance::info)
	).apply(inst, ConditionsTestInstance::new));

	private final List<ExpectedResultCondition> conditions;

	protected ConditionsTestInstance(List<ExpectedResultCondition> conditions,
									 TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
		this.conditions = conditions;
	}

	public List<ExpectedResultCondition> conditions() {
		return conditions;
	}

	@Override
	public void run(GameTestHelper helper) {
		SkeletonHorse skeletonHorse = helper.getEntities(EntityType.SKELETON_HORSE).getFirst();
		Skeleton skeleton = helper.getEntities(EntityType.SKELETON).getFirst();
		ServerPlayer player = createFakePlayer(helper);
		Arrow arrow = helper.getEntities(EntityType.ARROW).getFirst();
		skeletonHorse.setOwner(player);
		skeleton.startRiding(skeletonHorse);
		arrow.setOwner(skeleton);
		try {
			ParameterMap paramMap = createParamMap(
					createState(),
					createOrigin(),
					skeletonHorse,
					skeleton,
					createEntityBlock(),
					player,
					arrow,
					helper
			);
			GameContext context = GameContext.of(helper.getLevel(), paramMap);

			for (ExpectedResultCondition condition : conditions) {
				if (!condition.condition().isBound()) {
					helper.fail(Component.literal("Condition " + condition.condition() + " within Silicate Conditions Test is invalid."));
					return;
				}
				GamePredicate<?> gamePredicate = condition.condition().value();
				boolean conditionTest = gamePredicate.test(context);
				String conditionName = condition.name();

				if (condition.shouldSucceed()) {
					helper.assertTrue(conditionTest,
							Component.literal(conditionName + " test failed"));
				} else {
					helper.assertFalse(conditionTest,
							Component.literal(conditionName + " test succeeded unexpectedly"));
				}
			}
			helper.succeed();
		} catch (InvalidParameterException ex) {
			helper.fail(Component.literal(ex.getMessage()));
		}
	}

	@Override
	public MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected MutableComponent typeDescription() {
		return Component.literal("Silicate Conditions Test");
	}

	private static ParameterSet createParamSet() {
		return ParameterSet.Builder.of()
				.required(GlobalParameterKeys.BLOCK_STATE)
				.required(GlobalParameterKeys.ORIGIN)
				.required(GlobalParameterKeys.THIS_ENTITY)
				.optional(GlobalParameterKeys.BLOCK_ENTITY)
				.optional(GlobalParameterKeys.ATTACKING_ENTITY)
				.optional(GlobalParameterKeys.VICTIM_ENTITY)
				.optional(SilicateTestGlobalParameterKeys.PROJECTILE)
				.build();
	}

	private static ParameterMap createParamMap(BlockState state, BlockPos origin, Entity entity, Entity entity2, BlockState entityBlock, ServerPlayer fakePlayer, Projectile projectile, GameTestHelper helper) throws InvalidParameterException {
		ParameterSet paramSet = createParamSet();
		helper.setBlock(origin, state);
		BlockPos entityBlockPos = origin.east();
		helper.setBlock(entityBlockPos, entityBlock);
		ParameterMap.Builder builder = ParameterMap.Builder.of(paramSet)
				.withParameter(GlobalParameterKeys.BLOCK_STATE, state)
				.withParameter(GlobalParameterKeys.ORIGIN, origin.getCenter())
				.withParameter(GlobalParameterKeys.THIS_ENTITY, entity)
				.withParameter(GlobalParameterKeys.VICTIM_ENTITY, entity2)
				.withParameter(
						GlobalParameterKeys.BLOCK_ENTITY, helper.getBlockEntity(entityBlockPos, FurnaceBlockEntity.class))
				.withParameter(GlobalParameterKeys.ATTACKING_ENTITY, fakePlayer)
				.withParameter(SilicateTestGlobalParameterKeys.PROJECTILE, projectile);
		return builder.build();
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
		assert Silicate.getHelper() != null;
		ServerPlayer fakePlayer = Silicate.getHelper().createFakePlayer(helper.getLevel());
		fakePlayer.setGameMode(GameType.ADVENTURE);
		fakePlayer.setPos(createOrigin().getCenter());
		return fakePlayer;
	}

	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}
