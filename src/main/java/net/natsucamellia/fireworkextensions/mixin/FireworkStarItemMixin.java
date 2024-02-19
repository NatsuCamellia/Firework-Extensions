package net.natsucamellia.fireworkextensions.mixin;

import net.minecraft.item.FireworkStarItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.natsucamellia.fireworkextensions.FireworkExtensions;
import net.natsucamellia.fireworkextensions.shape.ExplosionShape;
import net.natsucamellia.fireworkextensions.shape.VanillaExplosionShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireworkStarItem.class)
public class FireworkStarItemMixin {

    /**
     * This injection add translation key support for displaying tooltips of custom explosion shapes.
     */
    @Inject(method = "appendFireworkTooltip", at = @At("HEAD"), cancellable = true)
    private static void appendFireworkTooltip(NbtCompound nbt, List<Text> tooltip, CallbackInfo ci) {
        int[] fadeColors;
        ExplosionShape shape = FireworkExtensions.getExplosionShapeById(nbt.getByte("Type")).orElse(VanillaExplosionShapes.SMALL_BALL);
        tooltip.add(Text.translatable("item." + shape.getIdentifier().getNamespace() +".firework_star.shape." + shape.getIdentifier().getPath()).formatted(Formatting.GRAY));
        int[] colors = nbt.getIntArray("Colors");
        if (colors.length > 0) {
            tooltip.add(appendColors(Text.empty().formatted(Formatting.GRAY), colors));
        }
        if ((fadeColors = nbt.getIntArray("FadeColors")).length > 0) {
            tooltip.add(appendColors(Text.translatable("item.minecraft.firework_star.fade_to").append(ScreenTexts.SPACE).formatted(Formatting.GRAY), fadeColors));
        }
        if (nbt.getBoolean("Trail")) {
            tooltip.add(Text.translatable("item.minecraft.firework_star.trail").formatted(Formatting.GRAY));
        }
        if (nbt.getBoolean("Flicker")) {
            tooltip.add(Text.translatable("item.minecraft.firework_star.flicker").formatted(Formatting.GRAY));
        }
        ci.cancel();
    }

    @Unique
    private static Text appendColors(MutableText line, int[] colors) {
        for (int i = 0; i < colors.length; ++i) {
            if (i > 0) {
                line.append(", ");
            }
            line.append(getColorText(colors[i]));
        }
        return line;
    }

    @Unique
    private static Text getColorText(int color) {
        DyeColor dyeColor = DyeColor.byFireworkColor(color);
        if (dyeColor == null) {
            return Text.translatable("item.minecraft.firework_star.custom_color");
        }
        return Text.translatable("item.minecraft.firework_star." + dyeColor.getName());
    }
}
