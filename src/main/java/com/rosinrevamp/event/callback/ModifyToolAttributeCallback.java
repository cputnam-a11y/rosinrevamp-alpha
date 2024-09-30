package com.rosinrevamp.event.callback;

import com.rosinrevamp.event.context.ToolAttributeCreationContext;

@FunctionalInterface
public interface ModifyToolAttributeCallback {
    void onModifyToolAttributes(ToolAttributeCreationContext context);
}
