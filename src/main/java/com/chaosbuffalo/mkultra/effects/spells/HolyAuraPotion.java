package com.chaosbuffalo.mkultra.effects.spells;


import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.passives.AuraPassiveBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class HolyAuraPotion extends AuraPassiveBase {
    public static final HolyAuraPotion INSTANCE = new HolyAuraPotion();

    private final static int TEAM_AURA_DURATION = 40 * GameConstants.TICKS_PER_SECOND;
    private final static int APPLICATION_PERIOD = 10 * GameConstants.TICKS_PER_SECOND;

    protected HolyAuraPotion() {
        super(APPLICATION_PERIOD);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    @Override
    public AreaEffectBuilder prepareAreaEffect(EntityPlayer source, IPlayerData playerData, int level,
                                               AreaEffectBuilder builder) {
        builder.spellCast(HolyAuraTeamPotion.Create(source), TEAM_AURA_DURATION, 1,
                Targeting.TargetType.FRIENDLY);
        return builder;
    }

    @Override
    public float getAuraDistance(int level) {
        return 30.0f;
    }

    public float getNegativeDistance(int level){
        return 5.0f;
    }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        super.doEffect(source, indirectSource, target, amplifier, cast);
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            AreaEffectBuilder builder = AreaEffectBuilder.Create(player, player)
                    .instant()
                    .disableParticle()
                    .spellCast(AbilityMagicDamage.Create(player, 4.0f, 4.0f),
                            1, Targeting.TargetType.ENEMY)
                    .radius(getNegativeDistance(amplifier), true);
            builder.spawn();
        }
    }
}