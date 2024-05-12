package com.wheat_ear.entity;

import com.wheat_ear.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Random;

public class ZombieEggEntity extends ThrownItemEntity {
    public ZombieEggEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ZombieEggEntity(World world, LivingEntity owner) {
        super(ModEntityType.ZOMBIE_EGG, owner, world);
    }

    public static ZombieEggEntity create(EntityType<ZombieEggEntity> entityType, World world) {
        return new ZombieEggEntity(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        entityHitResult.getEntity().damage(this.getDamageSources().thrown(this, this.getOwner()), 6.0F);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            double d = 0.08;

            Random random = new Random();

            int r = random.nextInt(64);
            int i = 0;

            if (r < 16) {
                i = 1;
            }
            if (r < 8) {
                i = 2;
            }
            if (r < 4) {
                i = 4;
            }
            if (r < 2) {
                i = 8;
            }
            if (r < 1) {
                i = 16;
            }

            for (int j = 0; j < i; ++j) {
                ZombieEntity zombieEntity = new ZombieEntity(getWorld());
                zombieEntity.setBaby(true);

                zombieEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                this.getWorld().spawnEntity(zombieEntity);
            }

            for (int j = 0; j < 8; ++j) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5) * d, (this.random.nextFloat() - 0.5) * d, (this.random.nextFloat() - 0.5) * d);
            }

            this.discard();
        }
    }

    @Override
    public Item getDefaultItem() {
        return ModItems.ZOMBIE_EGG;
    }
}
