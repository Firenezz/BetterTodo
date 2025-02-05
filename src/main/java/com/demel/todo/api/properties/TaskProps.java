package com.demel.todo.api.properties;

import betterquesting.api.properties.IPropertyType;
import betterquesting.api.properties.basic.PropertyTypeEnum;
import betterquesting.api.properties.basic.PropertyTypeInteger;
import betterquesting.api.properties.basic.PropertyTypeString;
import com.demel.todo.api.enums.TaskState;
import net.minecraft.util.ResourceLocation;

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
        TaskState.New);

    public static final IPropertyType<long> STATE = new PropertyTypeInteger(
        new ResourceLocation("todolist:state"),
        TaskState.New);
}
