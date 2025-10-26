package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import lgbt.greenhouse.silicate.duck.Duck_AbstractClientPlayer;

import java.util.List;
import java.util.Objects;

/**
 * Checks a player's {@link GameType} or "gamemode".
 * @param paramType The type of parameter.
 * @param gameTypes The {@link GameType}s to equality against. Tests true if any are equal.
 */
public record PlayerGameTypePredicate(
	GlobalParameterKey<Entity> paramType,
	List<GameType> gameTypes
) implements TypedGamePredicate<PlayerGameTypePredicate, Entity> {
	public static final MapCodec<PlayerGameTypePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		GlobalParameterKey.getCodec(Entity.class)
			.fieldOf("param_type")
			.forGetter(PlayerGameTypePredicate::paramType),
		Codec.mapEither(
			GameType.CODEC
				.listOf()
				.fieldOf("game_types"),
				GameType.CODEC
					.fieldOf("game_type")
		)
			.forGetter(PlayerGameTypePredicate::eitherGameType)
	).apply(instance, PlayerGameTypePredicate::of));
	public static final List<GameType> SURVIVAL_LIKE = List.of(GameType.SURVIVAL, GameType.ADVENTURE);

	private static PlayerGameTypePredicate of(GlobalParameterKey<Entity> paramType, Either<List<GameType>, GameType> eitherGameType) {
		if (eitherGameType.left().isPresent()) {
			return new PlayerGameTypePredicate(paramType, eitherGameType.left().get());
		} else if (eitherGameType.right().isPresent()) {
			return new PlayerGameTypePredicate(paramType, List.of(eitherGameType.right().get()));
		} else {
			throw new IllegalArgumentException("No value for Either (`game_types` or `game_type`) object: " + eitherGameType);
		}
	}

	private Either<List<GameType>, GameType> eitherGameType() {
		if (gameTypes.size() == 1) {
			return Either.right(gameTypes.getFirst());
		} else {
			return Either.left(gameTypes);
		}
	}

	@Override
	public boolean test(GameContext context) {
		Entity entity = context.getParam(paramType);
		if (entity instanceof Duck_AbstractClientPlayer player) {
			return gameTypes
				.stream()
				.anyMatch(
					Objects.requireNonNull(
						player.silicate$getPlayerInfo(),
						"Player has no GameType"
					).getGameMode()::equals
				);
		} else if (entity instanceof ServerPlayer player) {
			return gameTypes
				.stream()
				.anyMatch(player.gameMode.getGameModeForPlayer()::equals);
		} else {
			return false;
		}
	}

	@Override
	public MapCodec<PlayerGameTypePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<PlayerGameTypePredicate> getType() {
		return PredicateTypes.PLAYER_GAME_TYPE;
	}

	@Override
	public GlobalParameterKey<Entity> getParamType() {
		return paramType;
	}
}
