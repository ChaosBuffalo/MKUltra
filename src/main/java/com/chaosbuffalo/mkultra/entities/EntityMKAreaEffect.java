package com.chaosbuffalo.mkultra.entities;

import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.SpellManager;
import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.targeting_api.Targeting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// This class exists mostly because EntityAreaEffectCloud has all its members marked as private and many have no getters
public class EntityMKAreaEffect extends Entity {
    private static final DataParameter<Float> RADIUS = EntityDataManager.createKey(EntityMKAreaEffect.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityMKAreaEffect.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SINGLE_POINT = EntityDataManager.createKey(EntityMKAreaEffect.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PARTICLE = EntityDataManager.createKey(EntityMKAreaEffect.class, DataSerializers.VARINT);
    private final List<EffectEntry> effects;
    private final Map<Entity, Integer> reapplicationDelayMap;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean colorSet;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private EntityLivingBase owner;
    private UUID ownerUniqueId;

    private static class EffectEntry {
        final PotionEffect effect;
        final Targeting.TargetType targetType;
        final boolean excludeCaster;
        SpellCast cast;

        EffectEntry(PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
            this.effect = effect;
            this.targetType = targetType;
            this.excludeCaster = excludeCaster;
        }

        EffectEntry(SpellCast cast, PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
            this(effect, targetType, excludeCaster);
            this.cast = cast;
        }
    }

    private static final float DEFAULT_RADIUS = 3.0f;
    private static final float DEFAULT_HEIGHT = 1.0f;

    public EntityMKAreaEffect(World worldIn) {
        super(worldIn);
        this.effects = Lists.newArrayList();
        this.reapplicationDelayMap = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setRadius(DEFAULT_RADIUS);
    }

    public EntityMKAreaEffect(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(COLOR, 0);
        this.getDataManager().register(RADIUS, 0.5F);
        this.getDataManager().register(SINGLE_POINT, false);
        this.getDataManager().register(PARTICLE, EnumParticleTypes.SPELL_MOB.getParticleID());
    }

    public float getRadius() {
        return this.getDataManager().get(RADIUS);
    }

    public void setRadius(float radiusIn) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.setSize(radiusIn * 2.0F, DEFAULT_HEIGHT);
        this.setPosition(d0, d1, d2);

        if (!this.world.isRemote) {
            this.getDataManager().set(RADIUS, radiusIn);
        }
    }

    public void addSpellCast(SpellCast cast, PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
        this.effects.add(new EffectEntry(cast, effect, targetType, excludeCaster));
    }

    public void addEffect(PotionEffect effect, Targeting.TargetType targetType, boolean excludeCaster) {
        this.effects.add(new EffectEntry(effect, targetType, excludeCaster));
    }

    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int colorIn) {
        this.colorSet = true;
        this.getDataManager().set(COLOR, colorIn);
    }

    public EnumParticleTypes getParticle() {
        return EnumParticleTypes.getParticleFromId(this.getDataManager().get(PARTICLE));
    }

    public void setParticle(EnumParticleTypes particleIn) {
        this.getDataManager().set(PARTICLE, particleIn.getParticleID());
    }

    protected void setIsSinglePoint(boolean ignoreRadius) {
        this.getDataManager().set(SINGLE_POINT, ignoreRadius);
    }

    public boolean isSinglePoint() {
        return this.getDataManager().get(SINGLE_POINT);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        boolean singlePoint = this.isSinglePoint();
        float radius = this.getRadius();

        if (this.world.isRemote) {
            clientUpdate(singlePoint, radius);
        } else {
            if (this.ticksExisted >= this.waitTime + this.duration) {
                this.setDead();
                return;
            }
            if (getOwner() == null){
                this.setDead();
                return;
            }

            boolean mustWait = this.ticksExisted < this.waitTime;

            if (singlePoint != mustWait) {
                this.setIsSinglePoint(mustWait);
            }

            if (mustWait) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                radius += this.radiusPerTick;

                if (radius < 0.5F) {
                    this.setDead();
                    return;
                }

                this.setRadius(radius);
            }

            if (this.ticksExisted % 5 != 0) {
                return;
            }

            this.reapplicationDelayMap.entrySet().removeIf(entry -> this.ticksExisted >= entry.getValue());

            List<EffectEntry> potions = Lists.newArrayList();
            potions.addAll(this.effects);

            if (potions.isEmpty()) {
                this.reapplicationDelayMap.clear();
            } else {
                List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox(),
                        e -> e != null && EntitySelectors.NOT_SPECTATING.apply(e) && e.isEntityAlive() &&
                                !reapplicationDelayMap.containsKey(e) && e.canBeHitWithPotion());
                if (list.isEmpty()) {
                    return;
                }

                for (EntityLivingBase target : list) {

                    double d0 = target.posX - this.posX;
                    double d1 = target.posZ - this.posZ;
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 > (double) (radius * radius)) {
                        continue;
                    }

                    applyEffectsToTarget(potions, target);

                    if (this.radiusOnUse != 0.0F) {
                        radius += this.radiusOnUse;

                        if (radius < 0.5F) {
                            this.setDead();
                            return;
                        }

                        this.setRadius(radius);
                    }

                    if (this.durationOnUse != 0) {
                        this.duration += this.durationOnUse;

                        if (this.duration <= 0) {
                            this.setDead();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void applyEffectsToTarget(List<EffectEntry> potions, EntityLivingBase target) {
        this.reapplicationDelayMap.put(target, this.ticksExisted + this.reapplicationDelay);

        for (EffectEntry spellEffect : potions) {

            PotionEffect eff = spellEffect.effect;

            boolean validTarget;
            SpellPotionBase spBase = eff.getPotion() instanceof SpellPotionBase ? (SpellPotionBase) eff.getPotion() : null;
            if (spBase != null) {
                validTarget = spBase.isValidTarget(spellEffect.targetType, getOwner(), target, spellEffect.excludeCaster);
            } else {
                validTarget = Targeting.isValidTarget(spellEffect.targetType, getOwner(), target, spellEffect.excludeCaster);
            }

            if (!validTarget) {
                continue;
            }

            if (eff.getPotion().isInstant()) {

                if (spBase != null) {

                    SpellCast cast = spellEffect.cast;
                    if (cast == null) {
                        Log.warn("MKAREA instant null cast! Spell: %s", spellEffect.effect.getPotion().getName());
                        continue;
                    }

                    // We can skip affectEntity and go directly to the effect because we
                    // have already ensured the target is valid.
                    spBase.doEffect(this, getOwner(), target, eff.getAmplifier(), cast);
                } else {
                    eff.getPotion().affectEntity(this, this.getOwner(), target, eff.getAmplifier(), 0.5D);
                }
            } else {

                if (spBase != null) {
                    SpellCast cast = spellEffect.cast;
                    if (cast == null) {
                        Log.warn("MKAREA periodic null cast! Spell: %s", spellEffect.effect.getPotion().getName());
                        continue;
                    }

                    // The cast given to MKAreaEffect has no target, so we need to register
                    SpellManager.registerTarget(cast, target);
                }

                target.addPotionEffect(new PotionEffect(eff));
            }
        }
    }

    public void setRadiusOnUse(float radiusOnUseIn) {
        this.radiusOnUse = radiusOnUseIn;
    }

    public void setRadiusPerTick(float radiusPerTickIn) {
        this.radiusPerTick = radiusPerTickIn;
    }

    public void setWaitTime(int waitTimeIn) {
        this.waitTime = waitTimeIn;
    }

    public void setReapplicationDelay(int reapplicationDelay) {
        this.reapplicationDelay = reapplicationDelay;
    }

    @Nullable
    public EntityLivingBase getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase) {
                this.owner = (EntityLivingBase) entity;
            }
        }

        return this.owner;
    }

    public void setOwner(EntityLivingBase ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound tagCompund) {
        this.ticksExisted = tagCompund.getInteger("Age");
        this.duration = tagCompund.getInteger("Duration");
        this.waitTime = tagCompund.getInteger("WaitTime");
        this.reapplicationDelay = tagCompund.getInteger("ReapplicationDelay");
        this.durationOnUse = tagCompund.getInteger("DurationOnUse");
        this.radiusOnUse = tagCompund.getFloat("RadiusOnUse");
        this.radiusPerTick = tagCompund.getFloat("RadiusPerTick");
        this.setRadius(tagCompund.getFloat("Radius"));
        this.ownerUniqueId = tagCompund.getUniqueId("OwnerUUID");

        if (tagCompund.hasKey("Particle", 8)) {
            EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(tagCompund.getString("Particle"));

            if (enumparticletypes != null) {
                this.setParticle(enumparticletypes);
            }
        }

        if (tagCompund.hasKey("Color", 99)) {
            this.setColor(tagCompund.getInteger("Color"));
        }

        if (tagCompund.hasKey("Effects", 9)) {
            NBTTagList nbttaglist = tagCompund.getTagList("Effects", 10);
            this.effects.clear();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound pe = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(pe);

                Targeting.TargetType tt = Targeting.TargetType.valueOf(pe.getString("TargetType"));
                boolean excludeCaster = pe.getBoolean("NoCaster");

                if (potioneffect != null) {
                    this.addEffect(potioneffect, tt, excludeCaster);
                }
            }
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound tagCompound) {
        tagCompound.setInteger("Age", this.ticksExisted);
        tagCompound.setInteger("Duration", this.duration);
        tagCompound.setInteger("WaitTime", this.waitTime);
        tagCompound.setInteger("ReapplicationDelay", this.reapplicationDelay);
        tagCompound.setInteger("DurationOnUse", this.durationOnUse);
        tagCompound.setFloat("RadiusOnUse", this.radiusOnUse);
        tagCompound.setFloat("RadiusPerTick", this.radiusPerTick);
        tagCompound.setFloat("Radius", this.getRadius());
        tagCompound.setString("Particle", this.getParticle().getParticleName());

        if (this.ownerUniqueId != null) {
            tagCompound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }

        if (this.colorSet) {
            tagCompound.setInteger("Color", this.getColor());
        }

        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (EffectEntry potioneffect : this.effects) {
                NBTTagCompound pe = potioneffect.effect.writeCustomPotionEffectToNBT(new NBTTagCompound());

                pe.setString("TargetType", potioneffect.targetType.toString());
                pe.setBoolean("NoCaster", potioneffect.excludeCaster);

                nbttaglist.appendTag(pe);
            }

            tagCompound.setTag("Effects", nbttaglist);
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (RADIUS.equals(key)) {
            this.setRadius(this.getRadius());
        }

        super.notifyDataManagerChange(key);
    }

    @Nonnull
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    private void clientUpdate(boolean singlePoint, float radius) {
        if (ticksExisted % 5 != 0) {
            return;
        }
        EnumParticleTypes enumparticletypes = this.getParticle();

        if (singlePoint) {
            if (this.rand.nextBoolean()) {
                int[] aint = new int[enumparticletypes.getArgumentCount()];

                for (int i = 0; i < 2; ++i) {
                    float f1 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                    float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                    float f3 = MathHelper.cos(f1) * f2;
                    float f4 = MathHelper.sin(f1) * f2;

                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                        int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
                        int k = j >> 16 & 255;
                        int l = j >> 8 & 255;
                        int i1 = j & 255;
                        this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) f3, this.posY, this.posZ + (double) f4, (double) ((float) k / 255.0F), (double) ((float) l / 255.0F), (double) ((float) i1 / 255.0F));
                    } else {
                        this.world.spawnParticle(enumparticletypes, this.posX + (double) f3, this.posY, this.posZ + (double) f4, 0.0D, 0.0D, 0.0D, aint);
                    }
                }
            }
        } else {
            int particleCount = (int) radius * 10;
            int[] aint1 = new int[enumparticletypes.getArgumentCount()];

            for (int k1 = 0; k1 < particleCount; ++k1) {
                float f6 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.rand.nextFloat()) * radius;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;

                if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                    int l1 = this.getColor();
                    int i2 = l1 >> 16 & 255;
                    int j2 = l1 >> 8 & 255;
                    int j1 = l1 & 255;
                    this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) f8, this.posY, this.posZ + (double) f9, (double) ((float) i2 / 255.0F), (double) ((float) j2 / 255.0F), (double) ((float) j1 / 255.0F));
                } else if (enumparticletypes == EnumParticleTypes.NOTE) {
                    this.world.spawnParticle(enumparticletypes, this.posX + (double) f8, this.posY, this.posZ + (double) f9, this.rand.nextInt(24) / 24.0f, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint1);
                } else {
                    this.world.spawnParticle(enumparticletypes, this.posX + (double) f8, this.posY, this.posZ + (double) f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint1);
                }
            }
        }
    }
}
