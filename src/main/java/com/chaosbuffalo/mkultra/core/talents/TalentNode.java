package com.chaosbuffalo.mkultra.core.talents;

public class TalentNode {

    private final BaseTalent talent;
    private final int maxRanks;

    public TalentNode(BaseTalent talent, int maxRanks){
        this.talent = talent;
        this.maxRanks = maxRanks;
    }

    BaseTalent.TalentType getTalentType() {
        return talent.getTalentType();
    }

    public boolean hasSameTalent(BaseTalent talent){
        return talent.getRegistryName().equals(this.talent.getRegistryName());
    }

    public BaseTalent getTalent(){
        return talent;
    }

    public int getMaxRanks(){
        return maxRanks;
    }
}
