package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private static final String TIME_ENTRY_CREATED = "TimeEntry.created";
    private static final String TIME_ENTRY_READ = "TimeEntry.read";
    private static final String TIME_ENTRY_LISTED = "TimeEntry.listed";
    private static final String TIME_ENTRY_UPDATED = "TimeEntry.updated";
    private static final String TIME_ENTRY_DELETED = "TimeEntry.deleted";
    private static final String TIME_ENTRIES_COUNT = "timeEntries.count";
    private final TimeEntryRepository timeEntryRepository;
    private final CounterService counterService;
    private final GaugeService gaugeService;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               CounterService counterService,
                               GaugeService gaugeService) {
        this.timeEntryRepository = timeEntryRepository;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        TimeEntry repositoryEntry = timeEntryRepository.create(timeEntry);
        counterService.increment(TIME_ENTRY_CREATED);
        gaugeService.submit(TIME_ENTRIES_COUNT, timeEntryRepository.list().size());
        return new ResponseEntity<>(repositoryEntry, CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if (timeEntry == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            counterService.increment(TIME_ENTRY_READ);
            return new ResponseEntity<>(timeEntry, OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counterService.increment(TIME_ENTRY_LISTED);
        return new ResponseEntity<>(timeEntryRepository.list(), OK);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry update = timeEntryRepository.update(id, timeEntry);
        if (update == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            counterService.increment(TIME_ENTRY_UPDATED);
            return new ResponseEntity<>(update, OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        counterService.increment(TIME_ENTRY_DELETED);
        gaugeService.submit(TIME_ENTRIES_COUNT, timeEntryRepository.list().size());
        return new ResponseEntity<>(NO_CONTENT);
    }
}
