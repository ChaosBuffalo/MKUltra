package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkcore.entities.BaseProjectileEntity;
import com.chaosbuffalo.mkcore.fx.particles.ParticleAnimation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class TrailProjectileEntity extends BaseProjectileEntity {
    protected ParticleAnimation trailAnimation;

    public TrailProjectileEntity(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void clientGraphicalUpdate(float partialTicks) {
        if (getTrailAnimation() != null){
            double x = Mth.lerp(partialTicks, this.xo, this.getX());
            double y = Mth.lerp(partialTicks, this.yo, this.getY());
            double z = Mth.lerp(partialTicks, this.zo, this.getZ());
            getTrailAnimation().spawn(getCommandSenderWorld(), new Vec3(x, y, z), null);
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
