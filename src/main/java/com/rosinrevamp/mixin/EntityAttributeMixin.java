package com.rosinrevamp.mixin;

import net.minecraft.entity.attribute.EntityAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.rosinrevamp.EntityAttributeAccessor;

@Mixin(EntityAttribute.class)
public abstract class EntityAttributeMixin implements EntityAttributeAccessor {
	@Accessor public abstract void setFallback(double fallback);
}
