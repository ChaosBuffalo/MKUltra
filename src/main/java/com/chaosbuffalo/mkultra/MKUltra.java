package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.command.MKCommand;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.init.*;
import com.chaosbuffalo.mkultra.item.MKUltraTab;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.PacketHandler;
import com.chaosbuffalo.mkultra.party.PartyCommand;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;


@Mod(modid = MKUltra.MODID, name = MKUltra.MODNAME, version = MKUltra.VERSION,
        dependencies = "required-after:targeting_api;")
public class MKUltra {
    public static final String MODID = "mkultra";
    public static final String VERSION = "@VERSION@";
    public static final String MODNAME = "MKUltra";
    public static File config_loc;

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
        config_loc = e.getModConfigurationDirectory();
        MKConfig.init(e.getSuggestedConfigurationFile());
        MKConfig.setMaxHealthMax();
        RangedAttribute attackDamage = (RangedAttribute) SharedMonsterAttributes.ATTACK_DAMAGE;
        attackDamage.setShouldWatch(true);
        ModTileEntities.registerTileEntities();
        ModItems.initItems();
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData.class);
        CapabilityManager.INSTANCE.register(IMobData.class, new MobDataStorage(), MobData.class);
        proxy.preInit(e);

    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        TargetingExtensions.init();
        ModLootTables.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(MKUltra.INSTANCE, new ModGuiHandler());
        MKUltra.packetHandler = new PacketHandler(MKUltra.MODID);
        MKUltra.packetHandler.registerPackets();
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        MKConfig.registerArmors();
        MKConfig.setupAttackSpeedWhitelist();
        GameRegistry.findRegistry(Block.class).getKeys().forEach(key -> {
            if (key.getPath().toLowerCase().contains("fire")) {
                EnvironmentUtils.addFireBlock(
                        GameRegistry.findRegistry(Block.class).getValue(key)
                );
                Log.info(String.format("Registering fire: %s", key.toString()));
            }
        });
        ModSpawn.postInitJsonRegisistation();
        ModTalents.postInitJsonRegisistation();
        ClassLists.initFromConfig();
        proxy.postInit(e);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new PartyCommand());
        event.registerServerCommand(new MKCommand());
    }
}
