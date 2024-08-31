package com.rosinrevamp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rosinrevamp.KeyBindingAccessor;

@Mixin(value = MinecraftClient.class, priority = Integer.MAX_VALUE)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {
	private MinecraftClientMixin(RunArgs args) {
		super("Client");
	}
	@Inject(method = "handleInputEvents", at = @At("HEAD"))
	private void attackKeyHeld(CallbackInfo ci) {
		@SuppressWarnings("resource")
		MinecraftClient self = (MinecraftClient)(Object)this;
		if (self.options.attackKey.isPressed() && (self.crosshairTarget.getType() != Type.BLOCK && self.player.getAttackCooldownProgress(0.5F) >= 0.0F)) {
			KeyBinding.onKeyPressed(((KeyBindingAccessor)self.options.attackKey).getBoundKey());
		}
	}
}
