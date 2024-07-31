package raf.dsw.classycraft.app.model.factories;

import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

public class ProjectFactory extends FactoryMethod {

    private Project project;

    @Override
    public ClassyNode getNode() {
        return project;
    }

    @Override
    public ClassyNode createNode(ClassyNode parent) {
        if(parent instanceof ClassyNodeComposite && counterProject == 0){
            counterProject += ((ClassyNodeComposite) parent).getChildren().size();
        }
        project = new Project("Project" + counterProject++, parent);
        return project;
    }
}
