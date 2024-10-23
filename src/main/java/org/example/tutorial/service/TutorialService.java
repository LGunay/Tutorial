package org.example.tutorial.service;

import lombok.AllArgsConstructor;
import org.example.tutorial.config.BucketName;
import org.example.tutorial.exception.TutorialAlreadyExistsException;
import org.example.tutorial.exception.TutorialNotFoundException;
import org.example.tutorial.model.Tutorial;
import org.example.tutorial.repository.TutorialRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class TutorialService {

    TutorialRepository tutorialRepository;
    FileService fileService;

    public List<Tutorial> listTutorials(String title) {
        List<Tutorial> tutorials = new ArrayList<>();
        if (title == null)
            tutorialRepository.findAll().forEach(tutorials::add);
        else {
            tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
            if (tutorials.isEmpty())
                throw new TutorialNotFoundException("Tutorial not found");
        }
        return tutorials;
    }
    public Tutorial getTutorialsById(long id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(() -> new TutorialNotFoundException("Tutorial not found"));
        return tutorial;
    }
    public void createTutorial(MultipartFile file, String title, String description) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String path = String.format("%s", BucketName.TODO_IMAGE.getBucketName());
        String fileName = String.format("%s/%s", UUID.randomUUID(), file.getOriginalFilename());
        try {
            fileService.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e){
            throw new IllegalStateException("Failed to upload file", e);
        }
        Tutorial tutorial = new Tutorial();
        tutorial.setTitle(title);
        tutorial.setDescription(description);
        tutorial.setFilePath(path);
        tutorial.setFileName(fileName);
        try {
            tutorialRepository.save(tutorial);
        }
        catch (DuplicateKeyException e){
            throw new TutorialAlreadyExistsException("Tutorial already exists");
        }
    }

    public byte[] downloadTodoImage(Long id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(() -> new TutorialNotFoundException("Tutorial not found"));
        return fileService.download(tutorial.getFilePath(), tutorial.getFileName());
    }

    public void updateTutorial(long id, Tutorial tutorial) {
        Tutorial _tutorial = tutorialRepository.findById(id).orElseThrow(() -> new TutorialNotFoundException("Tutorial not found"));
        if (_tutorial != null) {
            _tutorial.setId(id);
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());
            tutorialRepository.updateTutorialByIds(id, _tutorial);
        } else {
            throw new TutorialNotFoundException("Cannot find Tutorial with id=" + id);
        }
    }
    public void deleteTutorial(long id) {
        try{
            tutorialRepository.deleteById(id);
        }catch(Exception e){
        throw new TutorialNotFoundException("Cannot find Tutorial with id= " + id);
    }
    }

    public void deleteAllTutorials() {
        tutorialRepository.deleteAll();
    }
    public List<Tutorial> getTutorialsByPublished() {
        List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
        if (tutorials.isEmpty()) {
            throw new TutorialNotFoundException("Tutorial not found");
        }
        return tutorials;
    }
}
