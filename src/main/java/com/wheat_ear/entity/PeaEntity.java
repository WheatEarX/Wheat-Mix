package com.wheat_ear.entity;

import com.wheat_ear.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PeaEntity extends ProjectileEntity implements FlyingItemEntity {
    public PeaEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static PeaEntity create(EntityType<PeaEntity> entityType, World world) {
        return new PeaEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.onCollision(hitResult);
        this.updateRotation();

        if (this.getWorld().getStatesInBox(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
            this.discard();
        }
        else {
            Vec3d vec3d = this.getVelocity();
            double x = this.getX() + vec3d.x;
            double y = this.getY() + vec3d.y;
            double z = this.getZ() + vec3d.z;
            this.setVelocity(vec3d.multiply(0.99));
            this.setPosition(x, y, z);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            entity.damage(getDamageSources().mobProjectile(this, livingEntity), 4);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    public ItemStack getStack() {
        return ModItems.PEA.getDefaultStack();
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setPosition(packet.getX(), packet.getY() + 0.8, packet.getZ());
    }

    @Override
    protected void initDataTracker() {
    }
}
