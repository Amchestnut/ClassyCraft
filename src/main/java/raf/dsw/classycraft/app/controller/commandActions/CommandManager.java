package raf.dsw.classycraft.app.controller.commandActions;

import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<AbstractCommand> commands;
    private int currentCommand;

    public CommandManager() {
        this.currentCommand = 0;
        commands = new ArrayList<>();
    }

    public void addCommand(AbstractCommand command){
        while(currentCommand < commands.size())
            commands.remove(currentCommand);
        commands.add(command);
        doCommand();
    }
    public void doCommand(){
        if(currentCommand < commands.size()){
            commands.get(currentCommand++).doCommand();
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(true);
        }
        if(currentCommand == commands.size()){
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(false);
        }
    }
    public void undoCommand(){
        if(currentCommand > 0){
            commands.get(--currentCommand).undoCommand();
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(true);
        }
        if(currentCommand == 0){
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(false);
        }
    }
    public void resetStack(){
        commands.clear();
        currentCommand = 0;
        MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(false);
        MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(false);
    }
    public void resetButtons(){
        if(commands.isEmpty()){
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(false);
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(false);
        }
        else if(currentCommand == 0) {
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(false);
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(true);
        }
        else if(currentCommand < commands.size()) {
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(true);
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(true);
        }
        else if(currentCommand == commands.size()) {
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(true);
            MainFrame.getInstance().getActionManager().getRedoAction().setEnabled(false);
        }
    }
}
