package com.rosinrevamp.injector;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;

import java.util.Collection;

/**
 * This injection point returns any constant string in the bytes provided.
 */
public class BeforeConstantString extends InjectionPoint {
    public BeforeConstantString(InjectionPointData data) {
        super(data);
    }
    @Override
    public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
        boolean found = false;
        for (var insin : insns) {
            if (insin.getType() == AbstractInsnNode.LDC_INSN) {
                var ldc = (org.objectweb.asm.tree.LdcInsnNode) insin;
                if (ldc.cst instanceof String) {
                    nodes.add(insin);
                    found = true;
                }
            }
        }
        return found;
    }
}
