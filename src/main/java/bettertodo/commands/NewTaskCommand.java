package bettertodo.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.ICommandSender;

import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;

import bettertodo.api.properties.TaskProps;
import bettertodo.api.todo.task.ITask;
import bettertodo.todo.TaskDatabase;

public class NewTaskCommand extends GTNHClientCommand {

    @Override
    public String getCommandName() {
        return "tasknew";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ITask task = TaskDatabase.INSTANCE.createNewWithUuidGen();
        if (args.length > 0) {
            if (args.length == 1) {
                task.setProperty(TaskProps.NAME, args[0]);
            }
            if (args.length == 2) {
                task.setProperty(TaskProps.DESC, args[1]);
            }
        } else {
            task.setProperty(TaskProps.NAME, "test");
        }
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tn");
    }
}
