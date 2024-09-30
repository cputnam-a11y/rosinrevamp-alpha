package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rosinrevamp.ItemsHelper;
import com.rosinrevamp.event.Events;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem {
	private SwordItemMixin(ToolMaterial material, Item.Settings settings) {
		super(material, settings);
	}
	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;"))
	private	static AttributeModifiersComponent fireModifyAttributeModifiers(AttributeModifiersComponent.Builder instance, Operation<AttributeModifiersComponent> original, ToolMaterial material, int baseAttackDamage, float attackSpeed) {
		var context = new ToolAttributeCreationContext(instance, ItemsHelper.CONTEXT_ID.get(), ItemsHelper.CONTEXT_ID.get(), material);
		Events.MODIFY_TOOL_ATTRIBUTES.invoker().onModifyToolAttributes(context);
		context.finish();
		return original.call(instance);
	}
	/**
	 * @author Coarse Rosinflower
	 * @reason Changes the vanilla values for the swords, which requires throwing the {@link AttributeModifiersComponent}s away and making a new one.
	 */

	//	@Overwrite
//	public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed) {
//		return AttributeModifiersComponent.builder()
//			.add(
//				EntityAttributes.GENERIC_ATTACK_DAMAGE,
//				new EntityAttributeModifier(
//					Identifier.of("base_attack_damage"),
//					(double)(3.0F + material.getAttackDamage()),
//					EntityAttributeModifier.Operation.ADD_VALUE
//				),
//				AttributeModifierSlot.MAINHAND
//			)
//			.add(
//				EntityAttributes.GENERIC_ATTACK_SPEED,
//				new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
//				AttributeModifierSlot.MAINHAND
//			)
//			.add(
//				EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
//				new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE),
//				AttributeModifierSlot.MAINHAND
//			)
//			.build();
//	}
//	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/attribute/EntityAttributeModifier;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;")))
//	private static EntityAttributeModifier modifyAttackDamage(Identifier identifier, double d, EntityAttributeModifier.Operation operation, Operation<EntityAttributeModifier> original, ToolMaterial material, int baseAttackDamage, float attackSpeed) {
//		return original.call(identifier, 3.0 + material.getAttackDamage(), operation);
//	}
//	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/attribute/EntityAttributeModifier;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_SPEED:Lnet/minecraft/registry/entry/RegistryEntry;")))
//	private static EntityAttributeModifier modifyAttackSpeed(Identifier identifier, double d, EntityAttributeModifier.Operation operation, Operation<EntityAttributeModifier> original) {
//		return original.call(identifier, 1.0, operation);
//	}
//	@WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;"))
//	private static AttributeModifiersComponent appendArbitrary(AttributeModifiersComponent.Builder instance, Operation<AttributeModifiersComponent> original) {
//		instance.add(
//				EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
//				new EntityAttributeModifier(
//						Identifier.ofVanilla("base_entity_interaction_range"),
//						0.5,
//						EntityAttributeModifier.Operation.ADD_VALUE
//				),
//				AttributeModifierSlot.MAINHAND
//		);
//		return original.call(instance);
//	}
}
