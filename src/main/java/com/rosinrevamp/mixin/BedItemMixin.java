package com.rosinrevamp.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BedItem.class)
public abstract class BedItemMixin extends BlockItem {
	private BedItemMixin(Block block, Item.Settings settings) {
		super(block, settings);
	}

//  com.rosinrevamp.mixin.ItemsMixin replaced this.
//	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
//	private static Item.Settings resetStackSize(Item.Settings settings) {
//		return settings.maxCount(64);
//	}
}
