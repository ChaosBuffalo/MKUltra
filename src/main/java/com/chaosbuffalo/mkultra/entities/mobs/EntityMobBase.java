package com.chaosbuffalo.mkultra.entities.mobs;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MobData;
import com.chaosbuffalo.mkultra.mob_ai.*;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.OpenLearnClassTileEntityPacket;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityMobBase extends EntityCreature implements IMob, IRangedAttackMob {
    private static final DataParameter<Boolean> SWINGING_ARMS;
    private final EntityAIAttackRangedBowMK<EntityMobBase> aiArrowAttack = new EntityAIAttackRangedBowMK<>(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMeleeMK aiAttackOnCollide = new EntityAIAttackMeleeMK(this, 1.0D, false) {
        public void resetTask() {
            super.resetTask();
            EntityMobBase.this.setSwingingArms(false);
        }

        public void startExecuting() {
            super.startExecuting();
            EntityMobBase.this.setSwingingArms(true);
        }
    };
    private boolean doStrafeOnSpells;

    public EntityMobBase(World world) {
        super(world);
        this.experienceValue = 10;
        doStrafeOnSpells = false;
        this.setSize(0.6F, 1.99F);
        this.setCombatTask();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
            setDropChance(slot, 0.0f);
        }
    }

    public void setupMKMobData(IMobData mobData) {}

    @Override
    protected void initEntityAI() {
        MobData data = (MobData) MKUMobData.get(this);
        setupMKMobData(data);
        this.tasks.addTask(1, new EntityAIReturnToSpawn(this, data, 1.0));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(3, new EntityAIBuffTeammates(this, data,
                5 * GameConstants.TICKS_PER_SECOND).setStrafe(doStrafeOnSpells));
        this.tasks.addTask(4, new EntityAISpellAttack(this, data,
                5 * GameConstants.TICKS_PER_SECOND).setStrafe(true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTargetMK(
                this, true, Targeting.TargetType.ENEMY));
        this.targetTasks.addTask(1, new EntityAIHurtByTargetMK(this, true));
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }

    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getFallSound(int fallStrength) {
        return fallStrength > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    public boolean attackEntityFrom(DamageSource source, float damage) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, damage);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;
        if (target instanceof EntityLivingBase) {
            damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
                    ((EntityLivingBase)target).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }
        boolean didAttack = target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (didAttack) {
            if (i > 0 && target instanceof EntityLivingBase) {
                ((EntityLivingBase)target).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int fireAspectModifier = EnchantmentHelper.getFireAspectModifier(this);
            if (fireAspectModifier > 0) {
                target.setFire(fireAspectModifier * 4);
            }

            if (target instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)target;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;
                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, target);
        }
        return didAttack;
    }

    @Override
    public float getBlockPathWeight(BlockPos blockPos) {
        return 0.5F;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(4.0);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED)
                .setBaseValue(4.0);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 1.74F;
    }

    @Override
    public double getYOffset() {
        return -0.6D;
    }

    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms() {
        return this.dataManager.get(SWINGING_ARMS);
    }

    @Override
    public void setSwingingArms(boolean isSwinging) {
        this.dataManager.set(SWINGING_ARMS, isSwinging);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float damage) {
        EntityArrow arrow = this.getArrow(damage);
        double xDist = target.posX - this.posX;
        double yDist = target.getEntityBoundingBox().minY + (double)(target.height / 4.0F) - arrow.posY;
        double zDist = target.posZ - this.posZ;
        double dist = (double)MathHelper.sqrt(xDist * xDist + zDist * zDist);
        arrow.shoot(xDist, yDist + dist * 0.20000000298023224D, zDist, 1.6F,
                (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F,
                1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(arrow);
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack mainhand = this.getHeldItemMainhand();
            if (mainhand.getItem() instanceof ItemBow) {
                int cooldown = 20;
                if (this.world.getDifficulty() != EnumDifficulty.HARD) {
                    cooldown = 40;
                }
                this.aiArrowAttack.setAttackCooldown(cooldown);
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }

    protected EntityArrow getArrow(float damage) {
        ItemStack inOffhand = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        if (inOffhand.getItem() == Items.SPECTRAL_ARROW) {
            EntitySpectralArrow spectralArrow = new EntitySpectralArrow(this.world, this);
            spectralArrow.setEnchantmentEffectsFromEntity(this, damage);
            return spectralArrow;
        } else {
            EntityTippedArrow tippedArrow = new EntityTippedArrow(this.world, this);
            tippedArrow.setEnchantmentEffectsFromEntity(this, damage);
            if (inOffhand.getItem() == Items.TIPPED_ARROW) {
                tippedArrow.setPotionEffect(inOffhand);
            }
            return tippedArrow;
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setCombatTask();
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack itemStack) {
        super.setItemStackToSlot(slot, itemStack);
        if (!this.world.isRemote && slot == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SWINGING_ARMS, false);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected SoundEvent getStepSound() { return SoundEvents.BLOCK_GRASS_STEP; }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player,
                                                   Vec3d vec,
                                                   EnumHand hand) {

        IMobData data = MKUMobData.get(this);
        if (data != null && data.hasSpawnPoint()){
            BlockPos spawnPoint = data.getSpawnPoint();
            TileEntity spawner = player.world.getTileEntity(spawnPoint);
            if (spawner != null && spawner instanceof TileEntityNPCSpawner){
                if (!world.isRemote && player instanceof EntityPlayerMP){
                    if (player.isSneaking()){
                        player.openGui(MKUltra.INSTANCE,
                                ModGuiHandler.NPC_SPAWNER_EQUIPMENT_SCREEN, world,
                                spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
                    } else {
                        MKUltra.packetHandler.sendTo(new OpenLearnClassTileEntityPacket(spawnPoint),
                                (EntityPlayerMP) player);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.PASS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void updateRidden() {
        super.updateRidden();
        if (this.getRidingEntity() instanceof EntityCreature) {
            EntityCreature mount = (EntityCreature)this.getRidingEntity();
            this.renderYawOffset = mount.renderYawOffset;
        }
    }

    static {
        SWINGING_ARMS = EntityDataManager.createKey(EntityMobBase.class, DataSerializers.BOOLEAN);
    }
}
