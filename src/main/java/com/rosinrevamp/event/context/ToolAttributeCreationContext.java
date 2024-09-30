package com.rosinrevamp.event.context;

import com.google.common.collect.ImmutableList;
import com.rosinrevamp.duck.UsefulAttributeModifiers;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

public class ToolAttributeCreationContext {
    private final UsefulAttributeModifiers.Builder attributeModifiersBuilder;
    private final ToolLevel level;
    private final ToolKind kind;
    private final ToolMaterial material;
    private boolean finished = false;
    public ToolAttributeCreationContext(AttributeModifiersComponent.Builder builder, ToolLevel level, ToolKind kind, ToolMaterial material) {
        this.attributeModifiersBuilder = (UsefulAttributeModifiers.Builder) builder;
        this.level = level;
        this.kind = kind;
        this.material = material;
    }
    public ToolAttributeCreationContext(AttributeModifiersComponent.Builder builder, String toolLevelContained, String toolKindContained, ToolMaterial material) {
        this(builder, ToolLevel.fromContainingString(toolLevelContained), ToolKind.fromContainingString(toolKindContained), material);
    }
    public ToolLevel getLevel() {
        return level;
    }
    public ToolKind getKind() {
        return kind;
    }
    public @Nullable ToolMaterial getMaterial() {
        return material;
    }
    public ImmutableList<UsefulAttributeModifiers.Entry> getEntries() {
        return attributeModifiersBuilder.rosinrevamp_alpha$getEntries();
    }
    public void addEntry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, AttributeModifierSlot slot) {
        attributeModifiersBuilder.rosinrevamp_alpha$addEntry(attribute, modifier, slot);
    }
    public ImmutableList<UsefulAttributeModifiers.Entry> getEntriesOf(RegistryEntry<EntityAttribute> attribute) {
        return attributeModifiersBuilder.rosinrevamp_alpha$getEntriesOf(attribute);
    }
    public ImmutableList<UsefulAttributeModifiers.Entry> getEntriesOf(AttributeModifierSlot slot) {
        return attributeModifiersBuilder.rosinrevamp_alpha$getEntriesOf(slot);
    }
    public void replaceAll(RegistryEntry<EntityAttribute> attribute, UsefulAttributeModifiers.Entry entry) {
        attributeModifiersBuilder.rosinrevamp_alpha$replaceAll(attribute, entry);
    }
    public void replaceAll(AttributeModifierSlot slot, UsefulAttributeModifiers.Entry entry) {
        attributeModifiersBuilder.rosinrevamp_alpha$replaceAll(slot, entry);
    }
    public void removeEntry(UsefulAttributeModifiers.Entry entry) {
        attributeModifiersBuilder.rosinrevamp_alpha$removeEntry(entry);
    }
    public void removeEntriesOf(RegistryEntry<EntityAttribute> attribute) {
        attributeModifiersBuilder.rosinrevamp_alpha$removeEntriesOf(attribute);
    }
    public void removeEntriesOf(AttributeModifierSlot slot) {
        attributeModifiersBuilder.rosinrevamp_alpha$removeEntriesOf(slot);
    }
    public void finish() {
        if (finished) {
            throw new IllegalStateException("Cannot finish a context that has already been finished");
        }
        attributeModifiersBuilder.rosinrevamp_alpha$finish();
        finished = true;
    }
    public enum ToolLevel {
        WOOD,
        STONE,
        IRON,
        GOLD,
        DIAMOND,
        NETHERITE,
        UNKNOWN;
        static final ToolLevel[] NORMALS = new ToolLevel[] {WOOD, STONE, IRON, GOLD, DIAMOND, NETHERITE};
        public static ToolLevel fromContainingString(String containingString) {
            for (ToolLevel level : NORMALS) {
                if (containingString.contains(level.name().toLowerCase())) {
                    return level;
                }
            }
            return UNKNOWN;
        }
    }
    public enum ToolKind {
        PICKAXE,
        AXE,
        SHOVEL,
        SWORD,
        HOE,
        MACE,
        TRIDENT,
        UNKNOWN;
        static final ToolKind[] NORMALS = new ToolKind[] {PICKAXE, AXE, SHOVEL, SWORD, HOE, MACE, TRIDENT};
        public static ToolKind fromContainingString(String containingString) {
            for (ToolKind kind : NORMALS) {
                if (containingString.contains(kind.name().toLowerCase())) {
                    return kind;
                }
            }
            return UNKNOWN;
        }
    }
}
