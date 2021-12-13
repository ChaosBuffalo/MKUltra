package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.entities.BaseProjectileEntity;
import com.chaosbuffalo.mkcore.fx.particles.ParticleAnimation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class TrailProjectileEntity extends BaseProjectileEntity {
    protected ParticleAnimation trailAnimation;

    public TrailProjectileEntity(EntityType<? extends ProjectileEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void clientGraphicalUpdate(float partialTicks) {
        if (getTrailAnimation() != null){
            double x = MathHelper.lerp(partialTicks, this.prevPosX, this.getPosX());
            double y = MathHelper.lerp(partialTicks, this.prevPosY, this.getPosY());
            double z = MathHelper.lerp(partialTicks, this.prevPosZ, this.getPosZ());
            getTrailAnimation().spawn(getEntityWorld(), new Vector3d(x, y, z), null);
        }
    }

    public void setTrailAnimation(ParticleAnimation trailAnimation) {
        this.trailAnimation = trailAnimation;
    }

    @Nullable
    public ParticleAnimation getTrailAnimation() {
        return trailAnimation;
    }
}
