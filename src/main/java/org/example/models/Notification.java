package org.example.models;

public record Notification(
    NotificationChannel channel,
    NotificationStatus status,
    String providerCallId,
    String lastError) {}
