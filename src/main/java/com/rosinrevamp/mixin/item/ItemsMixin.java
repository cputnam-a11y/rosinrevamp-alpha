package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SnowballItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Items.class)
public class ItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/BedItem;"))
    private static BedItem wrapNewBed(Block block, Item.Settings settings, Operation<BedItem> original) {
        return original.call(block, settings.maxCount(64));
    }
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/SnowballItem;"))
    private static SnowballItem wrapNewSnowball(Item.Settings settings, Operation<SnowballItem> original) {
        return original.call(settings.maxCount(64));
    }
}
