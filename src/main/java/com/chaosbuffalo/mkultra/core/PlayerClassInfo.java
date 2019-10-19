package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.talents.BaseTalent;
import com.chaosbuffalo.mkultra.core.talents.RangedAttributeTalent;
import com.chaosbuffalo.mkultra.core.talents.TalentTree;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import scala.Array;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class PlayerClassInfo {
    private ResourceLocation classId;
    private PlayerClass classObj;
    private int level;
    private int unspentPoints;
    private int totalTalentPoints;
    private int unspentTalentPoints;
    private HashMap<ResourceLocation, TalentTreeRecord> talentTrees;
    private ResourceLocation[] hotbar;
    private List<ResourceLocation> abilitySpendOrder;
    private ResourceLocation[] loadedPassives;
    private List<ResourceLocation> loadedUltimates;
    private Map<ResourceLocation, PlayerAbilityInfo> abilityInfoMap =
            new HashMap<>(GameConstants.ACTION_BAR_SIZE);

    public PlayerClassInfo(ResourceLocation classId) {
        this.classId = classId;
        this.level = 1;
        this.unspentPoints = 1;
        this.totalTalentPoints = 0;
        this.unspentTalentPoints = 0;
        this.classObj = MKURegistry.getClass(classId);
        if (this.classObj == null){
            Log.info("Didnt find class for: %s", classId.toString());
        }
        loadedPassives = new ResourceLocation[GameConstants.MAX_PASSIVES];
        Arrays.fill(loadedPassives, MKURegistry.INVALID_ABILITY);
        loadedUltimates = NonNullList.withSize(GameConstants.MAX_ULTIMATES, MKURegistry.INVALID_ABILITY);
        hotbar = new ResourceLocation[GameConstants.ACTION_BAR_SIZE];
        Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
        abilitySpendOrder = NonNullList.withSize(GameConstants.MAX_CLASS_LEVEL, MKURegistry.INVALID_ABILITY);
        talentTrees = new HashMap<>();
        for (TalentTree tree : MKURegistry.REGISTRY_TALENT_TREES.getValuesCollection()) {
            talentTrees.put(tree.getRegistryName(), new TalentTreeRecord(tree));
        }
    }

    public ResourceLocation getClassId() {
        return classId;
    }

    public int getLevel() {
        return level;
    }

    public int getUnspentPoints() {
        return unspentPoints;
    }

    public int getTotalTalentPoints() {
        return totalTalentPoints;
    }

    public int getUnspentTalentPoints() {
        return unspentTalentPoints;
    }

    public Collection<PlayerAbilityInfo> getAbilityInfos(){
        return abilityInfoMap.values();
    }

    void save(PlayerData data) {
        level = data.getLevel();
        unspentPoints = data.getUnspentPoints();
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            hotbar[i] = data.getAbilityInSlot(i);
        }
    }

    boolean checkTalentTotals() {
        int spent = getTotalSpentPoints();
        if (getTotalTalentPoints() - spent != getUnspentTalentPoints()) {
            unspentTalentPoints = getTotalTalentPoints() - spent;
            return true;
        }
        return false;
    }

    public void putInfo(ResourceLocation abilityId, PlayerAbilityInfo info){
        abilityInfoMap.put(abilityId, info);
    }

    private ResourceLocation[] parseNBTAbilityArray(NBTTagCompound tag, String name, int size) {
        NBTTagList list = tag.getTagList(name, Constants.NBT.TAG_STRING);
        ResourceLocation[] arr = new ResourceLocation[size];
        Arrays.fill(arr, MKURegistry.INVALID_ABILITY);
        for (int i = 0; i < size && i < list.tagCount(); i++) {
            arr[i] = new ResourceLocation(list.getStringTagAt(i));
        }
        return arr;
    }

    private List<ResourceLocation> parseNBTAbilityList(NBTTagCompound tag, String name, int size) {
        NBTTagList list = tag.getTagList(name, Constants.NBT.TAG_STRING);
        List<ResourceLocation> ids = NonNullList.withSize(size, MKURegistry.INVALID_ABILITY);
        for (int i = 0; i < size && i < list.tagCount(); i++) {
            ids.set(i, new ResourceLocation(list.getStringTagAt(i)));
        }
        return ids;
    }

    public void applyPassives(EntityPlayer player, IPlayerData data, World world) {
        Log.debug("applyPassives - loadedPassives %s %s", loadedPassives[0], loadedPassives[1]);
        for (ResourceLocation loc : loadedPassives) {
            if (!loc.equals(MKURegistry.INVALID_ABILITY)) {
                PlayerAbility ability = MKURegistry.getAbility(loc);
                if (ability != null) {
                    ability.execute(player, data, world);
                }
            }
        }
    }

    private void serializeAbilities(NBTTagCompound tag) {
        NBTTagList tagList = new NBTTagList();
        for (PlayerAbilityInfo info : abilityInfoMap.values()) {
            NBTTagCompound sk = new NBTTagCompound();
            info.serialize(sk);
            tagList.appendTag(sk);
        }

        tag.setTag("abilities", tagList);
    }

    private void deserializeAbilities(NBTTagCompound tag) {
        if (tag.hasKey("abilities")) {
            NBTTagList tagList = tag.getTagList("abilities", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound abilityTag = tagList.getCompoundTagAt(i);
                ResourceLocation abilityId = new ResourceLocation(abilityTag.getString("id"));
                PlayerAbility ability = MKURegistry.getAbility(abilityId);
                if (ability == null) {
                    continue;
                }

                PlayerAbilityInfo info = ability.createAbilityInfo();
                if (info.deserialize(abilityTag))
                    abilityInfoMap.put(abilityId, info);
            }
        } else {
            clearSpentAbilities();
        }
    }

    public boolean hasUltimate(){
        return getUltimateAbilitiesFromTalents().size() > 0;
    }

    public boolean addPassiveToSlot(ResourceLocation loc, int slotIndex) {
        if (canAddPassiveToSlot(loc, slotIndex)) {
            for (int i = 0; i < GameConstants.MAX_PASSIVES; i++) {
                if (!loc.equals(MKURegistry.INVALID_ABILITY) && i != slotIndex && loc.equals(loadedPassives[i])) {
                    loadedPassives[i] = loadedPassives[slotIndex];
                }
            }
            loadedPassives[slotIndex] = loc;
            return true;
        }
        return false;
    }

    public boolean addUltimateToSlot(ResourceLocation loc, int slotIndex) {
        if (canAddUltimateToSlot(loc, slotIndex)) {
            loadedUltimates.set(slotIndex, loc);
            return true;
        }
        return false;
    }

    public int getUltimateSlot(ResourceLocation loc) {
        int index = loadedUltimates.indexOf(loc);
        if (index == -1)
            return GameConstants.ULTIMATE_INVALID_SLOT;
        return index;
    }


    public int getPassiveSlot(ResourceLocation loc) {
        for (int i = 0; i < GameConstants.MAX_PASSIVES; i++) {
            if (loc.equals(loadedPassives[i])) {
                return i;
            }
        }

        return GameConstants.PASSIVE_INVALID_SLOT;
    }

    public void clearUltimateSlot(int slotIndex) {
        loadedUltimates.set(slotIndex, MKURegistry.INVALID_ABILITY);
    }

    public void clearPassiveSlot(int slotIndex) {
        loadedPassives[slotIndex] = MKURegistry.INVALID_ABILITY;
    }

    public ResourceLocation getPassiveForSlot(int slotIndex) {
        if (slotIndex >= GameConstants.MAX_PASSIVES) {
            return MKURegistry.INVALID_ABILITY;
        }
        return loadedPassives[slotIndex];
    }

    public ResourceLocation getUltimateForSlot(int slotIndex) {
        if (slotIndex >= GameConstants.MAX_ULTIMATES) {
            return MKURegistry.INVALID_ABILITY;
        }
        return loadedUltimates.get(slotIndex);
    }

    public boolean canAddUltimateToSlot(ResourceLocation loc, int slotIndex){
        return slotIndex < GameConstants.MAX_ULTIMATES && hasTrainedUltimate(loc);
    }

    public boolean canAddPassiveToSlot(ResourceLocation loc, int slotIndex) {
        return slotIndex < GameConstants.MAX_PASSIVES && hasTrainedPassive(loc);
    }

    public HashSet<PlayerAbility> getUltimateAbilitiesFromTalents(){
        HashSet<PlayerAbility> abilities = new HashSet<>();
        for (TalentTreeRecord rec: talentTrees.values()){
            if (rec.hasPointsInTree()){
                rec.getUltimatesWithPoints().forEach(talent -> abilities.add(talent.getAbility()));
            }
        }
        return abilities;
    }

    public HashSet<PlayerPassiveAbility> getPassiveAbilitiesFromTalents() {
        HashSet<PlayerPassiveAbility> abilities = new HashSet<>();
        for (TalentTreeRecord rec : talentTrees.values()) {
            if (rec.hasPointsInTree()) {
                rec.getPassivesWithPoints().forEach(talent -> abilities.add(talent.getAbility()));
            }
        }
        return abilities;
    }

    public boolean hasTrainedUltimate(ResourceLocation loc){
        if (loc.equals(MKURegistry.INVALID_ABILITY)){
            return true;
        }
        HashSet<PlayerAbility> abilities = getUltimateAbilitiesFromTalents();
        PlayerAbility ability = MKURegistry.getAbility(loc);
        return ability != null && abilities.contains(ability);
    }

    public boolean hasTrainedPassive(ResourceLocation loc) {
        if (loc.equals(MKURegistry.INVALID_ABILITY)) {
            return true;
        }
        HashSet<PlayerPassiveAbility> abilities = getPassiveAbilitiesFromTalents();
        PlayerAbility ability = MKURegistry.getAbility(loc);
        return ability instanceof PlayerPassiveAbility && abilities.contains(ability);
    }

    public HashSet<RangedAttributeTalent> getAttributeTalentSet() {
        HashSet<RangedAttributeTalent> attributeTalents = new HashSet<>();
        for (TalentTreeRecord rec : talentTrees.values()) {
            if (rec.hasPointsInTree()) {
                attributeTalents.addAll(rec.getAttributeTalentsWithPoints());
            }
        }
        return attributeTalents;
    }

    public Map<IAttribute, AttributeModifier> getAttributeModifiers() {
        HashSet<RangedAttributeTalent> presentTalents = getAttributeTalentSet();
        Map<IAttribute, AttributeModifier> attributeModifierMap = Maps.newHashMap();
        for (RangedAttributeTalent talent : presentTalents) {
            double value = 0.0;
            for (TalentTreeRecord rec : talentTrees.values()) {
                if (rec.hasPointsInTree()) {
                    value += rec.getTotalForAttributeTalent(talent);
                }
            }
//            Log.info("Total for attribute talent: %s, %f", talent.getRegistryName().toString(), value);
            attributeModifierMap.put(talent.getAttribute(), talent.createModifier(value));
        }
        return attributeModifierMap;
    }

    public void applyAttributesModifiersToPlayer(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();

        for (Map.Entry<IAttribute, AttributeModifier> entry : getAttributeModifiers().entrySet()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getKey());
            if (instance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                instance.removeModifier(attributemodifier);
                instance.applyModifier(attributemodifier);
            }
        }
    }

    public void removeAttributesModifiersFromPlayer(EntityPlayer player) {
        AbstractAttributeMap attributeMap = player.getAttributeMap();

        for (RangedAttributeTalent entry : getAttributeTalentSet()) {
            IAttributeInstance instance = attributeMap.getAttributeInstance(entry.getAttribute());
            if (instance != null) {
                instance.removeModifier(entry.getUUID());
            }
        }
    }

    private void writeNBTAbilityArray(NBTTagCompound tag, String name, Collection<ResourceLocation> array, int size) {
        NBTTagList list = new NBTTagList();
        if (array != null) {
            array.stream().limit(size).forEach(r -> list.appendTag(new NBTTagString(r.toString())));
        }
        tag.setTag(name, list);
    }

    public void writeTalentTrees(NBTTagCompound tag) {
        NBTTagCompound trees = new NBTTagCompound();
        boolean hadTalents = false;
        for (ResourceLocation loc : talentTrees.keySet()) {
            TalentTreeRecord record = talentTrees.get(loc);
            if (record.hasPointsInTree()) {
                trees.setTag(loc.toString(), record.toTag());
                hadTalents = true;
            }
        }
        if (hadTalents) {
            tag.setTag("trees", trees);
        }
    }

    public void parseTalentTrees(NBTTagCompound tag) {
        boolean doReset = false;
        if (tag.hasKey("trees")) {
            NBTTagCompound trees = tag.getCompoundTag("trees");
            for (String key : trees.getKeySet()) {
                ResourceLocation loc = new ResourceLocation(key);
                if (talentTrees.containsKey(loc)) {
                    boolean needsReset = talentTrees.get(loc).fromTag(trees.getCompoundTag(key));
                    if (needsReset) {
                        doReset = true;
                    }
                }
            }
        }
        if (doReset){
            clearUltimateAbilities();
            clearPassiveAbilities();
        }
    }

    public void serialize(NBTTagCompound tag) {
        tag.setString("id", classId.toString());
        tag.setInteger("level", level);
        if (classObj != null){
            tag.setString("classAbilityHash", classObj.hashAbilities());
        } else {
            tag.setString("classAbilityHash", "invalid_hash");
        }

        tag.setInteger("unspentPoints", unspentPoints);
        serializeAbilities(tag);
        writeNBTAbilityArray(tag, "abilitySpendOrder", abilitySpendOrder, GameConstants.MAX_CLASS_LEVEL);
        writeNBTAbilityArray(tag, "hotbar", Arrays.asList(hotbar), GameConstants.ACTION_BAR_SIZE);
        serializeTalentInfo(tag);
    }

    private void clearSpentAbilities(){
        unspentPoints = level;
        clearAbilitySpendOrder();
        clearActiveAbilities();
        abilityInfoMap.clear();
    }

    public void deserialize(NBTTagCompound tag) {
        classId = new ResourceLocation(tag.getString("id"));
        level = tag.getInteger("level");
        classObj = MKURegistry.getClass(classId);
        deserializeAbilities(tag);
        if (classObj != null){
            if (tag.hasKey("classAbilityHash")){
                String abilityHash = tag.getString("classAbilityHash");
                if (abilityHash.equals(classObj.hashAbilities())){
                    unspentPoints = tag.getInteger("unspentPoints");
                    abilitySpendOrder = parseNBTAbilityList(tag, "abilitySpendOrder", GameConstants.MAX_CLASS_LEVEL);
                    setActiveAbilities(parseNBTAbilityArray(tag, "hotbar", GameConstants.ACTION_BAR_SIZE));
                } else {
                    clearSpentAbilities();
                }
            } else {
                clearSpentAbilities();
            }
        } else {
           clearSpentAbilities();
        }
        deserializeTalentInfo(tag);
    }

    public void serializeTalentInfo(NBTTagCompound tag) {
        tag.setInteger("unspentTalentPoints", unspentTalentPoints);
        tag.setInteger("totalTalentPoints", totalTalentPoints);
        writeNBTAbilityArray(tag, "loadedPassives", Arrays.asList(loadedPassives), GameConstants.MAX_PASSIVES);
        writeNBTAbilityArray(tag, "loadedUltimates", loadedUltimates, GameConstants.MAX_ULTIMATES);
        writeTalentTrees(tag);
    }

    public void deserializeTalentInfo(NBTTagCompound tag) {
        unspentTalentPoints = tag.getInteger("unspentTalentPoints");
        totalTalentPoints = tag.getInteger("totalTalentPoints");
        if (tag.hasKey("loadedPassives")) {
            setLoadedPassives(parseNBTAbilityArray(tag, "loadedPassives", GameConstants.MAX_PASSIVES));
        }
        if (tag.hasKey("loadedUltimates")){
            loadedUltimates = parseNBTAbilityList(tag, "loadedUltimates", GameConstants.MAX_ULTIMATES);
        }
        parseTalentTrees(tag);
    }

    public ResourceLocation[] getActivePassives() {
        return loadedPassives;
    }

    public List<ResourceLocation> getActiveUltimates(){
        return Collections.unmodifiableList(loadedUltimates);
    }

    public ResourceLocation[] getActiveAbilities() {
        return hotbar;
    }

    public void addTalentPoints(int pointCount) {
        totalTalentPoints += pointCount;
        unspentTalentPoints += pointCount;
    }

    public boolean canIncrementPointInTree(ResourceLocation tree, String line, int index) {
        if (getUnspentTalentPoints() == 0)
            return false;
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree != null && talentTree.canIncrementPoint(line, index);
    }

    public boolean canDecrementPointInTree(ResourceLocation tree, String line, int index) {
        TalentTreeRecord talentTree = talentTrees.get(tree);
        return talentTree != null && talentTree.canDecrementPoint(line, index);
    }

    public boolean spendTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index) {
        if (canIncrementPointInTree(tree, line, index)) {
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent talentDef = talentTree.getTalentDefinition(line, index);
            if (talentDef.onAdd(player, this)) {
                talentTree.incrementPoint(line, index);
                unspentTalentPoints -= 1;
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    public int getTotalSpentPoints() {
        int tot = 0;
        for (TalentTreeRecord talentTree : talentTrees.values()) {
            tot += talentTree.getPointsInTree();
        }
        return tot;
    }

    public boolean refundTalentPoint(EntityPlayer player, ResourceLocation tree, String line, int index) {
        if (canDecrementPointInTree(tree, line, index)) {
            TalentTreeRecord talentTree = talentTrees.get(tree);
            BaseTalent talentDef = talentTree.getTalentDefinition(line, index);
            if (talentDef.onRemove(player, this)) {
                talentTree.decrementPoint(line, index);
                unspentTalentPoints += 1;
                return true;
            }

            return false;
        } else {
            return false;
        }
    }

    void setLoadedPassives(ResourceLocation[] passives) {
        this.loadedPassives = passives;
    }

    void setActiveAbilities(ResourceLocation[] hotbar) {
        this.hotbar = hotbar;
    }

    public void setAbilitySpendOrder(ResourceLocation abilityId, int level) {
        if (level > 0) {
            abilitySpendOrder.set(level - 1, abilityId);
        }
    }

    @Nullable
    public PlayerAbilityInfo getAbilityInfo(ResourceLocation abilityId) {
        return abilityInfoMap.get(abilityId);
    }

    public void clearUltimateAbilities(){
        loadedUltimates.clear();
    }

    public void clearPassiveAbilities(){
        Arrays.fill(loadedPassives, MKURegistry.INVALID_ABILITY);
    }

    public void clearActiveAbilities(){
        Arrays.fill(hotbar, MKURegistry.INVALID_ABILITY);
    }

    public void clearAbilitySpendOrder() {
        abilitySpendOrder.clear();
    }

    public ResourceLocation getAbilitySpendOrder(int index) {
        ResourceLocation id = MKURegistry.INVALID_ABILITY;
        if (index > 0) {
            id = abilitySpendOrder.get(index - 1);
            abilitySpendOrder.set(index - 1, MKURegistry.INVALID_ABILITY);
        }
        return id;
    }

    public TalentTreeRecord getTalentTree(ResourceLocation loc) {
        return talentTrees.get(loc);
    }


    public static PlayerClassInfo deserializeUpdate(PacketBuffer pb) {
        PlayerClassInfo info = new PlayerClassInfo(pb.readResourceLocation());

        info.level = pb.readInt();
        try {
            NBTTagCompound talentData = pb.readCompoundTag();
            info.deserializeTalentInfo(talentData);
            NBTTagCompound abilityData = pb.readCompoundTag();
            info.deserializeAbilities(abilityData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }

    public void serializeUpdate(PacketBuffer pb) {
        pb.writeResourceLocation(getClassId());
        pb.writeInt(getLevel());
        NBTTagCompound talentData = new NBTTagCompound();
        serializeTalentInfo(talentData);
        pb.writeCompoundTag(talentData);
        NBTTagCompound abilityData = new NBTTagCompound();
        serializeAbilities(abilityData);
        pb.writeCompoundTag(abilityData);
    }
}
