package com.rosinrevamp.mixin;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin {
	public float getTargetingMargin() {
		Entity self = (Entity)(Object)this;
		double breadth = Math.cbrt((double)(self.getWidth() * self.getWidth() * self.getHeight()));
		return breadth < 0.9 ? 0.5F * (0.9F - (float)breadth) : 0.0F;
	}
}
