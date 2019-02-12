package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;

import java.util.HashSet;
import java.util.function.BiConsumer;

public class AIModifiers {

    public static BiConsumer<EntityLivingBase, AIModifier> REMOVE_AI = (entity, modifier) -> {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving) entity;
            HashSet<Class<? extends EntityAIBase>> actions = new HashSet<>();
            for (BehaviorChoice choice : modifier.behaviorChoices){
                if (modifier.level >= choice.minLevel){
                    actions.add(choice.aiClass);
                }
            }
            HashSet<EntityAITasks.EntityAITaskEntry> toRemove = new HashSet<>();
            for (EntityAITasks.EntityAITaskEntry task : entLiv.tasks.taskEntries){
                if (actions.contains(task.action.getClass())){
                    toRemove.add(task);
                }
            }
            for (EntityAITasks.EntityAITaskEntry entry : toRemove){
                entLiv.tasks.removeTask(entry.action);
            }
        }
    };

    public static BiConsumer<EntityLivingBase, AIModifier> REMOVE_ALL_TASKS = (entity, modifier) -> {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving) entity;
            HashSet<EntityAITasks.EntityAITaskEntry> toRemove = new HashSet<>();
            for (EntityAITasks.EntityAITaskEntry task : entLiv.tasks.taskEntries){
                toRemove.add(task);
            }
            for (EntityAITasks.EntityAITaskEntry entry : toRemove){
                entLiv.tasks.removeTask(entry.action);
            }
        }
    };

    public static BiConsumer<EntityLivingBase, AIModifier> REMOVE_ALL_TARGET_TASKS = (entity, modifier) -> {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving) entity;
            HashSet<EntityAITasks.EntityAITaskEntry> toRemove = new HashSet<>();
            for (EntityAITasks.EntityAITaskEntry task : entLiv.targetTasks.taskEntries){
                toRemove.add(task);
            }
            for (EntityAITasks.EntityAITaskEntry entry : toRemove){
                entLiv.targetTasks.removeTask(entry.action);
            }
        }
    };

}
