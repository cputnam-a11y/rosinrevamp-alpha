package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ProjectileItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin extends Item implements ProjectileItem {
    private TridentItemMixin(Item.Settings settings) {
        super(settings);
    }

    /**
     * Note shouldn't be necessary
     * @author Coarse Rosinflower
     * @reason Changes the vanilla values for the trident, which requires throwing the {@link AttributeModifiersComponent}s away and making a new one.
     */
//    @Overwrite
//    public static AttributeModifiersComponent createAttributeModifiers() {
//        return AttributeModifiersComponent.builder().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Identifier.ofVanilla("base_attack_speed"), 0.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
//    }

    // modify constant might be more suitable here due to the inability to wrap
    @ModifyExpressionValue(method = "createAttributeModifiers", at = @At(value = "CONSTANT", args = "doubleValue=8.0"))
    private static double modifyTridentDamage(double value) {
        return 6.0;
    }

    @ModifyExpressionValue(method = "createAttributeModifiers", at = @At(value = "CONSTANT", args = "doubleValue=-2.9000000953674316"))
    private static double modifyTridentSpeed(double value) {
        return 0.0;
    }

    @WrapOperation(method = "createAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/AttributeModifiersComponent$Builder;build()Lnet/minecraft/component/type/AttributeModifiersComponent;"))
    private static AttributeModifiersComponent wrapAttributeModifiers(AttributeModifiersComponent.Builder instance, Operation<AttributeModifiersComponent> original) {
        return original.call(instance.add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(Identifier.ofVanilla("base_entity_interaction_range"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND));
    }
}
