package de.emaarco.example.domain;

import java.util.UUID;

public record Task(String id, String title, String description) {
    public Task(String title, String description) {
        this(UUID.randomUUID().toString(), title, description);
    }
}