package ac.stevano;

import dev.cobblesword.nachospigot.knockback.KnockbackProfile;
import org.bukkit.entity.Player;

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
}