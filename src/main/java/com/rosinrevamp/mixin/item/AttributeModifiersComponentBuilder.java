package com.rosinrevamp.mixin.item;

import com.google.common.collect.ImmutableList;
import com.rosinrevamp.duck.UsefulAttributeModifiers;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;

@Mixin(AttributeModifiersComponent.Builder.class)
public abstract class AttributeModifiersComponentBuilder implements UsefulAttributeModifiers.Builder {

    @Unique
    private List<AttributeModifiersComponent.Entry> override = null;
    @Shadow
    @Final
    @Mutable
    private ImmutableList.Builder<AttributeModifiersComponent.Entry> entries;

    @Unique
    private void checkOverride() {
        if (override == null)
            override = new ArrayList<>(this.entries.build());
    }

    @Override
    public ImmutableList<UsefulAttributeModifiers.Entry> rosinrevamp_alpha$getEntries() {
        checkOverride();
        return override.stream().map(UsefulAttributeModifiers.Entry::new).collect(ImmutableList.toImmutableList());
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$addEntry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
        checkOverride();
        override.add(new AttributeModifiersComponent.Entry(attribute, modifier, slot));
        return this;
    }

    @Override
    public ImmutableList<UsefulAttributeModifiers.Entry> rosinrevamp_alpha$getEntriesOf(RegistryEntry<EntityAttribute> attribute) {
        checkOverride();
        return override.stream().filter(entry -> entry.attribute() == attribute).map(UsefulAttributeModifiers.Entry::new).collect(ImmutableList.toImmutableList());
    }

    @Override
    public ImmutableList<UsefulAttributeModifiers.Entry> rosinrevamp_alpha$getEntriesOf(AttributeModifierSlot slot) {
        checkOverride();
        return override.stream().filter(entry -> entry.slot() == slot).map(UsefulAttributeModifiers.Entry::new).collect(ImmutableList.toImmutableList());
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$replaceAll(RegistryEntry<EntityAttribute> attribute, UsefulAttributeModifiers.Entry entry) {
        checkOverride();
        override.removeIf(entry1 -> entry1.attribute() == attribute);
        override.add(entry.backingEntry());
        return this;
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$replaceAll(AttributeModifierSlot slot, UsefulAttributeModifiers.Entry entry) {
        checkOverride();
        override.removeIf(entry1 -> entry1.slot() == slot);
        override.add(entry.backingEntry());
        return this;
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$removeEntry(UsefulAttributeModifiers.Entry entry) {
        checkOverride();
        override.remove(entry.backingEntry());
        return this;
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$removeEntriesOf(RegistryEntry<EntityAttribute> attribute) {
        checkOverride();
        override.removeIf(entry -> entry.attribute() == attribute);
        return this;
    }

    @Override
    public UsefulAttributeModifiers.Builder rosinrevamp_alpha$removeEntriesOf(AttributeModifierSlot slot) {
        checkOverride();
        override.removeIf(entry -> entry.slot() == slot);
        return this;
    }

    @Override
    public AttributeModifiersComponent.Builder rosinrevamp_alpha$finish() {
        checkOverride();
        entries = ImmutableList.builder();
        entries.addAll(override);
        override = null;
        return (AttributeModifiersComponent.Builder) (Object) this;
    }
}
