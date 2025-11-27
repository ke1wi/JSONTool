package com.jsontoll.commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandExecutor {
    private final Deque<Runnable> undoStack = new ArrayDeque<>(200);
    private final Deque<Runnable> redoStack = new ArrayDeque<>(100);

    public void execute(Runnable action, Runnable undoAction) {
        action.run();
        undoStack.push(undoAction);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            undoStack.pop().run();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            redoStack.pop().run();
        }
    }

    public boolean canUndo() { return !undoStack.isEmpty(); }
}