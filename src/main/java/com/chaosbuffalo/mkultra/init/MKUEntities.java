package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.CleansingSeedProjectileEntity;
import com.chaosbuffalo.mkultra.entities.projectiles.SpiritBombProjectileEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUEntities {

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
    }
}
