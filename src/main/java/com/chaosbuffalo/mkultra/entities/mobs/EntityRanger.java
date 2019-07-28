package com.chaosbuffalo.mkultra.entities.mobs;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityRanger extends EntityMobBase {

    public EntityRanger(World world) {
        super(world);
        setCustomNameTag("A Watchful Ranger");
    }

    @Override
    public void setupMKMobData(IMobData data){
        data.setMobLevel(6);
        data.setMobFaction(ModSpawn.NPC_FACTION_NAME);
        MobAbility heal = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.heal"));
        data.addAbility(heal);
        MobAbility fire_arrow = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.fire_arrow"));
        data.addAbility(fire_arrow);
        MobAbility warp_dash = MKURegistry.getMobAbility(new ResourceLocation(MKUltra.MODID,
                "mob_ability.warp_dash"));
        data.addAbility(warp_dash);
        data.setAggroRange(20.0);
    }
}
