package com.rosinrevamp;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.world.World;

public class FireflyEntity extends AmbientEntity {
	public FireflyEntity(EntityType<? extends FireflyEntity> entityType, World world) {
		super(entityType, world);
	}
	// public FireflyEntity(World world, double x, double y, double z) {
	// 	this(RosinRevamp.FIREFLY, world);
	// 	this.setPosition(x, y, z);
	// 	this.prevX = x;
	// 	this.prevY = y;
	// 	this.prevZ = z;
	// }
}
