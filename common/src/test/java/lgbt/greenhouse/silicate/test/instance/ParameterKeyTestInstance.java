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
import lgbt.greenhouse.silicate.api.context.parameter.ParameterSet;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

import java.util.List;

public class ParameterKeyTestInstance extends GameTestInstance {
	public static final MapCodec<ParameterKeyTestInstance> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TestData.CODEC
					.forGetter(ParameterKeyTestInstance::info)
	).apply(inst, ParameterKeyTestInstance::new));

	protected ParameterKeyTestInstance(TestData<Holder<TestEnvironmentDefinition>> info) {
		super(info);
	}

	@Override
	public void run(GameTestHelper helper) {
		ParameterSet parameterSet = ParameterSet.Builder.of()
				.required(GlobalParameterKeys.ORIGIN)
				.required(GlobalParameterKeys.BLOCK_STATE)
				.optional(GlobalParameterKeys.BLOCK_ENTITY)
				.optional(GlobalParameterKeys.THIS_ENTITY)
				.build();
		helper.assertFalse(
				parameterSet.isRequired(GlobalParameterKeys.THIS_ENTITY),
				Component.literal("ParameterSet.isRequired(GlobalParameterKeys.THIS_ENTITY) != false")
		);
		helper.assertTrue(
				parameterSet.getRequired()
						.containsAll(List.of(
								GlobalParameterKeys.ORIGIN,
								GlobalParameterKeys.BLOCK_STATE
						)),
				Component.literal("ParameterSet.getRequired() does not contain required")
		);
		helper.assertTrue(
				parameterSet.getAll()
						.containsAll(List.of(
								GlobalParameterKeys.ORIGIN,
								GlobalParameterKeys.BLOCK_STATE,
								GlobalParameterKeys.THIS_ENTITY
						)),
				Component.literal("ParameterSet.getAll() does not contain all")
		);
		helper.assertTrue(
				parameterSet.hasParam(GlobalParameterKeys.ORIGIN),
				Component.literal("ParameterSet.hasParam(GlobalParameterKeys.ORIGIN) != true")
		);
		helper.assertTrue(
				parameterSet.hasParam(GlobalParameterKeys.BLOCK_STATE),
				Component.literal("ParameterSet.hasParam(GlobalParameterKeys.ORIGIN) != true")
		);
		helper.assertTrue(
				parameterSet.hasParam(GlobalParameterKeys.THIS_ENTITY),
				Component.literal("ParameterSet.hasParam(GlobalParameterKeys.ORIGIN) != true")
		);
		helper.assertTrue(
				parameterSet.hasParam(GlobalParameterKeys.BLOCK_ENTITY),
				Component.literal("ParameterSet.hasParam(GlobalParameterKeys.BLOCK_ENTITY) != true")
		);
		helper.assertFalse(
				parameterSet.hasParam(GlobalParameterKeys.UNIT),
				Component.literal("ParameterSet.hasParam(GlobalParameterKeys.UNIT) != false")
		);
		helper.succeed();
	}

	@Override
	public MapCodec<? extends GameTestInstance> codec() {
		return CODEC;
	}

	@Override
	protected MutableComponent typeDescription() {
		return Component.literal("Silicate Parameter Key Test");
	}
}
