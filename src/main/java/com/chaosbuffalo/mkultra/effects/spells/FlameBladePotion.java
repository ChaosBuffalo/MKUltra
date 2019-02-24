package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.FlameBlade;
import com.chaosbuffalo.mkultra.effects.*;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityFlameBladeProjectile;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameBladePotion extends PassiveEffect {
    public static final FlameBladePotion INSTANCE = new FlameBladePotion();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private FlameBladePotion() {
        super(false, 4393423);
        setPotionName("effect.flame_blade");
        SpellTriggers.ATTACK_ENTITY.register(this, this::onAttackEntity);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/flame_blade.png");
    }

    private void onAttackEntity(EntityLivingBase player, Entity target, PotionEffect potion) {
        SpellCast flames = FlameBladeEffectPotion.Create(player, FlameBlade.BASE_DAMAGE, FlameBlade.DAMAGE_SCALE);
        SpellCast particles = ParticlePotion.Create(player,
                EnumParticleTypes.LAVA.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 4, 4, 1.0);

        AreaEffectBuilder.Create(player, target)
                .spellCast(flames, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                .spellCast(particles, potion.getAmplifier(), Targeting.TargetType.ENEMY)
                .instant()
                .color(16737305).radius(1.0f, true)
                .particle(EnumParticleTypes.LAVA)
                .spawn();
        if (player.isPotionActive(PhoenixAspectPotion.INSTANCE))
        {
            World world = player.getEntityWorld();
            EntityFlameBladeProjectile flamep = new EntityFlameBladeProjectile(world, player);
            flamep.setAmplifier(potion.getAmplifier());
            flamep.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, FlameBlade.PROJECTILE_SPEED,
                    FlameBlade.PROJECTILE_INACCURACY);
            world.spawnEntity(flamep);
        }
    }
}
