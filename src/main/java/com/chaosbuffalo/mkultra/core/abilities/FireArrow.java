package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.FireArrowPotion;
import com.chaosbuffalo.mkultra.entities.projectiles.SpellCastArrow;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireArrow extends BaseAbility {

    public static float BASE_ARROW_DAMAGE = 4.0f;
    public static float SCALE_ARROW_DAMAGE = 2.0f;
    public static float BASE_DAMAGE = 1.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public FireArrow() {
        super(MKUltra.MODID, "ability.fire_arrow");
    }

    @Override
    public String getAbilityName() {
        return "Fire Arrow";
    }

    @Override
    public String getAbilityDescription() {
        return "Launches an arrow that ignites an enemy. If the enemy is already on fire, they explode.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/firearrow.png");
    }


    @Override
    public String getAbilityType() {
        return "Projectile";
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 5 - currentLevel;
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
        return 7 - currentLevel;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
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
        arrow.setDamage(BASE_ARROW_DAMAGE + level * SCALE_ARROW_DAMAGE);
        arrow.addSpellCast(FireArrowPotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE, 10.0f), 1);
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        theWorld.spawnEntity(arrow);

        ItemHelper.shrinkStack(entity, ammo, 1);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}


