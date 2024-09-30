package com.rosinrevamp.mixin.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {
	private BowItemMixin(Settings settings) {
		super(settings);
	}
	// Modify Args to force priority
	@ModifyArgs(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V"))
	public void fatigue(Args args, ItemStack f, World m, LivingEntity l, int remainingUseTicks) {
		args.set(6, remainingUseTicks > 71920 ? 0.25F : 4.0F);
		args.set(7, remainingUseTicks > 71920 ? args.get(7) : false);
	}
//	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;shootAll(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;)V"))
//	private void wrapShootAll(BowItem instance, ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target, Operation<Void> original, ItemStack bigStack, World bigWorld, LivingEntity bigUser, int bigRemainingUseTicks) {
//		original.call(instance, world, shooter, hand, stack, projectiles, speed, bigRemainingUseTicks > 71920 ? 0.25F : 4.0F, bigRemainingUseTicks > 71920 && critical, target);
//	}
}
