package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.FlameBlade;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameBladePotion extends SpellPotionBase {
    public static final FlameBladePotion INSTANCE = new FlameBladePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private FlameBladePotion() {
        super(false, 4393423);
        SpellPotionBase.register(MKUltra.MODID, "effect.flame_blade", this);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/flameblade.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }


    public void onAttackEntity(EntityPlayer player, Entity target, PotionEffect potion) {
        SpellCast flames = FlameBladeEffectPotion.Create(player, FlameBlade.BASE_DAMAGE, FlameBlade.DAMAGE_SCALE);
        SpellCast particles = ParticlePotion.Create(player,
                EnumParticleTypes.LAVA.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 15, 5, 1.0);

        AreaEffectBuilder.Create(player, target)
                .spellCast(flames, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                .spellCast(particles, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                .instant()
                .color(16737305).radius(2.0f, true)
                .particle(EnumParticleTypes.LAVA)
                .spawn();
    }
}
