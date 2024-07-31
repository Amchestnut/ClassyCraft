package raf.dsw.classycraft.app.gui.swing.view;

import raf.dsw.classycraft.app.controller.ActionManager;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.logger.LoggerFactory;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTree;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTreeImplementation;
import raf.dsw.classycraft.app.notifications.NotificationForLoggers;
import raf.dsw.classycraft.app.observer.ISubscriber;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements ISubscriber {
    private static MainFrame instance;
    private ActionManager actionManager;
    private ClassyTree classyTree;
    private JTree projectExplorer;
    private PackageView packageView;

    //buduca polja za sve komponente view-a na glavnom prozoru

    private MainFrame(){
    }

    private void initialize(){
        classyTree = new ClassyTreeImplementation();
        actionManager = new ActionManager();
        packageView = new PackageView();
        actionManager.getUndoAction().setEnabled(false);
        actionManager.getRedoAction().setEnabled(false);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth * 3 / 4, screenHeight * 3 / 4);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("ClassyCrafT");

        MyMenyBar menu = new MyMenyBar();
        setJMenuBar(menu);

        MyToolBar toolBar = new MyToolBar();
        add(toolBar, BorderLayout.NORTH);

        projectExplorer = classyTree.generateTree(ApplicationFramework.getInstance().getClassyRepositoryImplementation().getRoot());

        JSplitPane split = new JSplitPane();

        JScrollPane scroll = new JScrollPane(projectExplorer);
        scroll.setMinimumSize(new Dimension(200,150));

        split.setLeftComponent(scroll);

        /// right side
        JToolBar rightToolbar = new RightToolBar();
        Box toolbarBox = Box.createVerticalBox();
        toolbarBox.add(Box.createVerticalGlue()); // Invisible component that takes up all the extra space above the toolbar
        toolbarBox.add(rightToolbar);
        toolbarBox.add(Box.createVerticalGlue());
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(packageView, BorderLayout.CENTER);
        contentPanel.add(toolbarBox, BorderLayout.EAST);
        split.setRightComponent(contentPanel);

        getContentPane().add(split,BorderLayout.CENTER);
        split.setDividerLocation(250);
        split.setOneTouchExpandable(true);
    }
    public void dodajMeKaoSubscribera(){
        ApplicationFramework.getInstance().getMessageGenerator().addSubscriber(LoggerFactory.createLogger("MAINFRAME"));
    }

    public static MainFrame getInstance() {
        if(instance == null) {
            instance = new MainFrame();
            instance.initialize();
        }
        return instance;
    }

    public String enterAuthorNewName(){
        return JOptionPane.showInputDialog("Ukucajte ime novog autora", null);
    }

    @Override
    public void update(Object notification) {
        if(notification instanceof NotificationForLoggers) {
            MessageType type = ((NotificationForLoggers) notification).getType();
            String message = ((NotificationForLoggers) notification).getNotification();

            if (type == MessageType.NOTIFICATION) {
                JOptionPane.showMessageDialog(this, message, "Logger", JOptionPane.INFORMATION_MESSAGE);
            } else if (type == MessageType.WARNING) {
                JOptionPane.showMessageDialog(this, message, "Logger", JOptionPane.WARNING_MESSAGE);
            } else if (type == MessageType.ERROR) {
                JOptionPane.showMessageDialog(this, message, "Logger", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public ClassyTree getClassyTree() {
        return classyTree;
    }
    public JTree getProjectExplorer() {
        return projectExplorer;
    }

    public PackageView getPackageView() {
        return packageView;
    }

    public void setPackageView(PackageView packageView) {
        this.packageView = packageView;
    }

}
