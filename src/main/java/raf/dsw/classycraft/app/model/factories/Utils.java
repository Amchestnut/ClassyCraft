package raf.dsw.classycraft.app.model.factories;

import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public class Utils {
    public static FactoryMethod getFactoryForParentNode(ClassyNode parent) {
        if (parent instanceof ProjectExplorer) {
            return new ProjectFactory();
        }
        else if (parent instanceof Project) {
            return new PackageFactory();
        }
        else if (parent instanceof Package) {
            return null;
        }
        else if(parent instanceof Diagram) {
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage("You can't add anything on the diagram!", MessageType.ERROR);
            return null;
        }
        else {
            throw new IllegalArgumentException("Unknown type!");
        }
    }
}
