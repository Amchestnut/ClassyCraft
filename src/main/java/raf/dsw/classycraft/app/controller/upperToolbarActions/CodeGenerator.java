package raf.dsw.classycraft.app.controller.upperToolbarActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.diagramElements.connections.Connection;
import raf.dsw.classycraft.app.model.diagramElements.connections.InheritanceConnection;
import raf.dsw.classycraft.app.model.diagramElements.connections.RealisationConnection;
import raf.dsw.classycraft.app.model.diagramElements.elements.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNodeComposite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator extends AbstractClassyAction {

    public CodeGenerator(){
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        putValue(SMALL_ICON, scaleIcon(loadIcon("/images/code_generation.png"), 32, 32));
        putValue(NAME, "Generate code from the created diagram");
        putValue(SHORT_DESCRIPTION, "Generate code from the created diagram");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Only proceed if a Diagram is selected
        ClassyNode selectedNode = MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        if (!(selectedNode instanceof Diagram)) {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Please select a Diagram to generate code from.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Diagram diagram = (Diagram) selectedNode;

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
            File outputFolder = jfc.getSelectedFile();

            // Collect all classes and interfaces in the diagram
            Map<Interclass, String> classCodeMap = new HashMap<>();
            for (ClassyNode element : diagram.getChildren()) {
                if (element instanceof Interclass) {
                    Interclass interclass = (Interclass) element;
                    String code = generateCodeForInterclass(interclass, diagram);
                    classCodeMap.put(interclass, code);
                }
            }

            // Write each class/interface to a .java file
            for (Map.Entry<Interclass, String> entry : classCodeMap.entrySet()) {
                Interclass interclass = entry.getKey();
                String code = entry.getValue();

                // Determine package name
                String packageName = getPackageName(interclass);
                File packageFolder = new File(outputFolder, packageName.replace('.', File.separatorChar));
                packageFolder.mkdirs();

                // Write to file
                File javaFile = new File(packageFolder, getClassName(interclass) + ".java");
                try (FileWriter writer = new FileWriter(javaFile)) {
                    writer.write(code);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Code generation completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String generateCodeForInterclass(Interclass interclass, Diagram diagram) {
        StringBuilder sb = new StringBuilder();

        // Package declaration
        String packageName = getPackageName(interclass);
        sb.append("package ").append(packageName).append(";\n\n");

        // For simplicity, not adding imports here

        // Class/interface declaration
        String modifiers = getModifiers(interclass);
        String classType = getClassType(interclass);
        String className = getClassName(interclass);
        String extendsClause = getExtendsClause(interclass, diagram);
        String implementsClause = getImplementsClause(interclass, diagram);

        sb.append(modifiers).append(classType).append(" ").append(className)
                .append(extendsClause).append(implementsClause).append(" {\n");

        // Class
        if (interclass instanceof Klasa) {
            List<ClassContent> attributes = ((Klasa) interclass).getAttributes();
            for(ClassContent classContent: attributes){
                if (classContent instanceof Attribute){
                    sb.append("\t").append(classContent.export()).append("\n");

                }
            }
            List<ClassContent> methods = ((Klasa) interclass).getMethods();
            for(ClassContent classContent: methods){
                if (classContent instanceof Method){
                    sb.append("\n\t").append(classContent.export()).append("\n");
                }
            }
        }

        // Abstract class
        if (interclass instanceof AbstractClass) {
            List<ClassContent> attributes = ((AbstractClass) interclass).getAttributes();
            for(ClassContent classContent: attributes){
                if (classContent instanceof Attribute){
                    sb.append("\t").append(classContent.export()).append("\n");

                }
            }
            List<ClassContent> methods = ((AbstractClass) interclass).getMethods();
            for(ClassContent classContent: methods){
                if (classContent instanceof Method){
                    sb.append("\n\t").append(classContent.export()).append("\n");
                }
            }
        }

        // Interface
        if (interclass instanceof Interfejs) {
            List<ClassContent> methods = ((Interfejs) interclass).getMethods();
            for(ClassContent classContent: methods){
                if (classContent instanceof Method){
                    sb.append("\n\t").append(classContent.export()).append("\n");
                }
            }
        }

        // Enum
        if (interclass instanceof Enum) {
            List<ClassContent> attributes = ((Enum) interclass).getAttributes();
            for(ClassContent classContent: attributes){
                if (classContent instanceof Attribute){
                    sb.append("\t").append(classContent.export()).append("\n");

                }
            }
        }


        sb.append("}\n");

        return sb.toString();
    }

    private String getModifiers(Interclass interclass) {
        String modifiers = "";

        if (interclass instanceof AbstractClass) {
            modifiers += "public abstract ";
        } else {
            modifiers += "public ";
        }

        return modifiers;
    }

    private String getClassType(Interclass interclass) {
        if (interclass instanceof Klasa || interclass instanceof AbstractClass) {
            return "class";
        } else if (interclass instanceof Interfejs) {
            return "interface";
        } else if (interclass instanceof Enum) {
            return "enum";
        }
        return "";
    }

    private String getClassName(Interclass interclass) {
        // Remove visibility symbols (+, -, #) from the name
        return interclass.getName().replaceAll("[+\\-#]", "").trim();
    }

    private String getExtendsClause(Interclass interclass, Diagram diagram) {
        // Find inheritance connections where this class is the subclass
        for (ClassyNode element : diagram.getChildren()) {
            if (element instanceof InheritanceConnection) {
                InheritanceConnection connection = (InheritanceConnection) element;
                if (connection.getInterclassFROM().equals(interclass)) {
                    String superclassName = getClassName(connection.getInterclassTO());
                    return " extends " + superclassName;
                }
            }
        }
        return "";
    }

    private String getImplementsClause(Interclass interclass, Diagram diagram) {
        // Find realisation connections where this class implements an interface
        List<String> interfaces = new ArrayList<>();
        for (ClassyNode element : diagram.getChildren()) {
            if (element instanceof RealisationConnection) {
                RealisationConnection connection = (RealisationConnection) element;
                if (connection.getInterclassFROM().equals(interclass)) {
                    String interfaceName = getClassName(connection.getInterclassTO());
                    interfaces.add(interfaceName);
                }
            }
        }
        if (!interfaces.isEmpty()) {
            return " implements " + String.join(", ", interfaces);
        }
        return "";
    }

    private String getPackageName(Interclass interclass) {
        // Build the package name based on the hierarchy
        ClassyNode node = interclass.getParent(); // Diagram
        ClassyNode packageNode = node.getParent(); // Package
        ClassyNode projectNode = packageNode.getParent(); // Project

        String packageName = projectNode.getName() + "." + packageNode.getName();
        return packageName.toLowerCase(); // Convert to lowercase for package naming conventions
    }
}
