package chestlib.storage.properties.propertytypes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import chestlib.storage.properties.EnumerationType;

public class PropertyTypeEnum<EnumType extends Enum<EnumType>> extends PropertyTypeBase<EnumType> {

    public final EnumerationType serializeType;
    private final Class<EnumType> enumClass;

    public PropertyTypeEnum(ResourceLocation key, EnumType defaultValue) {
        this(key, defaultValue, EnumerationType.String);
    }

    public PropertyTypeEnum(ResourceLocation key, EnumType defaultValue, EnumerationType serializeType) {
        super(key, defaultValue);
        this.serializeType = serializeType;

        this.enumClass = defaultValue.getDeclaringClass();
    }

    @Override
    public EnumType readValue(NBTBase nbt) {
        if (nbt == null) {
            return this.getDefault();
        }

        try {
            switch (serializeType) {
                case String -> {
                    if (nbt.getId() != 8) return this.getDefault();

                    return Enum.valueOf(enumClass, ((NBTTagString) nbt).func_150285_a_());
                }
                case Ordinal -> {
                    if (nbt.getId() != 2) return this.getDefault();

                    int ordinal = ((NBTBase.NBTPrimitive) nbt).func_150287_d();
                    return enumClass.getEnumConstants()[ordinal];
                }
                default -> throw new IllegalStateException("Unexpected value: " + serializeType);
            }
        } catch (Exception e) {
            // TODO: Add error log
            return this.getDefault();
        }
    }

    @Override
    public NBTBase writeValue(EnumType value) {
        if (value == null) {
            switch (serializeType) {
                case String -> {
                    return new NBTTagString(
                        this.getDefault()
                            .toString());
                }
                case Ordinal -> {
                    return new NBTTagInt(
                        this.getDefault()
                            .ordinal());
                }
                default -> throw new IllegalStateException("Unexpected value: " + serializeType);
            }
        }

        switch (serializeType) {
            case String -> {
                return new NBTTagString(value.toString());
            }
            case Ordinal -> {
                return new NBTTagInt(value.ordinal());
            }
            default -> throw new IllegalStateException("Unexpected value: " + serializeType);
        }
    }
}
