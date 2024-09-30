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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem {
    @Unique
    private static final Identifier BASE_ENTITY_INTERACTION_RANGE = Identifier.ofVanilla("base_entity_interaction_range");
    // pretty sure constructors don't get merged
    private MiningToolItemMixin(ToolMaterial material, TagKey<Block> effectiveBlocks, Item.Settings settings) {
        super(material, settings.component(DataComponentTypes.TOOL, material.createComponent(effectiveBlocks)));
    }


// think i got this one.
//    /**
//     * @author Coarse Rosinflower
//     * @reason Changes the vanilla values for the mining tools, which requires throwing the {@link AttributeModifiersComponent}s away and making a new one.
//     */
//    @Overwrite
//    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, float baseAttackDamage, float attackSpeed) {
//        if (baseAttackDamage < 1.0F) {
//            attackSpeed = switch (material) {
//                case ToolMaterials.IRON -> 1.0f;
//                case ToolMaterials.STONE -> 0.5f;
//                case ToolMaterials.WOOD -> 0.0f;
//                default -> 1.5F;
//            };
//            return AttributeModifiersComponent.builder()
//                    .add(
//                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
//                            material.getAttackDamage() >= 2.0F
//                                    ? new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double) (material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE)
//                                    : new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double) (1.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE),
//                            AttributeModifierSlot.MAINHAND
//                    )
//                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), (double) attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                    .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
//        }
//        if (baseAttackDamage == 1.0F) {
//            return AttributeModifiersComponent.builder()
//                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double) (2.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
//        }
//        if (baseAttackDamage == 1.5F) {
//            return AttributeModifiersComponent.builder()
//                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double) (1.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                    .build();
//        }
//        return AttributeModifiersComponent.builder()
//                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_damage"), (double) (4.0F + material.getAttackDamage()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
//                .build();
//    }

    @Inject(method = "createAttributeModifiers", at = @At("HEAD"))
    private static void onBeforeCreateAttributeModifiers(
            ToolMaterial material, float baseAttackDamage,
            float attackSpeed, CallbackInfoReturnable<AttributeModifiersComponent> cir,
            @Share("case") LocalIntRef caseREf
    ) {
        if (baseAttackDamage < 1.0F) {
            caseREf.set(0);
        } else if (baseAttackDamage == 1.0F) {
            caseREf.set(1);
        } else if (baseAttackDamage == 1.5F) {
            caseREf.set(2);
        } else {
            caseREf.set(3);
        }
    }
    /* using int to cache the case so we don't have to recalculate it
    * 0: baseAttackDamage < 1.0F
    * 1: baseAttackDamage == 1.0F
    * 2: baseAttackDamage == 1.5F
    * 3: default
    * */
    @WrapOperation(method = "createAttributeModifiers", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/attribute/EntityAttributeModifier;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_DAMAGE:Lnet/minecraft/registry/entry/RegistryEntry;")))
    private static EntityAttributeModifier modifyAttackDamage(Identifier identifier, double d, EntityAttributeModifier.Operation operation, Operation<EntityAttributeModifier> original, ToolMaterial material, float baseAttackDamage, float attackSpeed, @Share("case") LocalIntRef caseRef) {
        if (identifier != BASE_ATTACK_DAMAGE_MODIFIER_ID) {
            throw new IllegalArgumentException(String.format("Unexpected identifier: %s, expected %s", identifier, BASE_ATTACK_DAMAGE_MODIFIER_ID));
        }
        return switch (caseRef.get()) {
            case 0 -> {
                if (material.getAttackDamage() >= 2.0) {
                    yield original.call(identifier, (double) material.getAttackDamage(), operation);
                } else {
                    yield original.call(identifier, (double) (1.0F + material.getAttackDamage()), operation);
                }
            }
            case 1 -> original.call(identifier, (double) 2.0F + material.getAttackDamage(), operation);
            case 2 -> original.call(identifier, (double) 1.0F + material.getAttackDamage(), operation);
            default -> original.call(identifier, (double) 4.0F + material.getAttackDamage(), operation);
        };
    }

    @SuppressWarnings("ParameterCanBeLocal")
    @WrapOperation(method = "createAttributeModifiers", at = @At(value = "NEW", target = "(Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/attribute/EntityAttributeModifier;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributes;GENERIC_ATTACK_SPEED:Lnet/minecraft/registry/entry/RegistryEntry;")))
    private static EntityAttributeModifier modifyAttackSpeed(Identifier identifier, double d, EntityAttributeModifier.Operation operation, Operation<EntityAttributeModifier> original, ToolMaterial material, float baseAttackDamage, float attackSpeed, @Share("case") LocalIntRef caseRef) {
        if (identifier != BASE_ATTACK_SPEED_MODIFIER_ID) {
            throw new IllegalArgumentException(String.format("Unexpected identifier: %s, expected %s", identifier, BASE_ATTACK_SPEED_MODIFIER_ID));
        }
        return switch (caseRef.get()) {
            case 0 -> {
                attackSpeed = switch (material) {
                    case ToolMaterials.IRON -> 1.0f;
                    case ToolMaterials.STONE -> 0.5f;
                    case ToolMaterials.WOOD -> 0.0f;
                    default -> 1.5F;
                };
                yield original.call(identifier, (double) attackSpeed, operation);
            }
            case 1 -> original.call(identifier, 0.5, operation);
            // 2 is the same as 3
            default -> original.call(identifier, 0.0, operation);
        };
    }
    @WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;"))
    private static AttributeModifiersComponent appendAdditionalModifiers(AttributeModifiersComponent.Builder instance, Operation<AttributeModifiersComponent> original, ToolMaterial material, float baseAttackDamage, float attackSpeed, @Share("case") LocalIntRef caseRef) {
        if (caseRef.get() == 0) {
            instance
                    .add(
                            EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                            new EntityAttributeModifier(
                                    BASE_ENTITY_INTERACTION_RANGE,
                                    1.0,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            ),
                            AttributeModifierSlot.MAINHAND
                    );
        }
        return original.call(instance);

    }
}
