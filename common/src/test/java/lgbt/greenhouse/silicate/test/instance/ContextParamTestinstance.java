package lgbt.greenhouse.silicate.test.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import lgbt.greenhouse.silicate.api.context.param.ContextParamSet;
import lgbt.greenhouse.silicate.api.context.param.ContextParamTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContextParamTestinstance extends GameTestInstance {
	public static final MapCodec<ContextParamTestinstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TestData.CODEC
					.forGetter(ContextParamTestinstance::info)
	).apply(inst, ContextParamTestinstance::new));

	protected ContextParamTestinstance(TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
	}

	@Override
	public void run(@NotNull GameTestHelper helper) {
		ContextParamSet paramSet = ContextParamSet.Builder.of()
				.required(ContextParamTypes.ORIGIN)
				.required(ContextParamTypes.BLOCK_STATE)
				.optional(ContextParamTypes.BLOCK_ENTITY)
				.optional(ContextParamTypes.THIS_ENTITY)
				.build();
		helper.assertFalse(
				paramSet.isRequired(ContextParamTypes.THIS_ENTITY),
				Component.literal("ContextParamSet.isRequired(ContextParamTypes.THIS_ENTITY) != false")
		);
		helper.assertTrue(
				paramSet.getRequired()
						.containsAll(List.of(
								ContextParamTypes.ORIGIN,
								ContextParamTypes.BLOCK_STATE
						)),
				Component.literal("ContextParamSet.getRequired() does not contain required")
		);
		helper.assertTrue(
				paramSet.getAll()
						.containsAll(List.of(
								ContextParamTypes.ORIGIN,
								ContextParamTypes.BLOCK_STATE,
								ContextParamTypes.THIS_ENTITY
						)),
				Component.literal("ContextParamSet.getAll() does not contain all")
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.ORIGIN),
				Component.literal("ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true")
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.BLOCK_STATE),
				Component.literal("ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true")
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.THIS_ENTITY),
				Component.literal("ContextParamSet.hasParam(ContextParamTypes.ORIGIN) != true")
		);
		helper.assertTrue(
				paramSet.hasParam(ContextParamTypes.BLOCK_ENTITY),
				Component.literal("ContextParamSet.hasParam(ContextParamTypes.BLOCK_ENTITY) != true")
		);
		helper.assertFalse(
				paramSet.hasParam(ContextParamTypes.UNIT),
				Component.literal("ContextParamSet.hasParam(ContextParamTypes.UNIT) != false")
		);
		helper.succeed();
	}

	@Override
	public @NotNull MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected @NotNull MutableComponent typeDescription() {
		return Component.literal("Silicate Context Param Test");
	}
}
