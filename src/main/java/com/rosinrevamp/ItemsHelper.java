package com.rosinrevamp;

public class ItemsHelper {
    public static final ThreadLocal<String> CONTEXT_ID = ThreadLocal.withInitial(() -> "");
}
