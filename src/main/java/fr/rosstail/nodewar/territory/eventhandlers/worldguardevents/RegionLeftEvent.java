package fr.rosstail.nodewar.territory.eventhandlers.worldguardevents;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.rosstail.nodewar.territory.zonehandlers.CapturePoint;
import fr.rosstail.nodewar.territory.zonehandlers.WorldTerritoryManager;
import fr.rosstail.nodewar.territory.eventhandlers.Reasons;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * event that is triggered after a player left a WorldGuard region
 * @author mewin<mewin001@hotmail.de>
 */
public class RegionLeftEvent extends RegionEvent
{
    /**
     * creates a new RegionLeftEvent
     * @param region the region the player has left
     * @param player the player who triggered the event
     * @param reason the type of movement how the player left the region
     */
    public RegionLeftEvent(ProtectedRegion region, Player player, Reasons reason, PlayerEvent parent) {
        super(region, player, reason, parent);
        World world = parent.getPlayer().getWorld();
        if (WorldTerritoryManager.getUsedWorlds().containsKey(world)) {
            WorldTerritoryManager.getUsedWorlds().get(world).getTerritories().forEach((s, territory) -> {
                if (territory.getRegion().equals(region)) {
                    territory.bossBarRemove(player);
                    territory.getPlayersOnTerritory().remove(player);
                }
                territory.getCapturePoints().values().forEach(capturePoint -> {
                    if (capturePoint.getRegion().equals(region)) {
                        capturePoint.bossBarRemove(player);
                        capturePoint.getPlayersOnPoint().remove(player);
                    }
                });
            });
        }
    }
}