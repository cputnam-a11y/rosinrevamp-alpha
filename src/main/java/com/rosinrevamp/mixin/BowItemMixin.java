package com.rosinrevamp.mixin;

import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {
	private BowItemMixin(Settings settings) {
		super(settings);
	}
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!(user instanceof PlayerEntity playerEntity)) {
			return;
		}

		ItemStack itemStack = playerEntity.getProjectileType(stack);
		if (itemStack.isEmpty()) {
			return;
		}

		float charge = BowItem.getPullProgress(72000 - remainingUseTicks);
		if (charge < 0.1F) {
			return;
		}

		boolean isInaccurate = remainingUseTicks < 71920;
		List<ItemStack> list = load(stack, itemStack, playerEntity);
		if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
			this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, charge * 3.0F, isInaccurate ? 4.0F : 0.25F, charge == 1.0F && !isInaccurate, null);
		}
		world.playSound(
			null,
			playerEntity.getX(),
			playerEntity.getY(),
			playerEntity.getZ(),
			SoundEvents.ENTITY_ARROW_SHOOT,
			SoundCategory.PLAYERS,
			1.0F,
			(world.getRandom().nextFloat() * 0.2F + 0.615F) + (0.5F * charge)
		);
		playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
	}
}
