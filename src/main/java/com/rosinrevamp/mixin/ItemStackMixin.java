package com.rosinrevamp.mixin;

import java.util.function.Consumer;

import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ItemStack.class, priority = Integer.MAX_VALUE)
public abstract class ItemStackMixin implements ComponentHolder {
	/**
	 * @author Coarse Rosinflower
	 * @reason honestly can't be fucked rn
	 */
	@Overwrite
	private void appendAttributeModifierTooltip(
		Consumer<Text> textConsumer, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier
	) {
		double statValue = modifier.value();
		boolean isBaseValue = false;
		boolean isSharpened = false;
		if (player != null) {
			if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
				statValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				isBaseValue = true;
				int sharpness = ((ItemStack)(Object)this).getEnchantments()
					.getLevel(player.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS).get());
				if (sharpness > 0) {
					isSharpened = true;
					statValue += 0.5 * (double)(sharpness + 1);
				}
			} else if (modifier.idMatches(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
				statValue += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
				isBaseValue = true;
			} else if (modifier.idMatches(Identifier.ofVanilla("base_entity_interaction_range"))) {
				statValue += player.getAttributeBaseValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
				isBaseValue = true;
			}
		}

		double numberDisplayed;
		if (modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
			|| modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {

			numberDisplayed = statValue * 100.0;
		} else if (attribute.matchesKey(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getKey().get())) {
			numberDisplayed = statValue * 10.0;
		} else {
			numberDisplayed = statValue;
		}

		if (isBaseValue) {
			textConsumer.accept(
				ScreenTexts.space()
					.append(
						Text.translatable(
							"attribute.modifier.equals." + modifier.operation().getId(),
							AttributeModifiersComponent.DECIMAL_FORMAT.format(numberDisplayed),
							Text.translatable(attribute.value().getTranslationKey())
						)
					)
					.formatted(isSharpened ? Formatting.GOLD : Formatting.DARK_GREEN)
			);
		} else if (statValue >= 0.0) {
			textConsumer.accept(
				Text.translatable(
						"attribute.modifier.plus." + modifier.operation().getId(),
						AttributeModifiersComponent.DECIMAL_FORMAT.format(numberDisplayed),
						Text.translatable(attribute.value().getTranslationKey())
					)
					.formatted(attribute.value().getFormatting(true))
			);
		} else {
			textConsumer.accept(
				Text.translatable(
						"attribute.modifier.take." + modifier.operation().getId(),
						AttributeModifiersComponent.DECIMAL_FORMAT.format(-numberDisplayed),
						Text.translatable(attribute.value().getTranslationKey())
					)
					.formatted(attribute.value().getFormatting(false))
			);
		}
	}
}
