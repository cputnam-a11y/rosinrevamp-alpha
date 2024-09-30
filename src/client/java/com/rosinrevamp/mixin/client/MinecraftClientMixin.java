package com.rosinrevamp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = Integer.MAX_VALUE)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {
	@Shadow @Nullable public ClientPlayerEntity player;

	@Shadow @Nullable public HitResult crosshairTarget;

	@Shadow @Final public GameOptions options;

	private MinecraftClientMixin(RunArgs ignoredArgs) {
		super("Client");
	}
	@SuppressWarnings("DataFlowIssue")
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
	private void attackKeyHeld(CallbackInfo ci) {
		if (this.options.attackKey.isPressed() && (this.crosshairTarget.getType() != Type.BLOCK && this.player.getAttackCooldownProgress(0.5F) >= 0.0F)) {
			KeyBinding.onKeyPressed(((KeyBindingAccessor)this.options.attackKey).getBoundKey());
		}
	}
}
