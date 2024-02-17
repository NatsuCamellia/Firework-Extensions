package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.natsucamellia.fireworkextensions.FireworkExtensions;

import java.util.List;

public class VanillaExplosionShapes {
    public static ExplosionShape SMALL_BALL = new ExplosionShape(new Identifier("small_ball"), List.of()) {
        private final double size = 0.25;
        private final int amount = 2;
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            for (int i = -amount; i <= amount; ++i) {
                for (int j = -amount; j <= amount; ++j) {
                    for (int k = -amount; k <= amount; ++k) {
                        double g = (double)j + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        double h = (double)i + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        double l = (double)k + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        double m = Math.sqrt(g * g + h * h + l * l) / size + this.random.nextGaussian() * 0.05;
                        Vec3d velocity = new Vec3d(g / m, h / m, l / m);
                        this.addExplosionParticle(coords, velocity, colors, fadeColors, trail, flicker, particleManager);
                        if (i == -amount || i == amount || j == -amount || j == amount) continue;
                        k += amount * 2 - 1;
                    }
                }
            }
        }
    };

    public static void registerVanillaExplosionShapes() {
        FireworkExtensions.LOGGER.info("Registering vanilla explosion shapes");
        FireworkExtensions.registerExplosionShape(SMALL_BALL);
    }
}
