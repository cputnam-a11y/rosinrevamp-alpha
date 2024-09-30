package com.rosinrevamp.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow public abstract float getWidth();

	@Shadow public abstract float getHeight();

	@ModifyReturnValue(method = "getTargetingMargin", at = @At("RETURN"))
	public float getTargetingMargin(float original) {
		double breadth = Math.cbrt(this.getWidth() * this.getWidth() * this.getHeight());
		return original + (breadth < 0.9 ? 0.5F * (0.9F - (float)breadth) : 0.0F);
	}
}
