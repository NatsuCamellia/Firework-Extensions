package net.natsucamellia.fireworkextensions;

import net.fabricmc.api.ModInitializer;

import net.natsucamellia.fireworkextensions.shape.ExplosionShape;
import net.natsucamellia.fireworkextensions.shape.VanillaExplosionShapes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FireworkExtensions implements ModInitializer {
	public static final String MOD_ID = "fireworkextensions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Map<Integer, ExplosionShape> explosionShapes = new HashMap<>();
	private static int shapeCount = 0;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);
		VanillaExplosionShapes.registerVanillaExplosionShapes();
	}

	public static ExplosionShape registerExplosionShape(ExplosionShape shape) {
		LOGGER.info("Registering explosion type " + shape.getIdentifier());
		if (!explosionShapes.containsValue(shape)) {
			explosionShapes.put(shapeCount++, shape);
		}
		return shape;
	}

	public static Optional<ExplosionShape> getExplosionShapeById(int id) {
		return Optional.ofNullable(explosionShapes.get(id));
	}
}