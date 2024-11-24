package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import house.greenhouse.silicate.duck.Duck_AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;

import java.util.List;
import java.util.Objects;

/**
 * Checks a player's {@link GameType} or "gamemode".
 * @param paramType The type of parameter.
 * @param gameTypes The {@link GameType}s to equality against. Tests true if any are equal.
 */
public record PlayerGameTypeCondition(
	ContextParamType<Entity> paramType,
	List<GameType> gameTypes
) implements TypedGameCondition<PlayerGameTypeCondition, Entity> {
	public static final MapCodec<PlayerGameTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		ContextParamType.getCodec(Entity.class)
			.fieldOf("param_type")
			.forGetter(PlayerGameTypeCondition::paramType),
		Codec.mapEither(
			GameType.CODEC
				.listOf()
				.fieldOf("game_types"),
				GameType.CODEC
					.fieldOf("game_type")
		)
			.forGetter(PlayerGameTypeCondition::eitherGameType)
	).apply(instance, PlayerGameTypeCondition::of));
	public static List<GameType> SURVIVAL_LIKE = List.of(GameType.SURVIVAL, GameType.ADVENTURE);
	
	private static PlayerGameTypeCondition of(ContextParamType<Entity> paramType, Either<List<GameType>, GameType> eitherGameType) {
		if (eitherGameType.left().isPresent()) {
			return new PlayerGameTypeCondition(paramType, eitherGameType.left().get());
		} else if (eitherGameType.right().isPresent()) {
			return new PlayerGameTypeCondition(paramType, List.of(eitherGameType.right().get()));
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
	public MapCodec<PlayerGameTypeCondition> getCodec() {
		return CODEC;
	}
	
	@Override
	public GameConditionType<PlayerGameTypeCondition> getType() {
		return GameConditionTypes.PLAYER_GAME_TYPE;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}