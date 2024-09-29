package com.rosinrevamp.mixin;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item implements ProjectileItem {
	private TridentItemMixin(Item.Settings settings) {
		super(settings);
	}

	/**
	 * @author Coarse Rosinflower
	 * @reason Changes the vanilla values for the trident, which requires throwing the {@link AttributeModifiersComponent}s away and making a new one.
	 */
	@Overwrite
	public static AttributeModifiersComponent createAttributeModifiers() {
		return AttributeModifiersComponent.builder()
			.add(
				EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.add(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.add(
				EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
				new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.build();
	}
}
