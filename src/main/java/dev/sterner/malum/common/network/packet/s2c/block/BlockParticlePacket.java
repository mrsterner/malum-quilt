package dev.sterner.malum.common.network.packet.s2c.block;

import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import dev.sterner.malum.Malum;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.awt.*;

public class BlockParticlePacket {
    public static final Identifier ID = new Identifier(Malum.MODID, "block_particle");

    public static void send(PlayerEntity player, Color color, BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(color.getRed());
        buf.writeInt(color.getGreen());
        buf.writeInt(color.getBlue());
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());

        ServerPlayNetworking.send((ServerPlayerEntity) player, ID, buf);
    }


    public static void handle(MinecraftClient client, ClientPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
        Color color = new Color(buf.readInt(), buf.readInt(), buf.readInt());
        BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        ClientWorld world = client.world;
        client.execute(() -> {
            ParticleBuilders.create(LodestoneParticles.WISP_PARTICLE)
                .setAlpha(0.12f, 0.16f, 0)
                .setLifetime(10)
                .setSpin(0.2f)
                .setScale(0.15f, 0.2f, 0)
                .setScaleEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                .setColor(color, color)
                .enableNoClip()
                .randomOffset(0.1f, 0.1f)
                .randomMotion(0.001f, 0.001f)
                .overwriteRemovalProtocol(SimpleParticleEffect.SpecialRemovalProtocol.ENDING_CURVE_INVISIBLE)
                .repeatSurroundBlock(world, pos, 6, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

            ParticleBuilders.create(LodestoneParticles.SMOKE_PARTICLE)
                .setAlpha(0.04f, 0.08f, 0)
                .setLifetime(15)
                .setSpin(0.1f)
                .setScale(0.35f, 0.4f, 0)
                .setScaleEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                .setColor(color, color)
                .randomOffset(0.2f)
                .enableNoClip()
                .randomMotion(0.001f, 0.001f)
                .overwriteRemovalProtocol(SimpleParticleEffect.SpecialRemovalProtocol.ENDING_CURVE_INVISIBLE)
                .repeatSurroundBlock(world, pos, 8, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        });
    }
}
