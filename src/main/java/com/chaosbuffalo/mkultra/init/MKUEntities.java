package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mknpc.MKNpc;
import com.chaosbuffalo.mknpc.entity.MKSkeletonEntity;
import com.chaosbuffalo.mknpc.entity.MKZombifiedPiglinEntity;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.orcs.OrcEntity;
import com.chaosbuffalo.mkultra.entities.projectiles.CleansingSeedProjectileEntity;
import com.chaosbuffalo.mkultra.entities.projectiles.FireballProjectileEntity;
import com.chaosbuffalo.mkultra.entities.projectiles.SpiritBombProjectileEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUEntities {

    public static final String HYBOREAN_SKELETON_NAME = "hyborean_skeleton";
    public static EntityType<MKSkeletonEntity> HYBOREAN_SKELETON_TYPE;

    public static final String ORC_NAME = "orc";
    public static EntityType<OrcEntity> ORC_TYPE;

    public static final String ZOMBIFIED_PIGLIN_NAME = "zombified_piglin";
    public static EntityType<MKZombifiedPiglinEntity> ZOMBIFIED_PIGLIN_TYPE;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        evt.getRegistry().register(EntityType.Builder.<CleansingSeedProjectileEntity>create(
                CleansingSeedProjectileEntity::new, EntityClassification.MISC)
                .immuneToFire()
                .size(0.25f, 0.25f)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .disableSerialization()
                .build("cleansing_seed_projectile")
                .setRegistryName(new ResourceLocation(MKUltra.MODID, "cleansing_seed_projectile")));

        evt.getRegistry().register(EntityType.Builder.<SpiritBombProjectileEntity>create(
                SpiritBombProjectileEntity::new, EntityClassification.MISC)
                .immuneToFire()
                .size(0.15f, 0.15f)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .disableSerialization()
                .build("spirit_bomb_projectile")
                .setRegistryName(new ResourceLocation(MKUltra.MODID, "spirit_bomb_projectile")));

        evt.getRegistry().register(EntityType.Builder.<FireballProjectileEntity>create(
                FireballProjectileEntity::new, EntityClassification.MISC)
                .immuneToFire()
                .size(0.25f, 0.25f)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .disableSerialization()
                .build("fireball_projectile")
                .setRegistryName(new ResourceLocation(MKUltra.MODID, "fireball_projectile")));

        EntityType<MKSkeletonEntity> hyborean_skeleton = EntityType.Builder.create(
                MKSkeletonEntity::new, EntityClassification.MONSTER)
                .size(EntityType.SKELETON.getWidth(), EntityType.SKELETON.getHeight())
                .build(new ResourceLocation(MKUltra.MODID, HYBOREAN_SKELETON_NAME).toString());
        hyborean_skeleton.setRegistryName(MKUltra.MODID, HYBOREAN_SKELETON_NAME);
        HYBOREAN_SKELETON_TYPE = hyborean_skeleton;
        evt.getRegistry().register(HYBOREAN_SKELETON_TYPE);

        EntityType<OrcEntity> orc = EntityType.Builder.create(
                OrcEntity::new, EntityClassification.CREATURE)
                .size(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight())
                .build(new ResourceLocation(MKUltra.MODID, ORC_NAME).toString());
        orc.setRegistryName(MKUltra.MODID, ORC_NAME);
        ORC_TYPE = orc;
        evt.getRegistry().register(ORC_TYPE);

        EntityType<MKZombifiedPiglinEntity> zombiePiglin = EntityType.Builder.create(
                MKZombifiedPiglinEntity::new, EntityClassification.MONSTER)
                .size(EntityType.ZOMBIFIED_PIGLIN.getWidth(), EntityType.ZOMBIFIED_PIGLIN.getHeight())
                .build(new ResourceLocation(MKNpc.MODID, ZOMBIFIED_PIGLIN_NAME).toString());
        zombiePiglin.setRegistryName(MKNpc.MODID, ZOMBIFIED_PIGLIN_NAME);
        ZOMBIFIED_PIGLIN_TYPE = zombiePiglin;
        evt.getRegistry().register(ZOMBIFIED_PIGLIN_TYPE);

    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event){
        event.put(HYBOREAN_SKELETON_TYPE, MKSkeletonEntity.registerAttributes(1.0, 0.3)
                .createMutableAttribute(Attributes.ARMOR, 5.0).create());
        event.put(ORC_TYPE, OrcEntity.registerAttributes(2.0, 0.35).create());
        event.put(ZOMBIFIED_PIGLIN_TYPE, MKZombifiedPiglinEntity.registerAttributes(2.0, 0.25).create());
    }
}
