package com.rosinrevamp.attributemodifier;

import com.rosinrevamp.event.callback.ModifyToolAttributeCallback;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;

public class SingleKindAttributeModifier implements ModifyToolAttributeCallback{
    private final ToolAttributeCreationContext.ToolKind kind;
    private final ModifyToolAttributeCallback callback;
    private final boolean strict;
    public SingleKindAttributeModifier(ToolAttributeCreationContext.ToolKind kind, ModifyToolAttributeCallback callback) {
        this(kind, callback, false);
    }
    public SingleKindAttributeModifier(ToolAttributeCreationContext.ToolKind kind, ModifyToolAttributeCallback callback, boolean strict) {
        this.strict = strict;
        this.kind = kind;
        this.callback = callback;
    }
    @Override
    public void onModifyToolAttributes(ToolAttributeCreationContext context) {
        if (this.kind != context.getKind()) {
            if (strict)
                throw new IllegalStateException(String.format("Callback for %s called with %s and strict mode is enabled", kind, context.getKind()));
            return;
        }
        callback.onModifyToolAttributes(context);
    }
}
