import entity.TrackerZombie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TZPlugin extends JavaPlugin implements Listener {

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity != null) {
            TrackerZombie zombie = new TrackerZombie(entity.getLocation(), 1.5F);
            zombie.setTraceTarget(event.getPlayer());
            TrackerZombie.spawnEntity(zombie);
        }
    }
}
