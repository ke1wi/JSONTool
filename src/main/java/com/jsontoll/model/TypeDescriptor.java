package com.jsontoll.model;

import java.util.concurrent.ConcurrentHashMap;

public record TypeDescriptor(String type, String format, String pattern) {
    private static final ConcurrentHashMap<String, TypeDescriptor> CACHE = new ConcurrentHashMap<>();

    public static TypeDescriptor of(String type, String format, String pattern) {
        String key = type + "|" + (format == null ? "" : format) + "|" + (pattern == null ? "" : pattern);
        return CACHE.computeIfAbsent(key, k -> new TypeDescriptor(type, format, pattern));
    }
}