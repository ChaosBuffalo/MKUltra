package com.chaosbuffalo.mkultra.tiles;
import com.chaosbuffalo.mkultra.spawner.MobDefinition;
import com.chaosbuffalo.mkultra.utils.SpawnerUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;


public class TileEntityMKSpawner extends TileEntity {


    public void update()
    {

    }

    private EntityLivingBase getEntity(World theWorld, MobDefinition definition) {
        if (definition.entityClass != null){
            try {
                Constructor<?> ctor = definition.entityClass.getConstructor(World.class);
                Object object = ctor.newInstance(theWorld);
                return (EntityLivingBase)object;
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void spawnEntity(World theWorld, String mobDefinitionName){
        MobDefinition definition = SpawnerUtils.getDefinition(mobDefinitionName);
        if (definition != SpawnerUtils.EMPTY_MOB){
            EntityLivingBase entity = getEntity(theWorld, definition);
            if (entity == null){
                return;
            }
            definition.applyStats(entity);
            entity.setLocationAndAngles(
                    getPos().getX(), getPos().getY() + 0.25f, getPos().getZ(),
                    theWorld.rand.nextFloat() * 360.0F, 0.0F);

            // Testing remove AI
            if (entity instanceof EntityLiving){
                EntityLiving entLiv = (EntityLiving) entity;
                HashSet<EntityAITasks.EntityAITaskEntry> toRemove = new HashSet<>();
                for (EntityAITasks.EntityAITaskEntry task : entLiv.tasks.taskEntries){
                    if (task.action instanceof EntityAIWanderAvoidWater){
                        toRemove.add(task);
                    }
                }
                for (EntityAITasks.EntityAITaskEntry entry : toRemove){
                    entLiv.tasks.removeTask(entry.action);
                }
            }
            theWorld.spawnEntity(entity);
        }
    }
}
