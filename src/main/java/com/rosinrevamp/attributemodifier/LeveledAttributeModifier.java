package com.rosinrevamp.attributemodifier;

import com.google.common.collect.Maps;
import com.rosinrevamp.event.callback.ModifyToolAttributeCallback;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;

import java.util.HashMap;

public class LeveledAttributeModifier implements ModifyToolAttributeCallback {
    protected final boolean strict;
    protected ModifyToolAttributeCallback defaultCallback = null;
    HashMap<ToolAttributeCreationContext.ToolLevel, ModifyToolAttributeCallback> callbacks = Maps.newHashMap();
    public LeveledAttributeModifier() {
        this(false);
    }
    public LeveledAttributeModifier(boolean strict) {
        this.strict = strict;
    }
    public void addCallback(ToolAttributeCreationContext.ToolLevel level, ModifyToolAttributeCallback callback) {
        if (strict && callbacks.containsKey(level))
            throw new IllegalStateException(String.format("Callback already exists for %s and strict mode is enabled", level));
        callbacks.put(level, callback);
    }
    public void addDefault(ModifyToolAttributeCallback callback) {
        if (strict && this.defaultCallback != null) {
            throw new IllegalStateException("Default callback already and strict mode is enabled");
        }
        this.defaultCallback = callback;
    }
    @Override
    public void onModifyToolAttributes(ToolAttributeCreationContext context) {
        ModifyToolAttributeCallback callback = callbacks.get(context.getLevel());
        if (callback == null) {
            if (defaultCallback != null) {
                defaultCallback.onModifyToolAttributes(context);
                return;
            }
            if (strict)
                throw new IllegalStateException(String.format("No callback for %s and strict mode is enabled", context.getLevel()) + context.getLevel());
            return;
        }
        callback.onModifyToolAttributes(context);
    }
}
