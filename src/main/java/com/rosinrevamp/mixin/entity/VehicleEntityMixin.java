package com.rosinrevamp.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityMixin extends Entity {
	public VehicleEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "shouldAlwaysKill", at = @At("RETURN"))
	public boolean playerBoatBreak(boolean original, DamageSource damageSource) {
		return original || 
			((VehicleEntity)(Object)this) instanceof BoatEntity && damageSource.getSource() instanceof PlayerEntity;
	}
}
