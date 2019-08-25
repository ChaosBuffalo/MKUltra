package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.PoisonArrowPotion;
import com.chaosbuffalo.mkultra.entities.projectiles.SpellCastArrow;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoisonArrow extends PlayerAbility {
    public static float BASE_ARROW_DAMAGE = 4.0f;
    public static float SCALE_ARROW_DAMAGE = 2.0f;
    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public PoisonArrow() {
        super(MKUltra.MODID, "ability.poison_arrow");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 10 - currentRank * 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 8 - currentRank * 2;
    }


    @Override
    public int getRequiredLevel(int currentRank) {
        return  4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        ItemStack ammo = ItemHelper.findAmmo(entity);
        if (ammo.isEmpty() || !(entity.getHeldItemMainhand().getItem() instanceof ItemBow)) {
            return;
        }
        int level = pData.getAbilityRank(getAbilityId());
        pData.startAbility(this);
        ItemArrow itemarrow = (ItemArrow) (ammo.getItem() instanceof ItemArrow ? ammo.getItem() : Items.ARROW);
        EntityArrow entityarrow = itemarrow.createArrow(theWorld, ammo, entity);
        SpellCastArrow arrow = new SpellCastArrow(theWorld, entity);
        arrow.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 3.0F, 1.0F);
        arrow.setDamage(entityarrow.getDamage() + BASE_ARROW_DAMAGE + level * SCALE_ARROW_DAMAGE);
        arrow.addEffect(new PotionEffect(MobEffects.POISON, 9 * GameConstants.TICKS_PER_SECOND, level, false, true));
        arrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 9 * GameConstants.TICKS_PER_SECOND, level + 2, false, true));
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

