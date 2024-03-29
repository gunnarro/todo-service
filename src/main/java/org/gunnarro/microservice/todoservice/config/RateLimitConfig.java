package org.gunnarro.microservice.todoservice.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.function.Supplier;

public class RateLimitConfig {
    @Autowired
    public ProxyManager buckets;

    public Bucket resolveBucket(String key, int tps) {
        Supplier<BucketConfiguration> configurationSupplier = getConfigSupplier(key, tps);
        return buckets.builder().build(key, configurationSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplier(String key, int tps) {
        // For a rate limit of 10 requests per minute, we’ll create a bucket with capacity 10 and a refill rate of 10 tokens per minute:
        Refill refill = Refill.intervally(1, Duration.ofSeconds(1));
        Bandwidth limit = Bandwidth.classic(1, refill);

        return () -> (BucketConfiguration.builder()
                .addLimit(limit)
                .build());
    }
}
