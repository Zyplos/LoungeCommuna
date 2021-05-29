package dev.zyplos.loungecommuna.internals;

import dev.zyplos.loungecommuna.LoungeCommuna;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Utils {
    private final LoungeCommuna plugin;

    // https://stackoverflow.com/a/6802502
    public Map<Integer, String> communityNameMap = Map.of(
        1, "the lounge",
        2, "3 AM",
        3, "pegg group",
        4, "blaster group",
        5, "jay group",
        6, "vold group"
    );
    public Map<Integer, String> communityBrandColorMap = Map.of(
        1, "#ff3e3e",
        2, "#00a3a3",
        3, "#0094ff",
        4, "#7c00ff",
        5, "#f1f353",
        6, "#ffd300"
    );

    // chat color palette
    public Map<String, Integer> colors = Map.of(
        "muted", 0xbababa,
        "highlight", 0xffa631,
        "warning", 0xfa947d,
        "error", 0xff2929
    );

    public Utils(LoungeCommuna plugin) {
        this.plugin = plugin;
    }

    public String getCommunityName(int id) {
        return communityNameMap.get(id);
    }

    public String getCommunityBrandColor(int id) {
        return communityBrandColorMap.get(id);
    }

    public Component prefixedMessage() {
        return Component.text("").color(TextColor.color(0xffffff))
            .append(Component.text("[", TextColor.color(0x848484)))
            .append(Component.text("Server", TextColor.color(0xff3e3e)))
            .append(Component.text("] ", TextColor.color(0x848484))).asComponent();
    }

    public Component formatErrorMessage(String msg) {
        return Component.text(msg, TextColor.color(0xfa947d)).asComponent();
    }

    public void getPlayerHeadImage(String parsedUUID, AsyncCallback<BufferedImage> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://crafatar.com/avatars/" + parsedUUID + ".png?size=8&overlay");
                    BufferedImage image = ImageIO.read(url);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            callback.thenDo(image);
                        }
                    }.runTask(plugin);
                } catch (IOException e) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            callback.thenDo(null);
                        }
                    }.runTask(plugin);
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
