package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Backlog;
import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.BacklogRepository;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            /*if inserting then create backlog*/
            if (project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }
        catch (Exception ex){
            if (ex.getCause().getClass() == org.hibernate.exception.ConstraintViolationException.class){
                ConstraintViolationException cause = (ConstraintViolationException)ex.getCause();
                String errMessage = cause.getSQLException().getMessage();
                if (errMessage.toUpperCase().startsWith("UNIQUE INDEX OR PRIMARY KEY VIOLATION") && errMessage.toUpperCase().contains("PROJECT(PROJECT_IDENTIFIER)"))
                    throw new ProjectIdException("Project ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
            }
            throw ex;
        }

    }

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId);

        if (project == null){
            throw new ProjectIdException("Project ID " + projectId.toUpperCase() + " does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId);

        if (project == null){
            throw new ProjectIdException("Cannot delete project with Project ID " + projectId.toUpperCase() + " because it does not exist");
        }

        projectRepository.delete(project);
    }


}

