package raf.dsw.classycraft.app.core;

import raf.dsw.classycraft.app.gui.swing.logger.LoggerFactory;
import raf.dsw.classycraft.app.gui.swing.messanger.MessageGenerator;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.model.modelImplementation.ProjectExplorer;
import raf.dsw.classycraft.app.model.serializer.JacksonSerializer;

public class ApplicationFramework {

    private static ApplicationFramework instance;
    private ProjectExplorer projectExplorer;
    private ClassyRepositoryImplementation classyRepositoryImplementation;
    private MessageGenerator messageGenerator;
    private JacksonSerializer serializer;

    private ApplicationFramework(){
        classyRepositoryImplementation = new ClassyRepositoryImplementation();
        projectExplorer = classyRepositoryImplementation.getRoot();
    }

    public void initialize(){
        MainFrame.getInstance().setVisible(true);

        messageGenerator = new MessageGenerator();
        messageGenerator.addSubscriber(LoggerFactory.createLogger("CONSOLE_LOGGER"));
        messageGenerator.addSubscriber(LoggerFactory.createLogger("FILE_LOGGER"));

        serializer = new JacksonSerializer();

        MainFrame.getInstance().dodajMeKaoSubscribera();
    }

    public static ApplicationFramework getInstance(){
        if(instance==null){
            instance = new ApplicationFramework();
        }
        return instance;
    }

    public JacksonSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(JacksonSerializer serializer) {
        this.serializer = serializer;
    }

    public MessageGenerator getMessageGenerator() {
        return messageGenerator;
    }

    public ProjectExplorer getProjectExplorer() {
        return projectExplorer;
    }

    public ClassyRepositoryImplementation getClassyRepositoryImplementation() {
        return classyRepositoryImplementation;
    }
}
