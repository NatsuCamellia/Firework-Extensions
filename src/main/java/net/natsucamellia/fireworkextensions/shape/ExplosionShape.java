package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
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
     * @param size the size of the ball
     * @param amount the density of firework particles
     */
    protected void explodeBall(double size, int amount, Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
        explodeHorizontalRing(size, 1, amount, coords, colors, fadeColors, trail, flicker, particleManager);
    }

    /**
     * Explodes in horizontal ring shape.
     * @param size the size of the ring
     * @param amount the density of firework particles
     */
    protected void explodeHorizontalRing(double size, double heightRatio, int amount, Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
        for (int i = -(int)(amount * heightRatio); i <= (int)(amount * heightRatio); ++i) {
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

    /**
     * Explodes in mirrored {@code pattern} shape for {@code planes} times, rotating the Y-axis.
     * @param size the size of the shape
     * @param pattern a symmetric pattern of the explosion, only provide one side, the other side is mirrored automatically.
     * @param planes the number of planes
     * @param keepShape {@code true} for no rotating. (10% rotation compared to {@code false})
     */
    protected void explodeStar(double size, double[][] pattern, int planes, Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, boolean keepShape, ParticleManager particleManager) {
        double startH = pattern[0][0]; // Horizontal coordinate
        double startV = pattern[0][1]; // Vertical coordinate
        // Starting point
        this.addExplosionParticle(coords, new Vec3d(startH * size, startV * size, 0.0), colors, fadeColors, trail, flicker, particleManager);
        float thetaOffset = this.random.nextFloat() * (float)Math.PI; // Give random horizontal facing
        double rotationFactor = keepShape ? 0.1 / planes : 1.0 / planes;
        for (int i = 0; i < planes; ++i) {
            double theta = (double)thetaOffset + (double)((float)i * (float)Math.PI) * rotationFactor;
            double lastH = startH;
            double lastV = startV;
            for (int l = 1; l < pattern.length; ++l) {
                double currH = pattern[l][0];
                double currV = pattern[l][1];
                // Linear interpolation
                for (double o = 0.25; o <= 1.0; o += 0.25) {
                    double x = MathHelper.lerp(o, lastH, currH) * size;
                    double y = MathHelper.lerp(o, lastV, currV) * size;
                    double z = x * Math.sin(theta);
                    x *= Math.cos(theta);
                    // Mirror
                    for (double s = -1.0; s <= 1.0; s += 2.0) {
                        this.addExplosionParticle(coords, new Vec3d(x * s, y, z * s), colors, fadeColors, trail, flicker, particleManager);
                    }
                }
                lastH = currH;
                lastV = currV;
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
