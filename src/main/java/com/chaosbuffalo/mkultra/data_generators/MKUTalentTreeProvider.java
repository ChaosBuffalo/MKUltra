package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkcore.DataGenerators;
import com.chaosbuffalo.mkcore.core.talents.TalentLineDefinition;
import com.chaosbuffalo.mkcore.core.talents.TalentNode;
import com.chaosbuffalo.mkcore.core.talents.TalentTreeDefinition;
import com.chaosbuffalo.mkcore.core.talents.nodes.AttributeTalentNode;
import com.chaosbuffalo.mkcore.init.CoreTalents;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUTalents;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;

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
        tree.setVersion(1);
        tree.setDefault(true);
        TalentLineDefinition line = new TalentLineDefinition(tree, "tanking");
        line.addNode(new TalentNode(CoreTalents.ABILITY_SLOT_TALENT));
        line.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 2, 1.0));
        line.addNode(new AttributeTalentNode(CoreTalents.ARMOR_TALENT, 2, 1.0));
        line.addNode(new TalentNode(CoreTalents.ABILITY_SLOT_TALENT));
        line.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        line.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 3, 1.0));
        line.addNode(new AttributeTalentNode(CoreTalents.ATTACK_DAMAGE_TALENT, 1, 1.0));
        line.addNode(new TalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT));
        line.addNode(new TalentNode(MKUTalents.LIFE_SIPHON_TALENT));
        tree.addLine(line);
        TalentLineDefinition magic = new TalentLineDefinition(tree, "magic");
        magic.addNode(new TalentNode(CoreTalents.ABILITY_SLOT_TALENT));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 2, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 3, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new TalentNode(CoreTalents.ULTIMATE_ABILITY_SLOT_TALENT));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        magic.addNode(new TalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 3, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.MAX_HEALTH_TALENT, 2, 1.0));
        magic.addNode(new AttributeTalentNode(CoreTalents.COOLDOWN_REDUCTION_TALENT, 5, 0.01));
        magic.addNode(new TalentNode(CoreTalents.ULTIMATE_ABILITY_SLOT_TALENT));
        tree.addLine(magic);
        TalentLineDefinition heal = new TalentLineDefinition(tree, "healing");
        heal.addNode(new TalentNode(CoreTalents.ABILITY_SLOT_TALENT));
        heal.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 2, 1.0));
        heal.addNode(new AttributeTalentNode(CoreTalents.MANA_REGEN_TALENT, 2, 0.25));
        heal.addNode(new TalentNode(CoreTalents.ABILITY_SLOT_TALENT));
        heal.addNode(new AttributeTalentNode(CoreTalents.MAX_MANA_TALENT, 3, 1.0));
        heal.addNode(new AttributeTalentNode(CoreTalents.HEAL_BONUS_TALENT, 1, 1.0));
        heal.addNode(new TalentNode(CoreTalents.PASSIVE_ABILITY_SLOT_TALENT));
        heal.addNode(new TalentNode(MKUTalents.SOUL_DRAIN_TALENT));
        tree.addLine(heal);
        writeDefinition(tree, cache);

    }

    @Override
    public String getName() {
        return "MKU Talent Trees";
    }
}
