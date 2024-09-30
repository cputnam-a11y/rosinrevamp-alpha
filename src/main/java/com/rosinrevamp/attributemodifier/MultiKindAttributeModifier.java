package com.rosinrevamp.attributemodifier;

import com.google.common.collect.ImmutableList;
import com.rosinrevamp.event.callback.ModifyToolAttributeCallback;
import com.rosinrevamp.event.context.ToolAttributeCreationContext;

import java.util.ArrayList;
import java.util.List;
public record MultiKindAttributeModifier(ImmutableList<ToolAttributeCreationContext.ToolKind> kinds, ModifyToolAttributeCallback callback, boolean strict) implements ModifyToolAttributeCallback {
    public MultiKindAttributeModifier(ImmutableList<ToolAttributeCreationContext.ToolKind> kinds, ModifyToolAttributeCallback callback) {
        this(kinds, callback, false);
    }
    @Override
    public void onModifyToolAttributes(ToolAttributeCreationContext context) {
        if (!this.kinds.contains(context.getKind())) {
            if (strict)
                throw new IllegalStateException(String.format("Callback for %s called with %s and strict mode is enabled", kinds, context.getKind()));
            return;
        }
        callback.onModifyToolAttributes(context);
    }
    public static class Builder {
        private final boolean strict;
        private final List<ToolAttributeCreationContext.ToolKind> list = new ArrayList<>();
        private ModifyToolAttributeCallback callback;
        public Builder(boolean strict) {
            this.strict = strict;
            this.callback = null;
        }
        public Builder() {
            this(false);
        }
        public Builder add(ToolAttributeCreationContext.ToolKind t) {
            list.add(t);
            return this;
        }
        public Builder addCallback(ModifyToolAttributeCallback callback) {
            this.callback = callback;
            return this;
        }
        public MultiKindAttributeModifier build() {
            if (callback == null)
                throw new IllegalStateException("Callback is null");
            return new MultiKindAttributeModifier(ImmutableList.copyOf(list), callback, strict);
        }
    }
}

