package bettertodo.todo.integration;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import bettertodo.api.todo.task.ITaskExtension;

public class IdTaskExtension implements ITaskExtension {

    UUID objectId = null;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        return null;
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("bettertodo:id");
    }
}
