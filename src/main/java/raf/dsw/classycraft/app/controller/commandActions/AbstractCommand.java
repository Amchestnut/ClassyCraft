package raf.dsw.classycraft.app.controller.commandActions;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.AbstractClassPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.EnumPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.InterfejsPainter;
import raf.dsw.classycraft.app.gui.swing.painters.element_painters.KlasaPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.AbstractClass;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interfejs;
import raf.dsw.classycraft.app.model.diagramElements.elements.Klasa;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

public abstract class AbstractCommand {
    public abstract void doCommand();
    public abstract void undoCommand();
    protected ElementPainter getInstanceOfPainter(DiagramElement diagramElement){
        ElementPainter elementPainter = null;
        if(diagramElement instanceof Klasa){
            elementPainter = new KlasaPainter(diagramElement);
        }
        else if(diagramElement instanceof Interfejs){
            elementPainter = new InterfejsPainter(diagramElement);
        }
        else if(diagramElement instanceof Enum){
            elementPainter = new EnumPainter(diagramElement);
        }
        else if(diagramElement instanceof AbstractClass){
            elementPainter = new AbstractClassPainter(diagramElement);
        }
        return elementPainter;
    }
    protected void setChangedToTrueInCurrentProject(){
        DiagramView selectedDiagramView = MainFrame.getInstance().getPackageView().getSelectedDiagramView();
        if(selectedDiagramView == null){
            // We are likely loading a project; skip setting the changed flag
            return;
        }

        ClassyNode node = MainFrame.getInstance().getPackageView().getSelectedDiagramView().getDiagram();
        while (!(node instanceof Project)){
            node = node.getParent();
        }

        ((Project) node).setChanged(true);
        MainFrame.getInstance().getActionManager().getSaveProjectAction().setEnabled(true);
    }
}
