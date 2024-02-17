package net.natsucamellia.fireworkextensions.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Invoker(value = "setAlpha")
    void invokeSetAlpha(float f);

    @Accessor("x")
    double getX();

    @Accessor("y")
    double getY();

    @Accessor("z")
    double getZ();

    @Accessor("velocityX")
    double getVelocityX();

    @Accessor("velocityY")
    double getVelocityY();

    @Accessor("velocityZ")
    double getVelocityZ();
}
