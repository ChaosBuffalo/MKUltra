package com.chaosbuffalo.mkultra.spawner;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiConsumer;

import static com.chaosbuffalo.mkultra.utils.MathUtils.lerp_double;

public class MKSpawnAttributes {

    public static BiConsumer<EntityLivingBase, AttributeRange> SET_AGGRO_RADIUS = (entity, range) -> {
        IMobData data = MKUMobData.get(entity);
        if (data != null){
            data.setAggroRange(lerp_double(range.start, range.stop, range.level, range.maxLevel));
        }
    };
}
