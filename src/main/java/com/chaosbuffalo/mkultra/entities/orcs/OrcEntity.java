package com.chaosbuffalo.mkultra.entities.orcs;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.training.AbilityTrainingEntry;
import com.chaosbuffalo.mkcore.abilities.training.EntityAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainingEntity;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mknpc.entity.MKEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;

import java.util.List;


public class OrcEntity extends MKEntity implements IAbilityTrainingEntity {
    private final EntityAbilityTrainer abilityTrainer;

    public OrcEntity(EntityType<? extends OrcEntity> type, World worldIn) {
        super(type, worldIn);
        abilityTrainer = new EntityAbilityTrainer(this);
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

    @Override
    public IAbilityTrainer getAbilityTrainer() {
        return abilityTrainer;
    }
}
