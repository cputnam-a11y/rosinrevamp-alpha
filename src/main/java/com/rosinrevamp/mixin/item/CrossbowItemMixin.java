package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@ModifyExpressionValue(method = "use", at = @At(value = "CONSTANT", args = "floatValue=1.0f"))
//	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FFLnet/minecraft/entity/LivingEntity;)V"), index = 5)
	private float modifyDivergence(float divergence) {
		return 0.25F * divergence;
	}
}
