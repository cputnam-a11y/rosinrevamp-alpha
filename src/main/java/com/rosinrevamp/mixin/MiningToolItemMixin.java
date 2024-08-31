                                                                             /*
                                                                          \  -
                                                                       , *-
                                                                       ~ '
                                                                     *,  -*
                                                                      [  ^
                                                          _         L__;_
                                                       ,/*_*\,     H_____H
                                                    ,/*   /   *\,  H  /  H
                                                 ,/*L_____L_____L*\H__L__H
                                              ,/'/     /     /     H     H
                                           ,/*___L_____L_____L_____H_____H
                                        ,/*   /     /     /     /  H  /  H*\,
                                     ,/*L_____L_____L_____L_____L_____L_____L*\,
                                     H'''''/'''''/'''''/'''''/'''''/'''''/'''''H
                                     H_____L_____L ,......., L_____L_____L_____H
                                     H  /     /    |  _ _  |    /     /     /  H
                _____________        H__L_____L___ | |_|_| | ___L_____L_____L__H
                | - - - - - |        H     /       I |_|_| | /     /     /     H
                |  * hack   |        H_____L______ |       | L_____L_____L_____H
                | house OwO |        H  /     /    |      O|    /     /     /  H
                |___________|        H__L_____L___ I       | ___L_____L_____L__H
                     |/|             H     /       |       | /     /     /     H
                     |/|             H,,,,,L,,,,,,,l.......l,L,,,,,L,,,,,L,,,,,H
*~/*.~~*.**...*~-...-*-.~*~-*..--~~*~~../.-*-.--//~/--~~-/*~~-/-/*-/---~.---.**/

























package com.rosinrevamp.mixin;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem {
	private MiningToolItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Item.Settings settings) {
		super(material, settings.component(DataComponentTypes.TOOL, material.createComponent(effectiveBlocks)));
	}
	@Overwrite
	public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, float baseAttackDamage, float attackSpeed) {
		if (baseAttackDamage < 1.0F) {
			switch (material) {
				default:
					attackSpeed = 1.5F;
					break;
				case ToolMaterials.IRON:
					attackSpeed = 1.0F;
					break;
				case ToolMaterials.STONE:
					attackSpeed = 0.5F;
					break;
				case ToolMaterials.WOOD:
					attackSpeed = 0.0F;
			}
			return AttributeModifiersComponent.builder()
				.add(
					EntityAttributes.GENERIC_ATTACK_DAMAGE,
					material.getAttackDamage() >= 3.0F
						? new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double)(material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE)
						: new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double)(1.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND
				)
				.add(
					EntityAttributes.GENERIC_ATTACK_SPEED,
					new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), (double)attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND
				)
				.add(
					EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
					new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND
				)
				.build();
		}
		if (baseAttackDamage == 1.0F) {
			return AttributeModifiersComponent.builder()
				.add(
					EntityAttributes.GENERIC_ATTACK_DAMAGE,
					new EntityAttributeModifier(
						Identifier.ofVanilla("base_attack_damage"), (double)(2.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE
					),
					AttributeModifierSlot.MAINHAND
				)
				.add(
					EntityAttributes.GENERIC_ATTACK_SPEED,
					new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND
				)
				.build();
		}
		if (baseAttackDamage == 1.5F) {
			return AttributeModifiersComponent.builder()
				.add(
					EntityAttributes.GENERIC_ATTACK_DAMAGE,
					new EntityAttributeModifier(
						Identifier.ofVanilla("base_attack_damage"), (double)(1.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE
					),
					AttributeModifierSlot.MAINHAND
				)
				.add(
					EntityAttributes.GENERIC_ATTACK_SPEED,
					new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE),
					AttributeModifierSlot.MAINHAND
				)
				.build();
		}
		return AttributeModifiersComponent.builder()
			.add(
				EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(
					Identifier.ofVanilla("base_attack_damage"), (double)(4.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE
				),
				AttributeModifierSlot.MAINHAND
			)
			.add(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.build();
	}
}
