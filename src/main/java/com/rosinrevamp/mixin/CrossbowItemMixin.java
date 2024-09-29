package com.rosinrevamp.mixin;

import net.minecraft.item.CrossbowItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FFLnet/minecraft/entity/LivingEntity;)V"), index = 5)
	private float modifyDivergence(float divergence) {
		return 0.25F * divergence;
	}
}
