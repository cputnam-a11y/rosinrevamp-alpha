package com.rosinrevamp.attributemodifier;

import com.rosinrevamp.duck.UsefulAttributeModifiers;
import com.rosinrevamp.event.Events;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

import static com.rosinrevamp.event.context.ToolAttributeCreationContext.ToolKind.*;
import static com.rosinrevamp.event.context.ToolAttributeCreationContext.ToolLevel.*;
import static net.minecraft.item.Item.BASE_ATTACK_DAMAGE_MODIFIER_ID;
import static net.minecraft.item.Item.BASE_ATTACK_SPEED_MODIFIER_ID;

public class AttributeModifierModifiers {
    static final Identifier PLAYER_ENTITY_INTERACTION_RANGE_ID = Identifier.ofVanilla("base_entity_interaction_range");

    static {
        Events.MODIFY_TOOL_ATTRIBUTES.register(new SortedAttributeModifier() {{
            addCallback(MACE, new SingleKindAttributeModifier(MACE, context -> context.replaceAll(EntityAttributes.GENERIC_ATTACK_SPEED,
                    new UsefulAttributeModifiers.Entry(
                            EntityAttributes.GENERIC_ATTACK_SPEED,
                            new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -1.4F, EntityAttributeModifier.Operation.ADD_VALUE),
                            AttributeModifierSlot.MAINHAND
                    )
            )));
            addCallback(TRIDENT, new SingleKindAttributeModifier(TRIDENT, (context -> {
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 0.0, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                                new EntityAttributeModifier(PLAYER_ENTITY_INTERACTION_RANGE_ID, 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        )
                );
            })));
            addCallback(SWORD, new SingleKindAttributeModifier(SWORD, context -> {
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                        (3.0 + context.getMaterial().getAttackDamage()),
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                                new EntityAttributeModifier(
                                        Identifier.ofVanilla("base_entity_interaction_range"),
                                        0.5,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        1.0,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
            }));
            addCallback(
                    AXE,
                    new SingleKindAttributeModifier(AXE, (context) -> {
                        context.replaceAll(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new UsefulAttributeModifiers.Entry(
                                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                        new EntityAttributeModifier(
                                                BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                                (4 + context.getMaterial().getAttackDamage()),
                                                EntityAttributeModifier.Operation.ADD_VALUE
                                        ),
                                        AttributeModifierSlot.MAINHAND
                                )
                        );
                        context.replaceAll(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new UsefulAttributeModifiers.Entry(
                                        EntityAttributes.GENERIC_ATTACK_SPEED,
                                        new EntityAttributeModifier(
                                                BASE_ATTACK_SPEED_MODIFIER_ID,
                                                0,
                                                EntityAttributeModifier.Operation.ADD_VALUE
                                        ),
                                        AttributeModifierSlot.MAINHAND
                                )
                        );
                    })
            );
            addCallback(
                    SHOVEL,
                    new SingleKindAttributeModifier(SHOVEL, context -> {
                        context.replaceAll(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new UsefulAttributeModifiers.Entry(
                                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                        new EntityAttributeModifier(
                                                BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                                (1 + context.getMaterial().getAttackDamage()),
                                                EntityAttributeModifier.Operation.ADD_VALUE
                                        ),
                                        AttributeModifierSlot.MAINHAND
                                )
                        );
                        context.replaceAll(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new UsefulAttributeModifiers.Entry(
                                        EntityAttributes.GENERIC_ATTACK_SPEED,
                                        new EntityAttributeModifier(
                                                BASE_ATTACK_SPEED_MODIFIER_ID,
                                                0,
                                                EntityAttributeModifier.Operation.ADD_VALUE
                                        ),
                                        AttributeModifierSlot.MAINHAND
                                )
                        );
                    })
            );
            addCallback(PICKAXE, context -> {
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                        (2 + context.getMaterial().getAttackDamage()),
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        0.5f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
            });
            addCallback(HOE, new LeveledAttributeModifier() {{
                addCallback(IRON, context -> context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        1.5f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                ));
                addCallback(STONE, (ToolAttributeCreationContext context) -> context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        0.5f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                ));
                addCallback(WOOD, context -> context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        0.0f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                ));
                addDefault((context) -> context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        1.5f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                ));
            }});
            addCallback(HOE, new SingleKindAttributeModifier(HOE, (context) -> context.replaceAll(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new UsefulAttributeModifiers.Entry(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            new EntityAttributeModifier(
                                    BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                    (context.getMaterial().getAttackDamage() >= 2.0F
                                            ? 0.0F
                                            : 1.0F + context.getMaterial().getAttackDamage()),
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            ),
                            AttributeModifierSlot.MAINHAND
                    )
            )));
            addCallback(SHOVEL, new SingleKindAttributeModifier(SHOVEL, context -> {
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_DAMAGE_MODIFIER_ID,
                                        (1.0f + context.getMaterial().getAttackDamage()),
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
                context.replaceAll(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new UsefulAttributeModifiers.Entry(
                                EntityAttributes.GENERIC_ATTACK_SPEED,
                                new EntityAttributeModifier(
                                        BASE_ATTACK_SPEED_MODIFIER_ID,
                                        0.0f,
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                ),
                                AttributeModifierSlot.MAINHAND
                        )
                );
            }));
        }});
    }
    public static void init() {}
}
