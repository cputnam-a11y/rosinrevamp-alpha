package com.rosinrevamp.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HeldItemRenderer.class, priority = Integer.MAX_VALUE)
public abstract class HeldItemRendererMixin {
	@Accessor protected abstract MinecraftClient getClient();
	@Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

	@Redirect(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
	public float clampEquipProgress(ClientPlayerEntity player, float baseTime) {
		return Math.max(player.getAttackCooldownProgress(baseTime), 0.0F);
	}

	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
	private void renderBow(
		AbstractClientPlayerEntity player,
		float tickDelta,
		float pitch,
		Hand hand,
		float swingProgress,
		ItemStack item,
		float equipProgress,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		CallbackInfo info
	) {
		if (!player.isUsingItem() || player.getActiveHand() != hand || item.getUseAction() != UseAction.BOW) {
			return;
		}

		matrices.push();

		Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
		boolean isRighty = arm == Arm.RIGHT;
		float armSideSign = isRighty ? 1.0F : -1.0F;
		this.applyEquipOffset(matrices, arm, equipProgress);
		matrices.translate(armSideSign * -0.2785682F, 0.18344387F, 0.15731531F);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-13.935F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(armSideSign * 35.3F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armSideSign * -9.785F));
		float useTicks = (float)item.getMaxUseTime(player) - ((float)player.getItemUseTimeLeft() - tickDelta + 1.0F);
		float charge = useTicks / 20.0F;
		charge = (charge * charge + charge * 2.0F) / 3.0F;
		if (charge > 1.0F) {
			charge = 1.0F;
		}

		if (charge > 0.1F) {
			matrices.translate(0.0F, MathHelper.sin((useTicks - 0.1F) * 1.3F) * (charge - 0.1F) * (useTicks >= 80.0F ? 0.016F : 0.001F), 0.0F);
		}

		matrices.translate(0.0F, 0.0F, charge * 0.04F);
		matrices.scale(1.0F, 1.0F, 1.0F + charge * 0.2F);
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(armSideSign * 45.0F));

		((HeldItemRenderer)(Object)this).renderItem(
			player,
			item,
			isRighty ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
			!isRighty,
			matrices,
			vertexConsumers,
			light
		);

		matrices.pop();

		info.cancel();
	}
}
