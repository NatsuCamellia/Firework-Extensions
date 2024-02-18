package net.natsucamellia.fireworkextensions;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.natsucamellia.fireworkextensions.shape.ExplosionShape;
import net.natsucamellia.fireworkextensions.shape.VanillaExplosionShapes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FireworkExtensions implements ModInitializer {
	public static final String MOD_ID = "fireworkextensions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Map<Integer, ExplosionShape> explosionShapes = new HashMap<>();
	private static final Map<Item, Integer> ingredientShapeIdMap = new HashMap<>();
	private static Ingredient TYPE_MODIFIER = null;
	private static int shapeCount = 0;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);
		VanillaExplosionShapes.registerVanillaExplosionShapes();
	}

	public static ExplosionShape registerExplosionShape(ExplosionShape shape) {
		LOGGER.info("Registering explosion type " + shape.getIdentifier());
		if (!explosionShapes.containsValue(shape)) {
			explosionShapes.put(shapeCount, shape);
			shape.getIngredientItems().forEach(item -> ingredientShapeIdMap.put(item, shapeCount));
			shapeCount++;
		}
		return shape;
	}

	public static Ingredient getTypeModifier() {
		if (TYPE_MODIFIER == null) {
			TYPE_MODIFIER = Ingredient.ofItems(explosionShapes.values().stream().map(ExplosionShape::getIngredientItems).flatMap(Collection::stream).toArray(ItemConvertible[]::new));
		}
		return TYPE_MODIFIER;
	}

	public static Integer getExplosionShapeIdByIngredientItem(Item item) {
		return ingredientShapeIdMap.get(item);
	}

	public static Optional<ExplosionShape> getExplosionShapeById(int id) {
		return Optional.ofNullable(explosionShapes.get(id));
	}
}