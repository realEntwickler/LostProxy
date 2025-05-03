package eu.lostname.lostproxy.builder;

import eu.lostname.lostproxy.utils.$;
import net.md_5.bungee.api.chat.TextComponent;

public class DisconnectScreenBuilder {

    private final StringBuilder message;

    public DisconnectScreenBuilder()
    {
        this.message = new StringBuilder();
        message.append("§6•§e● LostName §8┃ §7our work is your experience §e●§6• \n \n");
    }

    public DisconnectScreenBuilder add(String message) {
        this.message.append(message);
        return this;
    }

    public DisconnectScreenBuilder newLine() {
        this.message.append("\n");
        return this;
    }

    public TextComponent build() {
        message.append("\n\n").append("§7Für weitere Fragen oder zum Stellen eines Entbannungsantrags besuche das Forum.").append("\n§8" + $.bigDot + " §e§nwww.lostname.eu/forum§r §8" + $.bigDot + "\n\n").append("§8§m--------------------------------------");
        return new TextComponent(message.toString());
    }
}
