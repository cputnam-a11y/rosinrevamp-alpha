package com.rosinrevamp.event;


import com.rosinrevamp.event.callback.ModifyToolAttributeCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class Events {
    public static final Event<ModifyToolAttributeCallback> MODIFY_TOOL_ATTRIBUTES = EventFactory.createArrayBacked(ModifyToolAttributeCallback.class, callbacks -> context -> {
        for (ModifyToolAttributeCallback callback : callbacks) {
            callback.onModifyToolAttributes(context);
        }
    });
}
