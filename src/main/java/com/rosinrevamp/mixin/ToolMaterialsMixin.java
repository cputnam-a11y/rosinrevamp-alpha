package com.rosinrevamp.mixin;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bigChungusJr(
		String bollocks,
		int bollocks2,
		final TagKey<Block> inverseTag,
		final int itemDurability,
		final float miningSpeed,
		final float attackDamage,
		final int enchantability,
		final Supplier<Ingredient> repairIngredient,
		CallbackInfo info
	) {
		Field attackDamageField = null;
		try {
			attackDamageField = ToolMaterials.class.getDeclaredField("field_8931");
		} catch (NoSuchFieldException iHaveAPreciousSettlementAndINeedCashNow) {
			try {
				attackDamageField = ToolMaterials.class.getDeclaredField("attackDamage");
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			attackDamageField.setAccessible(true);
			attackDamageField.setFloat((ToolMaterials)(Object)this, attackDamage > 0.0F ? attackDamage - 1.0F : attackDamage);
			attackDamageField.setAccessible(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
