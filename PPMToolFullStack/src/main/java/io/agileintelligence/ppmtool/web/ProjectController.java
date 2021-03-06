package io.agileintelligence.ppmtool.web;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.services.ProjectService;
import io.agileintelligence.ppmtool.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ValidationErrorService validationErrorService;

//    @PostMapping("")
//    public ResponseEntity<Project> saveProject(@RequestBody Project project, UriComponentsBuilder ucb){
//        HttpHeaders headers = new HttpHeaders();
//        Project project = projectService.save(project);
//        URI locationUri = ucb.path("/projects/")
//                        .path(String.valueOf("12345"))
//                        .build()
//                        .toUri();
//        headers.setLocation(locationUri);
//        ResponseEntity<Project> responseEntity = new ResponseEntity<Project>(
//                        project, headers, HttpStatus.CREATED)
//        return responseEntity;
//    }

    @PostMapping
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result,
                                              Principal principal){

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if (errorMap != null)
            return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project1,HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<String>("Project with id " + projectId + " was deleted",HttpStatus.OK);
    }

}
