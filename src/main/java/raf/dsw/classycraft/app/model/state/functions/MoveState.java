package raf.dsw.classycraft.app.model.state.functions;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.MoveCommand;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.DiagramView;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.state.State;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Ja hocu da ako je Move-ujem klasu na ekranu samo ako je ona selektovana
    - 1) provericu da li se klik nalazi na BILO KOM SELEKTOVANOM PAINTERU
        --- znaci procicu kroz listu selektovanih i videcu da li je bar neki selektovani na toj tacki gde sam kliknuo
    - 2) ako jeste, setovacu "isDragging" na true
    - 3) da bi se uopste pokrenula metoda MISPREVUCEN, mora "isDragging" da bude stavljen na true
    - 4) ako je true, i user krene da draguje, dragovace SVE SELEKTOVANE PRAVOUGAONIKE !!!
    - 5) kad otpusti mis, zavrsio je sa pomeranjem, i tacka se vraca na null
 */
public class MoveState implements State {

    private Point initialClickPoint = null;
    private boolean isDragging = false;
    private Map<InterclassPainter, Point> initialLocations = new HashMap<>();
    private Map<InterclassPainter, Point> newLocations = new HashMap<>();


    @Override
    public void misKliknut(int x, int y, DiagramView diagramView) {
        initialClickPoint = new Point(x,y); // Prvobitni klik se cuva ovde i on se vise NE MENJA tokom drag-a (dok ne pozovemo sve ponovo)
        isDragging = false;                 // iz nekog razloga msm da ovo treba da stoji ovde, mozda je i visak

        for (ElementPainter painter : diagramView.getSelectionedRectangles()) {
            if (painter instanceof InterclassPainter) {
                DiagramElement diagramElement = painter.getDiagramElement();

                if (diagramElement instanceof Interclass) {
                    initialLocations.put((InterclassPainter) painter, ((Interclass) diagramElement).getLocation());
                    newLocations.put((InterclassPainter) painter, ((Interclass) diagramElement).getLocation());
                }
                if (painter.elementAt(painter.getDiagramElement(), initialClickPoint)) {
                    isDragging = true;
                }
            }
        }
    }

    @Override
    public void misOtpusten(int x, int y, DiagramView diagramView) {
        boolean itIntersectsWithSomeone = false;

        List<ElementPainter> unselectedPainters = new ArrayList<>();

        for (ElementPainter painter : diagramView.getPainters()) {             // napunim listu unselectovanih Paintera
            if (!diagramView.getSelectionedRectangles().contains(painter)) {
                unselectedPainters.add(painter);
            }
        }

        for (ElementPainter painter : diagramView.getSelectionedRectangles()) { //da li se preklapa sa nekim
            if(painter instanceof InterclassPainter){
                Rectangle painterBounds = ((InterclassPainter) painter).getShape().getBounds();

                for(ElementPainter neselektovan : unselectedPainters){
                    if(neselektovan instanceof InterclassPainter){
                        Rectangle boundsOfUnselectedPainter = ((InterclassPainter) neselektovan).getShape().getBounds();
                        if(painterBounds.intersects(boundsOfUnselectedPainter)){
                            itIntersectsWithSomeone = true;
                        }
                    }
                }
            }
        }

        if(itIntersectsWithSomeone){                                            /// ako se preklapa sa nekim, vrati ih sve na pocetnu lokaciju
            for (ElementPainter selectedPainter : diagramView.getSelectionedRectangles()) {
                if(selectedPainter instanceof InterclassPainter){
                    DiagramElement diagramElement = selectedPainter.getDiagramElement();
                    Point lastLocation = null;

                    if(initialLocations.containsKey(selectedPainter)){
                        lastLocation = initialLocations.get(selectedPainter);
                    }
                    if(lastLocation != null){
                        ((Interclass) diagramElement).setLocation(lastLocation);
                        ((Interclass) diagramElement).setKonekcioneTacke(diagramElement);
                    }
                }
            }
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage(
                    "Ne mozete ovde pomeriti element jer se preklapa sa nekim!", MessageType.ERROR);
        }
        else {
            AbstractCommand command = new MoveCommand(initialLocations, newLocations);
            diagramView.getCommandManager().addCommand(command);
        }
    }
    @Override
    public void misPrevucen(int x, int y, DiagramView diagramView) {
        if (isDragging) {
            int dx = x - initialClickPoint.x;    // ako sam pomerio mis u desno, dx = trenutna tacka drag-a   -   pocetna tacna drag-a
            int dy = y - initialClickPoint.y;

            for (ElementPainter selectedPainter : diagramView.getSelectionedRectangles()) {
                if(selectedPainter instanceof InterclassPainter){
                    DiagramElement diagramElement = selectedPainter.getDiagramElement();
                    Point lastLocation = initialLocations.get(selectedPainter);

                    if(lastLocation != null){
                        Point newLocation = new Point(lastLocation.x + dx, lastLocation.y + dy);
                        ((Interclass) diagramElement).setLocation(newLocation);
                        ((Interclass) diagramElement).setKonekcioneTacke(diagramElement);

                        // Update the new location in the newLocations map - for undo-redo
                        newLocations.put((InterclassPainter) selectedPainter, ((Interclass)diagramElement).getLocation());
                    }
                }
            }
        }
    }
}

