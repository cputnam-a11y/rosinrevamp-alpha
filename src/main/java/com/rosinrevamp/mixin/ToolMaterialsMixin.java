package com.rosinrevamp.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin {
	@Shadow
	@Final
	@Mutable
	private float attackDamage;
	@Inject(method = "<init>", at = @At("TAIL"))
	private void modifyAttackDamage(
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
		this.attackDamage = attackDamage > 0.0F ? attackDamage - 1.0F : attackDamage;
//		Field attackDamageField = null;
//		String name = FabricLoader.getInstance().getMappingResolver().mapFieldName("intermediary", "net.minecraft.class_1832", "field_8931", "F");
//		try {
//			attackDamageField = ToolMaterials.class.getDeclaredField(name);
//		} catch (NoSuchFieldException iHaveAPreciousSettlementAndINeedCashNow) {
////			try {
////				attackDamageField = ToolMaterials.class.getDeclaredField("attackDamage");
////			} catch (NoSuchFieldException e) {
//				throw new RuntimeException(iHaveAPreciousSettlementAndINeedCashNow);
////			}
//		}
//		try {
//			attackDamageField.setAccessible(true);
//			attackDamageField.setFloat(this, attackDamage > 0.0F ? attackDamage - 1.0F : attackDamage);
//			attackDamageField.setAccessible(false);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
	}
}
