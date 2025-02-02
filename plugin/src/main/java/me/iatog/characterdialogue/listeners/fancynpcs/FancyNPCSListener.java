package me.iatog.characterdialogue.listeners.fancynpcs;

import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.actions.ActionTrigger;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import de.oliver.fancynpcs.api.events.NpcSpawnEvent;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.adapter.AdaptedNPC;
import me.iatog.characterdialogue.adapter.AdapterManager;
import me.iatog.characterdialogue.adapter.NPCAdapter;
import me.iatog.characterdialogue.dialogs.method.npc_control.ControlRegistry;
import me.iatog.characterdialogue.dialogs.method.npc_control.NPCControlMethod;
import me.iatog.characterdialogue.enums.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;

public class FancyNPCSListener implements Listener {

    private final CharacterDialoguePlugin main;

    public FancyNPCSListener(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(NpcInteractEvent event) {
        AdapterManager manager = main.getAdapterManager();
        NPCAdapter<Npc> adapter = main.getAdapter();
        AdaptedNPC npc = adapter.adapt(event.getNpc());

        manager.handleInteractEvent(
              event.getPlayer(),
              npc,
              parseClickType(event.getInteractionType())
        );
    }

    @EventHandler
    public void onSpawn(NpcSpawnEvent event) {
        Npc npc = event.getNpc();
        Player player = event.getPlayer();
        Map<UUID, ControlRegistry> registries = NPCControlMethod.registries;
        String name = npc.getData().getName();

        if(name.endsWith("_cloned")) {
            ControlRegistry registry = registries.get(player.getUniqueId());
            if(registry != null && registry.findCopy(name) != null) {
                return;
            }

            event.setCancelled(true);
        } else {
            // Prevent visibility
            UUID playerId = player.getUniqueId();
            ControlRegistry registry = registries.get(playerId);

            if(registry != null && registry.isOnRegistry(name) &&
                  registry.get(name).isHideOriginal() && registry.get(name).getOriginal().getId().equalsIgnoreCase(name)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleSpawn(NpcSpawnEvent event) {
        AdaptedNPC npc = main.getAdapter().adapt(event.getNpc());
        boolean result = main.getAdapterManager().handleHideNPCs(npc, event.getPlayer());

        if(result) {
            event.setCancelled(true);
        }
    }

    private ClickType parseClickType(ActionTrigger action) {
        switch(action) {
            case LEFT_CLICK -> {
                return ClickType.LEFT;
            }
            case RIGHT_CLICK -> {
                return ClickType.RIGHT;
            }
            default -> {
                return ClickType.ALL;
            }
        }
    }
}
