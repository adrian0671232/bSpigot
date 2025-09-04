package ac.stevano;

import dev.cobblesword.nachospigot.knockback.KnockbackProfile;
import org.bukkit.entity.Player;

public class PlayerKnockback {
    private Player player;

    public PlayerKnockback(Player player) {
        this.player = player;
    }

    public void setKnockback(Player player, KnockbackProfile profile) {
        player.setKnockbackProfile(profile);
    }

    public KnockbackProfile getKnockback(Player player) {
        return player.getKnockbackProfile();
    }
}