package com.rosinrevamp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.rosinrevamp.RosinRevampClient;

@Mixin(value = GameRenderer.class, priority = Integer.MAX_VALUE)
public abstract class GameRendererMixin implements AutoCloseable {
	@Accessor public abstract MinecraftClient getClient();
	@Shadow private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {return null;}

	/**
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	private HitResult findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
		MinecraftClient client = this.getClient();
		double range = Math.max(blockInteractionRange, entityInteractionRange);
		double sqRange = range * range;
		Vec3d cameraPos = camera.getCameraPosVec(tickDelta);
		HitResult hitResult = camera.raycast(range, tickDelta, false);
		double sqDistance = hitResult.getPos().squaredDistanceTo(cameraPos);
		if (hitResult.getType() != Type.MISS) {
			sqRange = sqDistance;
			range = Math.sqrt(sqDistance);
		}

		Vec3d cameraRotation = camera.getRotationVec(tickDelta);
		Box box = camera.getBoundingBox().stretch(cameraRotation.multiply(range)).expand(1.0, 1.0, 1.0);
		EntityHitResult entityHitResult = ProjectileUtil.raycast(
			camera,
			cameraPos,
			cameraPos.add(cameraRotation.x * range, cameraRotation.y * range, cameraRotation.z * range),
			box,
			entity -> !entity.isSpectator() && entity.canHit(),
			sqRange
		);

		// Code for sweeping attacks. Someone please improve this.
		if (entityHitResult == null && RosinRevampClient.canPlayerSweep(client.player)) {
			List<LivingEntity> sweepTargets = client.world.getNonSpectatingEntities(
				LivingEntity.class,
				client.player.getBoundingBox().offset(client.player.getRotationVec(tickDelta).multiply(1.3)).expand(1.0, 0.25, 1.0)
			);
			for (LivingEntity target: sweepTargets) {
				if (target != client.player
					&& !client.player.isTeammate(target)
					&& !(target instanceof ArmorStandEntity armorStandEntity && armorStandEntity.isMarker())
					&& client.player.squaredDistanceTo(target) < 9.0) {

					entityHitResult = new EntityHitResult(target);
					break;
				}
			}
		}

		return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(cameraPos) < sqDistance
			? ensureTargetInRange(entityHitResult, cameraPos, entityInteractionRange)
			: ensureTargetInRange(hitResult, cameraPos, blockInteractionRange);
	}
}
