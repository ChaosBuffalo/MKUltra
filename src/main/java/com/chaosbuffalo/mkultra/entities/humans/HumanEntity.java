package com.chaosbuffalo.mkultra.entities.humans;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.training.EntityAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainingEntity;
import com.chaosbuffalo.mknpc.entity.MKEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;

public class HumanEntity extends MKEntity implements IAbilityTrainingEntity {
    private final EntityAbilityTrainer abilityTrainer;

    public HumanEntity(EntityType<? extends HumanEntity> type, World worldIn) {
        super(type, worldIn);
        abilityTrainer = new EntityAbilityTrainer(this);
        if (!worldIn.isRemote()){
            setAttackComboStatsAndDefault(6, GameConstants.TICKS_PER_SECOND);
        }
        setLungeSpeed(0.75);
    }


    public static AttributeModifierMap.MutableAttribute registerAttributes(double attackDamage, double movementSpeed) {
        return MKEntity.registerAttributes(attackDamage, movementSpeed)
                .createMutableAttribute(Attributes.MAX_HEALTH, 100.0);
    }

    @Override
    public IAbilityTrainer getAbilityTrainer() {
        return abilityTrainer;
    }
}