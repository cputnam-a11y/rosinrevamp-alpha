package com.rosinrevamp.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
    @Shadow
    protected abstract void takeShieldHit(LivingEntity attacker);

    @Shadow
    protected abstract boolean tryUseTotem(DamageSource source);

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract void applyDamage(DamageSource source, float amount);

    @Shadow
    protected abstract void playHurtSound(DamageSource damageSource);

    @Shadow
    protected abstract void damageShield(float amount);

    @Shadow
    protected abstract void damageHelmet(DamageSource source, float amount);

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow
    public abstract boolean isDead();

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract boolean isSleeping();

    @Shadow
    public abstract void wakeUp();

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract Hand getActiveHand();

    @Shadow
    public abstract void clearActiveItem();

    @Shadow
    public abstract boolean blockedByShield(DamageSource source);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void setAttacker(@Nullable LivingEntity attacker);

    @Shadow
    public abstract void playSound(@Nullable SoundEvent sound);

    @Shadow
    public abstract Collection<StatusEffectInstance> getStatusEffects();

    @Shadow
    public abstract void onDeath(DamageSource damageSource);

    @Shadow
    public abstract void takeKnockback(double strength, double x, double z);

    @Shadow
    public abstract void tiltScreen(double deltaX, double deltaZ);

    @Shadow
    @Final
    public LimbAnimator limbAnimator;

    @Shadow
    public int hurtTime;

    @Shadow
    public int maxHurtTime;

    @Shadow
    protected float lastDamageTaken;

    @Shadow
    protected int playerHitTimer;

    @Shadow
    @Nullable
    protected PlayerEntity attackingPlayer;

    @Shadow
    @Nullable
    private DamageSource lastDamageSource;

    @Shadow
    private long lastDamageTime;

    @Shadow
    protected int despawnCounter;

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
        LivingEntity self = (LivingEntity) (Object) this;
        World world = this.getWorld();
        if (this.isInvulnerableTo(source)
                || world.isClient
                || this.isDead()
                || source.isIn(DamageTypeTags.IS_FIRE) && this.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {

            return false;
        }

        if (this.isSleeping() && !world.isClient) {
            this.wakeUp();
        }
        // TODO! Inserted
        if (source.getAttacker() instanceof LivingEntity) {
            ItemStack activeItemStack = this.getStackInHand(this.getActiveHand());
            if (activeItemStack.get(DataComponentTypes.FOOD) != null) {
                this.clearActiveItem();
            }
        }
        // end
        this.despawnCounter = 0;

        boolean fullyBlocked = false, knocked = true;
        float damageBlocked = 0.0F;
        boolean didBlock = amount > 0.0F && this.blockedByShield(source);
        if (didBlock) {
            if (!source.isIn(DamageTypeTags.IS_PROJECTILE) && source.getSource() instanceof LivingEntity livingEntity) {
                // TODO! changed
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

        if (source.isIn(DamageTypeTags.IS_FREEZING) && this.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            amount *= 5.0F;
        }

        if (source.isIn(DamageTypeTags.DAMAGES_HELMET) && !this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            this.damageHelmet(source, amount);
            amount *= 0.75F;
        }

        this.limbAnimator.setSpeed(1.5F);
        boolean notInIframes = true;
        if (this.hurtTime > 0 && !source.isIn(DamageTypeTags.IS_PROJECTILE) && !source.isIn(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (amount <= this.lastDamageTaken) {
                return false;
            }

            this.applyDamage(source, amount - this.lastDamageTaken);
            this.lastDamageTaken = amount;
            notInIframes = false;
        } else {
            this.lastDamageTaken = amount;
            this.timeUntilRegen = 20;
            this.applyDamage(source, amount);
            this.maxHurtTime = 10;
            try {
                if (source.getSource() instanceof LivingEntity attacker && attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED) > 2.0) {
                    this.maxHurtTime = (int) (20.0 / attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED));
                } else {
                    this.maxHurtTime = 10;
                }
            } catch (Exception e) {
                this.maxHurtTime = 10;
            }
            this.hurtTime = this.maxHurtTime;
        }

        Entity attacker = source.getAttacker();
        if (attacker != null) {
            if (attacker instanceof LivingEntity livingEntity2
                    && !source.isIn(DamageTypeTags.NO_ANGER)
                    && !(source.isOf(DamageTypes.WIND_CHARGE) && this.getType().isIn(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE))
            ) {
                this.setAttacker(livingEntity2);
            }

            if (attacker instanceof PlayerEntity playerEntity) {
                this.playerHitTimer = 100;
                this.attackingPlayer = playerEntity;
            } else if (attacker instanceof WolfEntity wolfEntity && wolfEntity.isTamed()) {
                this.playerHitTimer = 100;
                if (wolfEntity.getOwner() instanceof PlayerEntity playerEntity2) {
                    this.attackingPlayer = playerEntity2;
                } else {
                    this.attackingPlayer = null;
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
                    x = source.getPosition().getX() - this.getX();
                    z = source.getPosition().getZ() - this.getZ();
                }

                this.takeKnockback(didBlock ? 0.2F : 0.4F, x, z);
                this.tiltScreen(x, z);
            }
        }

        if (this.isDead()) {
            if (!this.tryUseTotem(source)) {
                if (notInIframes) {
                    this.playSound(this.getDeathSound());
                }

                this.onDeath(source);
            }
        } else if (notInIframes) {
            this.playHurtSound(source);
        }

        if (amount > 0.0F) {
            this.lastDamageSource = source;
            this.lastDamageTime = world.getTime();

            for (StatusEffectInstance statusEffectInstance : this.getStatusEffects()) {
                statusEffectInstance.onEntityDamage(self, source, amount);
            }
        }

        if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
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
