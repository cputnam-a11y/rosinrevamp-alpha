package com.rosinrevamp.duck;

import com.google.common.collect.ImmutableList;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class UsefulAttributeModifiers {
    public interface Builder {
        ImmutableList<Entry> rosinrevamp_alpha$getEntries();
        Builder rosinrevamp_alpha$addEntry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot);
        ImmutableList<Entry> rosinrevamp_alpha$getEntriesOf(RegistryEntry<EntityAttribute> attribute);
        ImmutableList<Entry> rosinrevamp_alpha$getEntriesOf(AttributeModifierSlot slot);
        Builder rosinrevamp_alpha$replaceAll(RegistryEntry<EntityAttribute> attribute, Entry entry);
        Builder rosinrevamp_alpha$replaceAll(AttributeModifierSlot slot, Entry entry);
        Builder rosinrevamp_alpha$removeEntry(Entry entry);
        Builder rosinrevamp_alpha$removeEntriesOf(RegistryEntry<EntityAttribute> attribute);
        Builder rosinrevamp_alpha$removeEntriesOf(AttributeModifierSlot slot);
        /**
         * Apply Changes to the builder,
         * after calling finish, no changes can be made without effectively
         * recreating the context of the builder.
         * @return the builder with the changes applied
         */
        AttributeModifiersComponent.Builder rosinrevamp_alpha$finish();
    }
    public record Entry(AttributeModifiersComponent.Entry backingEntry) {
        public Entry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
            this(new AttributeModifiersComponent.Entry(attribute, modifier, slot));
        }
    }
}
