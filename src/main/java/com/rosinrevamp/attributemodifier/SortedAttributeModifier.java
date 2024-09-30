package com.rosinrevamp.attributemodifier;

import com.google.common.collect.Maps;
import com.rosinrevamp.event.callback.ModifyToolAttributeCallback;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;

import java.util.HashMap;

public class SortedAttributeModifier implements ModifyToolAttributeCallback {
    private final boolean strict;
    HashMap<ToolAttributeCreationContext.ToolKind, ModifyToolAttributeCallback> callbacks = Maps.newHashMap();
    public SortedAttributeModifier() {
        this(false);
    }
    public SortedAttributeModifier(boolean strict) {
        this.strict = strict;
    }
    public void addCallback(ToolAttributeCreationContext.ToolKind kind, ModifyToolAttributeCallback callback) {
        if (strict && callbacks.containsKey(kind))
            throw new IllegalStateException(String.format("Callback already exists for kind %s and string mode is enabled", kind));
        callbacks.put(kind, callback);
    }
    public void addCallback(MultiKindAttributeModifier modifier) {
        for (ToolAttributeCreationContext.ToolKind kind : modifier.kinds()) {
            addCallback(kind, modifier.callback());
        }
    }
    @Override
    public void onModifyToolAttributes(ToolAttributeCreationContext context) {
        ModifyToolAttributeCallback callback = callbacks.get(context.getKind());
        if (callback == null) {
            if (strict)
                throw new IllegalStateException(String.format("No callback for %s and strict mode is enabled", context.getKind()) + context.getKind());
            return;
        }
        callback.onModifyToolAttributes(context);
    }
}
