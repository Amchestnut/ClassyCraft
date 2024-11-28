package raf.dsw.classycraft.app.tests;

import org.junit.jupiter.api.Test;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Package;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectExplorerTest {
    @Test
    public void testCreateProjectExplorer() {
        ProjectExplorer projectExplorer = new ProjectExplorer("Project Explorer");
        assertEquals("Project Explorer", projectExplorer.getName());
        assertTrue(projectExplorer.getChildren().isEmpty());
    }

    @Test
    public void testAddProjectToProjectExplorer() {
        ProjectExplorer projectExplorer = new ProjectExplorer("Project Explorer");
        Project project = new Project("Project1", projectExplorer);

        boolean added = projectExplorer.addChild(project);
        assertTrue(added);
        assertEquals(1, projectExplorer.getChildren().size());
        assertEquals(project, projectExplorer.getChildren().get(0));
    }

    @Test
    public void testAddPackageToProject() {
        Project project = new Project("Project1", null);
        Package pkg = new Package("Package1", project);

        boolean added = project.addChild(pkg);
        assertTrue(added);
        assertEquals(1, project.getChildren().size());
        assertEquals(pkg, project.getChildren().get(0));
    }

    @Test
    public void testAddDiagramToPackage() {
        Package pkg = new Package("Package1", null);
        Diagram diagram = new Diagram("Diagram1", pkg);

        boolean added = pkg.addChild(diagram);
        assertTrue(added);
        assertEquals(1, pkg.getChildren().size());
        assertEquals(diagram, pkg.getChildren().get(0));
    }

    @Test
    public void testRemoveProjectFromProjectExplorer() {
        ProjectExplorer projectExplorer = new ProjectExplorer("Project Explorer");
        Project project = new Project("Project1", projectExplorer);
        projectExplorer.addChild(project);

        projectExplorer.deleteChild(project);
        assertTrue(projectExplorer.getChildren().isEmpty());
    }

    @Test
    public void testProjectSetAuthor() {
        Project project = new Project("Project1", null);
        project.setAuthor("New Author");
        assertEquals("New Author", project.getAuthor());
    }

    @Test
    public void testPackageOpenPackage() {
        Package pkg = new Package("Package1", null);
        // Assuming that openPackage method would trigger some notification
        // We can test if subscribers receive the notification
        // For simplicity, I will just ensure the method can be called without errors
        assertDoesNotThrow(() -> pkg.openPackage());
    }
}
