package com.chaosbuffalo.mkultra.entities.orcs;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mknpc.entity.MKEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;


public class OrcEntity extends MKEntity {

    public OrcEntity(EntityType<? extends OrcEntity> type, World worldIn) {
        super(type, worldIn);
        if (!worldIn.isRemote()){
            setComboDefaults(6, GameConstants.TICKS_PER_SECOND);
            setAttackComboCount(6);
            setAttackComboCooldown(GameConstants.TICKS_PER_SECOND);
        }
        setLungeSpeed(0.75);
    }


    public static AttributeModifierMap.MutableAttribute registerAttributes(double attackDamage, double movementSpeed) {
        return MKEntity.registerAttributes(attackDamage, movementSpeed)
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0)
                .createMutableAttribute(Attributes.ARMOR, 4);
    }

}
