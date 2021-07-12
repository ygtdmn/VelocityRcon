package me.uniodex.velocityrcon.commandsource;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import static com.velocitypowered.api.permission.PermissionFunction.ALWAYS_TRUE;

public class IRconCommandSource implements RconCommandSource {

    private final StringBuffer buffer = new StringBuffer();
    private PermissionFunction permissionFunction = ALWAYS_TRUE;

    @Getter
    private ProxyServer server;

    public IRconCommandSource(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void sendMessage(@NonNull Component component) {
        buffer.append(LegacyComponentSerializer.legacySection().serialize(component)).append("\n");
    }

    @Override
    public @NonNull Tristate getPermissionValue(@NonNull String permission) {
        return this.permissionFunction.getPermissionValue(permission);
    }

    public String flush() {
        String result = buffer.toString();
        buffer.setLength(0);
        return result;
    }
}
