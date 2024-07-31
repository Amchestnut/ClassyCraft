package raf.dsw.classycraft.app.controller.commandActions.commands;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.gui.swing.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.painters.elementi.InterclassPainter;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveCommand extends AbstractCommand {
    private final List<Helper> helpers = new ArrayList<>();
    public MoveCommand(Map<InterclassPainter, Point> oldLocations, Map<InterclassPainter, Point> newLoactions){
        List<InterclassPainter> listOfPainters = new ArrayList<>(oldLocations.keySet());

        for(InterclassPainter painter : listOfPainters){
            Helper helper = new Helper();
            helper.setPainter(painter);
            helper.setOldLocation(oldLocations.get(painter));
            helper.setNewLocation(newLoactions.get(painter));
            helpers.add(helper);
        }
    }
    @Override
    public void doCommand() {
        for(Helper helper : helpers){
            if(helper.getPainter().getDiagramElement() instanceof Interclass) {
                ((Interclass) helper.getPainter().getDiagramElement()).setLocation(helper.newLocation);
                setKonekcioneTackeForPainter(helper.getPainter());
            }
        }
        setChangedToTrueInCurrentProject();
    }

    @Override
    public void undoCommand() {
        for(Helper helper : helpers){
            if(helper.getPainter().getDiagramElement() instanceof Interclass) {
                ((Interclass) helper.getPainter().getDiagramElement()).setLocation(helper.oldLocation);
                setKonekcioneTackeForPainter(helper.getPainter());
            }
        }
    }
    private void setKonekcioneTackeForPainter(ElementPainter painter){
        if(painter.getDiagramElement() instanceof Interclass)
            ((Interclass) painter.getDiagramElement()).setKonekcioneTacke(painter.getDiagramElement());
    }
    private static class Helper{
        private Point oldLocation;
        private Point newLocation;
        private InterclassPainter painter;
        public Point getOldLocation() {
            return oldLocation;
        }

        public void setOldLocation(Point oldLocation) {
            this.oldLocation = oldLocation;
        }

        public Point getNewLocation() {
            return newLocation;
        }

        public void setNewLocation(Point newLocation) {
            this.newLocation = newLocation;
        }

        public InterclassPainter getPainter() {
            return painter;
        }

        public void setPainter(InterclassPainter painter) {
            this.painter = painter;
        }
    }
}
