package net.natsucamellia.fireworkextensions.shape;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public interface ExplosionShape {
    Identifier getIdentifier();
    List<Item> getIngredientItems();
    void explode(ExplosionShape shape, Vec3d coords, int[] colors, int[] fadeColors, boolean trail, boolean flicker);
}
