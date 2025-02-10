package dem.todolist.todo;

import com.dem.chestlib.api.properties.IPropertyContainer;
import com.dem.chestlib.api.properties.IPropertyType;
import com.dem.chestlib.storage.properties.PropertyContainer;
import dem.todolist.api.todo.ITodoList;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class TodoList implements ITodoList {

    IPropertyContainer todoInfo = new PropertyContainer();

    public List<UUID> Tasks = new

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        // first write fields

        // then the property container
        nbt.setTag("properties", qInfo.writeToNBT(new NBTTagCompound()));

        return nbt;
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop) {
        return todoInfo.getProperty(prop);
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop, T def) {
        return todoInfo.getProperty(prop, def);
    }

    @Override
    public boolean hasProperty(IPropertyType<?> prop) {
        return todoInfo.hasProperty(prop);
    }

    @Override
    public <T> void setProperty(IPropertyType<T> prop, T value) {
        todoInfo.setProperty(prop, value);
    }

    @Override
    public void removeProperty(IPropertyType<?> prop) {
        todoInfo.removeProperty(prop);
    }

    @Override
    public void removeAllProps() {
        todoInfo.removeAllProps();
    }
}
