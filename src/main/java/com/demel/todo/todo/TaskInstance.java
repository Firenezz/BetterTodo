package com.demel.todo.todo;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.enums.EnumLogic;
import betterquesting.api.enums.EnumQuestVisibility;
import betterquesting.api.properties.IPropertyType;
import betterquesting.api.utils.BigItemStack;
import betterquesting.storage.PropertyContainer;
import com.demel.todo.api.enums.TaskState;
import com.demel.todo.api.properties.NativeProps;
import com.demel.todo.api.properties.TaskProps;
import com.demel.todo.api.todo.ITask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TaskInstance implements ITask {

    private final PropertyContainer qInfo = new PropertyContainer();

    public TaskInstance() {
        this.setupProps();
    }

    private void setupProps() {
        setupValue(TaskProps.NAME, "New Task");
        setupValue(TaskProps.DESC, "No Description");

        setupValue(TaskProps.STATE);
    }

    private <T> void setupValue(IPropertyType<T> prop) { this.setupValue(prop, prop.getDefault()); }

    private <T> void setupValue(IPropertyType<T> prop, T def) {
        qInfo.setProperty(prop, qInfo.getProperty(prop, def));
    }

    @Override
    public TaskState getState(EntityPlayer player) {
        return null;
    }

    @Override
    public void update(EntityPlayer player) {
        UUID playerID = QuestingAPI.getQuestingUUID(player);


    }

    @Override
    public boolean is(UUID uuid, TaskState state) {
        return false;
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop) {
        return qInfo.getProperty(prop);
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop, T def) {
        return qInfo.getProperty(prop, def);
    }

    @Override
    public boolean hasProperty(IPropertyType<?> prop) {
        return qInfo.hasProperty(prop);
    }

    @Override
    public <T> void setProperty(IPropertyType<T> prop, T value) {
        qInfo.setProperty(prop, value);
    }

    @Override
    public void removeProperty(IPropertyType<?> prop) {
        qInfo.removeProperty(prop);
    }

    @Override
    public void removeAllProps() {
        qInfo.removeAllProps();
    }

    @Override
    public NBTTagCompound writeProgressToNBT(NBTTagCompound nbt, @Nullable List<UUID> users) {
        return null;
    }

    @Override
    public void readProgressFromNBT(NBTTagCompound nbt, boolean merge) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

    }
}
