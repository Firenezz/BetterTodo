package dem.todolist.commands;

import betterquesting.client.gui2.GuiQuestHelp;
import betterquesting.core.BetterQuesting;
import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;
import dem.todolist.api.properties.TaskProps;
import dem.todolist.api.todo.task.ITask;
import dem.todolist.core.Todo;
import dem.todolist.todo.TaskDatabase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Collections;
import java.util.List;

public class ListTasksCommand extends GTNHClientCommand {
    @Override
    public String getCommandName() {
        return "tasklist";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerSP entityClientPlayerSP) {
            var tasks = TaskDatabase.INSTANCE.values();
            Minecraft.getMinecraft().displayGuiScreen(new GuiQuestHelp(null));
            if (!tasks.isEmpty()) {
                tasks.forEach((task) -> {
                    addChatMessage(task.toChatMessage());
                });
            } else {
                addChatMessage("There is no tasks :(");
            }
        } else if (sender instanceof EntityPlayer) {

        }
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tl");
    }
}
