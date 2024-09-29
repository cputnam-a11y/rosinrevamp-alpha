package com.rosinrevamp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
	@Accessor public abstract float getLastDamageTaken();
	@Accessor public abstract void setAttackingPlayer(PlayerEntity attackingPlayer);
	@Accessor public abstract void setDespawnCounter(int despawnCounter);
	@Accessor public abstract void setItemUseTimeLeft(int itemUseTimeLeft);
	@Accessor public abstract void setLastDamageTaken(float lastDamageTaken);
	@Accessor public abstract void setLastDamageSource(DamageSource lastDamageSource);
	@Accessor public abstract void setLastDamageTime(long lastDamageTime);
	@Accessor public abstract void setPlayerHitTimer(int playerHitTimer);
	@Shadow protected abstract void takeShieldHit(LivingEntity attacker);
	@Shadow protected abstract void knockback(LivingEntity target);
	@Shadow protected abstract boolean tryUseTotem(DamageSource source);
	@Shadow protected abstract SoundEvent getDeathSound();
	@Shadow protected abstract void applyDamage(DamageSource source, float amount);
	@Shadow protected abstract void playHurtSound(DamageSource damageSource);
	@Shadow protected abstract void damageShield(float amount);
	@Shadow protected abstract void damageHelmet(DamageSource source, float amount);
	private LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	@Redirect(method = "blockedByShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;dotProduct(Lnet/minecraft/util/math/Vec3d;)D"))
	public double decreaseShieldArc(Vec3d self, Vec3d vec) {
		return self.dotProduct(vec) + 0.6427876096865394;
	}
	@WrapOperation(method = "takeKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"))
	public void airborneKnockback(LivingEntity self, double dx, double dy, double dz, Operation<Double> original, double strength, double x, double z) {
		original.call(self, dx, 0.5 * self.getVelocity().y + (self.isOnGround() ? 1.0 : 0.5) * Math.min(0.4, strength), dz);
	}
	/**
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	public boolean damage(DamageSource source, float amount) {
		LivingEntity self = (LivingEntity)(Object)this;
		World world = self.getWorld();
		if (self.isInvulnerableTo(source)
			|| world.isClient
			|| self.isDead()
			|| source.isIn(DamageTypeTags.IS_FIRE) && self.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {

			return false;
		}

		if (self.isSleeping() && !world.isClient) {
			self.wakeUp();
		}

		if (source.getAttacker() instanceof LivingEntity) {
			ItemStack activeItemStack = self.getStackInHand(self.getActiveHand());
			if (activeItemStack.get(DataComponentTypes.FOOD) != null) {
				self.clearActiveItem();
			}
		}

		this.setDespawnCounter(0);

		boolean fullyBlocked = false, knocked = true;
		float damageBlocked = 0.0F;
		boolean didBlock = amount > 0.0F && self.blockedByShield(source);
		if (didBlock) {
			if (!source.isIn(DamageTypeTags.IS_PROJECTILE) && source.getSource() instanceof LivingEntity livingEntity) {
				damageBlocked = Math.min(amount, 5.0F);
				this.damageShield(damageBlocked);
				amount -= damageBlocked;
				this.takeShieldHit(livingEntity);
			} else {
				knocked = false;
				damageBlocked = amount;
				amount = 0.0F;
			}

			fullyBlocked = amount == 0.0F;
		}

		if (source.isIn(DamageTypeTags.IS_FREEZING) && self.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
			amount *= 5.0F;
		}

		if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !self.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
			this.damageHelmet(source, amount);
			amount *= 0.75F;
		}

		self.limbAnimator.setSpeed(1.5F);
		boolean notInIframes = true;
		if (self.hurtTime > 0 && !source.isIn(DamageTypeTags.IS_PROJECTILE) && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
			if (amount <= this.getLastDamageTaken()) {
				return false;
			}

			this.applyDamage(source, amount - this.getLastDamageTaken());
			this.setLastDamageTaken(amount);
			notInIframes = false;
		} else {
			this.setLastDamageTaken(amount);
			self.timeUntilRegen = 20;
			this.applyDamage(source, amount);
			self.maxHurtTime = 10;
			try {
				if (source.getSource() instanceof LivingEntity attacker && attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) > 2.0) {
					self.maxHurtTime = (int)(20.0 / attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED));
				} else {
					self.maxHurtTime = 10;
				}
			} catch (Exception e) {
				self.maxHurtTime = 10;
			}
			self.hurtTime = self.maxHurtTime;
		}

		Entity attacker = source.getAttacker();
		if (attacker != null) {
			if (attacker instanceof LivingEntity livingEntity2
				&& !source.isIn(DamageTypeTags.NO_ANGER)
				&& !(source.isOf(DamageTypes.WIND_CHARGE) && self.getType().isIn(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE))
			) {
				self.setAttacker(livingEntity2);
			}

			if (attacker instanceof PlayerEntity playerEntity) {
				this.setPlayerHitTimer(100);
				this.setAttackingPlayer(playerEntity);
			} else if (attacker instanceof WolfEntity wolfEntity && wolfEntity.isTamed()) {
				this.setPlayerHitTimer(100);
				if (wolfEntity.getOwner() instanceof PlayerEntity playerEntity2) {
					this.setAttackingPlayer(playerEntity2);
				} else {
					this.setAttackingPlayer(null);
				}
			}
		}

		if (notInIframes) {
			if (fullyBlocked) {
				world.sendEntityStatus(self, EntityStatuses.BLOCK_WITH_SHIELD);
			} else {
				world.sendEntityDamage(self, source);
			}

			if (knocked && !source.isIn(DamageTypeTags.NO_KNOCKBACK) && !source.isIn(DamageTypeTags.NO_IMPACT) && (amount > 0.0F || didBlock)) {
				this.scheduleVelocityUpdate();

				double x = 0.0, z = 0.0;
				if (source.getSource() instanceof ProjectileEntity projectileEntity) {
					DoubleDoubleImmutablePair knockback = projectileEntity.getKnockback(self, source);
					x = -knockback.leftDouble();
					z = -knockback.rightDouble();
				} else if (source.getPosition() != null) {
					x = source.getPosition().getX() - self.getX();
					z = source.getPosition().getZ() - self.getZ();
				}

				self.takeKnockback(didBlock ? 0.2F : 0.4F, x, z);
				self.tiltScreen(x, z);
			}
		}

		if (self.isDead()) {
			if (!this.tryUseTotem(source)) {
				if (notInIframes) {
					self.playSound(this.getDeathSound());
				}

				self.onDeath(source);
			}
		} else if (notInIframes) {
			this.playHurtSound(source);
		}
		
		if (amount > 0.0F) {
			this.setLastDamageSource(source);
			this.setLastDamageTime(world.getTime());

			for (StatusEffectInstance statusEffectInstance : self.getStatusEffects()) {
				statusEffectInstance.onEntityDamage(self, source, amount);
			}
		}

		if (self instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.ENTITY_HURT_PLAYER.trigger(serverPlayerEntity, source, amount, amount, fullyBlocked);
			if (didBlock) {
				serverPlayerEntity.increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0F));
			}
		}

		if (attacker instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.PLAYER_HURT_ENTITY.trigger(serverPlayerEntity, self, source, amount, amount, fullyBlocked);
		}

		return amount > 0.0F;
	}
}
