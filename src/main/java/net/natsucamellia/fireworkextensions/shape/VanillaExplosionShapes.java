package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.natsucamellia.fireworkextensions.FireworkExtensions;

import java.util.List;

public class VanillaExplosionShapes {
    public static ExplosionShape SMALL_BALL = new ExplosionShape(new Identifier("small_ball"), List.of()) {
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            explodeBall(0.25, 2, coords, colors, fadeColors, trail, flicker, particleManager);
        }
    };

    public static void registerVanillaExplosionShapes() {
        FireworkExtensions.LOGGER.info("Registering vanilla explosion shapes");
        FireworkExtensions.registerExplosionShape(SMALL_BALL);
    }
}
