package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.component.type.AttributeModifiersComponent;
@Mixin(MaceItem.class)
public abstract class MaceItemMixin extends Item {
	private MaceItemMixin(Settings settings) {
		super(settings);
	}

	/**
	 * @author Coarse Rosinflower
	 * @reason Changes the vanilla values for the mace, which requires throwing the {@link AttributeModifiersComponent} away and making a new one.
	 */
// NOTE! removed in favor of modifying the value directly
//	@Overwrite
//	public static AttributeModifiersComponent createAttributeModifiers() {
//		return AttributeModifiersComponent.builder()
//			.add(
//				EntityAttributes.GENERIC_ATTACK_DAMAGE,
//				new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 5.0, EntityAttributeModifier.Operation.ADD_VALUE),
//				AttributeModifierSlot.MAINHAND
//			)
//			.add(
//				EntityAttributes.GENERIC_ATTACK_SPEED,
//				new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -1.4F, EntityAttributeModifier.Operation.ADD_VALUE),
//				AttributeModifierSlot.MAINHAND
//			)
//			.build();
//	}
	@ModifyExpressionValue(method = "createAttributeModifiers", at = @At(value = "CONSTANT", args = "doubleValue=-3.4000000953674316"))
	private static double modifyAttackSpeed(double value) {
		return -1.4F;
	}
}
