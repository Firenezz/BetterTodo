package chestlib.api.versionning;

import net.minecraft.nbt.NBTTagCompound;

import chestlib.api.storage.INBTSaveLoad;

public class Version implements INBTSaveLoad<NBTTagCompound> {

    byte major = 0;
    byte minor = 0;
    byte patch = 0;

    String display() {
        return major + "." + minor + "." + patch;
    }

    public Version(int major) {
        this(major, 0, 0);
    }

    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public Version(int major, int minor, int patch) {
        this((byte) major, (byte) minor, (byte) patch);
    }

    public Version(byte major, byte minor, byte patch) {

        this.major = major;
        this.minor = minor;
        this.patch = patch;

    }

    public Version(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("major")) major = nbt.getByte("major");
        if (nbt.hasKey("minor")) minor = nbt.getByte("minor");
        if (nbt.hasKey("patch")) patch = nbt.getByte("patch");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (major > 0) nbt.setByte("major", major);
        if (minor > 0) nbt.setByte("minor", minor);
        if (patch > 0) nbt.setByte("patch", patch);

        return nbt;
    }
}
