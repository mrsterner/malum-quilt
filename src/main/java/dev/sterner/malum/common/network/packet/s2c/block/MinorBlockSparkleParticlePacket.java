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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.awt.*;

public class MinorBlockSparkleParticlePacket {
    public static final Identifier ID = new Identifier(Malum.MODID, "block_minor_sparkle_particle");

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
            var rand = world.random;
            for (int i = 0; i < 3; i++) {
                int spinDirection = (rand.nextBoolean() ? 1 : -1);
                int spinOffset = rand.nextInt(360);
                ParticleBuilders.create(LodestoneParticles.SMOKE_PARTICLE)
                    .setAlpha(0.02f, 0.095f, 0)
                    .setAlphaEasing(Easing.SINE_IN, Easing.CIRC_IN)
                    .setLifetime(50+rand.nextInt(10))
                    .setSpinOffset(spinOffset)
                    .setSpin((0.125f+rand.nextFloat()*0.075f)*spinDirection)
                    .setScale(0.25f, 0.45f, 0)
                    .setScaleEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                    .setColor(color, color)
                    .randomOffset(0.4f)
                    .enableNoClip()
                    .randomMotion(0.01f, 0.01f)
                    .overwriteRemovalProtocol(SimpleParticleEffect.SpecialRemovalProtocol.ENDING_CURVE_INVISIBLE)
                    .repeatRandomFace(world, pos, 1);
            }
        });
    }
}