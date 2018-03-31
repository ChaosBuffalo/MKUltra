package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.PoisonArrowPotion;
import com.chaosbuffalo.mkultra.entities.projectiles.SpellCastArrow;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoisonArrow extends BaseAbility {

    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public PoisonArrow() {
        super(MKUltra.MODID, "ability.poison_arrow");
    }

    @Override
    public String getAbilityName() {
        return "Poison Arrow";
    }

    @Override
    public String getAbilityDescription() {
        return "Launches an arrow that poisons an enemy, slowing and poisoning them." +
                "At level 2 the effects are spread to nearby enemies.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/poisonarrow.png");
    }


    @Override
    public String getAbilityType() {
        return "Projectile";
    }

    @Override
    public int getIconU() {
        return 108;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 10 - currentLevel * 2;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 8 - currentLevel * 2;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return  4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        ItemStack ammo = ItemHelper.findAmmo(entity);
        if (ammo.isEmpty() || !(entity.getHeldItemMainhand().getItem() instanceof ItemBow)) {
            return;
        }
        int level = pData.getLevelForAbility(getAbilityId());
        pData.startAbility(this);

        SpellCastArrow arrow = new SpellCastArrow(theWorld, entity);
        arrow.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrow.setDamage(BASE_DAMAGE + level * DAMAGE_SCALE);
        arrow.addEffect(new PotionEffect(MobEffects.POISON, 9 * 20, level, false, true));
        arrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 9 * 20, level + 2, false, true));
        if (level == 2){
            arrow.addSpellCast(PoisonArrowPotion.Create(entity, 10.0f), level);
        }
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        theWorld.spawnEntity(arrow);

        ItemHelper.shrinkStack(entity, ammo, 1);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SLIME.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}

