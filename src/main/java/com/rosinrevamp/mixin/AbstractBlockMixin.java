package com.rosinrevamp.mixin;

import com.rosinrevamp.AbstractBlockAccessor;

import net.minecraft.block.AbstractBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements AbstractBlockAccessor {
	@Accessor("collidable") public abstract boolean isCollidable();
}
