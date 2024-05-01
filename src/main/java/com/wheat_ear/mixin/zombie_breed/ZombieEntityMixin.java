package com.wheat_ear.mixin.zombie_breed;

import com.wheat_ear.entity.ai.goal.ZombieMateGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {
    @Shadow public abstract boolean isBaby();

    @Unique
    private int breedingAge = 0;
    @Unique
    private int loveTicks;

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (breedingAge > 0) {
            --breedingAge;
        }
    }

    @Inject(method = "tickMovement", at = @At("RETURN"))
    public void tickMovement(CallbackInfo ci) {
        if (this.getBreedingAge() != 0) {
            this.loveTicks = 0;
        }

        if (this.loveTicks > 0) {
            --this.loveTicks;
            if (this.loveTicks % 10 == 0) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        }
    }

    @Inject(method = "initCustomGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 2))
    public void initCustomGoals(CallbackInfo ci) {
        goalSelector.add(2, new ZombieMateGoal((ZombieEntity) (HostileEntity) this, 1.0));
    }

    @Unique
    @Override
    public void handleStatus(byte status) {
        if (status == 18) {
            for (int i = 0; i < 7; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }

    }

    @Unique
    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.IRON_INGOT)) {
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && loveTicks <= 0 && !this.isBaby()) {
                itemStack.decrement(1);
                setLoveTicks(1000);
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }

        return super.interactMob(player, hand);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Unique
    public void breed(ServerWorld world, ZombieEntity other) {
        ZombieEntity zombieEntity = EntityType.ZOMBIE.create(world);

        zombieEntity.setBaby(true);
        zombieEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
        world.spawnEntityAndPassengers(zombieEntity);

        setBreedingAge(2000);
        ((ZombieEntityMixin) (HostileEntity) other).setBreedingAge(2000);
        this.setLoveTicks(0);
        ((ZombieEntityMixin) (HostileEntity) other).setLoveTicks(0);
        world.sendEntityStatus(this, (byte) 18);

        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Unique
    public void setBreedingAge(int age) {
        this.breedingAge = age;
    }

    @Unique
    public int getBreedingAge() {
        return breedingAge;
    }

    @Unique
    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
    }
}
