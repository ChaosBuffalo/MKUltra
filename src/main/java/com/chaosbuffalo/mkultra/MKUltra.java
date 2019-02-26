package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.command.MKCommand;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.init.ModTileEntities;
import com.chaosbuffalo.mkultra.item.MKUltraTab;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.PacketHandler;
import com.chaosbuffalo.mkultra.network.packets.client.*;
import com.chaosbuffalo.mkultra.network.packets.server.*;
import com.chaosbuffalo.mkultra.party.PartyCommand;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingAPI;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;


@Mod(modid = MKUltra.MODID, name= MKUltra.MODNAME, version = MKUltra.VERSION,
        dependencies="required-after:targeting_api;")
public class MKUltra {
    public static final String MODID = "mkultra";
    public static final String VERSION = "@VERSION@";
    public static final String MODNAME = "MKUltra";

    public static final CreativeTabs MKULTRA_TAB = new MKUltraTab(CreativeTabs.getNextID(), MODID + ".general");

    @Mod.Instance
    public static MKUltra INSTANCE = new MKUltra();

    @SidedProxy(clientSide = "com.chaosbuffalo.mkultra.ClientProxy",
            serverSide = "com.chaosbuffalo.mkultra.ServerProxy")
    public static CommonProxy proxy;

    public static PacketHandler packetHandler;
    public static Logger LOG;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        LOG = e.getModLog();
        MKConfig.init(e.getSuggestedConfigurationFile());
        ModTileEntities.registerTileEntities();
        ModItems.initItems();
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData.class);
        CapabilityManager.INSTANCE.register(IMobData.class, new MobDataStorage(), MobData.class);
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        TargetingExtensions.init();
        ArmorClass.registerDefaults();
        NetworkRegistry.INSTANCE.registerGuiHandler(MKUltra.INSTANCE, new ModGuiHandler());
        MKUltra.packetHandler = new PacketHandler(MKUltra.MODID);
        PacketHandler packetHandler = MKUltra.packetHandler;

        packetHandler.registerPacket(PlayerSyncRequestPacket.class, new PlayerSyncRequestPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(ClassLearnPacket.class, new ClassLearnPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(LevelAbilityPacket.class, new LevelAbilityPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(ExecuteActivePacket.class, new ExecuteActivePacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(LevelUpRequestPacket.class, new LevelUpRequestPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(PartyInviteResponsePacket.class, new PartyInviteResponsePacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(MKSpawnerSetPacket.class, new MKSpawnerSetPacket.Handler(), Side.SERVER);

        packetHandler.registerPacket(ParticleEffectSpawnPacket.class, new ParticleEffectSpawnPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(AbilityUpdatePacket.class, new AbilityUpdatePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(ClassUpdatePacket.class, new ClassUpdatePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(PartyInvitePacket.class, new PartyInvitePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(ForceOpenClientGUIPacket.class, new ForceOpenClientGUIPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(AbilityCooldownPacket.class, new AbilityCooldownPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(CritMessagePacket.class, new CritMessagePacket.Handler(), Side.CLIENT);
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        MKConfig.registerArmors();
        GameRegistry.findRegistry(Block.class).getKeys().forEach(key -> {
            if (key.getPath().toLowerCase().contains("fire")){
                EnvironmentUtils.addFireBlock(
                        GameRegistry.findRegistry(Block.class).getValue(key)
                );
                Log.info(String.format("Registering fire: %s", key.toString()));
            }
        });
        proxy.postInit(e);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new PartyCommand());
        event.registerServerCommand(new MKCommand());
    }
}
