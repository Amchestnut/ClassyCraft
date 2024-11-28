package raf.dsw.classycraft.app.model.diagramElements.elements;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Method")
public class Method extends ClassContent{
    private String methodName;
    public Method(String visibility, String methodName) {
        super(visibility);
        this.methodName = methodName;
    }
    public Method(){
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
