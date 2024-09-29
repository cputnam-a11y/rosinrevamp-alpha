package com.rosinrevamp.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@ModifyReturnValue(method = "getTargetingMargin", at = @At("RETURN"))
	public float getTargetingMargin(float original) {
		Entity self = (Entity)(Object)this;
		double breadth = Math.cbrt((double)(self.getWidth() * self.getWidth() * self.getHeight()));
		return original + (breadth < 0.9 ? 0.5F * (0.9F - (float)breadth) : 0.0F);
	}
}
