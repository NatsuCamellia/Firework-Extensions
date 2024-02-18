package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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

    public static ExplosionShape LARGE_BALL = new ExplosionShape(new Identifier("large_ball"), List.of(Items.FIRE_CHARGE)) {
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            explodeBall(0.5, 4, coords, colors, fadeColors, trail, flicker, particleManager);
        }
    };

    public static ExplosionShape STAR = new DummyExplosionShape(new Identifier("star"), List.of(Items.GOLD_NUGGET));
    public static ExplosionShape CREEPER = new DummyExplosionShape(new Identifier("creeper"), List.of(Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.PLAYER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD, Items.PIGLIN_HEAD));
    public static ExplosionShape BURST = new DummyExplosionShape(new Identifier("burst"), List.of(Items.FEATHER));

    public static void registerVanillaExplosionShapes() {
        FireworkExtensions.LOGGER.info("Registering vanilla explosion shapes");
        FireworkExtensions.registerExplosionShape(SMALL_BALL);
        FireworkExtensions.registerExplosionShape(LARGE_BALL);
        FireworkExtensions.registerExplosionShape(STAR);
        FireworkExtensions.registerExplosionShape(CREEPER);
        FireworkExtensions.registerExplosionShape(BURST);
    }

    /**
     * Since this mod only overrides explodeBall(), used by only {@link #SMALL_BALL} and {@link #LARGE_BALL}, method,
     * the remaining vanilla shapes aren't affected. This class is specified for these unaffected shapes
     * for registering translations and ingredients. The implementation of explode is hardcoded in vanilla code.
     */
    private static class DummyExplosionShape extends ExplosionShape {
        public DummyExplosionShape(Identifier identifier, List<Item> ingredientItems) {
            super(identifier, ingredientItems);
        }

        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {

        }
    }
}
