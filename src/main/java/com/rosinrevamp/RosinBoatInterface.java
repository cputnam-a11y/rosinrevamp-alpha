package com.rosinrevamp;

import net.minecraft.util.math.Vec3d;

public interface RosinBoatInterface {
	public void breakBoat();
	public void breakWithVelocity(double x, double y, double z);
	public void breakWithVelocity(Vec3d velocity);
}
