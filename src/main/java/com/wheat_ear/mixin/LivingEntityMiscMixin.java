package com.wheat_ear.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.wheat_ear.effect.ModEffects;
import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
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
        int fireLevel, gravityCancellationLevel = 0;
        while (itemStacks.hasNext()) {
            itemStack = itemStacks.next();
            fireLevel = EnchantmentHelper.getLevel(ModEnchantments.FIRE, itemStack);
            // New
            gravityCancellationLevel = EnchantmentHelper.getLevel(ModEnchantments.GRAVITY_CANCELLATION, itemStack);
            if (fireLevel > 0) {
                this.setFireTicks(20);
            }
            if (gravityCancellationLevel > 0) {
                ModUtil.setGravityCancelled(this);
                break;
            }
        }
        if (gravityCancellationLevel <= 0) {
            this.setNoGravity(false);
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

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;", shift = At.Shift.AFTER))
    protected void travel1(Vec3d movementInput, CallbackInfo ci, @Local double d) {
        if (this.getVelocity().y <= 0.0 && this.hasStatusEffect(ModEffects.QUICK_FALLING)) {
            d += (this.getStatusEffect(ModEffects.QUICK_FALLING).getAmplifier() + 1) * 0.06;
        }
    }

    @Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isTouchingWater()Z"))
    protected void travel2(Vec3d movementInput, CallbackInfo ci, @Local double f) {
        if (this.hasStatusEffect(ModEffects.SLOW_SWIMMING)) {
            f -= (this.getStatusEffect(ModEffects.SLOW_SWIMMING).getAmplifier() + 1) * 0.05F;
        }
    }
}
