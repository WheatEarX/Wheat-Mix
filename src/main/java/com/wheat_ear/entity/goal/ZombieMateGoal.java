package com.wheat_ear.entity.goal;

import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class ZombieMateGoal extends Goal {
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0).ignoreVisibility();
    protected final ZombieEntity zombieEntity;
    private final Class<? extends ZombieEntity> entityClass;
    private final World world;
    @Nullable
    protected ZombieEntity mate;
    private int timer;
    private final double speed;

    public ZombieMateGoal(ZombieEntity zombieEntity, double speed) {
        this(zombieEntity, speed, ZombieEntity.class);
    }

    public ZombieMateGoal(ZombieEntity zombieEntity, double speed, Class<? extends ZombieEntity> entityClass) {
        this.zombieEntity = zombieEntity;
        this.world = zombieEntity.getWorld();
        this.entityClass = entityClass;
        this.speed = speed;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public boolean canStart() {
        if (!isInLove(this.zombieEntity)) {
            return false;
        } else {
            this.mate = this.findMate();
            return this.mate != null;
        }
    }

    public boolean shouldContinue() {
        if (this.mate != null) {
            return this.mate.isAlive() && isInLove(this.mate) && this.timer < 60 && !this.mate.isPanicking();
        }
        return false;
    }

    public void stop() {
        this.mate = null;
        this.timer = 0;
    }

    public void tick() {
        this.zombieEntity.getLookControl().lookAt(this.mate, 10.0F, this.zombieEntity.getMaxLookPitchChange());
        this.zombieEntity.getNavigation().startMovingTo(this.mate, this.speed);
        ++this.timer;
        if (this.timer >= this.getTickCount(60) && this.zombieEntity.squaredDistanceTo(this.mate) < 9.0) {
            this.breed();
        }

    }

    @Nullable
    private ZombieEntity findMate() {
        List<? extends ZombieEntity> list = this.world.getTargets(this.entityClass, VALID_MATE_PREDICATE, this.zombieEntity, this.zombieEntity.getBoundingBox().expand(8.0));
        double d = Double.MAX_VALUE;
        ZombieEntity zombieEntity1 = null;

        for (ZombieEntity zombieEntity2 : list) {
            if ((isInLove(this.zombieEntity) && isInLove(zombieEntity2)) && !zombieEntity2.isPanicking() && this.zombieEntity.squaredDistanceTo(zombieEntity2) < d) {
                zombieEntity1 = zombieEntity2;
                d = this.zombieEntity.squaredDistanceTo(zombieEntity2);
            }
        }

        return zombieEntity1;
    }

    private void breed() {
        ModUtil.invokeMethod(ZombieEntity.class, this.zombieEntity, "breed",
                new Class[] { ServerWorld.class, ZombieEntity.class }, this.world, this.mate);
    }

    private boolean isInLove(ZombieEntity zombieEntity) {
        return ModUtil.getValue(ZombieEntity.class, int.class, zombieEntity, "loveTicks") > 0;
    }
}
