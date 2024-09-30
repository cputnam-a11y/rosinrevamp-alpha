package com.rosinrevamp.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	@SuppressWarnings("PointlessBooleanExpression")
    @ModifyReturnValue(method = "hasLimitedAttackSpeed", at = @At("RETURN"))
	private boolean noLimitedAttackSpeed(boolean original) {
		return original || false;
	}
}
