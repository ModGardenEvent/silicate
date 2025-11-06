module lgbt.greenhouse.silicate {
	requires org.spongepowered.mixin;
	requires org.jetbrains.annotations;
	requires dev.lukebemish.codecextras;
	requires com.mojang.datafixerupper;
	requires vanilla;
	requires mixinextras.common;
	requires authlib;
	exports lgbt.greenhouse.silicate.api;
	exports lgbt.greenhouse.silicate.api.condition;
	exports lgbt.greenhouse.silicate.api.condition.meta;
	exports lgbt.greenhouse.silicate.api.condition.builtin;
	exports lgbt.greenhouse.silicate.api.condition.builtin.math;
	exports lgbt.greenhouse.silicate.api.type;
	exports lgbt.greenhouse.silicate.api.context;
	exports lgbt.greenhouse.silicate.api.context.parameter;
	exports lgbt.greenhouse.silicate.api.exception;

	exports lgbt.greenhouse.silicate.impl to lgbt.greenhouse.silicate.fabric;
	exports lgbt.greenhouse.silicate.impl.platform to lgbt.greenhouse.silicate.fabric;
}
