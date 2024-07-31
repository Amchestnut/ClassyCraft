package raf.dsw.classycraft.app.gui.swing.tree.controller;

import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class ClassyTreeCellEditor extends DefaultTreeCellEditor implements ActionListener {

    private Object clickedOn = null;
    private JTextField edit = null;

    public ClassyTreeCellEditor(JTree arg0, DefaultTreeCellRenderer arg1) {
        super(arg0, arg1);
    }

    public Component getTreeCellEditorComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4, int arg5) {
        clickedOn =arg1;
        edit = new JTextField(arg1.toString());
        edit.addActionListener(this);                                      // dodajem sebe, da bi pokrenuo metodu: actionPerformed
        return edit;                                                          // koja trenutna komponenta menja moj cvor u stablu (textbox poslat)
    }

    public boolean isCellEditable(EventObject arg0) {                        // sta treba da se desi na mom stablu da bi se pokrenuo editabilni event
        // ako user klikne triput, aktivirace akciju
        if (arg0 instanceof MouseEvent)
            return ((MouseEvent) arg0).getClickCount() == 3;
        return false;
    }

    public void actionPerformed(ActionEvent e){                                  // ova metoda garantuje da ce posle kliknutog enter-a da se sacuva izmenjeni naziv

        if (!(clickedOn instanceof ClassyTreeItem))
            return;

        ClassyTreeItem clicked = (ClassyTreeItem) clickedOn;
        String newName = e.getActionCommand();

        boolean canChangeName = true;
        if(clicked.getClassyNode().getParent() instanceof ClassyNodeComposite){
            for(ClassyNode node : ((ClassyNodeComposite) clicked.getClassyNode().getParent()).getChildren()){
                if (node.getName().equals(newName)) {
                    canChangeName = false;
                    break;
                }
            }
        }

        if(canChangeName) {
            clicked.setName(newName);
            if (clicked.getClassyNode() instanceof Package || clicked.getClassyNode() instanceof Diagram) {
                clicked.getClassyNode().setName(newName);
            }
        }
        else{
            ApplicationFramework.getInstance().getMessageGenerator().generateMessage
                    ("Ime " + newName + " vec postoji, pokusajte ponovo!", MessageType.ERROR);
        }
    }
}

