package com.rosinrevamp;

import com.rosinrevamp.attributemodifier.AttributeModifierModifiers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RosinRevamp implements ModInitializer {
    public static final String MOD_ID = "rosinrevamp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EntityAttributeModifier RANGE_EXTENTION_MODIFIER = new EntityAttributeModifier(Identifier.of(MOD_ID, "range_extension"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE);
    // public static final EntityType<FireflyEntity> FIREFLY = registerType("firefly", EntityType.Builder.<FireflyEntity>create(FireflyEntity::new, SpawnGroup.AMBIENT).dimensions(0.125F, 0.0625F).eyeHeight(0.03125F).maxTrackingRange(5));

    @Override
    public void onInitialize() {
        // brigadier runs before mod initializer so we don't need to set this here
        AttributeModifierModifiers.init();
    }

    public static void sendDebugCrash(Throwable e) {
        throw new RuntimeException(e);
    }

    public static void printStackTrace() {
        new Throwable().printStackTrace(System.out);
    }

    private static <T extends Entity> EntityType<T> registerType(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, id), type.build(id));
    }
}
