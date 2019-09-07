package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMeleeDamage;
import com.chaosbuffalo.mkultra.effects.spells.HolyDamagePotion;
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
    private static String MOB_ABILITY_DAMAGE_TYPE = "mkUltraMobAbility";
    private static String MELEE_ABILITY_DMG_TYPE = "mkUltraMeleeAbility";

    private final ResourceLocation abilityId;

    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    public MKDamageSource(ResourceLocation abilityId, String damageTypeIn,
                          Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source, indirectEntityIn);
        this.abilityId = abilityId;
    }

    public boolean isIndirectMagic() {
        return abilityId.equals(AbilityMagicDamage.INDIRECT_MAGIC_DMG_ABILITY_ID);
    }

    public boolean isHolyDamage() {
        return abilityId.equals(HolyDamagePotion.HOLY_DMG_ABILITY_ID);
    }

    public boolean isMeleeAbility() {
        return abilityId.equals(AbilityMeleeDamage.INDIRECT_DMG_ABILITY_ID) || damageType.equals(MELEE_ABILITY_DMG_TYPE);
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation abilityId, Entity source,
                                                        @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, ABILITY_DMG_TYPE, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource causeIndirectMobMagicDamage(ResourceLocation abilityId, Entity source,
                                                           @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, MOB_ABILITY_DAMAGE_TYPE, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource fromMeleeSkill(ResourceLocation abilityId, Entity source,
                                              @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, MELEE_ABILITY_DMG_TYPE, source, indirectEntityIn);
    }
}
