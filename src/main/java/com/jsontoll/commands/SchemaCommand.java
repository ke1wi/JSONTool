package com.jsontoll.commands;

public sealed interface SchemaCommand permits SimpleChangeCommand {
    void execute();
    Runnable undo();
    String description();
}

record SimpleChangeCommand(Runnable exec, Runnable undo, String description) implements SchemaCommand {
    @Override public void execute() { exec.run(); }
    @Override public Runnable undo() { undo.run();
        return null;
    }
}