package com.chaosbuffalo.mkultra.spawner;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;

import java.util.function.BiConsumer;
import static com.chaosbuffalo.mkultra.utils.MathUtils.lerp_double;

public class BaseSpawnAttributes {

    public static BiConsumer<EntityLivingBase, AttributeRange> MAX_HEALTH = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.MAX_HEALTH, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> FOLLOW_RANGE = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.FOLLOW_RANGE, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> ARMOR = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ARMOR, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> ARMOR_TOUGHNESS = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ARMOR_TOUGHNESS, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> ATTACK_DAMAGE = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ATTACK_DAMAGE, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> ATTACK_SPEED = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.ATTACK_SPEED, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> KNOCKBACK_RESISTANCE = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, range);
    };

    public static BiConsumer<EntityLivingBase, AttributeRange> MOVEMENT_SPEED = (entity, range) -> {
        setEntityAttributeBaseValueFromRange(entity, SharedMonsterAttributes.MOVEMENT_SPEED, range);
    };

    public static void setEntityAttributeBaseValueFromRange(EntityLivingBase entity,
                                                            IAttribute attr,
                                                            AttributeRange range)
    {
        entity.getEntityAttribute(attr).setBaseValue(
                lerp_double(range.start, range.stop, range.level, range.maxLevel));
    }

}
