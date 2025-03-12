package dem.todolist.api.properties;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import com.dem.chestlib.api.properties.IPropertyType;
import com.dem.chestlib.storage.nbt.serializers.OptionalData;
import com.dem.chestlib.storage.properties.EnumerationType;
import com.dem.chestlib.storage.properties.propertytypes.PropertyTypeBase;
import com.dem.chestlib.storage.properties.propertytypes.PropertyTypeEnum;
import com.dem.chestlib.storage.properties.propertytypes.PropertyTypeString;
import com.dem.chestlib.util.nbt.NBTUuidUtil;

import dem.todolist.api.enums.TaskState;

/// Props for tasks
public class TaskProps {

    public static final IPropertyType<String> NAME = new PropertyTypeString(
        new ResourceLocation("todolist:name"),
        "untitled.name");
    public static final IPropertyType<String> DESC = new PropertyTypeString(
        new ResourceLocation("todolist:desc"),
        "untitled.desc");
    public static final IPropertyType<Optional<UUID>> ASSIGNEE = new PropertyTypeBase<Optional<UUID>>(
        new ResourceLocation("todolist:assignee"),
        Optional.empty()) {

        @Override
        public Optional<UUID> readValue(NBTBase nbt) {
            if (!(nbt instanceof NBTTagCompound tag) || nbt.getId() != Constants.NBT.TAG_COMPOUND) {
                return this.getDefault();
            }

            try {
                return OptionalData.readFromNBT(tag, (optionNbt) -> {
                    if (optionNbt instanceof NBTTagCompound optionTag && nbt.getId() == Constants.NBT.TAG_COMPOUND) {
                        return NBTUuidUtil.tryReadFromNbt("", optionTag);
                    }
                    return Optional.empty();
                });
            } catch (Exception e) {
                return this.getDefault();
            }
        }

        @Override
        public NBTBase writeValue(Optional<UUID> value) {
            return OptionalData.writeValue(value, (NBTUuidUtil::writeIdToNbt));
        }
    };

    public static final IPropertyType<TaskState> STATE = new PropertyTypeEnum<>(
        new ResourceLocation("todolist:state"),
        TaskState.New,
        EnumerationType.Ordinal);
}
