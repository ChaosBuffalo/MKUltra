package com.chaosbuffalo.mkultra.core.talents;

import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;

public class TalentTreeRecord {
    private TalentTree tree;
    public HashMap<String, ArrayList<TalentRecord>> records;

    public TalentTreeRecord(TalentTree tree) {
        this.tree = tree;
        this.records = new HashMap<>();
        setup();
    }

    private void setup() {
        Multimap<String, TalentNode> lines = tree.getLines();
        for (String key : lines.keySet()) {
            ArrayList<TalentRecord> recordLine = new ArrayList<>();
            for (TalentNode node : lines.get(key)) {
                recordLine.add(new TalentRecord(node));
            }
            records.put(key, recordLine);
        }
    }

    public boolean canIncrementPoint(String lineName, int index) {
        ArrayList<TalentRecord> line = records.get(lineName);
        TalentRecord record = line.get(index);
        if (index != 0) {
            TalentRecord previous = line.get(index - 1);
            if (previous.getRank() == 0) {
                return false;
            }
        }
        return record.getRank() < record.getNode().getMaxRanks();
    }

    public void incrementPoint(String lineName, int index) {
        ArrayList<TalentRecord> line = records.get(lineName);
        TalentRecord record = line.get(index);
        record.addToRank(1);
    }

    public BaseTalent.TalentType getTypeForPoint(String lineName, int index) {
        ArrayList<TalentRecord> line = records.get(lineName);
        TalentRecord record = line.get(index);
        return record.getNode().getTalentType();
    }

    public void decrementPoint(String lineName, int index) {
        ArrayList<TalentRecord> line = records.get(lineName);
        TalentRecord record = line.get(index);
        record.addToRank(-1);
    }

    public boolean containsIndex(String lineName, int index) {
        return records.containsKey(lineName) && records.get(lineName).size() > index;
    }

    public boolean canDecrementPoint(String lineName, int index) {
        ArrayList<TalentRecord> line = records.get(lineName);
        TalentRecord record = line.get(index);
        if (index < line.size() - 1) {
            TalentRecord next = line.get(index + 1);
            if (next.getRank() > 0) {
                return false;
            }
        }
        return record.getRank() > 0;
    }


    public NBTTagCompound toTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("version", tree.getVersion());
        NBTTagCompound lines = new NBTTagCompound();
        for (String key : records.keySet()) {
            if (hasPointsInLine(key)) {
                ArrayList<TalentRecord> line = records.get(key);
                int[] intArray = new int[line.size()];
                for (int i = 0; i < line.size(); i++) {
                    intArray[i] = line.get(i).getRank();
                }
                lines.setIntArray(key, intArray);
            }
        }
        tag.setTag("lines", lines);
        return tag;
    }

    public void fromTag(NBTTagCompound tag) {
        if (tag.hasKey("version")) {
            int version = tag.getInteger("version");
            if (version != tree.getVersion()) {
                Log.info("Can't load talent tree for %s, found version %d, but we have loaded %d", tree.getRegistryName().toString(),
                        version, tree.getVersion());
                return;
            }
            if (tag.hasKey("lines")) {
                NBTTagCompound lines = tag.getCompoundTag("lines");
                for (String key : lines.getKeySet()) {
                    if (records.containsKey(key)) {
                        int[] lineArray = lines.getIntArray(key);
                        ArrayList<TalentRecord> line = records.get(key);
                        for (int i = 0; i < lineArray.length; i++) {
                            line.get(i).setRank(lineArray[i]);
                        }
                    } else {
                        Log.info("Can't load talent line %s for %s", key, tree.getRegistryName().toString());
                    }
                }
            }
        }
    }

    public boolean hasPointsInLine(String lineName) {
        if (records.containsKey(lineName)) {
            ArrayList<TalentRecord> line = records.get(lineName);
            if (line.get(0).getRank() == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean hasPointsInTree() {
        for (String key : records.keySet()) {
            if (hasPointsInLine(key)) {
                return true;
            }
        }
        return false;
    }

    public int getPointsInLine(String lineName) {
        if (records.containsKey(lineName)) {
            ArrayList<TalentRecord> line = records.get(lineName);
            int count = 0;
            for (TalentRecord rec : line) {
                if (rec.getRank() == 0) {
                    return count;
                }
                count += rec.getRank();
            }
            return count;
        } else {
            return 0;
        }
    }

    public int getPointsInTree() {
        int count = 0;
        for (String key : records.keySet()) {
            count += getPointsInLine(key);
        }
        return count;
    }

    public HashSet<PassiveAbilityTalent> getPassivesWithPoints() {
        HashSet<PassiveAbilityTalent> talents = new HashSet<>();
        iterateKnownTalents(PassiveAbilityTalent.class, BaseTalent.TalentType.PASSIVE, (r, t) -> talents.add(t));
        return talents;
    }

    public HashSet<RangedAttributeTalent> getAttributeTalentsWithPoints() {
        HashSet<RangedAttributeTalent> talents = new HashSet<>();
        iterateKnownTalents(RangedAttributeTalent.class, BaseTalent.TalentType.ATTRIBUTE, (r, t) -> talents.add(t));
        return talents;
    }

    private <T extends BaseTalent> void iterateKnownTalents(Class<T> clazz, BaseTalent.TalentType type, BiConsumer<TalentRecord, T> consumer) {
        for (String key : records.keySet()) {
            if (hasPointsInLine(key)) {
                for (TalentRecord rec : records.get(key)) {
                    if (rec.getRank() == 0) {
                        break;
                    } else {
                        if (rec.getNode().getTalentType() == type) {
                            consumer.accept(rec, (T) rec.getNode().getTalent());
                        }
                    }
                }
            }
        }
    }

    public double getTotalForAttributeTalent(RangedAttributeTalent talent) {
        // Sorta awkward, maybe revisit this later
        AtomicDouble val = new AtomicDouble(0.0);
        iterateKnownTalents(RangedAttributeTalent.class, BaseTalent.TalentType.ATTRIBUTE, (r, t) -> {
            if (r.getNode().hasSameTalent(talent)) {
                AttributeTalentNode attrNode = (AttributeTalentNode) r.getNode();
                val.addAndGet(attrNode.getValue(r.getRank()));
            }
        });
        return val.get();
    }
}
