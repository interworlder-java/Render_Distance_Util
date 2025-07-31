package rinat.render_distance_util.ClientCommands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class SetRenderDistance {
    public static int SetMinimalRenderDistance(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient.getInstance().options.getViewDistance().setValue(2);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Now render distance on 2(min)"), false);

        return 1;
    }

    public static int SetMaximumRenderDistance(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient.getInstance().options.getViewDistance().setValue(32);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Now render distance on 32(max)"), false);

        return 1;
    }

    public static int SetRenderDistance(CommandContext<FabricClientCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");

        if (value < 2 || value > 32) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("You must input number from 2 to 32"), false);

            return 1;
        }

        MinecraftClient.getInstance().options.getViewDistance().setValue(value);
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Now render distance on " + value), false);

        return 1;
    }
}
