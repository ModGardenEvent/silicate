package house.greenhouse.silicate.api.condition.builtin.math;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record Vec3Comparison(
		Comparison x,
		Comparison y,
		Comparison z
) {
	@SuppressWarnings("SequencedCollectionMethodCanBeUsed") // breaks 0, 1, 2 pattern
	public static final Codec<Vec3Comparison> CODEC = Comparison.CODEC
			.listOf()
			.comapFlatMap(
					list -> Util.fixedSize(list, 3)
							.map(ops -> new Vec3Comparison(ops.get(0), ops.get(1), ops.get(2))),
					comparison -> List.of(comparison.x, comparison.y, comparison.z)
			);
	
	public boolean compare(Vec3 former, Vec3 latter) {
		return this.x.compare(former.x(), latter.x()) &&
				this.y.compare(former.y(), latter.y()) &&
				this.z.compare(former.z(), latter.z());
	}
}