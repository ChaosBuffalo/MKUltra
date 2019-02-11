package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.spawner.MobDefinition;
import com.chaosbuffalo.mkultra.spawner.StatRange;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import java.util.HashMap;
import java.util.function.BiFunction;

public class SpawnerUtils {

    public static final double find_double_increment(double start, double stop, int increment, int max_increments){
        double range = stop - start;
        double incrementVal = range / max_increments;
        return start + (incrementVal * increment);
    }

    public static final int MAX_LEVEL = 10;

    public static final MobDefinition EMPTY_MOB = new MobDefinition(null, 0);

    private static final HashMap<String, MobDefinition> MOB_DEFINITIONS = new HashMap<>();
    static {
        MOB_DEFINITIONS.put("empty_mob", EMPTY_MOB);
        BiFunction<EntityLivingBase, StatRange,Boolean> healthRange = (entity, range) -> {
          entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(
                  find_double_increment(range.start, range.stop,
                  entity.world.rand.nextInt(MAX_LEVEL), MAX_LEVEL));
          entity.heal(entity.getMaxHealth() - entity.getHealth());
          return Boolean.TRUE;
        };
        MOB_DEFINITIONS.put("test_skeleton", new MobDefinition(EntitySkeleton.class, 10,
                new StatRange(healthRange, 20.0, 200.0)));

    }

    public static MobDefinition getDefinition(String defintionName){
        return MOB_DEFINITIONS.getOrDefault(defintionName, EMPTY_MOB);
    }

}
