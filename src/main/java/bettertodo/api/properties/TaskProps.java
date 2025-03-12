package bettertodo.api.properties;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import chestlib.api.properties.IPropertyType;
import chestlib.storage.nbt.serializers.OptionalData;
import chestlib.storage.properties.EnumerationType;
import chestlib.storage.properties.propertytypes.PropertyTypeBase;
import chestlib.storage.properties.propertytypes.PropertyTypeEnum;
import chestlib.storage.properties.propertytypes.PropertyTypeString;
import chestlib.util.nbt.NBTUuidUtil;
import bettertodo.api.enums.TaskState;

/// Props for tasks
public class TaskProps {

    private static final String ROOT = "bt";

    public static final IPropertyType<String> NAME = new PropertyTypeString(
        new ResourceLocation(ROOT + ":name"),
        "untitled.name");
    public static final IPropertyType<String> DESC = new PropertyTypeString(
        new ResourceLocation(ROOT + ":desc"),
        "untitled.desc");
    public static final IPropertyType<Optional<UUID>> ASSIGNEE = new PropertyTypeBase<Optional<UUID>>(
        new ResourceLocation(ROOT + ":assignee"),
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
        new ResourceLocation(ROOT + ":state"),
        TaskState.New,
        EnumerationType.Ordinal);
}
