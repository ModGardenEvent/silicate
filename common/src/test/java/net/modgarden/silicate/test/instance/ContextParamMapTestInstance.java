package net.modgarden.silicate.test.instance;

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
import net.modgarden.silicate.api.context.param.ContextParam;
import net.modgarden.silicate.api.context.param.ContextParamMap;
import net.modgarden.silicate.api.context.param.ContextParamSet;
import net.modgarden.silicate.api.context.param.ContextParamTypes;
import net.modgarden.silicate.api.exception.InvalidContextParameterException;
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
			ContextParamSet paramSet = createParamSet();
			ContextParamMap paramMap = createParamMap(createOrigin());
			helper.assertTrue(
					paramMap.getParamSet().equals(paramSet),
					Component.literal("ContextParamMap.getParamSet() does not equal paramSet")
			);
			helper.assertTrue(
					paramMap.get(ContextParamTypes.BLOCK_ENTITY) == null,
					Component.literal("ContextParamMap.get(ContextParamTypes.BLOCK_ENTITY) != null")
			);
			helper.assertTrue(
					paramMap.get(ContextParamTypes.ORIGIN)
							.value()
							.equals(createOrigin().getCenter()),
					Component.literal("ContextParamTypes.ORIGIN is not equal to origin")
			);
			helper.assertFalse(
					paramMap.has(ContextParamTypes.BLOCK_ENTITY),
					Component.literal("ContextParamMap.has(ContextParamTypes.BLOCK_ENTITY) != false")
			);
			helper.assertTrue(
					paramMap.has(ContextParamTypes.ORIGIN),
					Component.literal("ContextParamMap.has(ContextParamTypes.ORIGIN) != true")
			);
			ContextParamMap.Mutable mutableParamMap = ContextParamMap.Mutable.of(paramMap);
			Vec3 newOrigin = createOrigin().getBottomCenter();
			helper.assertTrue(
					mutableParamMap.get(ContextParamTypes.ORIGIN)
							.equals(paramMap.get(ContextParamTypes.ORIGIN)),
					Component.literal("ContextParamMap.Mutable.get(ContextParamTypes.ORIGIN) != oldOrigin")
			);
			ContextParam<Vec3> oldOrigin = mutableParamMap.set(ContextParamTypes.ORIGIN, newOrigin);
			helper.assertTrue(
					paramMap.get(ContextParamTypes.ORIGIN).equals(oldOrigin),
					Component.literal("ContextParamMap.get(ContextParamTypes.ORIGIN) != oldOrigin")
			);
			helper.assertTrue(
					mutableParamMap.get(ContextParamTypes.ORIGIN)
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

	private static @NotNull ContextParamSet createParamSet() {
		return ContextParamSet.Builder.of()
				.required(ContextParamTypes.ORIGIN)
				.build();
	}

	private static ContextParamMap createParamMap(BlockPos origin) throws InvalidContextParameterException {
		ContextParamSet paramSet = createParamSet();
		ContextParamMap.Builder builder = ContextParamMap.Builder.of(paramSet)
				.withParameter(ContextParamTypes.ORIGIN, origin.getCenter());
		return builder.build();
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
