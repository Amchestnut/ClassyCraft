package raf.dsw.classycraft.app.model.factories;

import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

public class DiagramFactory extends FactoryMethod{
    private Diagram diagram;

    @Override
    public ClassyNode getNode() {
        return diagram;
    }

    @Override
    public ClassyNode createNode(ClassyNode parent) {
        if(parent instanceof ClassyNodeComposite && counterDiagram == 0){
            counterDiagram += ((ClassyNodeComposite) parent).getChildren().size();
        }
        diagram = new Diagram("Diagram" + counterDiagram++, parent);
        return diagram;
    }
}
