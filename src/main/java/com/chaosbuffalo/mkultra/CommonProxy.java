package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.init.ModEntities;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataStorage;
import com.chaosbuffalo.mkultra.integration.Integrations;
import com.chaosbuffalo.mkultra.core.ArmorClass;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.item.FireExtinguisherFlask;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.PacketHandler;
import com.chaosbuffalo.mkultra.network.packets.client.*;
import com.chaosbuffalo.mkultra.network.packets.server.*;
import com.chaosbuffalo.mkultra.tiles.ModTileEntities;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import net.minecraft.block.Block;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        Log.info("pre init");
        ModTileEntities.registerTileEntities();
        ModEntities.registerEntities();

        ModItems.initItems();

        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), PlayerData.class);
    }

    public void init(FMLInitializationEvent e) {
        Log.info("init");
        ArmorClass.registerDefaults();
        Integrations.setup();
        NetworkRegistry.INSTANCE.registerGuiHandler(MKUltra.INSTANCE, new ModGuiHandler());
        MKUltra.packetHandler = new PacketHandler(MKUltra.MODID);
        PacketHandler packetHandler = MKUltra.packetHandler;

        packetHandler.registerPacket(PlayerSyncRequestPacket.class, new PlayerSyncRequestPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(RangeSwordAttackPacket.class, new RangeSwordAttackPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(ClassLearnPacket.class, new ClassLearnPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(LevelAbilityPacket.class, new LevelAbilityPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(ExecuteActivePacket.class, new ExecuteActivePacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(LevelUpRequestPacket.class, new LevelUpRequestPacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(PartyInviteResponsePacket.class, new PartyInviteResponsePacket.Handler(), Side.SERVER);
        packetHandler.registerPacket(ResurrectPlayerPacket.class, new ResurrectPlayerPacket.Handler(), Side.SERVER);

        packetHandler.registerPacket(ParticleEffectSpawnPacket.class, new ParticleEffectSpawnPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(AbilityUpdatePacket.class, new AbilityUpdatePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(ClassUpdatePacket.class, new ClassUpdatePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(PartyInvitePacket.class, new PartyInvitePacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(ForceOpenClientGUIPacket.class, new ForceOpenClientGUIPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(AbilityCooldownPacket.class, new AbilityCooldownPacket.Handler(), Side.CLIENT);
        packetHandler.registerPacket(CritMessagePacket.class, new CritMessagePacket.Handler(), Side.CLIENT);
    }

    public void postInit(FMLPostInitializationEvent e) {
        GameRegistry.findRegistry(Block.class).getKeys().forEach( key -> {
            if (key.getResourcePath().toString().toLowerCase().contains("fire")){
                EnvironmentUtils.addFireBlock(
                        GameRegistry.findRegistry(Block.class).getValue(key)
                );
                Log.info(String.format("Registering fire: %s", key.toString()));
            }
        });
        Log.info("post init");
    }
}