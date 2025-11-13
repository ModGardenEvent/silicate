import org.jspecify.annotations.NullMarked;

@NullMarked
module lgbt.greenhouse.silicate {
	requires org.spongepowered.mixin;
	requires org.jetbrains.annotations;
	requires dev.lukebemish.codecextras;
	requires com.mojang.datafixerupper;
	requires vanilla;
	requires mixinextras.common;
	requires authlib;
	requires org.jspecify;
	requires jdk.unsupported;
	exports lgbt.greenhouse.silicate.api;
	exports lgbt.greenhouse.silicate.api.predicate;
	exports lgbt.greenhouse.silicate.api.predicate.meta;
	exports lgbt.greenhouse.silicate.api.predicate.minecraft;
	exports lgbt.greenhouse.silicate.api.predicate.minecraft.math;
	exports lgbt.greenhouse.silicate.api.predicate.std;
	exports lgbt.greenhouse.silicate.api.type;
	exports lgbt.greenhouse.silicate.api.context;
	exports lgbt.greenhouse.silicate.api.context.parameter;
	exports lgbt.greenhouse.silicate.api.exception;
}
