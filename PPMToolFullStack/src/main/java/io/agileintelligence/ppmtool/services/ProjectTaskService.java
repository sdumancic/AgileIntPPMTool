package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.ProjectTask;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){
        //Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);

        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog();

        if (backlog == null)
            throw new ProjectNotFoundException("Project with identifier " + projectIdentifier.toUpperCase() + " does not exist");

        if (backlog != null){
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(projectIdentifier + "-"  + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0)
                projectTask.setPriority(3);
            if (projectTask.getStatus() == null || projectTask.getStatus().isEmpty()) {
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        }
        return null;

    }

    public Iterable<ProjectTask> findBacklogById(String backlogId, String username) {

        /*Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
        if (backlog == null)
            throw new ProjectNotFoundException("Project with identifier " + backlogId.toUpperCase() + " does not exist");
        */
        projectService.findProjectByIdentifier(backlogId, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId, String username){

        projectService.findProjectByIdentifier(backlogId, username);

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
        if (projectTask == null)
            throw new ProjectNotFoundException("Project Task " + ptId + " not found");

        if (!projectTask.getProjectIdentifier().equals(backlogId)){
            throw new ProjectNotFoundException("Project task " + ptId + " does not exist in project " + backlogId);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogId,ptId,username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlogId, String ptId, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlogId,ptId,username);
        //System.out.println(projectTask.toString());
        projectTaskRepository.delete(projectTask);

    }
}
