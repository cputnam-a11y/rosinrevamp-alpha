
package com.rosinrevamp.mixin.handlers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityAbstractHandler {
	@Inject(method = "onBlockCollision", at = @At("TAIL"))
	protected /* abstract */ void blockCollisionHandler(BlockState state, CallbackInfo info) {
	}
}
