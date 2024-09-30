package com.rosinrevamp.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.rosinrevamp.ItemsHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
// this can be the new hack house
@Mixin(Items.class)
@Debug(export = true)
public class ItemStackContextCaptureMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            at = {
                    @At(value = "com.rosinrevamp.injector.BeforeConstantString", slice = "maintoolsslice"),
                    @At(value = "com.rosinrevamp.injector.BeforeConstantString", slice = "maceslice"),
                    @At(value = "com.rosinrevamp.injector.BeforeConstantString", slice = "tridentslice")
            },
            slice = {
                    @Slice(
                            id = "maintoolsslice",
                            from = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;NETHERITE_SCRAP:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;NETHERITE_HOE:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            )
                    ),
                    @Slice(
                            id = "maceslice",
                            from = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;WRITTEN_BOOK:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;MACE:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            )
                    ),
                    @Slice(
                            id = "tridentslice",
                            from = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;DISC_FRAGMENT_5:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;TRIDENT:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            )
                    )
            }
    )
    private static String modifyStringValue(String original) {
        ItemsHelper.CONTEXT_ID.set(original);
        return original;
    }
    @WrapOperation(
            method = "<clinit>",
            at = {
                    @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
                        slice = "maintoolsslice"
                    ),
                    @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
                            slice = "maceslice"
                    ),
                    @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
                        slice = "tridentslice"
                    )
            }
            ,
            slice = {
                    @Slice(
                            id = "maintoolsslice",
                            from = @At(
                                    value = "CONSTANT",
                                    args = "stringValue=wooden_sword"
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;NETHERITE_HOE:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC)
                    ),
                    @Slice(
                            id = "maceslice",
                            from = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;WRITTEN_BOOK:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;MACE:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            )
                    ),
                    @Slice(
                            id = "tridentslice",
                            from = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;DISC_FRAGMENT_5:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            ),
                            to = @At(
                                    value = "FIELD",
                                    target = "Lnet/minecraft/item/Items;TRIDENT:Lnet/minecraft/item/Item;",
                                    opcode = Opcodes.PUTSTATIC
                            )
                    )
            }
    )
    private static Item afterRegister(String id, Item item, Operation<Item> original) {
        item = original.call(id, item);
        ItemsHelper.CONTEXT_ID.remove();
        return item;
    }

}
