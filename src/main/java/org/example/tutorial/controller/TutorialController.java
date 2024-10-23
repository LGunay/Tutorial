package org.example.tutorial.controller;

import org.example.tutorial.model.Tutorial;
import org.example.tutorial.repository.TutorialRepository;
import org.example.tutorial.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialService tutorialService;
    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title)
    {
        List<Tutorial> tutorials = tutorialService.listTutorials(title);
        if(tutorials.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }
    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Tutorial tutorial = tutorialService.getTutorialsById(id);
        if (tutorial != null) {
            return new ResponseEntity<>(tutorial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/tutorials")
    public ResponseEntity<String> createTutorial(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("description") String description) {
        tutorialService.createTutorial(file, title, description);
        return new ResponseEntity<>("Tutorial was created successfully.", HttpStatus.CREATED);
    }

    @GetMapping(value = "/tutorials/{id}/image/download")
    public ResponseEntity<?> downloadTodoImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(tutorialService.downloadTodoImage(id));
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<String> updateTutorial(@PathVariable("id") long id, @RequestBody
    Tutorial tutorial) {
        tutorialService.updateTutorial(id, tutorial);
        return new ResponseEntity<>("Tutorial was updated successfully.", HttpStatus.OK);
    }
    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable("id") long id) {
        tutorialService.deleteTutorial(id);
        return new ResponseEntity<>("Tutorial was deleted successfully.", HttpStatus.OK);
    }
    @DeleteMapping("/tutorials")
    public ResponseEntity<String> deleteAllTutorials() {
        tutorialService.deleteAllTutorials();
            return new ResponseEntity<>("Deleted Tutorial(s) successfully.",
                    HttpStatus.OK);
        }
    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {
        List<Tutorial> tutorials = tutorialService.getTutorialsByPublished();
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
        }
}
