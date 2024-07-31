package raf.dsw.classycraft.app.model.factories;

import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

public class PackageFactory extends FactoryMethod{
    private Package paket;
    @Override
    public ClassyNode getNode() {
        return paket;
    }

    @Override
    public ClassyNode createNode(ClassyNode parent) {
        if(parent instanceof ClassyNodeComposite && counterPackage == 0){
            counterPackage += ((ClassyNodeComposite) parent).getChildren().size();
        }
        paket = new Package("Package" + counterPackage++, parent);
        return paket;
    }
}
