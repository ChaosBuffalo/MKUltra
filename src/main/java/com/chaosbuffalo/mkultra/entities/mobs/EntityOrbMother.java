package com.chaosbuffalo.mkultra.entities.mobs;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import com.chaosbuffalo.mkultra.network.packets.OpenTalentGuiPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nullable;

public class EntityOrbMother extends EntityMobBase {

    public EntityOrbMother(World world) {
        super(world);
        setCustomNameTag("The Orb Mother");
    }

    @Override
    public void setupMKMobData(IMobData data){
        data.setMobLevel(6);
        data.setMobFaction(ModSpawn.NPC_FACTION_NAME);
        MobAbility heal = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.heal"));
        data.addAbility(heal);
        MobAbility fullHeal = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.full_heal"));
        data.addAbility(fullHeal);
        MobAbility graspingRoots = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.grasping_roots"));
        data.addAbility(graspingRoots);
    }

    @Nullable
    @Override
    public IMessage getInteractionPacket(EntityPlayer player, TileEntity spawner, BlockPos spawnPoint) {
        return new OpenTalentGuiPacket(spawnPoint);
    }
}
