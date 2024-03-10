package com.wheat_ear.mixin;

import com.wheat_ear.effect.ModEffects;
import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;


@SuppressWarnings("DataFlowIssue")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMiscMixin extends Entity {
    public LivingEntityMiscMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Unique
    @Override
    public void setFireTicks(int fireTicks) {
        super.setFireTicks(fireTicks);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract void setStackInHand(Hand hand, ItemStack stack);

    @Shadow
    protected abstract float getBaseMovementSpeedMultiplier();

    @Shadow
    protected abstract boolean shouldSwimInFluids();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Shadow
    public abstract boolean canWalkOnFluid(FluidState state);

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract boolean isClimbing();

    @Shadow
    public abstract Vec3d applyFluidMovingSpeed(double gravity, boolean falling, Vec3d motion);

    @Shadow
    public abstract boolean isFallFlying();

    @Shadow
    protected abstract SoundEvent getFallSound(int distance);

    @Shadow
    public abstract Vec3d applyMovementInput(Vec3d movementInput, float slipperiness);

    @Shadow
    public abstract boolean hasNoDrag();

    @Shadow
    public abstract void updateLimbs(boolean flutter);

    @Inject(method = "jump", at = @At("RETURN"))
    public void jump(CallbackInfo ci) {
        Iterator<ItemStack> itemStacks = this.getArmorItems().iterator();
        ItemStack itemStack;
        int explosionLevel, projectileLevel;
        while (itemStacks.hasNext()) {
            itemStack = itemStacks.next();
            explosionLevel = EnchantmentHelper.getLevel(ModEnchantments.EXPLOSION, itemStack);
            projectileLevel = EnchantmentHelper.getLevel(ModEnchantments.PROJECTILE, itemStack);
            if (explosionLevel > 0) {
                this.createExplosion((float) explosionLevel * 2.5F, (explosionLevel > 2));
            }
            if (projectileLevel > 0) {
                for (int t = 0; t < projectileLevel; t++) {
                    this.createRandomProjectile();
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.DISAPPEAR, this.getStackInHand(Hand.MAIN_HAND)) > 0) {
            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.AIR));
        }
    }

    @Inject(method = "tickMovement", at = @At("RETURN"))
    public void tickMovement(CallbackInfo ci) {
        Iterator<ItemStack> itemStacks = this.getArmorItems().iterator();
        ItemStack itemStack;
        int fireLevel, gravityCancellationLevel;
        while (itemStacks.hasNext()) {
            itemStack = itemStacks.next();
            fireLevel = EnchantmentHelper.getLevel(ModEnchantments.FIRE, itemStack);

            gravityCancellationLevel = EnchantmentHelper.getLevel(ModEnchantments.GRAVITY_CANCELLATION, itemStack);

            if (fireLevel > 0) {
                this.setFireTicks(20);
                break;
            }
            if (gravityCancellationLevel > 0) {
                ModUtil.setGravityCancelled(this);
                break;
            }
        }
    }

    @Unique
    private void createExplosion(float power, boolean createFire) {
        Explosion explosion = new Explosion(this.getWorld(), this,
                this.getX(), this.getY(), this.getZ(),
                power, createFire, Explosion.DestructionType.DESTROY);
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(true);
    }

    @Unique
    private void createRandomProjectile() {
        ProjectileEntity projectile;
        World world = this.getWorld();
        projectile = switch (MathHelper.nextInt(Random.create(), 0, 9)) {
            case 0 -> new ArrowEntity(world, (LivingEntity) (Entity) this, new ItemStack(Items.AIR));
            case 1 -> new EggEntity(world, (LivingEntity) (Entity) this);
            case 2 -> new FireballEntity(world, (LivingEntity) (Entity) this, 0.0, 0.05, 0.0, 1);
            case 3 -> new SnowballEntity(world, (LivingEntity) (Entity) this);
            case 4 -> new DragonFireballEntity(world, (LivingEntity) (Entity) this, 0.0, 0.05, 0.0);
            case 5 -> new ExperienceBottleEntity(world, (LivingEntity) (Entity) this);
            case 6 -> new SmallFireballEntity(world, (LivingEntity) (Entity) this, 0.0, 0.05, 0.0);
            case 7 ->
                    new FireworkRocketEntity(world, getX(), getY() + 0.1, getZ(), new ItemStack(Items.FIREWORK_ROCKET));
            case 8 -> new ShulkerBulletEntity(world, (LivingEntity) (Entity) this, this, Direction.Axis.Y);
            case 9 -> new TridentEntity(world, (LivingEntity) (Entity) this, new ItemStack(Items.AIR));
            default -> throw new IllegalStateException("Unexpected value");
        };
        world.spawnEntity(projectile);
    }

    @Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
    public void getGroup(CallbackInfoReturnable<EntityGroup> ci) {
        Iterator<ItemStack> itemStacks = this.getArmorItems().iterator();
        ItemStack itemStack;
        while (itemStacks.hasNext()) {
            itemStack = itemStacks.next();
            if (EnchantmentHelper.getLevel(ModEnchantments.UNDEAD, itemStack) > 0) {
                ci.setReturnValue(EntityGroup.UNDEAD);
            }
            if (EnchantmentHelper.getLevel(ModEnchantments.ARTHROPOD, itemStack) > 0) {
                ci.setReturnValue(EntityGroup.ARTHROPOD);
            }
        }
    }

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void computeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        if (this.getType().isIn(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            cir.setReturnValue(0);
        } else {
            StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
            float f = statusEffectInstance == null ? 0.0F : (float) (statusEffectInstance.getAmplifier() + 1);
            cir.setReturnValue(MathHelper.ceil((fallDistance - 3.0F + f) * damageMultiplier));
        }
    }

    /**
     * @author Wheat_ear
     * @reason That's true
     */

    @Overwrite
    public float getJumpBoostVelocityModifier() {
        float amplifier = 0.0F;
        if (this.hasStatusEffect(ModEffects.JUMP_REDUCTION)) {
            amplifier -= 0.09F * ((float) this.getStatusEffect(ModEffects.JUMP_REDUCTION).getAmplifier() + 1.0F);
        }
        if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            amplifier += 0.1F * ((float) this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1.0F);
        }
        return amplifier;
    }

    @Inject(method = "modifyAppliedDamage", at = @At("HEAD"), cancellable = true)
    protected void modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (this.hasStatusEffect(ModEffects.RESISTANCE_REDUCTION) || (this.hasStatusEffect(ModEffects.QUICK_FALLING) && source.isIn(DamageTypeTags.IS_FALL))) {
            double i = 1.0;
            if (this.hasStatusEffect(ModEffects.RESISTANCE_REDUCTION)) {
                i *= (this.getStatusEffect(ModEffects.RESISTANCE_REDUCTION).getAmplifier() + 1) * 1.5;
            }
            if (this.hasStatusEffect(ModEffects.QUICK_FALLING)) {
                i *= (this.getStatusEffect(ModEffects.QUICK_FALLING).getAmplifier() + 1) * 1.8;
            }
            float f = amount * (float) i;
            amount = Math.max(f, 0.0F);
            if (f > 0.0F && f < 3.4028235E37F) {
                if ((Entity) this instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) (Entity) this).increaseStat(Stats.DAMAGE_RESISTED, Math.round(f * 10.0F));
                } else if (source.getAttacker() instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) source.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f * 10.0F));
                }
            }
            if (amount <= 0.0F) {
                cir.setReturnValue(0.0F);
            }
            cir.setReturnValue(amount);
        }
    }

    /**
     * @author Wheat_ear
     * @reason The only way to do that. Do not worry.
     */
    @SuppressWarnings("deprecation")
    @Overwrite
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement()) {
            double d = 0.08;
            boolean bl = this.getVelocity().y <= 0.0;
            if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01;
            }
            if (bl && this.hasStatusEffect(ModEffects.QUICK_FALLING)) {
                d += (this.getStatusEffect(ModEffects.QUICK_FALLING).getAmplifier() + 1) * 0.06;
            }

            FluidState fluidState = this.getWorld().getFluidState(this.getBlockPos());
            float f;
            double e;
            if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState)) {
                e = this.getY();
                f = this.isSprinting() ? 0.9F : this.getBaseMovementSpeedMultiplier();
                float g = 0.02F;
                float h = (float) EnchantmentHelper.getDepthStrider((LivingEntity) (Entity) this);
                if (h > 3.0F) {
                    h = 3.0F;
                }

                if (!this.isOnGround()) {
                    h *= 0.5F;
                }

                if (h > 0.0F) {
                    f += (0.54600006F - f) * h / 3.0F;
                    g += (this.getMovementSpeed() - g) * h / 3.0F;
                }

                if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    f = 0.96F;
                }

                if (this.hasStatusEffect(ModEffects.SLOW_SWIMMING)) {
                    f -= (this.getStatusEffect(ModEffects.SLOW_SWIMMING).getAmplifier() + 1) * 0.05F;
                }

                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }

                this.setVelocity(vec3d.multiply(f, 0.800000011920929, f));
                Vec3d vec3d2 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + 0.6000000238418579 - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.30000001192092896, vec3d2.z);
                }
            } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState)) {
                e = this.getY();
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d3;
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                    this.setVelocity(this.getVelocity().multiply(0.5, 0.800000011920929, 0.5));
                    vec3d3 = this.applyFluidMovingSpeed(d, bl, this.getVelocity());
                    this.setVelocity(vec3d3);
                } else {
                    this.setVelocity(this.getVelocity().multiply(0.5));
                }

                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, -d / 4.0, 0.0));
                }

                vec3d3 = this.getVelocity();
                if (this.horizontalCollision && this.doesNotCollide(vec3d3.x, vec3d3.y + 0.6000000238418579 - this.getY() + e, vec3d3.z)) {
                    this.setVelocity(vec3d3.x, 0.30000001192092896, vec3d3.z);
                }
            } else if (this.isFallFlying()) {
                this.limitFallDistance();
                Vec3d vec3d4 = this.getVelocity();
                Vec3d vec3d5 = this.getRotationVector();
                f = this.getPitch() * 0.017453292F;
                double i = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                double j = vec3d4.horizontalLength();
                double k = vec3d5.length();
                double l = Math.cos(f);
                l = l * l * Math.min(1.0, k / 0.4);
                vec3d4 = this.getVelocity().add(0.0, d * (-1.0 + l * 0.75), 0.0);
                double m;
                if (vec3d4.y < 0.0 && i > 0.0) {
                    m = vec3d4.y * -0.1 * l;
                    vec3d4 = vec3d4.add(vec3d5.x * m / i, m, vec3d5.z * m / i);
                }

                if (f < 0.0F && i > 0.0) {
                    m = j * (double) (-MathHelper.sin(f)) * 0.04;
                    vec3d4 = vec3d4.add(-vec3d5.x * m / i, m * 3.2, -vec3d5.z * m / i);
                }

                if (i > 0.0) {
                    vec3d4 = vec3d4.add((vec3d5.x / i * j - vec3d4.x) * 0.1, 0.0, (vec3d5.z / i * j - vec3d4.z) * 0.1);
                }

                this.setVelocity(vec3d4.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision && !this.getWorld().isClient) {
                    m = this.getVelocity().horizontalLength();
                    double n = j - m;
                    float o = (float) (n * 10.0 - 3.0);
                    if (o > 0.0F) {
                        this.playSound(this.getFallSound((int) o), 1.0F, 1.0F);
                        this.damage(this.getDamageSources().flyIntoWall(), o);
                    }
                }

                if (this.isOnGround() && !this.getWorld().isClient) {
                    this.setFlag(7, false);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                float p = this.getWorld().getBlockState(blockPos).getBlock().getSlipperiness();
                f = this.isOnGround() ? p * 0.91F : 0.91F;
                Vec3d vec3d6 = this.applyMovementInput(movementInput, p);
                double q = vec3d6.y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    q += (0.05 * (double) (this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
                } else if (this.getWorld().isClient && !this.getWorld().isChunkLoaded(blockPos)) {
                    if (this.getY() > (double) this.getWorld().getBottomY()) {
                        q = -0.1;
                    } else {
                        q = 0.0;
                    }
                } else if (!this.hasNoGravity()) {
                    q -= d;
                }

                if (this.hasNoDrag()) {
                    this.setVelocity(vec3d6.x, q, vec3d6.z);
                } else {
                    this.setVelocity(vec3d6.x * (double) f, q * 0.9800000190734863, vec3d6.z * (double) f);
                }
            }
        }

        this.updateLimbs(this instanceof Flutterer);
    }
}