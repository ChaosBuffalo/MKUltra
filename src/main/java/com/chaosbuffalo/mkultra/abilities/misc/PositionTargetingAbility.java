package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.utils.TargetUtil;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;

import java.util.Set;

public abstract class PositionTargetingAbility extends MKAbility {

    public PositionTargetingAbility() {
        super();
    }

    public abstract void castAtPosition(LivingEntity castingEntity, Vec3 position);

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        context.getMemory(MKAbilityMemories.ABILITY_POSITION_TARGET)
                .flatMap(TargetUtil.LivingOrPosition::getPosition).ifPresent(x -> castAtPosition(castingEntity, x));
    }

    // FIXME: when we get the multiproject working lets make some of these calls less protected for stuff like this
    public Component exposeAbilityDescription(IMKEntityData casterData) {
        return getAbilityDescription(casterData);
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.POSITION_INCLUDE_ENTITIES;
    }
}
