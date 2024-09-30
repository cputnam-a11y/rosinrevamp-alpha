package com.rosinrevamp.mixin.item;

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
    private boolean doSharpnessCheck(boolean value, @Share("isSharpened") LocalBooleanRef isSharpened, @Share("player") LocalRef<PlayerEntity> player, @Local LocalDoubleRef baseValue) {
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
}
