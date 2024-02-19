package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.natsucamellia.fireworkextensions.mixin.ParticleAccessor;

import java.util.List;

public abstract class ExplosionShape {
    private final Identifier identifier;
    private final List<Item> ingredientItems;
    protected final Random random = Random.create();

    public ExplosionShape(Identifier identifier, List<Item> ingredientItems) {
        this.identifier = identifier;
        this.ingredientItems = ingredientItems;
    }

    protected void addExplosionParticle(Vec3d coords, Vec3d velocity, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
        FireworksSparkParticle.Explosion explosion = (FireworksSparkParticle.Explosion)particleManager.addParticle(ParticleTypes.FIREWORK, coords.x, coords.y, coords.z, velocity.x, velocity.y, velocity.z);
        explosion.setTrail(trail);
        explosion.setFlicker(flicker);
        ((ParticleAccessor)explosion).invokeSetAlpha(0.99f);
        int i = this.random.nextInt(colors.length);
        explosion.setColor(colors[i]);
        if (fadeColors.length > 0) {
            explosion.setTargetColor(Util.getRandom(fadeColors, this.random));
        }
    }

    /**
     * Explodes in ball shape.
     * @param size the radius of the ball
     * @param amount the total number of firework particle is about the cube of amount
     * @param coords the center of the ball
     * @param colors the initial colors of firework particles
     * @param fadeColors the final colors of firework particles
     * @param trail if a firework particle has a trail
     * @param flicker if a firework particle flickers
     */
    protected void explodeBall(double size, int amount, Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
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

    public Identifier getIdentifier() {
        return identifier;
    }

    public List<Item> getIngredientItems() {
        return ingredientItems;
    }

    public abstract void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager);
}
