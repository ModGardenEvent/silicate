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
import net.minecraft.world.phys.Vec3;
import lgbt.greenhouse.silicate.api.context.parameter.Parameter;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterSet;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;
import lgbt.greenhouse.silicate.api.exception.InvalidParameterException;

public class ParameterMapTestInstance extends GameTestInstance {
	public static final MapCodec<ParameterMapTestInstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TestData.CODEC
					.forGetter(ParameterMapTestInstance::info)
	).apply(inst, ParameterMapTestInstance::new));

	protected ParameterMapTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
	}

	@Override
	public void run(GameTestHelper helper) {
		try {
			ParameterSet parameterSet = createParameterSet();
			ParameterMap parameterMap = createParameterMap(createOrigin());
			helper.assertTrue(
					parameterMap.getParameterSet().equals(parameterSet),
					Component.literal("ParameterMap.getParamSet() does not equal parameterSet")
			);
			helper.assertTrue(
					parameterMap.getOrThrow(GlobalParameterKeys.ORIGIN)
							.value()
							.equals(createOrigin().getCenter()),
					Component.literal("ParameterMap.ORIGIN is not equal to origin")
			);
			helper.assertFalse(
					parameterMap.has(GlobalParameterKeys.BLOCK_ENTITY),
					Component.literal("ParameterMap.has(GlobalParameterKeys.BLOCK_ENTITY) != false")
			);
			helper.assertTrue(
					parameterMap.has(GlobalParameterKeys.ORIGIN),
					Component.literal("ParameterMap.has(GlobalParameterKeys.ORIGIN) != true")
			);
			Vec3 newOrigin = createOrigin().getBottomCenter();
			helper.assertTrue(
					parameterMap.getOrThrow(GlobalParameterKeys.ORIGIN)
							.equals(parameterMap.getOrThrow(GlobalParameterKeys.ORIGIN)),
					Component.literal("ParameterMap.Mutable.get(GlobalParameterKeys.ORIGIN) != oldOrigin")
			);
			Parameter<Vec3> oldOrigin = parameterMap.set(GlobalParameterKeys.ORIGIN, newOrigin);
			helper.assertTrue(
					parameterMap.getOrThrow(GlobalParameterKeys.ORIGIN).equals(oldOrigin),
					Component.literal("ParameterMap.get(GlobalParameterKeys.ORIGIN) != oldOrigin")
			);
			helper.assertTrue(
					parameterMap.getOrThrow(GlobalParameterKeys.ORIGIN)
							.value()
							.equals(newOrigin),
					Component.literal("ParameterMap.Mutable.get(GlobalParameterKeys.ORIGIN) != newOrigin")
			);
			try {
				createInvalidParameterMap();
				helper.fail(Component.literal("Invalid parameter key allowed in ParameterMap"));
			} catch (InvalidParameterException ignored) {
			}
			try {
				createMissingParameterMap();
				helper.fail(Component.literal("Missing parameter required in ParameterMap"));
			} catch (InvalidParameterException ignored) {
			}
			helper.succeed();
		} catch (InvalidParameterException ex) {
			helper.fail(Component.literal(ex.getMessage()));
		}
	}

	private static ParameterSet createParameterSet() {
		return ParameterSet.Builder.of()
				.required(GlobalParameterKeys.ORIGIN)
				.build();
	}

	private static ParameterMap createParameterMap(BlockPos origin) throws InvalidParameterException {
		ParameterSet paramSet = createParameterSet();
		ParameterMap.Builder builder = ParameterMap.Builder.of(paramSet)
				.withParameter(GlobalParameterKeys.ORIGIN, origin.getCenter());
		return builder.build();
	}

	private static void createInvalidParameterMap() throws InvalidParameterException {
		ParameterSet paramSet = ParameterSet.Builder.of()
				.required(GlobalParameterKeys.BLOCK_STATE)
				.required(GlobalParameterKeys.ORIGIN)
				.build();
		// this is invalid anyway, we don't care
		//noinspection DataFlowIssue
		ParameterMap.Builder.of(paramSet)
				.withParameter(GlobalParameterKeys.THIS_ENTITY, new Parameter<>(null))
				.build();
	}

	private static void createMissingParameterMap() throws InvalidParameterException {
		ParameterSet paramSet = ParameterSet.Builder.of()
				.required(GlobalParameterKeys.BLOCK_STATE)
				.required(GlobalParameterKeys.ORIGIN)
				.build();
		ParameterMap.Builder.of(paramSet)
				.build();
	}

	@Override
	public MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected MutableComponent typeDescription() {
		return Component.literal("Silicate Parameter Map Test");
	}

	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}
