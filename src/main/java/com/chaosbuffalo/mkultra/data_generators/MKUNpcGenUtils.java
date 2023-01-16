package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mknpc.npc.options.SkillOption;

public class MKUNpcGenUtils {


    enum NpcSkillClass {
        CLERIC,
        NECROMANCER,
        FIGHTER,
        MAGE,
        WARRIOR,
        PALADIN,
        ARCHER
    }


    public static SkillOption GetSkillOptionForClass(NpcSkillClass skillClass) {
        switch (skillClass) {
            case CLERIC:
                return new SkillOption()
                        .addMajorSkill(MKAttributes.RESTORATION)
                        .addMajorSkill(MKAttributes.ALTERATON)
                        .addMinorSkill(MKAttributes.EVOCATION)
                        .addMinorSkill(MKAttributes.NECROMANCY)
                        .addMinorSkill(MKAttributes.ABJURATION)
                        .addRemedialSkill(MKAttributes.ONE_HAND_BLUNT)
                        .addRemedialSkill(MKAttributes.TWO_HAND_BLUNT);
            case NECROMANCER:
                return new SkillOption()
                        .addMajorSkill(MKAttributes.CONJURATION)
                        .addMajorSkill(MKAttributes.NECROMANCY)
                        .addMinorSkill(MKAttributes.EVOCATION)
                        .addMinorSkill(MKAttributes.ALTERATON)
                        .addMinorSkill(MKAttributes.ABJURATION)
                        .addRemedialSkill(MKAttributes.ONE_HAND_PIERCE);
            case WARRIOR:
                return new SkillOption()
                        .addMajorSkill(MKAttributes.ONE_HAND_PIERCE)
                        .addMajorSkill(MKAttributes.ONE_HAND_BLUNT)
                        .addMajorSkill(MKAttributes.ONE_HAND_SLASH)
                        .addMajorSkill(MKAttributes.TWO_HAND_BLUNT)
                        .addMajorSkill(MKAttributes.TWO_HAND_SLASH)
                        .addMajorSkill(MKAttributes.TWO_HAND_PIERCE)
                        .addMinorSkill(MKAttributes.PANKRATION)
                        .addRemedialSkill(MKAttributes.ARETE)
                        .addRemedialSkill(MKAttributes.PNEUMA);
            case MAGE:
                return new SkillOption()
                        .addMinorSkill(MKAttributes.EVOCATION)
                        .addMinorSkill(MKAttributes.ALTERATON)
                        .addMinorSkill(MKAttributes.ABJURATION)
                        .addMinorSkill(MKAttributes.CONJURATION)
                        .addRemedialSkill(MKAttributes.ONE_HAND_PIERCE);
            case FIGHTER:
                return new SkillOption()
                        .addMinorSkill(MKAttributes.ONE_HAND_PIERCE)
                        .addMinorSkill(MKAttributes.ONE_HAND_BLUNT)
                        .addMinorSkill(MKAttributes.ONE_HAND_SLASH)
                        .addMinorSkill(MKAttributes.TWO_HAND_BLUNT)
                        .addMinorSkill(MKAttributes.TWO_HAND_SLASH)
                        .addMinorSkill(MKAttributes.TWO_HAND_PIERCE)
                        .addRemedialSkill(MKAttributes.PANKRATION)
                        .addRemedialSkill(MKAttributes.ARETE)
                        .addRemedialSkill(MKAttributes.PNEUMA);
            case PALADIN:
                return GetSkillOptionForClass(NpcSkillClass.WARRIOR)
                        .addMinorSkill(MKAttributes.RESTORATION)
                        .addMinorSkill(MKAttributes.ALTERATON)
                        .addMinorSkill(MKAttributes.ABJURATION)
                        .addRemedialSkill(MKAttributes.EVOCATION);
            case ARCHER:
                return new SkillOption()
                        .addMajorSkill(MKAttributes.MARKSMANSHIP)
                        .addMinorSkill(MKAttributes.ONE_HAND_PIERCE)
                        .addMinorSkill(MKAttributes.ONE_HAND_BLUNT)
                        .addMinorSkill(MKAttributes.ONE_HAND_SLASH)
                        .addRemedialSkill(MKAttributes.PANKRATION)
                        .addRemedialSkill(MKAttributes.ARETE)
                        .addRemedialSkill(MKAttributes.PNEUMA);
        }
        return new SkillOption();
    }
}
