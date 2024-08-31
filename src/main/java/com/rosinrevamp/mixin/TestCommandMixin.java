package com.rosinrevamp.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TestCommand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TestCommand.class)
public class TestCommandMixin {
	@Overwrite
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		return;
	}
}
