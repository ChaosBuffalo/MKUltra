package com.chaosbuffalo.mkultra.spawn;


import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.math.AxisAlignedBB;

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

    public static BiConsumer<EntityLivingBase, AttributeRange> SCALE_SIZE = (entity, range) -> {
        // This one doesnt actually work cause you gotta change the gl rendering to scale the model.
        float scale = (float)lerp_double(range.start, range.stop, range.level, range.maxLevel);
        Log.info("Scaling mob to %f, %f", entity.width*scale, entity.height * scale);
        setEntitySize(entity,entity.width * scale, entity.height * scale);
    };

    public static void setEntitySize(Entity entity, float width, float height)
    {
        if (width != entity.width || height != entity.height)
        {
            float f = entity.width;
            entity.width = width;
            entity.height = height;
            if (entity.width < f)
            {
                double d0 = (double)width / 2.0D;
                entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0,
                        entity.posX + d0, entity.posY + (double)entity.height, entity.posZ + d0));
                return;
            }
            AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
            entity.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ,
                    axisalignedbb.minX + (double)entity.width,
                    axisalignedbb.minY + (double)entity.height,
                    axisalignedbb.minZ + (double)entity.width));
        }
    }

    public static void setEntityAttributeBaseValueFromRange(EntityLivingBase entity,
                                                            IAttribute attr,
                                                            AttributeRange range)
    {
        entity.getEntityAttribute(attr).setBaseValue(
                lerp_double(range.start, range.stop, range.level, range.maxLevel));
    }

}
