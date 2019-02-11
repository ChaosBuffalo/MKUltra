package com.chaosbuffalo.mkultra.spawner;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.utils.SpawnerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;

import static com.chaosbuffalo.mkultra.utils.MathUtils.lerp_double;

public class BaseSpawnAttributes {
    
    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> MAX_HEALTH = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.MAX_HEALTH, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> ARMOR = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ARMOR, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> ARMOR_TOUGHNESS = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ARMOR_TOUGHNESS, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> ATTACK_DAMAGE = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ATTACK_DAMAGE, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> ATTACK_SPEED = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ATTACK_SPEED, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> KNOCKBACK_RESISTANCE = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, range);
        return Boolean.TRUE;
    };

    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> MOVEMENT_SPEED = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.MOVEMENT_SPEED, range);
        return Boolean.TRUE;
    };

    public static void setEntityAttributeBaseValueFromRange(EntityLivingBase entity,
                                                            IAttribute attr,
                                                            AttributeRange range)
    {
        entity.getEntityAttribute(attr).setBaseValue(
                lerp_double(range.start, range.stop, range.level, range.maxLevel));
    }

    public static void setup(){
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"max_health"), MAX_HEALTH);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"armor"), ARMOR);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"armor_toughness"), ARMOR_TOUGHNESS);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"attack_damage"), ATTACK_DAMAGE);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"attack_speed"), ATTACK_SPEED);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"knockback_resistance"), KNOCKBACK_RESISTANCE);
        SpawnerUtils.registerAttributeFunction(
                new ResourceLocation(MKUltra.MODID,"movement_speed"), MOVEMENT_SPEED);

    }
}
