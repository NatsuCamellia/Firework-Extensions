package net.natsucamellia.fireworkextensions.mixin;

import com.google.common.collect.Lists;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;
import net.natsucamellia.fireworkextensions.FireworkExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeMixin {
    @Final
    @Shadow
    private static Ingredient FLICKER_MODIFIER;

    @Final
    @Shadow
    private static Ingredient TRAIL_MODIFIER;

    @Final
    @Shadow
    private static Ingredient GUNPOWDER;

    /**
     * Used to check if a firework can be crafted with the given ingredients.
     * This injection uses custom TYPE_MODIFIER rather than the original one.
     */
    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    void matches(RecipeInputInventory recipeInputInventory, World world, CallbackInfoReturnable<Boolean> ci) {
        boolean gunpowder = false;
        boolean dye = false;
        boolean type = false;
        boolean trail = false;
        boolean flicker = false;
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack = recipeInputInventory.getStack(i);
            if (itemStack.isEmpty()) continue;

            if (FireworkExtensions.getTypeModifier().test(itemStack)) {
                if (type) {
                    ci.setReturnValue(false);
                    return;
                }
                type = true;
            } else if (FLICKER_MODIFIER.test(itemStack)) {
                if (flicker) {
                    ci.setReturnValue(false);
                    return;
                }
                flicker = true;
            } else if (TRAIL_MODIFIER.test(itemStack)) {
                if (trail) {
                    ci.setReturnValue(false);
                    return;
                }
                trail = true;
            } else if (GUNPOWDER.test(itemStack)) {
                if (gunpowder) {
                    ci.setReturnValue(false);
                    return;
                }
                gunpowder = true;
            } else if (itemStack.getItem() instanceof DyeItem) {
                dye = true;
            } else {
                ci.setReturnValue(false);
                return;
            }
        }
        ci.setReturnValue(gunpowder && dye);
    }

    /**
     * The main purpose of this injection is to give the firework a correct type id
     * according to the ingredient.
     */
    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    void craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager, CallbackInfoReturnable<ItemStack> ci) {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_STAR);
        NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("Explosion");
        int typeId = 0;
        ArrayList<Integer> colorList = Lists.newArrayList();

        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack2 = recipeInputInventory.getStack(i);
            if (itemStack2.isEmpty()) continue;

            if (FireworkExtensions.getTypeModifier().test(itemStack2)) {
                typeId = FireworkExtensions.getExplosionShapeIdByIngredientItem(itemStack2.getItem());
            } else if (FLICKER_MODIFIER.test(itemStack2)) {
                nbtCompound.putBoolean("Flicker", true);
            } else if (TRAIL_MODIFIER.test(itemStack2)) {
                nbtCompound.putBoolean("Trail", true);
            } else if (itemStack2.getItem() instanceof DyeItem) {
                colorList.add(((DyeItem)itemStack2.getItem()).getColor().getFireworkColor());
            }
        }

        nbtCompound.putIntArray("Colors", colorList);
        nbtCompound.putByte("Type", (byte)typeId);
        ci.setReturnValue(itemStack);
    }
}
