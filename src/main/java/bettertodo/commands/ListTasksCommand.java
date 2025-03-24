package bettertodo.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;

import bettertodo.core.Todo;
import bettertodo.handlers.GuiHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ListTasksCommand extends GTNHClientCommand {

    private EntityPlayer targetPlayer = null;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && targetPlayer != null) {
            // Open the GUI
            targetPlayer.openGui(
                Todo.INSTANCE,
                GuiHandler.GUI_LIST,
                targetPlayer.worldObj,
                (int) targetPlayer.posX,
                (int) targetPlayer.posY,
                (int) targetPlayer.posZ);

            // Clean up - unregister this handler and reset variables
            FMLCommonHandler.instance()
                .bus()
                .unregister(this);
            targetPlayer = null;
        }
    }

    @Override
    public String getCommandName() {
        return "tasklist";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer player) {
            targetPlayer = player;
            FMLCommonHandler.instance()
                .bus()
                .register(this);
        }
        /*
         * if (sender instanceof EntityPlayerSP entityClientPlayerSP) {
         * var tasks = TaskDatabase.INSTANCE.values();
         * Minecraft.getMinecraft().displayGuiScreen(new GuiQuestHelp(null));
         * if (!tasks.isEmpty()) {
         * tasks.forEach((task) -> {
         * addChatMessage(task.toChatMessage());
         * });
         * } else {
         * addChatMessage("There is no tasks :(");
         * }
         * }
         */
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tl");
    }
}
