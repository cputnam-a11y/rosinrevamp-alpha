package com.rosinrevamp;

import net.fabricmc.api.ModInitializer;
import net.minecraft.SharedConstants;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RosinRevamp implements ModInitializer {
	public static final String MOD_ID = "rosinrevamp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EntityAttributeModifier RANGE_EXTENTION_MODIFIER = new EntityAttributeModifier(Identifier.of(MOD_ID, "range_extension"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE);

	@Override
	public void onInitialize() {
		SharedConstants.isDevelopment = true;
		LOGGER.info("hai :3 i loggin to conzole");
	}

	public static void sendDebugCrash(Throwable e) {
		throw new RuntimeException(e);
	}

	public static void printStackTrace() {
		new Throwable().printStackTrace(System.out);
	}
}
