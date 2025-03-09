package dem.todolist.commands;

import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;
import dem.todolist.api.properties.TaskProps;
import dem.todolist.api.todo.task.ITask;
import dem.todolist.handlers.persistence.DatabasePersistence;
import dem.todolist.todo.TaskDatabase;
import net.minecraft.command.ICommandSender;

import java.util.Collections;
import java.util.List;

public class ClearDatabasesCommand extends GTNHClientCommand {
    @Override
    public String getCommandName() {
        return "todocleardb";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        DatabasePersistence.INSTANCE.unloadDatabases();
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tcd");
    }
}
