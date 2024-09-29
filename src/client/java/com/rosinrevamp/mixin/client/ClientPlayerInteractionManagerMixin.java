package com.rosinrevamp.mixin.client;

import net.minecraft.client.network.ClientPlayerInteractionManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	@Inject(method = "hasLimitedAttackSpeed", at = @At("HEAD"), cancellable = true)
	private void noLimitedAttackSpeed(CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(false);
	}
}
