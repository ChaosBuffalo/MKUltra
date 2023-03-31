package com.chaosbuffalo.mkultra.entities.orcs;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.training.EntityAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainer;
import com.chaosbuffalo.mkcore.abilities.training.IAbilityTrainingEntity;
import com.chaosbuffalo.mknpc.entity.MKEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;



public class OrcEntity extends MKEntity implements IAbilityTrainingEntity {
    private final EntityAbilityTrainer abilityTrainer;

    public OrcEntity(EntityType<? extends OrcEntity> type, Level worldIn) {
        super(type, worldIn);
        abilityTrainer = new EntityAbilityTrainer(this);
        if (!worldIn.isClientSide()){
            setAttackComboStatsAndDefault(6, GameConstants.TICKS_PER_SECOND);
        }
        setLungeSpeed(0.75);
    }


    public static AttributeSupplier.Builder registerAttributes(double attackDamage, double movementSpeed) {
        return MKEntity.registerAttributes(attackDamage, movementSpeed)
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.ARMOR, 4);
    }

    @Override
    public IAbilityTrainer getAbilityTrainer() {
        return abilityTrainer;
    }
}
