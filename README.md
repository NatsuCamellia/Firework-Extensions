# Firework Extensions

Feeling the vanilla explosion effects in Minecraft too boring?
This is a Minecraft Fabric Library for mod developers to add custom firework effects.

## Version

- Minecraft 1.20.1
- Fabric Loader 0.15.6+

## Usage

### Custom Explosion Shape

In order to create your custom explosion shape,
you can create a `ExplosionShape` instance and override the `explode()` method.

1. First, we need to give the instance an identifier to display its translated name on the tooltip.
The translation key is `"item.<namespace>.firework_star.shape.<path>"`.
In the following example, the translation key of `GIGANTIC_BALL` is `"item.fireworkextensions.firework_star.shape.gigantic_ball"`.

2. Next, we have to designate what kind of ingredients can be used to create firework stars with this explosion shape.
In vanilla Minecraft, fire charge is for `LARGE_BALL` and skull for `CREEPER`.
In the following example, to create firework star with `GIGANTIC_BALL` we need to put a ghast tear on a crafting table.
Keep in mind that only one ExplosionShape can exist in a firework star,
meaning you cannot put a ghast tear and a fire charge in a firework star for both shapes to exist.
You can pass in many items if you want.

3. Finally, we override `explode()` to modify the explosion shape.
If you want to create easy shapes such as `GIGANTIC_BALL` in the following example,
you can simply use methods defined in `ExplosionShape` class as the example does.
Or, if you want to create more complicated shapes, you can use your own explode method by checking methods in `ExplosionShape` to know how to program a explosion.
Those are vanilla codes with some minimal modifications for this library.

After we've created the explosion shape, we have to add it to the game by calling `FireworkExtensions.registerExplosionShape(shape)`.
Then we're done!


``` Java
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.natsucamellia.fireworkextensions.FireworkExtensions;

import java.util.List;

public class ExampleExplosionShapes {
    public static ExplosionShape GIGANTIC_BALL = new ExplosionShape(new Identifier(FireworkExtensions.MOD_ID, "gigantic_ball"), List.of(Items.GHAST_TEAR)) {
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            explodeBall(0.75f, 6, coords, colors, fadeColors, trail, flicker, particleManager);
        }
    };

    public static ExplosionShape SMALL_RING = new ExplosionShape(new Identifier(FireworkExtensions.MOD_ID, "small_ring"), List.of(Items.SLIME_BALL)) {
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            explodeHorizontalRing(0.25f, 0.4, 4, coords, colors, fadeColors, trail, flicker, particleManager);
        }
    };

    public static ExplosionShape SMALL_CUBE = new ExplosionShape(new Identifier(FireworkExtensions.MOD_ID, "small_cube"), List.of(Items.STONE)) {
        @Override
        public void explode(Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker, ParticleManager particleManager) {
            explodeStar(0.25f, new double[][]{{0, 1}, {1, 0}, {0, -1}}, 2, coords, colors, fadeColors, trail, flicker, false, particleManager);
        }
    };

    public static void registerCustomExplosionShapes() {
        FireworkExtensions.LOGGER.info("Registering custom explosion shapes");
        FireworkExtensions.registerExplosionShape(GIGANTIC_BALL);
        FireworkExtensions.registerExplosionShape(SMALL_RING);
        FireworkExtensions.registerExplosionShape(SMALL_CUBE);
    }
}
```
