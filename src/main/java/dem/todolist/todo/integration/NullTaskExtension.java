package dem.todolist.todo.integration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import dem.todolist.api.todo.task.ITaskExtension;

public class NullTaskExtension implements ITaskExtension {

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        return nbtTagCompound;
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("todolist:null");
    }
}
