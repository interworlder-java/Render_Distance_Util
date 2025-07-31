package rinat.render_distance_util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import rinat.render_distance_util.ClientCommands.SetRenderDistance;

public class RenderDistanceUtilClient implements ClientModInitializer, ClientTickEvents.EndTick {
	public static KeyBinding first_render_distance, second_render_distance, third_render_distance;
	public static KeyBinding config_key;

	public static Config.ConfigData config_data;

	@Override
	public void onInitializeClient() {
		config_data = Config.getConfigData();

		registerKeybinds();

		ClientTickEvents.END_CLIENT_TICK.register(this);

		ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) -> {
			commandDispatcher.register(ClientCommandManager.literal("set_render_distance")
					.then(ClientCommandManager.literal("min")
							.executes(SetRenderDistance::SetMinimalRenderDistance))
					.then(ClientCommandManager.literal("max")
							.executes(SetRenderDistance::SetMaximumRenderDistance))
					.then(ClientCommandManager.literal("set")
							.then(ClientCommandManager.argument("value", IntegerArgumentType.integer())
									.executes(SetRenderDistance::SetRenderDistance))));
		});
	}

	private void registerKeybinds() {
		config_key = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.render-distance-util.config_key",
						InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.config_key"));

		first_render_distance = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.render-distance-util.config_key.first_config",
						InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.config_key"));
		second_render_distance = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.render-distance-util.config_key.second_config",
						InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.config_key"));
		third_render_distance = KeyBindingHelper.registerKeyBinding(
				new KeyBinding("key.render-distance-util.config_key.third_config",
						InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.config_key"));
	}

	@Override
	public void onEndTick(MinecraftClient client) {
		if (config_key.isPressed()) {
			config_key.setPressed(false);
			client.setScreen(Config.createConfigScreen(client.currentScreen));
		}

		if (first_render_distance != null && first_render_distance.wasPressed()) {
			changeRenderDistance(Config.getConfigData().first_configured_render_distance);
		} else if (second_render_distance != null && second_render_distance.wasPressed()) {
			changeRenderDistance(Config.getConfigData().second_configured_render_distance);
		} else if (third_render_distance != null && third_render_distance.wasPressed()) {
			changeRenderDistance(Config.getConfigData().third_configured_render_distance);
		}
	}


	private  void changeRenderDistance(int renderDistance) {

		MinecraftClient.getInstance().options.getViewDistance().setValue(renderDistance);
		MinecraftClient.getInstance().player.sendMessage(Text.literal("Now render distance on " + renderDistance), false);
	}
}