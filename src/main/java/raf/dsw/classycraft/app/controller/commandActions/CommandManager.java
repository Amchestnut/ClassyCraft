package raf.dsw.classycraft.app.controller.commandActions;

import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<AbstractCommand> commands;
    private int currentCommand;
    private boolean isLoading = false; // New flag for serialisation, we dont want commands to be present when loaded

    public CommandManager() {
        this.currentCommand = 0;
        commands = new ArrayList<>();
    }

    public void addCommand(AbstractCommand command){
        if (isLoading) {
            return; // Skip adding commands during loading
        }
        while(currentCommand < commands.size())
            commands.remove(currentCommand);
        commands.add(command);
        doCommand();
    }

    public void doCommand() {
        if (isLoading) {
            return; // Skip command execution during loading
        }

        if (currentCommand < commands.size()) {
            commands.get(currentCommand++).doCommand();
            MainFrame.getInstance().getActionManager().getUndoAction().setEnabled(true);
        }
        if (currentCommand == commands.size()) {
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

    public boolean isLoading() {    // Could use it in AddInterclassCommand to stop adding if loading from project, to make this cleaner
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }
}
