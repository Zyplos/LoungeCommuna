package dev.zyplos.loungecommuna;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Utils {

    public Component prefixedMessage() {
        return Component.text("").color(TextColor.color(0xffffff))
            .append(Component.text("[", TextColor.color(0x848484)))
            .append(Component.text("Server", TextColor.color(0xff3e3e)))
            .append(Component.text("] ", TextColor.color(0x848484))).asComponent();
    }

    public Component formatErrorMessage(String msg) {
        return Component.text(msg, TextColor.color(0xfa947d)).asComponent();
    }
}
