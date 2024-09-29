package com.rosinrevamp.mixin.handlers;

import com.rosinrevamp.AbstractBlockAccessor;
import com.rosinrevamp.RosinRevamp;

import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.BoatEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class BoatEntityHandlers extends EntityAbstractHandler {
	@Override
	protected void blockCollisionHandler(BlockState state, CallbackInfo __) {
		if (!((AbstractBlockAccessor)state.getBlock()).isCollidable()) return;
		RosinRevamp.LOGGER.info("Collided with " + state.getBlock());
	}
}
