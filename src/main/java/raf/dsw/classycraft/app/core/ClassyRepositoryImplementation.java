package raf.dsw.classycraft.app.core;

import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;

public class ClassyRepositoryImplementation implements ClassyRepository{
    private final ProjectExplorer projectExplorer;

    public ClassyRepositoryImplementation() {
        projectExplorer = new ProjectExplorer("Project Explorer");
    }

    @Override
    public ProjectExplorer getRoot() {
        return this.projectExplorer;
    }
}
