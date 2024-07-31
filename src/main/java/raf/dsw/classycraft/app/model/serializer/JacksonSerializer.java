package raf.dsw.classycraft.app.model.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import raf.dsw.classycraft.app.model.diagramElements.connections.*;
import raf.dsw.classycraft.app.model.diagramElements.elements.Enum;
import raf.dsw.classycraft.app.model.diagramElements.elements.*;
import raf.dsw.classycraft.app.model.modelImplementation.Diagram;
import raf.dsw.classycraft.app.model.modelImplementation.Project;
import raf.dsw.classycraft.app.model.repositoryModel.ClassyNode;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonSerializer {
    private final ObjectMapper objectMapper;
    public JacksonSerializer(){
        List<ClassyNode> a = new ArrayList<>();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()              //prihvatamo ove tipove podataka (bunio se dzekson)
                .allowIfSubType(Project.class)
                .allowIfSubType(Package.class)
                .allowIfSubType(Diagram.class)
                .allowIfSubType(Klasa.class)
                .allowIfSubType(ApstraktnaKlasa.class)
                .allowIfSubType(Enum.class)
                .allowIfSubType(Interfejs.class)
                .allowIfSubType(Agregacija.class)
                .allowIfSubType(Asocijacija.class)
                .allowIfSubType(Kompozicija.class)
                .allowIfSubType(Nasledjivanje.class)
                .allowIfSubType(Realizacija.class)
                .allowIfSubType(Zavisnost.class)
                .allowIfSubType(Point.class)
                .allowIfSubType("java.util.List")
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("java.awt.Point")
                .allowIfSubType("java.awt.Dimension")
                .allowIfSubType(Atribut.class)
                .allowIfSubType(Metoda.class)
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
    }
    public void saveProject(Project project) {
        try (FileWriter writer = new FileWriter(project.getPath())) {
            objectMapper.writeValue(writer, project);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Project loadProject(File file){
        try (FileReader fileReader = new FileReader(file)) {
            return objectMapper.readValue(fileReader, Project.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void saveDiagram(Diagram diagram){
        try (FileWriter writer = new FileWriter(diagram.getPath())) {
            objectMapper.writeValue(writer, diagram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Diagram loadDiagram(File file){
        try (FileReader fileReader = new FileReader(file)) {
            return objectMapper.readValue(fileReader, Diagram.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
