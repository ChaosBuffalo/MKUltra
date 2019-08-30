package com.chaosbuffalo.mkultra.core.talents;


public class AttributeTalentNode extends TalentNode {

    private final double perRank;
    private final RangedAttributeTalent rangedTalent;

    public AttributeTalentNode(RangedAttributeTalent talent, int maxRank, double perRank) {
        super(talent, maxRank);
        this.perRank = perRank;
        rangedTalent = talent;
    }

    public double getValue(int rank) {
        return perRank * rank;
    }

    public double getPerRank() {
        return perRank;
    }

    public RangedAttributeTalent getRangedTalent() {
        return rangedTalent;
    }

}
