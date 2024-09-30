package com.rosinrevamp.attributemodifier;

import com.rosinrevamp.event.context.ToolAttributeCreationContext;

public class NormallyLeveledAttributeModifier extends LeveledAttributeModifier {
    public NormallyLeveledAttributeModifier() {
        super(false);
        addCallback(ToolAttributeCreationContext.ToolLevel.UNKNOWN, (context) -> {
            if (this.strict)
                throw new IllegalStateException("Unknown level and strict mode is enabled");
        });
    }
    public NormallyLeveledAttributeModifier(boolean strict) {
        super(strict);
        addCallback(ToolAttributeCreationContext.ToolLevel.UNKNOWN, (context) -> {
            if (this.strict)
                throw new IllegalStateException("Unknown level and strict mode is enabled");
        });
    }
}
