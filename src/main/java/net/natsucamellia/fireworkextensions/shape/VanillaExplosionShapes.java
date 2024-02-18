package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.natsucamellia.fireworkextensions.FireworkExtensions;

import java.util.List;

public class VanillaExplosionShapes {
    public static ExplosionShape SMALL_BALL = new VanillaExplosionShape(new Identifier("small_ball"), List.of());
    public static ExplosionShape LARGE_BALL = new VanillaExplosionShape(new Identifier("large_ball"), List.of(Items.FIRE_CHARGE));
    public static ExplosionShape STAR = new VanillaExplosionShape(new Identifier("star"), List.of(Items.GOLD_NUGGET));
    public static ExplosionShape CREEPER = new VanillaExplosionShape(new Identifier("creeper"), List.of(Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.PLAYER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD));
    public static ExplosionShape BURST = new VanillaExplosionShape(new Identifier("burst"), List.of(Items.FEATHER));

    public static void registerVanillaExplosionShapes() {
        FireworkExtensions.LOGGER.info("Registering vanilla explosion shapes");
        FireworkExtensions.registerExplosionShape(SMALL_BALL);
        FireworkExtensions.registerExplosionShape(LARGE_BALL);
        FireworkExtensions.registerExplosionShape(STAR);
        FireworkExtensions.registerExplosionShape(CREEPER);
        FireworkExtensions.registerExplosionShape(BURST);
    }

    /**
     * The implementation of vanilla explosions are in {@link net.minecraft.client.particle.FireworksSparkParticle.FireworkParticle#tick()}.
     */
    private static class VanillaExplosionShape extends ExplosionShape {
        public VanillaExplosionShape(Identifier identifier, List<Item> ingredientItems) {
            super(identifier, ingredientItems);
        }

        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {

        }
    }
}
