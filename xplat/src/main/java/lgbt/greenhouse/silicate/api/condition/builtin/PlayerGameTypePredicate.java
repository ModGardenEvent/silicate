package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.impl.duck.Duck_AbstractClientPlayer;

import java.util.List;
import java.util.Objects;

/**
 * Checks a player's {@link GameType} or "gamemode".
 * @param player The player.
 * @param gameTypes The {@link GameType}s to equality against. Tests true if any are equal.
 */
public record PlayerGameTypePredicate(
	ParameterKey<Player> player,
	List<GameType> gameTypes
) implements GamePredicate<PlayerGameTypePredicate> {
	@Override
	public boolean test(GameContext context) {
		Entity entity = context.getParam(this.player);
		if (entity instanceof Duck_AbstractClientPlayer duck) {
			return gameTypes
				.stream()
				.anyMatch(
					Objects.requireNonNull(
						duck.silicate$getPlayerInfo(),
						"Player has no GameType"
					).getGameMode()::equals
				);
		} else if (entity instanceof ServerPlayer serverPlayer) {
			return gameTypes
				.stream()
				.anyMatch(serverPlayer.gameMode.getGameModeForPlayer()::equals);
		} else {
			return false;
		}
	}

	@Override
	public GamePredicate.Type<PlayerGameTypePredicate> getType() {
		return SilicatePredicateTypes.PLAYER_GAME_TYPE;
	}

	public static final class Type extends GamePredicate.Type<PlayerGameTypePredicate> {
		@Override
		protected MapCodec<PlayerGameTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(PlayerGameTypePredicate.class))
					.withParameter(
							"player",
							SilicateValueTypes.PLAYER,
							PlayerGameTypePredicate::player
					)
					.withValue(
							"game_type",
							SilicateValueTypes.LIST_GAME_TYPE,
							PlayerGameTypePredicate::gameTypes
					)
					.build();
		}
	}
}
