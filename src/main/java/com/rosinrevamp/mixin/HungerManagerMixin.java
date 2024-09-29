package com.rosinrevamp.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Accessor public abstract int getFoodLevel();
	@Accessor public abstract void setFoodLevel(int foodLevel);
	@Accessor public abstract float getSaturationLevel();
	@Accessor public abstract void setSaturationLevel(float saturationLevel);
	@Accessor public abstract float getExhaustion();
	@Accessor public abstract void setExhaustion(float exhaustion);
	@Accessor public abstract int getFoodTickTimer();
	@Accessor public abstract void setFoodTickTimer(int foodTickTimer);
	@Accessor public abstract int getPrevFoodLevel();
	@Accessor public abstract void setPrevFoodLevel(int prevFoodLevel);
	/**
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	public void update(PlayerEntity player) {
		HungerManager self = (HungerManager)(Object)this;
		Difficulty difficulty = player.getWorld().getDifficulty();
		this.setPrevFoodLevel(getFoodLevel());
		if (this.getExhaustion() > 4.0F) {
			this.setExhaustion(this.getExhaustion() - 4.0F);
			if (this.getSaturationLevel() > 0.0F) {
				this.setSaturationLevel(Math.max(this.getSaturationLevel() - 1.0F, 0.0F));
			} else if (difficulty != Difficulty.PEACEFUL) {
				this.setFoodLevel(Math.max(this.getFoodLevel() - 1, 0));
			}
		}

		if (player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) && this.getFoodLevel() >= 7 && player.canFoodHeal()) {
			this.setFoodTickTimer(this.getFoodTickTimer() + 1);
			if (this.getFoodTickTimer() >= 40) {
				player.heal(1.0F);
				self.addExhaustion(2.0F);
				this.setFoodTickTimer(0);
			}
		} else if (this.getFoodLevel() <= 0) {
			this.setFoodTickTimer(this.getFoodTickTimer() + 1);
			if (this.getFoodTickTimer() >= 80) {
				if (player.getHealth() > 10.0F || /* player.getHealth() > 0.0F && */ difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
					player.damage(player.getDamageSources().starve(), 1.0F);
				}

				this.setFoodTickTimer(0);
			}
		} else {
			this.setFoodTickTimer(0);
		}
	}
}
