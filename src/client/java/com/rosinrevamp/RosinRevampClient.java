package com.rosinrevamp;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class RosinRevampClient implements ClientModInitializer {
	public static boolean canPlayerSweep(PlayerEntity player) {
		World world = player.getWorld();
		return player.getAttackCooldownProgress(0.5F) > 0.875F
			&& !player.isSprinting()
			&& player.isOnGround()
			&& player.horizontalSpeed - player.prevHorizontalSpeed < player.getMovementSpeed()
			&& player.getStackInHand(Hand.MAIN_HAND).getEnchantments()
				.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SWEEPING_EDGE).get()) > 0;
	}
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}