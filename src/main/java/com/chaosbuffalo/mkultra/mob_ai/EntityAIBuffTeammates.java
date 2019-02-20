package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.*;
import java.util.Collections;
import java.util.List;

public class EntityAIBuffTeammates extends EntityAISpellCastingBase {

    private float healThreshold;
    private double MAX_BUFF_RANGE = 20.0;


    public EntityAIBuffTeammates(EntityLivingBase entity, IMobData mobData, float healThreshold, int cooldown){
        super(entity, mobData, cooldown);
        this.healThreshold = healThreshold;
        setStrafeRange(.1f, .5f);
        desiredTargetType = Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean shouldExecute() {

        if (mobData.hasAbilities()){
            List<Entity> entities = getEntitiesInRange(MAX_BUFF_RANGE, false, Targeting.TargetType.FRIENDLY);
            if (entities.size() <= 0){
                return false;
            }
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()){
                if (!tracker.isAbilityOnCooldown()){
                    switch (tracker.getAbility().getAbilityType()){
                        case HEAL:
                            Entity min = Collections.min(entities, this::compareHealth);
                            if (min instanceof EntityLivingBase){
                                EntityLivingBase minLiv = (EntityLivingBase) min;
                                if (tracker.getAbility().getEffectPotion() != null){
                                    if (minLiv.getHealth() <= (minLiv.getMaxHealth() * healThreshold) &&
                                            !minLiv.isPotionActive(tracker.getAbility().getEffectPotion())){
                                        currentAbility = tracker;
                                        targetEntity = minLiv;
                                        return true;
                                    }
                                } else {
                                    if (minLiv.getHealth() <= (minLiv.getMaxHealth() * healThreshold)){
                                        currentAbility = tracker;
                                        targetEntity = minLiv;
                                        return true;
                                    }
                                }

                            }
                            break;
                        case BUFF:
                            min = Collections.min(entities, this::compareDistance);
                            if (min instanceof EntityLivingBase){
                                EntityLivingBase minLiv = (EntityLivingBase) min;
                                if (tracker.getAbility().getEffectPotion() != null){
                                    if (!minLiv.isPotionActive(tracker.getAbility().getEffectPotion())){
                                        currentAbility = tracker;
                                        targetEntity = minLiv;
                                        return true;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }



}
