package com.rosinrevamp.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = ItemStack.class, priority = Integer.MAX_VALUE)
public abstract class ItemStackMixin implements ComponentHolder {

    @Shadow
    public abstract ItemEnchantmentsComponent getEnchantments();

    @Inject(method = "appendAttributeModifierTooltip", at = @At("HEAD"))
    private void initIsSharpened(Consumer<Text> textConsumer, PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, CallbackInfo ci, @Share("isSharpened") LocalBooleanRef isSharpened, @Share("player") LocalRef<PlayerEntity> playerEntityRef) {
        playerEntityRef.set(player);
        isSharpened.set(false);
    }

    /**
     * @author cputnam-a11y
     * @reason using modify variable to enable targeting the store opcode
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @ModifyVariable(method = "appendAttributeModifierTooltip", at = @At(value = "STORE"), index = 7, ordinal = 0, slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/item/Item;BASE_ATTACK_DAMAGE_MODIFIER_ID:Lnet/minecraft/util/Identifier;")))
    private boolean doSharpnessCheck(boolean value, @Share("isSharpened") LocalBooleanRef isSharpened, @Share("player") LocalRef<PlayerEntity> player, @Local(print = true) LocalDoubleRef baseValue) {
        RegistryEntry<Enchantment> sharpnessEnchantment = player.get().getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS).get();
        int sharpness = this.getEnchantments().getLevel(sharpnessEnchantment);
        if (sharpness > 0) {
            isSharpened.set(true);
            baseValue.set(baseValue.get() + 0.5 * (double) (sharpness + 1));
        }
        return value;
    }

    @ModifyExpressionValue(method = "appendAttributeModifierTooltip", at = @At(value = "FIELD", target = "Lnet/minecraft/util/Formatting;DARK_GREEN:Lnet/minecraft/util/Formatting;"))
    private Formatting modifyFormatting(Formatting value, @Share("isSharpened") LocalBooleanRef isSharpened) {
        return isSharpened.get() ? Formatting.GOLD : Formatting.DARK_GREEN;
    }
    // I think i got everything
//	/**
//	 * @author Coarse Rosinflower
//	 * @reason honestly can't be fucked rn
//	 */
//	@Overwrite
//	private void appendAttributeModifierTooltip(
//		Consumer<Text> textConsumer, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier
//	) {
//		double statValue = modifier.value();
//		boolean isBaseValue = false;
//		boolean isSharpened = false;
//
//		if (player != null) {
//			if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
//				statValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
//				isBaseValue = true;
////				int sharpness = ((ItemStack)(Object)this).getEnchantments()
////					.getLevel(player.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS).get());
////				if (sharpness > 0) {
////					isSharpened = true;
////					statValue += 0.5 * (double)(sharpness + 1);
////				}
//			} else if (modifier.idMatches(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
//				statValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
//				isBaseValue = true;
//			} else if (modifier.idMatches(Identifier.ofVanilla("base_entity_interaction_range"))) {
//				statValue += player.getAttributeBaseValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
//				isBaseValue = true;
//			}
//		}
//
//		double numberDisplayed;
//		if (modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
//			|| modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
//			numberDisplayed = statValue * 100.0;
//		} else if (attribute.matchesKey(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getKey().get())) {
//			numberDisplayed = statValue * 10.0;
//		} else {
//			numberDisplayed = statValue;
//		}
//
//		if (isBaseValue) {
//			textConsumer.accept(
//				ScreenTexts.space()
//					.append(
//						Text.translatable(
//							"attribute.modifier.equals." + modifier.operation().getId(),
//							AttributeModifiersComponent.DECIMAL_FORMAT.format(numberDisplayed),
//							Text.translatable(attribute.value().getTranslationKey())
//						)
//					)
//					.formatted(/*isSharpened ? Formatting.GOLD :*/ Formatting.DARK_GREEN)
//			);
//		} else if (statValue >= 0.0) {
//			textConsumer.accept(
//				Text.translatable(
//						"attribute.modifier.plus." + modifier.operation().getId(),
//						AttributeModifiersComponent.DECIMAL_FORMAT.format(numberDisplayed),
//						Text.translatable(attribute.value().getTranslationKey())
//					)
//					.formatted(attribute.value().getFormatting(true))
//			);
//		} else {
//			textConsumer.accept(
//				Text.translatable(
//						"attribute.modifier.take." + modifier.operation().getId(),
//						AttributeModifiersComponent.DECIMAL_FORMAT.format(-numberDisplayed),
//						Text.translatable(attribute.value().getTranslationKey())
//					)
//					.formatted(attribute.value().getFormatting(false))
//			);
//		}
//	}
}
