package com.rosinrevamp.mixin.entity;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Shadow
    private int foodLevel;
	@Shadow
	private float saturationLevel;
	@Shadow
	private float exhaustion;
	@Shadow
	private int foodTickTimer;
	@Shadow
	private int prevFoodLevel;

	@Shadow public abstract void addExhaustion(float exhaustion);

	/**
	 * TODO! FIx Overwrite, need to catalog changes + have conversation
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	public void update(PlayerEntity player) {
		Difficulty difficulty = player.getWorld().getDifficulty();
		this.prevFoodLevel = foodLevel;
		if (this.exhaustion > 4.0F) {
			this.exhaustion -= 4.0F;
			if (this.saturationLevel > 0.0F) {
				this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
			} else if (difficulty != Difficulty.PEACEFUL) {
				this.foodLevel = Math.max(this.foodLevel - 1, 0);
			}
		}

		if (player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) && this.foodLevel >= 7 && player.canFoodHeal()) {
			this.foodTickTimer += 1;
			if (this.foodTickTimer >= 40) {
				player.heal(1.0F);
				this.addExhaustion(2.0F);
				this.foodTickTimer = 0;
			}
		} else if (this.foodLevel <= 0) {
			this.foodTickTimer += 1;
			if (this.foodTickTimer >= 80) {
				if (player.getHealth() > 10.0F || /* player.getHealth() > 0.0F && */ difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
					player.damage(player.getDamageSources().starve(), 1.0F);
				}

				this.foodTickTimer = 0;
			}
		} else {
			this.foodTickTimer = 0;
		}
	}
}
