package dem.todolist.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import dem.todolist.client.gui.todolist.GuiTodoList;

public class GuiHandler implements IGuiHandler {

    public static final int GUI_LIST = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            return new GuiTodoList(null);
        }

        return null;
    }
}
