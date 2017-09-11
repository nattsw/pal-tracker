package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryHealthIndicator;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimeEntryHealthIndicatorTest {
    @Test
    public void health_listHasFive_returnsDown() throws Exception {
        TimeEntryRepository repository = mock(TimeEntryRepository.class);
        TimeEntry entry = mock(TimeEntry.class);
        when(repository.list()).thenReturn(Arrays.asList(entry, entry, entry, entry, entry));
        TimeEntryHealthIndicator indicator = new TimeEntryHealthIndicator(repository);

        Health result = indicator.health();

        assertThat(result).isEqualTo(Health.down().build());
    }

    @Test
    public void health_listHasLessThanFive_returnsUp() throws Exception {
        TimeEntryRepository repository = mock(TimeEntryRepository.class);
        TimeEntry entry = mock(TimeEntry.class);
        when(repository.list()).thenReturn(Arrays.asList(entry, entry, entry, entry));
        TimeEntryHealthIndicator indicator = new TimeEntryHealthIndicator(repository);

        Health result = indicator.health();

        assertThat(result).isEqualTo(Health.up().build());
    }
}
