package rinat.render_distance_util;

import com.google.gson.Gson;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private static final Path config_dir = Paths.get(MinecraftClient.getInstance().runDirectory.getPath() + "/config");
    private static final Path config_file = Paths.get(config_dir + "/render-distance-util.json");
    private static ConfigData config_data;

    public static ConfigData getConfigData() {
        if (config_data != null) return config_data;

        try {
            if (!Files.exists(config_file)) {
                Files.createDirectories(config_dir);
                Files.createFile(config_file);
                config_data = ConfigData.getDefault();
                config_data.save();
                return config_data;
            }
        } catch (IOException e) {
            e.printStackTrace();
            config_data = ConfigData.getDefault();
            return config_data;
        }
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(config_file.toFile());
            config_data = gson.fromJson(reader, ConfigData.class);
        } catch (IOException e) {
            e.printStackTrace();
            config_data = ConfigData.getDefault();
        }
        return config_data;
    }

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("screen.render-distance-util.config.title"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.render-distance-util.category.general"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startIntSlider(
                                Text.translatable("config.render-distance-util.option.first_configured_render_distance"),
                                config_data.first_configured_render_distance,
                                2, 32
                        )
                        .setDefaultValue(2)
                        .setSaveConsumer(newValue -> config_data.first_configured_render_distance = newValue)
                        .build()
        );

        general.addEntry(entryBuilder.startIntSlider(
                                Text.translatable("config.render-distance-util.option.second_configured_render_distance"),
                                config_data.second_configured_render_distance,
                                2, 32
                        )
                        .setDefaultValue(16)
                        .setSaveConsumer(newValue -> config_data.second_configured_render_distance = newValue)
                        .build()
        );

        general.addEntry(entryBuilder.startIntSlider(
                                Text.translatable("config.render-distance-util.option.third_configured_render_distance"),
                                config_data.third_configured_render_distance,
                                2, 32
                        )
                        .setDefaultValue(32)
                        .setSaveConsumer(newValue -> config_data.third_configured_render_distance = newValue)
                        .build()
        );

        builder.setSavingRunnable(config_data::save);

        return builder.build();
    }

    public static class ConfigData {
        public int first_configured_render_distance;
        public int second_configured_render_distance;
        public int third_configured_render_distance;

        public ConfigData(int first_configured_render_distance, int second_configured_render_distance, int third_configured_render_distance) {
            this.first_configured_render_distance = first_configured_render_distance;
            this.second_configured_render_distance = second_configured_render_distance;
            this.third_configured_render_distance = third_configured_render_distance;
        }

        public static ConfigData getDefault() {
            return new ConfigData(2, 16, 32);
        }

        public void save() {
            try {
                Gson gson = new Gson();
                FileWriter writer = new FileWriter(config_file.toFile());
                gson.toJson(this, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}