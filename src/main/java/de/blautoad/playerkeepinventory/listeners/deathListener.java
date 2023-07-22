package de.blautoad.playerkeepinventory.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class deathListener implements Listener {
    private final NamespacedKey nk;

    public deathListener(NamespacedKey nk){
        this.nk = nk;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player p = event.getEntity();
            PersistentDataContainer pdc = p.getPersistentDataContainer();
            byte keepInventory = 0;
            if (pdc.has(nk, PersistentDataType.BYTE)) {
                keepInventory = pdc.get(nk, PersistentDataType.BYTE);
            }
            if (keepInventory == 1) {
                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.setDroppedExp(0);
                event.getDrops().removeAll(event.getDrops());
            }
        }
    }
}
