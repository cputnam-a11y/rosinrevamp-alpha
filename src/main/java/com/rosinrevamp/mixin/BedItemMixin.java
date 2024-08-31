package com.rosinrevamp.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BedItem.class)
public abstract class BedItemMixin extends BlockItem {
	private BedItemMixin(Block block, Item.Settings settings) {
		super(block, settings);
	}
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
	private static Item.Settings resetStackSize(Item.Settings settings) {
		return settings.maxCount(64);
	}
}
