package me.uniodex.velocityrcon;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.uniodex.velocityrcon.utils.Utils;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import static com.velocitypowered.api.permission.PermissionFunction.ALWAYS_TRUE;

public class RconCommandSource implements CommandSource {

    private final StringBuffer buffer = new StringBuffer();
    private final PermissionFunction permissionFunction = ALWAYS_TRUE;

    @Getter
    private final ProxyServer server;

    public RconCommandSource(ProxyServer server) {
        this.server = server;
    }

    private void addToBuffer(Component message) {
        String txt = LegacyComponentSerializer.legacySection().serialize(message);
        txt = Utils.stripMcColor(txt);
        if (buffer.length() != 0)
            buffer.append("\n");
        buffer.append(txt);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message) {
        addToBuffer(message);
    }

    @Override
    public void sendMessage(@NonNull Component message) {
        addToBuffer(message);
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
