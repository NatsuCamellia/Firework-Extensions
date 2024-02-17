package net.natsucamellia.fireworkextensions.mixin;

import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import net.natsucamellia.fireworkextensions.FireworkExtensions;
import net.natsucamellia.fireworkextensions.shape.ExplosionShape;
import net.natsucamellia.fireworkextensions.shape.VanillaExplosionShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworksSparkParticle.FireworkParticle.class)
public class FireworkParticleMixin {
    @Shadow
    @Final
    private ParticleManager particleManager;

    @Inject(method = "explodeBall", at = @At("HEAD"), cancellable = true)
    private void injectExplodeBall(double size, int amount, int[] colors, int[] fadeColors, boolean trail, boolean flicker, CallbackInfo ci) {
        Particle particle = (Particle)(Object) this;
        FireworkParticleAccessor accessor = (FireworkParticleAccessor) particle;
        int index = accessor.getAge() / 2;
        NbtCompound explosionNbt = accessor.getExplosions().getCompound(index);

        ExplosionShape shape = FireworkExtensions.getExplosionShapeById(explosionNbt.getByte("Type")).orElse(VanillaExplosionShapes.SMALL_BALL);
        trail = explosionNbt.getBoolean("Trail");
        flicker = explosionNbt.getBoolean("Flicker");
        colors = explosionNbt.getIntArray("Colors");
        fadeColors = explosionNbt.getIntArray("FadeColors");
        if (colors.length == 0) {
            colors = new int[]{DyeColor.BLACK.getFireworkColor()};
        }

        ParticleAccessor particleAccessor = (ParticleAccessor) particle;
        Vec3d coords = new Vec3d(particleAccessor.getX(), particleAccessor.getY(), particleAccessor.getZ());
        Vec3d velocity = new Vec3d(particleAccessor.getVelocityX(), particleAccessor.getVelocityY(), particleAccessor.getVelocityZ());

        shape.explode(coords, colors, fadeColors, trail, flicker, particleManager);
        ci.cancel();
    }
}
