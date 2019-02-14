package com.chaosbuffalo.mkultra.spawner;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import javax.annotation.Nullable;
import java.util.function.BiFunction;


public class BehaviorChoice {
    public final int minLevel;
    private Class<? extends EntityAIBase> aiClass;
    private BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> aiProvider;
    private int taskPriority;
    public enum TaskType {
        TARGET_TASK,
        TASK
    }
    private TaskType taskType;

    public BehaviorChoice(Class<? extends EntityAIBase> aiClass, int minLevel, TaskType type){
        this.aiClass = aiClass;
        this.minLevel = minLevel;
        this.taskType = type;
    }

    public BehaviorChoice(BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> provider, int minLevel,
                          int taskPriority, TaskType type){
        this.minLevel = minLevel;
        this.aiProvider = provider;
        this.taskPriority = taskPriority;
        this.taskType = type;
    }

    public TaskType getTaskType(){
        return taskType;
    }

    @Nullable
    public Class<? extends EntityAIBase> getAIClass(){
        return aiClass;
    }

    public int getTaskPriority(){
        return taskPriority;
    }

    @Nullable
    public EntityAIBase getTask(EntityLiving entity){
        if (aiProvider == null) {
            return null;
        } else {
            return aiProvider.apply(entity, this);
        }
    }
}
