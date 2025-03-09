package dem.todolist.commands;

import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;
import dem.todolist.api.properties.TaskProps;
import dem.todolist.api.todo.task.ITask;
import dem.todolist.todo.TaskDatabase;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Collections;
import java.util.List;

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
