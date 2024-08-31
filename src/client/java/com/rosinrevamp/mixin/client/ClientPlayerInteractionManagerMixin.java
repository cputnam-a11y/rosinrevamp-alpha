package com.rosinrevamp.mixin.client;

import net.minecraft.client.network.ClientPlayerInteractionManager;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	public boolean hasLimitedAttackSpeed() {
		return false;
	}
}
