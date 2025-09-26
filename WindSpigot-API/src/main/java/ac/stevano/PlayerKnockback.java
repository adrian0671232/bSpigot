package ac.stevano;

import dev.cobblesword.nachospigot.knockback.KnockbackProfile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class PlayerKnockback {
    private final Player player;

    public PlayerKnockback(Player player) {
        this.player = player;
    }

    public void setKnockback(KnockbackProfile profile) {
        player.setKnockbackProfile(profile);
    }

    public KnockbackProfile getKnockback() {
        return player.getKnockbackProfile();
    }

    public static Set<String> getAllProfileNames() {
        File file = new File("knockback.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("knockback.profiles");
        return (section != null) ? section.getKeys(false) : Collections.emptySet();
    }
}