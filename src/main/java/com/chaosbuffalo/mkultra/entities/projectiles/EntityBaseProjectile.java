package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IThrowableEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;


public abstract class EntityBaseProjectile extends Entity implements IProjectile, IThrowableEntity {
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    protected boolean inGround;
    public int throwableShake;
    /**
     * The entity that threw this throwable item.
     */
    private EntityLivingBase thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    private int deathTime;
    private int airProcTime;
    private boolean doAirProc;
    private int groundProcTime;
    private boolean doGroundProc;
    private int amplifier;

    public EntityBaseProjectile(World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setDeathTime(100);
        this.setDoGroundProc(false);
        this.setGroundProcTime(20);
        this.setSize(.20f, .20f);
        this.setAirProcTime(20);
        this.setDoAirProc(false);
        this.setAmplifier(0);

    }

    public EntityBaseProjectile(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityBaseProjectile(World worldIn, EntityLivingBase throwerIn) {
        this(worldIn, throwerIn.posX, throwerIn.posY + (double) throwerIn.getEyeHeight() - 0.10000000149011612D,
                throwerIn.posZ);
        this.thrower = throwerIn;
    }

    public EntityBaseProjectile(World worldIn, EntityLivingBase throwerIn, double verticalOffset) {
        this(worldIn, throwerIn.posX, throwerIn.posY + verticalOffset,
                throwerIn.posZ);
        this.thrower = throwerIn;
    }

    protected abstract Targeting.TargetType getTargetType();

    protected boolean shouldExcludeCaster() {
        return false;
    }


    protected void entityInit() {
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setAmplifier(int newVal) {
        this.amplifier = newVal;
    }

    public boolean getDoGroundProc() {
        return this.doGroundProc;
    }

    public boolean getDoAirProc() {
        return this.doAirProc;
    }

    public int getTicksInAir() {
        return this.ticksInAir;
    }

    public int getTicksInGround() {
        return this.ticksInGround;
    }

    public void setTicksInAir(int newVal) {
        this.ticksInAir = newVal;
    }

    public void setTicksInGround(int newVal) {
        this.ticksInGround = newVal;
    }

    public void setDoAirProc(boolean newVal) {
        this.doAirProc = newVal;
    }

    public int getAirProcTime() {
        return this.airProcTime;
    }

    public void setAirProcTime(int newVal) {
        this.airProcTime = newVal;
    }

    public int getDeathTime() {
        return this.deathTime;
    }

    public void setDeathTime(int newVal) {
        this.deathTime = newVal;
    }

    public void setDoGroundProc(boolean newVal) {
        this.doGroundProc = newVal;
    }

    public int getGroundProcTime() {
        return this.groundProcTime;
    }

    public void setGroundProcTime(int newVal) {
        this.groundProcTime = newVal;
    }

    /**
     * Checks if the entity is in range to render.
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    /**
     * Sets throwable heading based on an entity that's throwing it
     */
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn,
                      float pitchOffset, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
        this.motionX += entityThrower.motionX;
        this.motionZ += entityThrower.motionZ;

        if (!entityThrower.onGround) {
            this.motionY += entityThrower.motionY;
        }
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
        this.ticksInGround = 0;
    }

    /**
     * Updates the velocity of the entity to a new value.
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
        }
    }

    /**
     * Called to onTick the entity's position/logic.
     */
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.throwableShake > 0) {
            --this.throwableShake;
        }
        if (this.ticksExisted == this.getDeathTime()) {
            this.setDead();
        }

        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState iblockstate = this.world.getBlockState(blockpos);

        if (this.inGround) {
            if (iblockstate.getBlock() == this.inTile) {
                ++this.ticksInGround;
                if (this.getDoGroundProc() && this.ticksInGround > 0 && this.ticksInGround % this.getGroundProcTime() == 0) {
                    if (this.onGroundProc(this.getThrower(), this.getAmplifier())) {
                        this.setDead();
                    }
                }

                return;
            }
            this.inGround = false;
            this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        } else {
            ++this.ticksInAir;
            if (this.getDoAirProc() && this.ticksInAir == this.getAirProcTime()) {
                if (this.onAirProc(this.getThrower(), this.getAmplifier())) {
                    this.setDead();
                }
            }

            Vec3d traceStart = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d traceEnd = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult trace = this.world.rayTraceBlocks(traceStart, traceEnd, false, true, false);
            traceStart = new Vec3d(this.posX, this.posY, this.posZ);
            traceEnd = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (trace != null) {
                traceEnd = new Vec3d(trace.hitVec.x, trace.hitVec.y, trace.hitVec.z);
            }

            trace = checkRayTraceEntities(traceStart, traceEnd, trace);

            if (trace != null) {
                if (trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    IBlockState hitState = this.world.getBlockState(trace.getBlockPos());
                    if (isValidBlockCollision(trace.getBlockPos())) {
                        BlockPos blockposOfHit = trace.getBlockPos();

                        AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockposOfHit);
                        if (axisalignedbb != Block.NULL_AABB &&
                                axisalignedbb.offset(blockposOfHit).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                            this.inGround = true;
                            this.inTile = hitState.getBlock();
                            this.motionX = 0.0;
                            this.motionY = 0.0;
                            this.motionZ = 0.0;
                        }

                        this.xTile = blockposOfHit.getX();
                        this.yTile = blockposOfHit.getY();
                        this.zTile = blockposOfHit.getZ();
                    }
                }

                if (trace.typeOfHit != RayTraceResult.Type.MISS) {
                    if (this.onImpact(this.getThrower(), trace, this.getAmplifier())) {
                        this.setDead();
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));

            while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
                this.prevRotationPitch -= 360.0F;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            float drag;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) f3,
                            this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX,
                            this.motionY, this.motionZ);
                }

                drag = getWaterDrag();
            } else {
                drag = getFlightDrag();
            }

            this.motionX *= (double) drag;
            this.motionY *= (double) drag;
            this.motionZ *= (double) drag;
            if (!this.inGround && !world.isRemote) {
                this.motionY -= this.getGravityVelocity();
            }
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private RayTraceResult checkRayTraceEntities(Vec3d traceStart, Vec3d traceEnd, RayTraceResult blockTrace) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
                this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D),
                e -> isValidEntityTarget(e));
        double shortestEntityDistance = 0.0D;

        for (Entity entity1 : list) {
            if (!entity1.canBeCollidedWith()) {
                continue;
            }
            if (isValidEntityTarget(entity1)) {
                AxisAlignedBB mobHitBB = entity1.getEntityBoundingBox().grow(0.3);
                RayTraceResult traceToMob = mobHitBB.calculateIntercept(traceStart, traceEnd);

                if (traceToMob != null) {
                    double distToEntity = traceStart.squareDistanceTo(traceToMob.hitVec);

                    if (distToEntity < shortestEntityDistance || shortestEntityDistance == 0.0D) {
                        entity = entity1;
                        shortestEntityDistance = distToEntity;
                    }
                }
            }
        }

        if (entity != null) {
            blockTrace = new RayTraceResult(entity);
        } else if (blockTrace != null && blockTrace.typeOfHit == RayTraceResult.Type.ENTITY) {
            // handle the case where we hit an entity but all returned invalid
            return null;
        }
        return blockTrace;
    }

    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    public float getGravityVelocity() {
        return 0.03F;
    }

    public float getFlightDrag() {
        return 0.99f;
    }

    public float getWaterDrag() {
        return 0.8f;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected boolean onImpact(EntityLivingBase caster, RayTraceResult result, int amplifier) {
        return false;
    }

    protected boolean onGroundProc(EntityLivingBase caster, int amplifier) {
        return false;
    }

    protected boolean onAirProc(EntityLivingBase caster, int amplifier) {
        return false;
    }

    private boolean isValidEntityTargetGeneric(Entity entity) {
        return entity != this && EntitySelectors.NOT_SPECTATING.apply(entity) &&
                EntitySelectors.IS_ALIVE.apply(entity);
    }

    protected boolean isValidEntityTarget(Entity entity) {
        if (entity instanceof EntityLivingBase && getThrower() != null) {
            return Targeting.isValidTarget(getTargetType(), getThrower(), entity, shouldExcludeCaster());
        }
        return isValidEntityTargetGeneric(entity);
    }

    protected boolean isValidBlockCollision(BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();

        return !canPassThroughBlock(block);
    }

    protected boolean canPassThroughBlock(Block block) {
        return block instanceof BlockBush ||
                block instanceof BlockReed ||
                block instanceof BlockLeaves;
    }


    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("shake", (byte) this.throwableShake);
        compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));


        if ((this.throwerName == null || this.throwerName.isEmpty()) && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getName();
        }

        compound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
        compound.setBoolean("doAirProc", this.getDoAirProc());
        compound.setBoolean("doGroundProc", this.getDoGroundProc());
        compound.setInteger("airProcTime", this.getAirProcTime());
        compound.setInteger("groundProcTime", this.getGroundProcTime());
        compound.setInteger("deathTime", this.getDeathTime());
        compound.setInteger("amplifier", this.getAmplifier());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");

        if (compound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        } else {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
        }

        this.throwableShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;
        this.thrower = null;
        this.throwerName = compound.getString("ownerName");

        if (this.throwerName != null && this.throwerName.isEmpty()) {
            this.throwerName = null;
        }

        this.thrower = this.getThrower();

        this.setDoAirProc(compound.getBoolean("doAirProc"));
        this.setDoGroundProc(compound.getBoolean("doGroundProc"));
        this.setAirProcTime(compound.getInteger("airProcTime"));
        this.setGroundProcTime(compound.getInteger("groundProcTime"));
        this.setDeathTime(compound.getInteger("deathTime"));
        this.setAmplifier(compound.getInteger("amplifier"));
    }

    public EntityLivingBase getThrower() {
        if (this.thrower == null && this.throwerName != null && !this.throwerName.isEmpty()) {
            this.thrower = this.world.getPlayerEntityByName(this.throwerName);

            if (this.thrower == null && this.world instanceof WorldServer) {
                try {
                    Entity entity = ((WorldServer) this.world).getEntityFromUuid(UUID.fromString(this.throwerName));

                    if (entity instanceof EntityLivingBase) {
                        this.thrower = (EntityLivingBase) entity;
                    }
                } catch (Throwable var2) {
                    this.thrower = null;
                }
            }
        }

        return this.thrower;
    }

    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.thrower = (EntityLivingBase) entity;
        }
    }
}
