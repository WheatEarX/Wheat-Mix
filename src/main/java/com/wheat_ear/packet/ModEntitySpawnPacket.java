package com.wheat_ear.packet;

import com.wheat_ear.WheatMix;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ModEntitySpawnPacket implements FabricPacket {
    public static final PacketType<ModEntitySpawnPacket> TYPE = PacketType.create(new Identifier(WheatMix.MOD_ID, "entity_spawn"), ModEntitySpawnPacket::new);
    private final EntityType<?> entityType;
    private final double x;
    private final double y;
    private final double z;
    private final int velocityX;
    private final int velocityY;
    private final int velocityZ;

    public ModEntitySpawnPacket(Entity entity) {
        this(entity.getX(), entity.getY(), entity.getZ(), entity.getType(), entity.getVelocity());
    }

    public ModEntitySpawnPacket(PacketByteBuf buf) {
        this.entityType = buf.readRegistryValue(Registries.ENTITY_TYPE);
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.velocityX = buf.readShort();
        this.velocityY = buf.readShort();
        this.velocityZ = buf.readShort();
    }

    public ModEntitySpawnPacket(double x, double y, double z, EntityType<?> entityType, Vec3d velocity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityType = entityType;
        this.velocityX = (int)(MathHelper.clamp(velocity.x, -3.9, 3.9) * 8000.0);
        this.velocityY = (int)(MathHelper.clamp(velocity.y, -3.9, 3.9) * 8000.0);
        this.velocityZ = (int)(MathHelper.clamp(velocity.z, -3.9, 3.9) * 8000.0);
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeRegistryValue(Registries.ENTITY_TYPE, this.entityType);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeShort(this.velocityX);
        buf.writeShort(this.velocityY);
        buf.writeShort(this.velocityZ);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
