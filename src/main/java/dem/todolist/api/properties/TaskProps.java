package dem.todolist.api.properties;

import net.minecraft.util.ResourceLocation;

import com.dem.chestlib.api.properties.IPropertyType;
import com.dem.chestlib.storage.properties.EnumerationType;
import com.dem.chestlib.storage.properties.propertytypes.PropertyTypeEnum;
import com.dem.chestlib.storage.properties.propertytypes.PropertyTypeString;

import dem.todolist.api.enums.TaskState;

/// Props for tasks
public class TaskProps {

    public static final IPropertyType<String> NAME = new PropertyTypeString(
        new ResourceLocation("todolist:name"),
        "untitled.name");
    public static final IPropertyType<String> DESC = new PropertyTypeString(
        new ResourceLocation("todolist:desc"),
        "untitled.desc");

    public static final IPropertyType<TaskState> STATE = new PropertyTypeEnum<>(
        new ResourceLocation("todolist:state"),
        TaskState.New,
        EnumerationType.Ordinal);
}
