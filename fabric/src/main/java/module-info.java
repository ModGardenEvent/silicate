import org.jspecify.annotations.NullMarked;

@NullMarked
module lgbt.greenhouse.silicate.fabric {
	requires lgbt.greenhouse.silicate;
	requires net.fabricmc.loader;
	requires org.jetbrains.annotations;
	requires vanilla;
	requires fabric.events.interaction.v0;
	requires fabric.registry.sync.v0;
	requires org.jspecify;
	requires mixinextras.fabric;
}
