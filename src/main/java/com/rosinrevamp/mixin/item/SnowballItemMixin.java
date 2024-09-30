package com.rosinrevamp.mixin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowballItem.class)
public abstract class SnowballItemMixin extends Item implements ProjectileItem {
	private SnowballItemMixin(Item.Settings settings) {
		super(settings);
	}
// replaced by items mixin
//	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
//	private static Item.Settings resetStackSize(Item.Settings settings) {
//		return settings.maxCount(64);
//	}

	@Inject(method = "use", at = @At("TAIL"))
	private void addCooldown(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
		user.getItemCooldownManager().set(this, 4);
	}
}
