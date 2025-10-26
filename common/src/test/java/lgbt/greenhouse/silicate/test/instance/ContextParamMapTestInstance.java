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
import lgbt.greenhouse.silicate.api.context.param.Parameter;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.ParameterSet;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterTypes;
import lgbt.greenhouse.silicate.api.exception.InvalidContextParameterException;
import org.jetbrains.annotations.NotNull;

public class ContextParamMapTestInstance extends GameTestInstance {
	public static final MapCodec<ContextParamMapTestInstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TestData.CODEC
					.forGetter(ContextParamMapTestInstance::info)
	).apply(inst, ContextParamMapTestInstance::new));

	protected ContextParamMapTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
	}

	@Override
	public void run(@NotNull GameTestHelper helper) {
		try {
			ParameterSet paramSet = createParamSet();
			ParameterMap paramMap = createParamMap(createOrigin());
			helper.assertTrue(
					paramMap.getParamSet().equals(paramSet),
					Component.literal("ContextParamMap.getParamSet() does not equal paramSet")
			);
			helper.assertTrue(
					paramMap.get(GlobalParameterTypes.BLOCK_ENTITY) == null,
					Component.literal("ContextParamMap.get(ContextParamTypes.BLOCK_ENTITY) != null")
			);
			helper.assertTrue(
					paramMap.get(GlobalParameterTypes.ORIGIN)
							.value()
							.equals(createOrigin().getCenter()),
					Component.literal("ContextParamTypes.ORIGIN is not equal to origin")
			);
			helper.assertFalse(
					paramMap.has(GlobalParameterTypes.BLOCK_ENTITY),
					Component.literal("ContextParamMap.has(ContextParamTypes.BLOCK_ENTITY) != false")
			);
			helper.assertTrue(
					paramMap.has(GlobalParameterTypes.ORIGIN),
					Component.literal("ContextParamMap.has(ContextParamTypes.ORIGIN) != true")
			);
			ParameterMap.Mutable mutableParamMap = ParameterMap.Mutable.of(paramMap);
			Vec3 newOrigin = createOrigin().getBottomCenter();
			helper.assertTrue(
					mutableParamMap.get(GlobalParameterTypes.ORIGIN)
							.equals(paramMap.get(GlobalParameterTypes.ORIGIN)),
					Component.literal("ContextParamMap.Mutable.get(ContextParamTypes.ORIGIN) != oldOrigin")
			);
			Parameter<Vec3> oldOrigin = mutableParamMap.set(GlobalParameterTypes.ORIGIN, newOrigin);
			helper.assertTrue(
					paramMap.get(GlobalParameterTypes.ORIGIN).equals(oldOrigin),
					Component.literal("ContextParamMap.get(ContextParamTypes.ORIGIN) != oldOrigin")
			);
			helper.assertTrue(
					mutableParamMap.get(GlobalParameterTypes.ORIGIN)
							.value()
							.equals(newOrigin),
					Component.literal("ContextParamMap.Mutable.get(ContextParamTypes.ORIGIN) != newOrigin")
			);
			try {
				createInvalidParamMap();
				helper.fail(Component.literal("Invalid parameter type allowed in ContextParamMap"));
			} catch (InvalidContextParameterException ignored) {
			}
			try {
				createMissingParamMap();
				helper.fail(Component.literal("Missing parameter allowed in ContextParamMap"));
			} catch (InvalidContextParameterException ignored) {
			}
			helper.succeed();
		} catch (InvalidContextParameterException ex) {
			helper.fail(Component.literal(ex.getMessage()));
		}
	}

	private static @NotNull ParameterSet createParamSet() {
		return ParameterSet.Builder.of()
				.required(GlobalParameterTypes.ORIGIN)
				.build();
	}

	private static ParameterMap createParamMap(BlockPos origin) throws InvalidContextParameterException {
		ParameterSet paramSet = createParamSet();
		ParameterMap.Builder builder = ParameterMap.Builder.of(paramSet)
				.withParameter(GlobalParameterTypes.ORIGIN, origin.getCenter());
		return builder.build();
	}

	private static void createInvalidParamMap() throws InvalidContextParameterException {
		ParameterSet paramSet = ParameterSet.Builder.of()
				.required(GlobalParameterTypes.BLOCK_STATE)
				.required(GlobalParameterTypes.ORIGIN)
				.build();
		ParameterMap.Builder.of(paramSet)
				.withParameter(GlobalParameterTypes.THIS_ENTITY, new Parameter<>(null))
				.build();
	}

	private static void createMissingParamMap() throws InvalidContextParameterException {
		ParameterSet paramSet = ParameterSet.Builder.of()
				.required(GlobalParameterTypes.BLOCK_STATE)
				.required(GlobalParameterTypes.ORIGIN)
				.build();
		ParameterMap.Builder.of(paramSet)
				.build();
	}

	@Override
	public @NotNull MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected @NotNull MutableComponent typeDescription() {
		return Component.literal("Silicate Context Param Map Test");
	}

	private static BlockPos createOrigin() {
		return new BlockPos(1, 1, 1);
	}
}
