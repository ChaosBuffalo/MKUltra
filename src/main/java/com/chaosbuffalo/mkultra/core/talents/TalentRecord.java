package com.chaosbuffalo.mkultra.core.talents;

public class TalentRecord {

    private int currentRank;
    private final TalentNode node;

    public TalentRecord(TalentNode node){
        this.node = node;
        currentRank = 0;
    }

    public TalentRecord(TalentNode node, int rank){
        this(node);
        currentRank = rank;
    }

    public TalentNode getNode(){
        return node;
    }

    public int getRank(){
        return currentRank;
    }

    public void addToRank(int value){
        currentRank += value;
    }

    public void setRank(int value){
        currentRank = value;
    }
}
