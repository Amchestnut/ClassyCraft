package raf.dsw.classycraft.app.controller.commandActions;

import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.ApstraknaKlasaPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.EnumPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterfejsPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.KlasaPainter;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.ApstraktnaKlasa;
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
        else if(diagramElement instanceof ApstraktnaKlasa){
            elementPainter = new ApstraknaKlasaPainter(diagramElement);
        }
        return elementPainter;
    }
    protected void setChangedToTrueInCurrentProject(){
        ClassyNode node = MainFrame.getInstance().getPackageView().getSelectedDiagramView().getDiagram();
        while (!(node instanceof Project)){
            node = node.getParent();
        }
        ((Project) node).setChanged(true);
        MainFrame.getInstance().getActionManager().getSaveProjectAction().setEnabled(true);
    }
}