package raf.dsw.classycraft.app.model.factories;

import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public abstract class FactoryMethod {
    protected static int counterPackage = 1;
    protected static int counterDiagram = 1;
    protected static int counterProject = 1;
    public abstract ClassyNode getNode();

    public abstract ClassyNode createNode(ClassyNode parent);
}
