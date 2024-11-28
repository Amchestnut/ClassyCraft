# ClassyCraft: UML Class Diagram Tool  

**ClassyCraft** is an intuitive and powerful software tool designed for creating and managing **UML Class Diagrams**. It allows users to visually design classes, interfaces, and their relationships, offering advanced options for code generation, diagram export, and customization. Ideal for developers, students, and educators, ClassyCraft makes modeling class structures simple and efficient.

<p align="center"> 
  <img src="src/main/resources/image_1.png" alt="ClassyCraft Interface" width="1000" />
  <img src="src/main/resources/image_2.png" alt="Class Diagram Example" width="1000" /> 
</p>

## Features
- **Interactive Diagram Creation**:  
  - Add **classes, abstract classes, interfaces**, and **enums**.
  - Define **attributes** and **methods** with rich details.
  - Establish relationships using **inheritance**, **realization**, and other connection types.

- **Code Generation**:
  - Automatically generate **Java code** from UML diagrams.
  - Support for **classes**, **interfaces**, **abstract classes**, and **enums**.
  - Handles **extends** and **implements** relationships based on diagram connections.

- **Extensive Customization**:
  - Customize element styles, colors, and dimensions.
  - Arrange elements with snapping and alignment tools.

- **File Handling**:
  - Save and load projects in a **structured format**.
  - Export diagrams as **image files** or **code repositories**.

- **Error Handling and Validation**:
  - Prevent overlapping or invalid relationships.
  - Highlight errors in real-time during diagram creation.


## Getting Started
**Installation**
1) Clone the repository to your local machine:
```
git clone https://github.com/your-username/classycraft.git
```
2) Import the project into your IDE (e.g., IntelliJ IDEA).  
3) Build and run the project using Gradle or your IDE's build tool.


## **How to Use**
1. **Create a Diagram**
  - Add new elements such as **classes** or **interfaces** using the toolbar.
  - Double-click elements to edit their **name**, **attributes**, or **methods**.
  - Drag and drop to arrange elements.
    
2. **Connect Elements**
Use the **connection tools** to define relationships like:  
- Association  
- Inheritance
- Realisation
- Dependency
- Aggregation
- Composition
  
3. **Generate Code**
- Select the diagram and click the **Generate Code** button.  
- Choose a destination folder, and ClassyCraft will generate structured Java code for all diagram elements.  

4. **Save and Export**
- Save projects for future editing or export diagrams as images for documentation purposes.

## Real-World Applications  
**Software Development**:  
- Design and prototype class structures for new projects.
  
**Education**:  
- Teach or learn object-oriented design principles through interactive diagrams.

**Documentation**:  
- Create professional-grade UML diagrams for software documentation.  

## Tech Stack
**Languages**: Java, Swing (GUI)  
**Libraries**:  
- **Jackson**: For serialization and deserialization of diagrams.  
- **Java AWT/Swing**: For graphical interface and drawing operations.  

## Code Example
**Diagram Code Generation**
```
Diagram diagram = new Diagram("MyDiagram");
Klasa myClass = new Klasa("+MyClass");
myClass.addAttribute(new Attribute("+attribute1: String"));
myClass.addMethod(new Method("+myMethod(): void"));

diagram.addElement(myClass);

String code = new CodeGenerator().generateCodeForInterclass(myClass, diagram);
System.out.println(code);
```

## Contributing
**Contributions are welcome! Follow these steps to contribute**:
1) Fork the repository.
2) Create a new branch for your feature or bug fix.
3) Commit your changes with descriptive messages.
4) Submit a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
