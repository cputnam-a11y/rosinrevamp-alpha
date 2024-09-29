package com.rosinrevamp.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.rosinrevamp.RosinRevamp;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Accessor public abstract int getCurrentExplosionResetGraceTime();
	@Accessor public abstract HungerManager getHungerManager();
	@Accessor public abstract ItemCooldownManager getItemCooldownManager();
	@Accessor public abstract ItemStack getSelectedItem();
	@Accessor public abstract int getSleepTimer();
	@Accessor public abstract void setCurrentExplosionResetGraceTime(int currentExplosionResetGraceTime);
	@Accessor public abstract void setSelectedItem(ItemStack selectedItem);
	@Accessor public abstract void setSleepTimer(int sleepTimer);
	@Shadow protected abstract void closeHandledScreen();
	@Shadow protected abstract float getDamageAgainst(Entity target, float baseDamage, DamageSource damageSource);
	@Shadow private void updateCapeAngles() {}
	@Shadow private void updateTurtleHelmet() {}
	@Shadow protected abstract void updatePose();
	@Shadow protected abstract boolean updateWaterSubmersionState();
	private PlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(EntityType.PLAYER, world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void modifyAttributes(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo info) {
		this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED).setBaseValue(2.0);
		this.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE).setBaseValue(2.5);
	}

	@ModifyArg(method = "disableShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"), index = 1)
	private int modifyDuration(int duration) {
		int cleaving = this.getStackInHand(Hand.MAIN_HAND).getEnchantments()
			.getLevel(this.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.EFFICIENCY).get());
		return 32 + (cleaving > 0 ? 5 * (cleaving + 1) : 0);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void onTick(CallbackInfo info) {
		try {
			if (((PlayerEntity)(Object)this).getAttackCooldownProgress(0.5F) >= 0.72F) {
				this.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE).addTemporaryModifier(RosinRevamp.RANGE_EXTENTION_MODIFIER);
			} else {
				this.getAttributeInstance(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE).removeModifier(RosinRevamp.RANGE_EXTENTION_MODIFIER.id());
			}
		} catch (IllegalArgumentException e) {}
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"))
	private void don_tResetLastAttackedTicks(PlayerEntity self) {
	}

	/**
	 * @author Coarse Rosinflower
	 * @reason Completely reworks the attack function.
	 */
	@Overwrite
	public void attack(Entity target) {
		PlayerEntity self = (PlayerEntity)(Object)this;
		World world = this.getWorld();
		if (!target.isAttackable() || target.handleAttack(this)) {
			return;
		}
		float cooldown = self.getAttackCooldownProgress(0.5F) * 0.8F + 0.2F;
		if (cooldown < 0.0F) {
			return;
		}
		DamageSource damageSource = this.getDamageSources().playerAttack(self);
		float calculatedDamage = this.getDamageAgainst(
			target,
			this.isUsingRiptide()
				? this.riptideAttackDamage
				: (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE),
			damageSource
		);
		ItemStack weaponStack = this.getWeaponStack();
		this.lastAttackedTicks = (int)(-1.5F * self.getAttackCooldownProgressPerTick());
		if (target.getType().isIn(EntityTypeTags.REDIRECTABLE_PROJECTILE)
			&& target instanceof ProjectileEntity projectileEntity
			&& projectileEntity.deflect(ProjectileDeflection.REDIRECTED, this, this, true)) {

			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory());
			return;
		}

		if (calculatedDamage <= 0.0F) {
			return;
		}
		boolean isStrong = this.isSprinting();
		if (isStrong) {
			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
		}
		
		calculatedDamage += weaponStack.getItem().getBonusAttackDamage(target, calculatedDamage, damageSource);
		boolean isCritical = this.fallDistance > 0.0F
			&& !this.isOnGround()
			&& !this.isClimbing()
			&& !this.isTouchingWater()
			&& !this.hasStatusEffect(StatusEffects.BLINDNESS)
			&& !this.hasVehicle()
			&& target instanceof LivingEntity;
		if (isCritical) {
			calculatedDamage *= 1.5F;
		}

		boolean isSweeping = cooldown > 0.9F
			&& !isStrong
			&& this.isOnGround()
			&& this.horizontalSpeed - this.prevHorizontalSpeed < this.getMovementSpeed()
			&& this.getStackInHand(Hand.MAIN_HAND).getEnchantments()
				.getLevel(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SWEEPING_EDGE).get()) > 0;

		float targetHealth = target instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 0.0F;

		Vec3d targetVelocity = target.getVelocity();
		if (!target.damage(damageSource, calculatedDamage)) {
			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
			return;
		}
		float knockbackDealt = this.getKnockbackAgainst(target, damageSource) + (isStrong ? 1.0F : 0.0F);
		if (knockbackDealt > 0.0F) {
			if (target instanceof LivingEntity livingEntity2) {
				livingEntity2.takeKnockback(
					(double)knockbackDealt * 0.5,
					Math.sin(Math.toRadians(this.getYaw())),
					-Math.cos(Math.toRadians(this.getYaw()))
				);
			} else {
				target.addVelocity(
					-Math.sin(Math.toRadians(this.getYaw())) * (double)knockbackDealt * 0.5,
					0.1,
					Math.cos(Math.toRadians(this.getYaw())) * (double)knockbackDealt * 0.5
				);
			}

			this.setVelocity(this.getVelocity().multiply(0.6, 1.0, 0.6));
			this.setSprinting(false);
		}

		if (isSweeping) {
			float sweepDamage = 1.0F + (float)this.getAttributeValue(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO) * calculatedDamage;
			for (LivingEntity sweepTarget : world.getNonSpectatingEntities(
					LivingEntity.class,
					this.getBoundingBox().offset(this.getRotationVec(1.0F).multiply(this.distanceTo(target))).expand(1.0, 0.25, 1.0))
				) {

				if (sweepTarget != this
					&& sweepTarget != target
					&& !this.isTeammate(sweepTarget)
					&& !(sweepTarget instanceof ArmorStandEntity && ((ArmorStandEntity)sweepTarget).isMarker())
					&& this.squaredDistanceTo(sweepTarget) < 9.0) {

					sweepTarget.takeKnockback(
						0.4F, Math.sin(Math.toRadians(this.getYaw())), -Math.cos(Math.toRadians(this.getYaw()))
					);
					sweepTarget.damage(damageSource, sweepDamage * cooldown);
					if (world instanceof ServerWorld serverWorld) {
						EnchantmentHelper.onTargetDamaged(serverWorld, sweepTarget, damageSource);
					}
				}
			}
			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
			self.spawnSweepAttackParticles();
		}

		if (target instanceof ServerPlayerEntity && target.velocityModified) {
			((ServerPlayerEntity)target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
			target.velocityModified = false;
			target.setVelocity(targetVelocity);
		}

		if (isCritical) {
			world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
			self.addCritParticles(target);
		}

		if (!isCritical && !isSweeping) {
			if (calculatedDamage > 1.0F) {
				world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
			} else {
				world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
			}
		}

		this.onAttacking(target);
		Entity entity = target instanceof EnderDragonPart part ? part.owner : target;

		boolean bl6 = false;
		if (world instanceof ServerWorld serverWorld2) {
			if (entity instanceof LivingEntity livingEntity3x) {
				bl6 = weaponStack.postHit(livingEntity3x, self);
			}

			EnchantmentHelper.onTargetDamaged(serverWorld2, target, damageSource);
		}

		if (!world.isClient && !weaponStack.isEmpty() && entity instanceof LivingEntity) {
			if (bl6) {
				weaponStack.postDamageEntity((LivingEntity)entity, self);
			}

			if (weaponStack.isEmpty()) {
				if (weaponStack == this.getMainHandStack()) {
					this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				} else {
					this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
				}
			}
		}

		if (world instanceof ServerWorld && target instanceof LivingEntity) {
			float damageDealt = targetHealth - ((LivingEntity)target).getHealth();
			self.increaseStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
			if (damageDealt > 2.0F) {
				((ServerWorld)world).spawnParticles(
					ParticleTypes.DAMAGE_INDICATOR,
					target.getX(),
					target.getBodyY(0.5),
					target.getZ(),
					(int)(damageDealt * 0.5F),
					0.1,
					0.0,
					0.1,
					0.2
				);
			}
		}

		self.addExhaustion(0.1F);
	}

	@ModifyReturnValue(method = "getAttackCooldownProgressPerTick", at = @At("RETURN"))
	public float modifyAttackCooldownProgressPerTick(float original) {
		return 0.8F * original;
	}
	@ModifyArg(method = "getAttackCooldownProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"), index = 1)
	public float extendClampBounds(float min) {
		return -1.5F;
	}

	@WrapOperation(method = "resetLastAttackedTicks", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;lastAttackedTicks:I", opcode = Opcodes.PUTFIELD))
	public void tareLastAttackedTicks(PlayerEntity self, int newValue, Operation<Void> original) {
		int zeroValue = (int)(-0.25F * self.getAttackCooldownProgressPerTick());
		if (this.lastAttackedTicks >= zeroValue) {
			original.call(self, zeroValue - 4);
		}
	}
}
