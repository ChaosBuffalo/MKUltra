package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkcore.DataGenerators;
import com.chaosbuffalo.mkcore.core.talents.TalentLineDefinition;
import com.chaosbuffalo.mkcore.core.talents.TalentNode;
import com.chaosbuffalo.mkcore.core.talents.TalentTreeDefinition;
import com.chaosbuffalo.mkcore.core.talents.nodes.AttributeTalentNode;
import com.chaosbuffalo.mkcore.core.talents.nodes.EntitlementGrantTalentNode;
import com.chaosbuffalo.mkcore.init.CoreTalents;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUTalents;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

public class MKUTalentTreeProvider extends DataGenerators.TalentTreeDataGenerator {
    public MKUTalentTreeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        generateCoreTree(cache);
    }


    private void generateCoreTree(@Nonnull DirectoryCache cache){
        TalentTreeDefinition tree = new TalentTreeDefinition(new ResourceLocation(MKUltra.MODID, "core_talents"));
        tree.setVersion(2);
        tree.setDefault(true);
        TalentLineDefinition line = new TalentLineDefinition(tree, "tanking");
        line.addNode(new EntitlementGrantTalentNode(CoreTalents.ABILITY_SLOT_TALENT, UUID.fromString("119917ea-b852-4cb5-8bfe-2cdad488f279")));
        line.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 2, 5.0));
        line.addNode(new AttributeTalentNode(CoreTalents.ARMOR_TALENT, 2, 1.0));
        line.addNode(new EntitlementGrantTalentNode(CoreTalents.ABILITY_SLOT_TALENT, UUID.fromString("121817fa-1cfc-4334-aa77-13c02ede83ff")));
        line.addNode(new EntitlementGrantTalentNode(CoreTalents.POOL_COUNT_TALENT, UUID.fromString("108549d0-7935-4386-bf38-2ca48329305e")));
        line.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        line.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 3, 1.0));
        line.addNode(new EntitlementGrantTalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT, UUID.fromString("95725b31-da3a-4a3e-b6cc-e5036a6e9a87")));
        line.addNode(new TalentNode(MKUTalents.LIFE_SIPHON_TALENT));
        tree.addLine(line);
        TalentLineDefinition magic = new TalentLineDefinition(tree, "magic");
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.ABILITY_SLOT_TALENT, UUID.fromString("2e1ff629-b139-4303-831d-1c1bc5ebc21e")));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 2, 5.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 3, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.ULTIMATE_ABILITY_SLOT_TALENT, UUID.fromString("0c751a99-a186-439c-83f1-abb55f67b17e")));
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.POOL_COUNT_TALENT, UUID.fromString("9b23bee2-d159-4d32-aca8-1d726de0f875")));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT, UUID.fromString("4818f37e-16c4-4010-ab7a-a664cab4ab97")));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 3, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 2, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.COOLDOWN_REDUCTION_TALENT, 5, 0.01));
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.POOL_COUNT_TALENT, UUID.fromString("93de6f66-4d6d-4721-b774-b12ee92be288")));
        magic.addNode(new EntitlementGrantTalentNode(CoreTalents.ULTIMATE_ABILITY_SLOT_TALENT, UUID.fromString("ecfaa441-35c7-46ce-aa67-f8372bc4fd7d")));
        tree.addLine(magic);
        TalentLineDefinition heal = new TalentLineDefinition(tree, "healing");
        heal.addNode(new EntitlementGrantTalentNode(CoreTalents.ABILITY_SLOT_TALENT, UUID.fromString("3a31b74d-cf08-451f-a483-8eb9e47ce89b")));
        heal.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.5));
        heal.addNode(new AttributeTalentNode(CoreTalents.HEAL_BONUS_TALENT, 2, 2.0));
        heal.addNode(new EntitlementGrantTalentNode(CoreTalents.ABILITY_SLOT_TALENT, UUID.fromString("de5a37a4-b7e5-4565-9217-2d5d8de5d448")));
        heal.addNode(new EntitlementGrantTalentNode(CoreTalents.POOL_COUNT_TALENT, UUID.fromString("fbbab80a-c3f8-460f-81cf-5184a7c7f39a")));
        heal.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 3, 1.0));
        heal.addNode(new AttributeTalentNode(CoreTalents.HEAL_BONUS_TALENT, 1, 1.0));
        heal.addNode(new EntitlementGrantTalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT, UUID.fromString("05865420-0069-45e1-856e-331c9900f99c")));
        heal.addNode(new TalentNode(MKUTalents.SOUL_DRAIN_TALENT));
        tree.addLine(heal);
        writeDefinition(tree, cache);

    }

    @Override
    public String getName() {
        return "MKU Talent Trees";
    }
}
