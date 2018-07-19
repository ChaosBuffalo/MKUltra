package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectDamagePotion;
import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectMagicDamagePotion;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Jacob on 7/14/2018.
 */
public class MKDamageSource extends EntityDamageSourceIndirect {

    private static String ABILITY_DMG_TYPE = "mkUltraAbility";

    private final ResourceLocation abilityId;

    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    public MKDamageSource(ResourceLocation abilityId, String damageTypeIn,
                          Entity source, @Nullable Entity indirectEntityIn) {
        super(ABILITY_DMG_TYPE, source, indirectEntityIn);
        this.abilityId = abilityId;
    }

    public boolean isIndirectMagic() {
        return abilityId.equals(InstantIndirectMagicDamagePotion.INDIRECT_MAGIC_DMG_ABILITY_ID);
    }

    public boolean isMeleeAbility() {
        return abilityId.equals(InstantIndirectDamagePotion.INDIRECT_DMG_ABILITY_ID);
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation abilityId, Entity source,
                                                        @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource fromMeleeSkill(ResourceLocation abilityId, Entity source,
                                              @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn);
    }
}
