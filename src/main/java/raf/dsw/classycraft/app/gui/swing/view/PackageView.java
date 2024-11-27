package raf.dsw.classycraft.app.gui.swing.view;

import raf.dsw.classycraft.app.controller.commandActions.AbstractCommand;
import raf.dsw.classycraft.app.controller.commandActions.CommandManager;
import raf.dsw.classycraft.app.controller.commandActions.commands.AddConnectionCommand;
import raf.dsw.classycraft.app.controller.commandActions.commands.AddInterclassCommand;
import raf.dsw.classycraft.app.model.diagramElements.DiagramElement;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.connections.DataForConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.Interclass;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;
import raf.dsw.classycraft.app.model.state.State;
import raf.dsw.classycraft.app.model.state.StateManager;
import raf.dsw.classycraft.app.model.state.classesAndConnections.AddInterclassState;
import raf.dsw.classycraft.app.model.state.connections.AddConnectionState;
import raf.dsw.classycraft.app.model.state.functions.EditState;
import raf.dsw.classycraft.app.model.state.functions.ZoomState;
import raf.dsw.classycraft.app.model.state.functions.ZoomToFitState;
import raf.dsw.classycraft.app.notifications.NotificationForAddingAndRemoving;
import raf.dsw.classycraft.app.notifications.NotificationForChangingAuthor;
import raf.dsw.classycraft.app.notifications.NotificationForChangingName;
import raf.dsw.classycraft.app.notifications.NotificationForOpeningPackage;
import raf.dsw.classycraft.app.observer.ISubscriber;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

public class PackageView extends JLabel implements ISubscriber {
    private JLabel projectName;
    private JLabel authorName;
    private JTabbedPane tabbedPane;
    private Package openedPackage;
    private StateManager stateManager;
    private List<DiagramView> listOfAllDiagramViews;
    private JScrollPane scrollPane = null;
    private CommandManager currentCommandManager;
    private DiagramView selectedDiagramView;
    public PackageView() {
        initialise();
    }
    private void initialise(){
        projectName = new JLabel("\n");
        authorName = new JLabel("\n");
        tabbedPane = new JTabbedPane();
        stateManager = new StateManager();   // Empty, but set on addClassState in the beginning
        listOfAllDiagramViews = new ArrayList<>();
        scrollPane = new JScrollPane();

        ChangeListener changeListener = new ChangeListener() {  // Listener for tab changes, needed for setting commandManager and setting buttons
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if(index != -1) {
                    if(sourceTabbedPane.getComponentAt(index) instanceof JScrollPane) {
                        DiagramView selected = (DiagramView) ((JScrollPane) sourceTabbedPane.getComponentAt(index)).getViewport().getView();
                        currentCommandManager = selected.getCommandManager();
                        currentCommandManager.resetButtons();
                        selectedDiagramView = selected;
                    }
                }
            }
        };
        tabbedPane.addChangeListener(changeListener);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        projectName.setAlignmentX(CENTER_ALIGNMENT);
        authorName.setAlignmentX(CENTER_ALIGNMENT);
        this.add(projectName);
        this.add(authorName);
        this.add(tabbedPane);
    }

    @Override
    public void update(Object notification) {

        if(notification instanceof NotificationForOpeningPackage){
            ClassyNode node = ((NotificationForOpeningPackage) notification).getNode();
            ClassyNode loaded = ((NotificationForOpeningPackage) notification).getLoaded();
            if(node instanceof Package){
                openedPackage = (Package) node;
                openDiagramsForPackage(node, loaded);
                ClassyNode tmp = node;
                while(!(tmp instanceof Project)){
                    if(tmp.getParent() == null)
                        break;
                    tmp = tmp.getParent();
                }
                if(tmp instanceof Project) {
                    getProjectName().setText(tmp.getName());
                    getAuthorName().setText(((Project) tmp).getAuthor());
                }
            }
        }
        else if(notification instanceof NotificationForAddingAndRemoving){
            String type = ((NotificationForAddingAndRemoving) notification).getType();
            ClassyNode node = ((NotificationForAddingAndRemoving) notification).getNode();

            if(node instanceof Diagram){
                if(node.getParent().equals(openedPackage)) {
                    if(type.equalsIgnoreCase("ADD")){
                        DiagramView diagramView = new DiagramView(node.getName(), (Diagram)node);
                        listOfAllDiagramViews.add(diagramView);

                        diagramView.setPreferredSize(new Dimension(2000, 2000));
                        JScrollPane scrollPane = new JScrollPane(diagramView);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane.getViewport().setViewPosition(new Point(500, 500));
                        this.scrollPane = scrollPane;

                        ((Diagram) node).addSubscriber(diagramView);
                        if(node.getParent().equals(openedPackage)) {
                            tabbedPane.addTab(diagramView.getTabName(), scrollPane);
                        }
                    }
                    if(type.equalsIgnoreCase("REMOVE")) {
                        if (tabbedPane.indexOfTab(node.getName()) != -1) {
                            tabbedPane.removeTabAt(tabbedPane.indexOfTab(node.getName()));

                            for(DiagramView view : listOfAllDiagramViews){
                                if(view.getDiagram().equals(node)){
                                    listOfAllDiagramViews.remove(view);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else if(node instanceof Package){
                if(type.equalsIgnoreCase("REMOVE")){

                    for(ClassyNode dete : ((Package) node).getChildren()){
                        if(dete instanceof Diagram){
                            for(DiagramView view : listOfAllDiagramViews){
                                if(view.getDiagram().equals(dete)){
                                    listOfAllDiagramViews.remove(view);
                                    break;
                                }
                            }
                        }
                    }

                    if(node.equals(openedPackage)){
                        tabbedPane.removeAll();
                        openedPackage = null;
                    }
                    else {
                        ClassyNode tmp = openedPackage;
                        while(tmp != null && !(tmp instanceof Project)){
                            if(tmp.equals(node)){
                                tabbedPane.removeAll();
                                openedPackage = null;
                                break;
                            }
                            tmp = tmp.getParent();
                        }
                    }
                }
            }
            else if(node instanceof Project){
                if(type.equalsIgnoreCase("REMOVE")){
                    if(((Project) node).getChildren().contains(openedPackage)){
                        tabbedPane.removeAll();
                        openedPackage = null;
                        setAuthorAndNameToNull();
                    }
                    else {
                        ClassyNode tmp = openedPackage;
                        while(tmp != null && !(tmp instanceof Project)){
                            tmp = tmp.getParent();
                        }
                        if(tmp != null && tmp.equals(node)){
                            tabbedPane.removeAll();
                            openedPackage = null;
                            setAuthorAndNameToNull();
                        }
                    }
                }
            }
        }
        else if(notification instanceof NotificationForChangingAuthor){
            ClassyNode node = ((NotificationForChangingAuthor) notification).getNode();
            if(node instanceof Project) {
                if(((Project) node).getChildren().contains(openedPackage)) {
                    String newAuthorName = ((NotificationForChangingAuthor) notification).getNewAuthorName();
                    authorName.setText(newAuthorName);
                }
            }
        }
        else if(notification instanceof NotificationForChangingName){
            ClassyNode node = ((NotificationForChangingName) notification).getNode();
            String newName = ((NotificationForChangingName) notification).getName();
            if(node instanceof Project){
                if(((Project) node).getChildren().contains(openedPackage)){
                    projectName.setText(newName);
                }
            }
            else if(node instanceof Diagram){
                if(tabbedPane.indexOfTab(node.getName()) != -1)
                    tabbedPane.setTitleAt(tabbedPane.indexOfTab(node.getName()), newName);
            }
        }
    }
    private void openDiagramsForPackage(ClassyNode node, ClassyNode loaded){
        if(node instanceof ClassyNodeComposite){
            tabbedPane.removeAll();
            openedPackage = (Package) node;

            for(ClassyNode dete : ((Package) node).getChildren()){
                if(dete instanceof Diagram){
                    DiagramView tmp = null;
                    boolean containsThisDiagram = false;
                    for(DiagramView view : listOfAllDiagramViews){
                        if(view.getDiagram().equals(dete)){
                            tmp = view;
                            containsThisDiagram = true;
                            break;
                        }
                    }
                    if(dete.equals(loaded)){
                        if(!containsThisDiagram) {
                            tmp = new DiagramView(dete.getName(), (Diagram) dete);
                            ((Diagram) dete).addSubscriber(tmp);
                            listOfAllDiagramViews.add(tmp);
                        }

                        for(ClassyNode diagramChild : ((Diagram) dete).getChildren()){     // Adds loaded interclasses on diagramView (and create them)
                            if(diagramChild instanceof Interclass){
                                AbstractCommand command = new AddInterclassCommand(((Interclass) diagramChild).getLocation(), (DiagramElement) diagramChild, tmp);
                                tmp.getCommandManager().addCommand(command);
                            }
                        }

                        for(ClassyNode diagramChild : ((Diagram) dete).getChildren()){     // Adds loaded connections on diagramView (and create them)
                            if(diagramChild instanceof Connection) {
                                Interclass interclassOD = ((Connection) diagramChild).getInterclassOD();
                                Interclass interclassDO = ((Connection) diagramChild).getInterclassDO();
                                DataForConnection dataForConnection = new DataForConnection(((Connection) diagramChild).getType());

                                for(ClassyNode deteDiagrama2 : ((Diagram) dete).getChildren()){
                                    if(deteDiagrama2 instanceof Interclass){
                                        if(((Interclass) deteDiagrama2).getLocation().equals(interclassOD.getLocation())){
                                            interclassOD = (Interclass) deteDiagrama2;
                                        }
                                        if(((Interclass) deteDiagrama2).getLocation().equals(interclassDO.getLocation())){
                                            interclassDO = (Interclass) deteDiagrama2;
                                        }
                                    }
                                }

                                dataForConnection.setInterclassOD(interclassOD);
                                dataForConnection.setInterclassDO(interclassDO);
                                dataForConnection.setInstanceOfTheFirstElement(((Connection) diagramChild).getInstanceOfTheFirstElement());
                                dataForConnection.setInstanceOfTheSecondElement(((Connection) diagramChild).getInstanceOfTheSecondElement());
                                dataForConnection.setKardinalnostOfTheFirstElement(((Connection) diagramChild).getKardinalnostOfTheFirstElement());
                                dataForConnection.setKardinalnostOfTheSecondElement(((Connection) diagramChild).getInstanceOfTheSecondElement());
                                dataForConnection.setVisibilityOfTheFirstElement(((Connection) diagramChild).getVisibilityOfTheFirstElement());
                                dataForConnection.setVisibilityOfTheSecondElement(((Connection) diagramChild).getVisibilityOfTheSecondElement());

                                AbstractCommand command = new AddConnectionCommand(interclassOD, interclassDO, tmp, dataForConnection, true);
                                tmp.getCommandManager().addCommand(command);
                            }
                        }
                        tmp.resetCommandStack();

                        if(currentCommandManager == null){
                            currentCommandManager = tmp.getCommandManager();
                        }
                        currentCommandManager.resetButtons();
                    }
                    else if(!containsThisDiagram){
                        tmp = new DiagramView(dete.getName(), (Diagram) dete);
                        ((Diagram) dete).addSubscriber(tmp);
                        listOfAllDiagramViews.add(tmp);
                    }

                    tmp.setPreferredSize(new Dimension(2000, 2000));
                    scrollPane = new JScrollPane(tmp);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.getViewport().setViewPosition(new Point(500, 500));
                    tabbedPane.add(tmp.getTabName(), scrollPane);
                }
            }
        }
    }
    public void dodajMeKaoSubscribera(ClassyNode node){
        if(node instanceof Diagram)
            ((Diagram) node).addSubscriber(this);
        else if(node instanceof Package)
            ((Package) node).addSubscriber(this);
        else if(node instanceof Project)
            ((Project) node).addSubscriber(this);
    }

    private Point applyTransformation(Point originalPoint, DiagramView paintedPanel) {
        AffineTransform transform = paintedPanel.getTransform();        // Here I get the transformation
        Point newPoint = new Point();
        try {
            transform.inverseTransform(originalPoint, newPoint);        // Here I inverse it
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return newPoint;
    }

    public void misJeKliknut(int x, int y, DiagramView diagramView){
        Point p = applyTransformation(new Point(x,y),diagramView);

        // If some elements were clicked, here we return them back on original color (only for some states, not all)
        State s = this.stateManager.getCurrentState();
        if(s instanceof AddInterclassState || s instanceof ZoomState || s instanceof AddConnectionState || s instanceof ZoomToFitState || s instanceof EditState){
            diagramView.getSelectionedRectangles().clear();
            diagramView.getSelectionedConnections().clear();
            diagramView.backToOriginalColor();
        }
        this.stateManager.getCurrentState().misKliknut(p.x, p.y, diagramView);  // For current state, call method misKliknut, and then call method in some State
    }
    public void misJeOtpusten(int x, int y, DiagramView diagramView){
        Point p = applyTransformation(new Point(x,y),diagramView);
        this.stateManager.getCurrentState().misOtpusten(p.x, p.y, diagramView);
    }
    public void misJePrevucen(int x, int y, DiagramView diagramView){
        Point p = applyTransformation(new Point(x,y),diagramView);
        this.stateManager.getCurrentState().misPrevucen(p.x, p.y, diagramView);
    }
    private void setAuthorAndNameToNull(){
        authorName.setText(null);
        projectName.setText(null);
    }


    public DiagramView getDiagramViewFromScrollPane(JScrollPane scrollPane) {   // for screenshot, dto take out diagramView from JScrollPane
        for (DiagramView diagramView : listOfAllDiagramViews) {
            if (scrollPane.getViewport().getView() == diagramView) {
                return diagramView;
            }
        }
        return null;
    }

    public void startSelectState(){
        this.stateManager.setSelectState();
    }

    public void startMoveState(){
        this.stateManager.setMoveState();
    }

    public void startDeleteState(){
        this.stateManager.setDeleteState();
    }

    public void startEditState() {
        this.stateManager.setEditState();
    }

    public void startZoomState() {
        this.stateManager.setZoomState();
    }

    public void startAddDuplicateState() {
        this.stateManager.setAddDuplicateState();
    }

    public void startAddConnectionState() {
        this.stateManager.setConnectionState();
    }

    public void startInterclassState() {
        this.stateManager.setAddInterclassState();
    }

    public void startZoomToFitState() {
        this.stateManager.setZoomToFitState();
    }

    // Getters and setters
    public JLabel getProjectName() {
        return projectName;
    }

    public void setProjectName(JLabel projectName) {
        this.projectName = projectName;
    }

    public JLabel getAuthorName() {
        return authorName;
    }

    public void setAuthorName(JLabel authorName) {
        this.authorName = authorName;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public List<DiagramView> getListOfAllDiagramViews() {
        return listOfAllDiagramViews;
    }

    public void setListOfAllDiagramViews(List<DiagramView> listOfAllDiagramViews) {
        this.listOfAllDiagramViews = listOfAllDiagramViews;
    }

    public CommandManager getCurrentCommandManager() {
        return currentCommandManager;
    }

    public void setCurrentCommandManager(CommandManager currentCommandManager) {
        this.currentCommandManager = currentCommandManager;
    }

    public DiagramView getSelectedDiagramView() {
        return selectedDiagramView;
    }

    public void setSelectedDiagramView(DiagramView selectedDiagramView) {
        this.selectedDiagramView = selectedDiagramView;
    }
}
