package raf.dsw.classycraft.app.model.diagramElements.elements;

public class Metoda extends ClassContent{
    private String methodName;
    public Metoda(String vidljivost, String methodName) {
        super(vidljivost);
        this.methodName = methodName;
    }
    public Metoda(){
        super();
    }
    public String getMethodName() {
        return methodName;
    }
    @Override
    public String export() {
        StringBuilder stringBuilder = new StringBuilder();

        String[] split = methodName.split(":");
        String povratnaVr = split[1];
        String ime = split[0];

        stringBuilder.append(povratnaVr).append(" ").append(ime).append("(){}");

        return stringBuilder.toString();
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
