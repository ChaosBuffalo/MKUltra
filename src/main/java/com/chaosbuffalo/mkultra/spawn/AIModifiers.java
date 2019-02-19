package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.log.Log;
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
            HashSet<Class<? extends EntityAIBase>> targetActions = new HashSet<>();
            for (BehaviorChoice choice : modifier.behaviorChoices){
                if (modifier.level >= choice.minLevel){
                    switch (choice.getTaskType()){
                        case TASK:
                            actions.add(choice.getAIClass());
                            break;
                        case TARGET_TASK:
                            targetActions.add(choice.getAIClass());
                            break;
                    }
                }
            }
            if (actions.size() > 0){
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
            if (targetActions.size() > 0){
                HashSet<EntityAITasks.EntityAITaskEntry> toRemove = new HashSet<>();
                for (EntityAITasks.EntityAITaskEntry task : entLiv.targetTasks.taskEntries){
                    if (targetActions.contains(task.action.getClass())){
                        toRemove.add(task);
                    }
                }
                for (EntityAITasks.EntityAITaskEntry entry : toRemove){
                    entLiv.targetTasks.removeTask(entry.action);
                }
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

    public static BiConsumer<EntityLivingBase, AIModifier> ADD_TASKS = (entity, modifier) -> {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving) entity;
            for (BehaviorChoice choice: modifier.behaviorChoices){
                if (modifier.level >= choice.minLevel){
                    EntityAIBase toAdd = choice.getTask(entLiv);
                    if (toAdd != null){
                        Log.info("Adding %s to entity %s", choice.getTaskType().name(), entity.toString());
                        switch (choice.getTaskType()){
                            case TARGET_TASK:
                                entLiv.targetTasks.addTask(choice.getTaskPriority(), toAdd);
                                break;
                            case TASK:
                                entLiv.tasks.addTask(choice.getTaskPriority(), toAdd);
                                break;
                        }

                    }
                }
            }
        }
    };


}
