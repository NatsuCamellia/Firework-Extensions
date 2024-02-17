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

    public Identifier getIdentifier() {
        return identifier;
    }

    public List<Item> getIngredientItems() {
        return ingredientItems;
    }

    public abstract void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager);
}
