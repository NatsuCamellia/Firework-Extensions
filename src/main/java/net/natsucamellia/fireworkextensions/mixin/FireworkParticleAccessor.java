package net.natsucamellia.fireworkextensions.mixin;

import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FireworksSparkParticle.FireworkParticle.class)
public interface FireworkParticleAccessor {
    @Accessor
    int getAge();

    @Accessor
    NbtList getExplosions();
}
