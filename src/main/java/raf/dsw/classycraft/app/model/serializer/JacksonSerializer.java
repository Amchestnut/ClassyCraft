package raf.dsw.classycraft.app.model.serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
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

    public JacksonSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Enable default typing with a base package or trusted types
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType("raf.dsw.classycraft.app.model")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.awt.") // Allow all java.awt classes
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public void saveProject(Project project) {
        try {
            objectMapper.writeValue(new File(project.getPath()), project);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Project loadProject(File file) {
        try {
            return objectMapper.readValue(file, Project.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveDiagram(Diagram diagram){
//        try (FileWriter writer = new FileWriter(diagram.getPath())) {
//            objectMapper.writeValue(writer, diagram);
        try{
            objectMapper.writeValue(new File(diagram.getPath()), diagram);
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
