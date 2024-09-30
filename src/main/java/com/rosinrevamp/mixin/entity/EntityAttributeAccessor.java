package com.rosinrevamp.mixin.entity;

import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityAttribute.class)
@SuppressWarnings("unused")
public interface EntityAttributeAccessor {
    @Accessor void setFallback(double fallback);
}
