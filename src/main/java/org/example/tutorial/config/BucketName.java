package org.example.tutorial.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    TODO_IMAGE("tutorial-spring");
    private final String bucketName;
}