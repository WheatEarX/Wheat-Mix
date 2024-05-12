package com.wheat_ear.mixin.zombie_breed;

import com.wheat_ear.entity.goal.ZombieMateGoal;
import com.wheat_ear.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

import java.util.Random;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {
    @Shadow public abstract boolean isBaby();

    @Shadow public abstract void setBaby(boolean baby);

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
        if (itemStack.isOf(ModItems.VILLAGER_MEAT) && !this.getWorld().isClient) {
            int i = this.getBreedingAge();
            if (!this.getWorld().isClient && i == 0 && loveTicks <= 0 && !this.isBaby()) {
                itemStack.decrement(1);
                setLoveTicks(1000);
                addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0));

                return ActionResult.SUCCESS;
            }
            else if (isBaby()) {
                itemStack.decrement(1);
                if (random.nextInt(3) == 0) {
                    setBaby(false);
                }
                return ActionResult.SUCCESS;
            }
        }
        else if (getWorld().isClient) {
            return ActionResult.CONSUME;
        }

        return super.interactMob(player, hand);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Unique
    public void breed(ServerWorld world, ZombieEntity other) {
        ZombieEntity zombieEntity = EntityType.ZOMBIE.create(world);

        zombieEntity.setBaby(true);

        modifyAttributes(other, zombieEntity);

        zombieEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
        world.spawnEntityAndPassengers(zombieEntity);

        setBreedingAge(2000);
        ((ZombieEntityMixin) (HostileEntity) other).setBreedingAge(2000);
        this.setLoveTicks(500);
        ((ZombieEntityMixin) (HostileEntity) other).setLoveTicks(500);
        world.sendEntityStatus(this, (byte) 18);

        if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            world.spawnEntity(new ExperienceOrbEntity(world, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private void modifyAttributes(ZombieEntity other, ZombieEntity modifier) {
        AttributeContainer attribute1 = this.getAttributes();
        AttributeContainer attribute2 = other.getAttributes();
        AttributeContainer attribute3 = modifier.getAttributes();

        double speed1 = attribute1.getBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        double speed2 = attribute2.getBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        attribute3.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(getAttributeResult(speed1, speed2));

        double armor1 = attribute1.getBaseValue(EntityAttributes.GENERIC_ARMOR);
        double armor2 = attribute2.getBaseValue(EntityAttributes.GENERIC_ARMOR);
        attribute3.getCustomInstance(EntityAttributes.GENERIC_ARMOR).setBaseValue(getAttributeResult(armor1, armor2));

        double maxHealth1 = attribute1.getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
        double maxHealth2 = attribute2.getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
        attribute3.getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(getAttributeResult(maxHealth1, maxHealth2));

        double followRange1 = attribute1.getBaseValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        double followRange2 = attribute2.getBaseValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        attribute3.getCustomInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(getAttributeResult(followRange1, followRange2));

        double knockBackResistance1 = attribute1.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        double knockBackResistance2 = attribute2.getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        attribute3.getCustomInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(getAttributeResult(knockBackResistance1, knockBackResistance2));

        double zombieSpawnReinforcements1 = attribute1.getBaseValue(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
        double zombieSpawnReinforcements2 = attribute2.getBaseValue(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
        attribute3.getCustomInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(getAttributeResult(zombieSpawnReinforcements1, zombieSpawnReinforcements2));

        modifier.getAttributes().setFrom(attribute3);
    }

    @Unique
    private static double getAttributeResult(double a, double b) {
        double c = Math.max(a, b);

        Random random = new Random();
        c += c * random.nextDouble();

        return c;
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
