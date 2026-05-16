package com.hackathon.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for IBM Bob API.
 * Validates configuration at startup to fail fast if misconfigured.
 */
@Configuration
@ConfigurationProperties(prefix = "bob.api")
@Validated
public class BobApiConfig {
    
    @NotBlank(message = "Bob API URL must be configured")
    @Pattern(regexp = "^https?://.*", message = "Bob API URL must be a valid HTTP/HTTPS URL")
    private String url;
    
    @NotBlank(message = "Bob API key must be configured")
    private String key;
    
    private int timeoutMs = 30000; // Default 30 seconds
    
    private String mode = "advanced";
    
    private boolean repoContext = true;
    
    // Getters and Setters
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public int getTimeoutMs() {
        return timeoutMs;
    }
    
    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
    
    public String getMode() {
        return mode;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public boolean isRepoContext() {
        return repoContext;
    }
    
    public void setRepoContext(boolean repoContext) {
        this.repoContext = repoContext;
    }
    
    @Override
    public String toString() {
        return String.format("BobApiConfig{url='%s', mode='%s', timeout=%dms, repoContext=%b}", 
            url, mode, timeoutMs, repoContext);
    }
}

// Made with Bob
