package com.rosinrevamp.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TestCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TestCommand.class)
public class TestCommandMixin {
	// please explain this
	@Inject(method = "register", at = @At("HEAD"), cancellable = true)
	private static void pleaseDon_tCrashThisVeryFragileGame(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo info) {

//		info.cancel();
	}
}
