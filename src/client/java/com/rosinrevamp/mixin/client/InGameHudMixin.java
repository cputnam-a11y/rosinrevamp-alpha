package com.rosinrevamp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;

import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

@Mixin(value = InGameHud.class, priority = Integer.MAX_VALUE)
public abstract class InGameHudMixin {
	@Shadow private MinecraftClient client;
	@Shadow private DebugHud debugHud;
	@Shadow protected abstract boolean shouldRenderSpectatorCrosshair(HitResult hitResult);
	@Shadow protected abstract PlayerEntity getCameraPlayer();
	@Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

	/**
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	private void renderCrosshair(DrawContext context, RenderTickCounter tickCounter) {
		GameOptions gameOptions = this.client.options;
		if (gameOptions.getPerspective().isFirstPerson()) {
			if (this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || this.shouldRenderSpectatorCrosshair(this.client.crosshairTarget)) {
				RenderSystem.enableBlend();
				if (this.debugHud.shouldShowDebugHud() && !this.client.player.hasReducedDebugInfo() && !gameOptions.getReducedDebugInfo().getValue()) {
					Camera camera = this.client.gameRenderer.getCamera();
					Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
					matrix4fStack.pushMatrix();
					matrix4fStack.mul(context.getMatrices().peek().getPositionMatrix());
					matrix4fStack.translate((float)(context.getScaledWindowWidth() / 2), (float)(context.getScaledWindowHeight() / 2), 0.0F);
					matrix4fStack.rotateX(-camera.getPitch() * (float) (Math.PI / 180.0));
					matrix4fStack.rotateY(camera.getYaw() * (float) (Math.PI / 180.0));
					matrix4fStack.scale(-1.0F, -1.0F, -1.0F);
					RenderSystem.applyModelViewMatrix();
					RenderSystem.renderCrosshair(10);
					matrix4fStack.popMatrix();
					RenderSystem.applyModelViewMatrix();
				} else {
					RenderSystem.blendFuncSeparate(
						GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
					);
					context.drawGuiTexture(Identifier.ofVanilla("hud/crosshair"), (context.getScaledWindowWidth() - 15) / 2, (context.getScaledWindowHeight() - 15) / 2, 15, 15);
					if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
						float cooldown = this.client.player.getAttackCooldownProgress(0.0F);
						boolean showPlus = this.client.targetedEntity != null
							&& this.client.targetedEntity instanceof LivingEntity
							&& cooldown >= 1.0F
							&& this.client.player.getStackInHand(Hand.MAIN_HAND).contains(DataComponentTypes.DAMAGE)
							&& this.client.targetedEntity.isAlive();

						int currY = context.getScaledWindowHeight() / 2 + 9;
						int leftsideX = context.getScaledWindowWidth() / 2 - 8;
						if (showPlus) {
							context.drawGuiTexture(Identifier.ofVanilla("hud/crosshair_attack_indicator_full"), leftsideX, currY, 16, 16);
						} else if (cooldown >= -0.25F && cooldown < 1.0F) {
							int calculatedWidth = (int)(cooldown * 17.0F);
							context.drawGuiTexture(Identifier.ofVanilla("hud/crosshair_attack_indicator_background"), leftsideX, currY, 16, 4);
							context.drawGuiTexture(Identifier.ofVanilla("hud/crosshair_attack_indicator_progress"), 16, 4, 0, 0, leftsideX, currY, calculatedWidth, 4);
						}
					}

					RenderSystem.defaultBlendFunc();
				}

				RenderSystem.disableBlend();
			}
		}
	}
	@SuppressWarnings("unused")
	private void renderHotbar(DrawContext context, RenderTickCounter tickCounter) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			ItemStack itemStack = playerEntity.getOffHandStack();
			Arm offHand = playerEntity.getMainArm().getOpposite();
			int midpointX = context.getScaledWindowWidth() / 2;
			RenderSystem.enableBlend();
			context.getMatrices().push();
			context.getMatrices().translate(0.0F, 0.0F, -90.0F);
			context.drawGuiTexture(Identifier.ofVanilla("hud/hotbar"), midpointX - 91, context.getScaledWindowHeight() - 22, 182, 22);
			context.drawGuiTexture(
				Identifier.ofVanilla("hud/hotbar_selection"), midpointX - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, context.getScaledWindowHeight() - 22 - 1, 24, 23
			);
			if (!itemStack.isEmpty()) {
				if (offHand == Arm.LEFT) {
					context.drawGuiTexture(Identifier.ofVanilla("hud/hotbar_offhand_left"), midpointX - 91 - 29, context.getScaledWindowHeight() - 23, 29, 24);
				} else {
					context.drawGuiTexture(Identifier.ofVanilla("hud/hotbar_offhand_right"), midpointX + 91, context.getScaledWindowHeight() - 23, 29, 24);
				}
			}

			context.getMatrices().pop();
			RenderSystem.disableBlend();
			int noFuckingClue = 0;

			for (int pos = 0; pos < 9; pos++) {
				int currX = midpointX - 90 + pos * 20 + 2;
				int currY = context.getScaledWindowHeight() - 19;
				this.renderHotbarItem(context, currX, currY, tickCounter, playerEntity, (ItemStack)playerEntity.getInventory().main.get(pos), ++noFuckingClue);
			}

			if (!itemStack.isEmpty()) {
				int currY = context.getScaledWindowHeight() - 19;
				if (offHand == Arm.LEFT) {
					this.renderHotbarItem(context, midpointX - 91 - 26, currY, tickCounter, playerEntity, itemStack, ++noFuckingClue);
				} else {
					this.renderHotbarItem(context, midpointX + 91 + 10, currY, tickCounter, playerEntity, itemStack, ++noFuckingClue);
				}
			}

			if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
				RenderSystem.enableBlend();
				float cooldown = this.client.player.getAttackCooldownProgress(0.0F);
				if (cooldown >= -0.25F && cooldown < 1.0F) {
					int currY = context.getScaledWindowHeight() - 20;
					int currX = midpointX + 97;
					if (offHand == Arm.RIGHT) {
						currX = midpointX - 113;
					}

					int calculatedHeight = (int)(cooldown * 19.0F);
					context.drawGuiTexture(Identifier.ofVanilla("hud/hotbar_attack_indicator_background"), currX, currY, 18, 18);
					context.drawGuiTexture(Identifier.ofVanilla("hud/hotbar_attack_indicator_progress"), 18, 18, 0, 18 - calculatedHeight, currX, currY + 18 - calculatedHeight, 18, calculatedHeight);
				}

				RenderSystem.disableBlend();
			}
		}
	}
}
