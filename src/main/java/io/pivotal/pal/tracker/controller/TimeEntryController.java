package io.pivotal.pal.tracker.controller;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository){
        this.timeEntryRepository = timeEntryRepository;
    }
    @PostMapping
    public ResponseEntity<TimeEntry> create (@RequestBody TimeEntry timeEntry){
        TimeEntry createdTimeEntry =  timeEntryRepository.create(timeEntry);
        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read (@PathVariable  Long id){
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if(timeEntry!=null) {
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list (){
        return new ResponseEntity<>(timeEntryRepository.list(),HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update (@PathVariable Long id, @RequestBody TimeEntry timeEntry){
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id,timeEntry);
        if(updatedTimeEntry!=null) {
            return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete (@PathVariable long id){
        timeEntryRepository.delete(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
