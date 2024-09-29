package com.rosinrevamp.mixin;

import com.rosinrevamp.RosinBoatInterface;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends VehicleEntity implements RosinBoatInterface {
	private BoatEntityMixin(EntityType<? extends BoatEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean shouldAlwaysKill(DamageSource damageSource) {
		if (damageSource.getSource() instanceof PlayerEntity) {
			return true;
		}
		return false;
	}

	public void breakBoat() {
		this.kill();
		if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			for (int i = 0; i < 3; i++) {
				this.dropItem(((BoatEntity)(Object)this).getVariant().getBaseBlock());
			}

			for (int i = 0; i < 2; i++) {
				this.dropItem(Items.STICK);
			}
		}
	}
	public void breakWithVelocity(double x, double y, double z) {
		this.breakBoat();
		if (this.hasPassengers()) {
			for (Entity entity : this.getPassengerList()) {
				entity.addVelocity(x, y, z);
			}
		}
	}
	public void breakWithVelocity(Vec3d velocity) {
		this.breakBoat();
		if (this.hasPassengers()) {
			for (Entity entity : this.getPassengerList()) {
				entity.addVelocity(velocity);
			}
		}
	}
}
