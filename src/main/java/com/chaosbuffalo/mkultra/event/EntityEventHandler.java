package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.spawn.DefaultSpawnIndex;
import com.chaosbuffalo.mkultra.spawn.MobDefinition;
import com.chaosbuffalo.mkultra.spawn.SpawnList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class EntityEventHandler {

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onEntityJoinWorldEventServer(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
//            for (PotionEffect effect : ((EntityPlayer) event.getEntity()).getActivePotionEffects()) {
//                System.out.println(effect.getPotion().getRegistryName().toString());
//                if (effect.getPotion() instanceof SongPotionBase) {
//                    SongPotionBase songPotion = (SongPotionBase) effect.getPotion();
//                    if (songPotion.isHostSong) {
//                        ((EntityPlayer)event.getEntity()).removePotionEffect(songPotion);
//                    }
//                }
//            }
        } else if (event.getEntity() instanceof EntityLivingBase){
            handleMobJoinWorld(event);
        }
    }

    private static void handleMobJoinWorld(EntityJoinWorldEvent event) {
        EntityLivingBase entLiv = (EntityLivingBase) event.getEntity();
        MobData mobD = (MobData) MKUMobData.get(entLiv);
        World world = event.getWorld();
        if (mobD != null){
            if (mobD.isMKSpawned()) {
                entLiv.setDead();
            } else if (!mobD.getIsMKSpawning()) {
                ResourceLocation mobDefinition = mobD.getMobDefinition();
                MobDefinition definition = MKURegistry.getMobDefinition(mobDefinition);
                if (definition != MKURegistry.EMPTY_MOB){
//                    addAttackSpeed(entLiv);
                    definition.applyDefinition(world, entLiv, mobD.getMobLevel());
                } else {
                    // here we should randomly choose a mob definition appropriate for the entity type if available.
                    ResourceLocation entityId = EntityList.getKey(entLiv);
                    SpawnList spawnList = DefaultSpawnIndex.getSpawnListForEntity(entityId);
                    if (spawnList != null){
//                        addAttackSpeed(entLiv);
                        MobDefinition def = spawnList.getNextDefinition();
                        mobD.setMobDefinition(def.getRegistryName());
                        mobD.setMobFaction(entityId);
                        mobD.setMobLevel(ModSpawn.levelChances.next());
                        def.applyDefinition(world, entLiv, mobD.getMobLevel());
                        entLiv.setHealth(entLiv.getMaxHealth());

                    }
                }
            }
        }
    }

    @Nullable
    private static EntityItem entityDropItem(ItemStack itemToDrop, float dropOffset, EntityLivingBase entity) {
        if (itemToDrop.isEmpty()) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(
                    entity.world, entity.posX,
                    entity.posY + (double)dropOffset,
                    entity.posZ, itemToDrop);
            entityitem.setDefaultPickupDelay();
            entity.world.spawnEntity(entityitem);
            return entityitem;
        }
    }

    @SubscribeEvent
//    @SideOnly(Side.SERVER)
    public static void onLootDropEvent(LivingDropsEvent event){
        IMobData mobData = MKUMobData.get(event.getEntityLiving());
        EntityLivingBase entity = event.getEntityLiving();
        if (mobData != null && mobData.hasAdditionalLootTable()){
            ResourceLocation lootLoc = mobData.getAdditionalLootTable();
            LootTable loottable = event.getEntityLiving().getEntityWorld().getLootTableManager()
                    .getLootTableFromLocation(lootLoc);
            LootContext.Builder builder = (new LootContext.Builder((WorldServer)entity.world)).withLootedEntity(entity)
                    .withDamageSource(event.getSource());
            if (event.getSource().getTrueSource() instanceof  EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                builder = builder.withPlayer(player).withLuck(player.getLuck());
            }
            for (ItemStack itemstack : loottable.generateLootForPools(entity.getRNG(), builder.build())) {
                entityDropItem(itemstack, 0.0F, entity);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onEntityJoinWorldEventClient(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayerSP) {
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
//            for (PotionEffect effect : ((EntityPlayer) event.getEntity()).getActivePotionEffects()) {
//                System.out.println(effect.getPotion().getRegistryName().toString());
//                if (effect.getPotion() instanceof SongPotionBase) {
//                    SongPotionBase songPotion = (SongPotionBase) effect.getPotion();
//                    if (songPotion.isHostSong) {
//                        ((EntityPlayer)event.getEntity()).removePotionEffect(songPotion);
//                    }
//                }
//            }
        // Run this on the server if we are single player.
        } else if (event.getEntity() instanceof EntityLivingBase && !event.getWorld().isRemote){
            handleMobJoinWorld(event);
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateTick(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            PlayerData playerData = (PlayerData) MKUPlayerData.get((EntityPlayer) e.getEntityLiving());
            if (playerData != null) {
                playerData.onTick();
            }
        } else {
            IMobData mobData = MKUMobData.get(e.getEntityLiving());
            if (mobData != null){
                mobData.onTick();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
        // I would hope this can't happen
        if (e.getEntityPlayer() == null)
            return;

        PlayerData newData = (PlayerData) MKUPlayerData.get(e.getEntityPlayer());
        if (newData == null)
            return;

        // Die on the original so we can clone properly and not need an immediate onTick packet
        if (e.isWasDeath() && !MKConfig.cheats.PEPSI_BLUE_MODE) {
            IPlayerData oldData = MKUPlayerData.get(e.getOriginal());
            if (oldData == null)
                return;
            ((PlayerData) oldData).doDeath();
        }

        newData.clone(e.getOriginal());
        newData.onRespawn();
    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer){
            event.addCapability(new ResourceLocation(MKUltra.MODID, "player_data"),
                    new PlayerDataProvider((EntityPlayer) event.getObject()));
        } else if (event.getObject() instanceof EntityLivingBase){
            event.addCapability(new ResourceLocation(MKUltra.MODID, "mob_data"),
                    new MobDataProvider((EntityLivingBase) event.getObject()));
        }
    }
}
