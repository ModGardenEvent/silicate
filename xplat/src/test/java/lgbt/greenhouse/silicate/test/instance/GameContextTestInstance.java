package lgbt.greenhouse.silicate.test.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

public class GameContextTestInstance extends GameTestInstance {
	public static final MapCodec<GameContextTestInstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TestData.CODEC
					.forGetter(GameContextTestInstance::info)
	).apply(inst, GameContextTestInstance::new));

	protected GameContextTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
	}

	@Override
	public void run(GameTestHelper helper) {
		ParameterMap parameterMap = createParamMap(createState(), createOrigin(), helper);
		GameContext context = GameContext.of(helper.getLevel(), parameterMap);
		helper.assertTrue(
				context.getLevel().equals(helper.getLevel()),
				Component.literal("GameContext.getLevel() is not equal to level")
		);
		helper.assertTrue(
				context.getParameterMap().equals(parameterMap),
				Component.literal("GameContext.getParameterMap() is not equal to parameterMap")
		);
		helper.succeed();
	}

	private static ParameterMap createParamMap(BlockState state, BlockPos origin, GameTestHelper helper) {
		helper.setBlock(origin, state);
		ParameterMap.Builder builder = ParameterMap.Builder.of()
				.withParameter(GlobalParameterKeys.ORIGIN, origin.getCenter())
				.withParameter(GlobalParameterKeys.BLOCK_STATE, state);
		return builder.build();
	}

	private static BlockState createState() {
		return Blocks.NOTE_BLOCK.defaultBlockState()
				.setValue(NoteBlock.NOTE, 2)
				.setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.BANJO);
	}

	@Override
	public MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected MutableComponent typeDescription() {
		return Component.literal("Silicate Context Param Map Test");
	}

	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}
