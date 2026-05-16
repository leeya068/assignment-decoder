package com.hackathon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration for asynchronous task execution.
 * Enables async processing for long-running Bob API calls.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);
    
    /**
     * Creates a thread pool executor for async tasks.
     * Configured for Bob API calls and code generation tasks.
     */
    @Bean(name = "bobTaskExecutor")
    public Executor bobTaskExecutor() {
        logger.info("Initializing Bob Task Executor");
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("bob-async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        // Rejection policy: caller runs the task if queue is full
        executor.setRejectedExecutionHandler(
            (r, exec) -> {
                logger.warn("Task rejected, running in caller thread");
                r.run();
            }
        );
        
        executor.initialize();
        logger.info("Bob Task Executor initialized with core pool size: {}, max pool size: {}", 
            executor.getCorePoolSize(), executor.getMaxPoolSize());
        
        return executor;
    }
}

// Made with Bob
