package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    private final TimeEntryRepository repository;

    public TimeEntryHealthIndicator(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        return repository.list().size() < 5 ? up().build() : down().build();
    }
}
