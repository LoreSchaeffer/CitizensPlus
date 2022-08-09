package it.multicoredev.cp.listeners;

import it.multicoredev.cp.CitizensPlus;
import it.multicoredev.cp.api.NPCInteractEvent;
import it.multicoredev.cp.model.Action;
import it.multicoredev.cp.model.NPCData;
import it.multicoredev.mclib.misc.Space3D;
import it.multicoredev.mclib.objects.Pos3D;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of CitizensPlus.
 * CitizensPlus is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class ActionListener implements Listener {
    private final CitizensPlus plugin;
    private final NPCRegistry npcRegistry;
    private final Random random = new Random();
    private final Map<Player, Map<Integer, Integer>> playerIndexes = new HashMap<>();

    public ActionListener(CitizensPlus plugin) {
        this.plugin = plugin;
        npcRegistry = CitizensAPI.getNPCRegistry();
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        NPCData data = plugin.npcs().getData(event.getNPC().getId());
        if (data != null)
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> runNPCActions(data, event.getNPC().getName(), event.getClicker(), true));
    }

    @EventHandler
    public void onNPCApproach(PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        final Player player = event.getPlayer();
        final List<Entity> entities = player.getNearbyEntities(16, 5, 16);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Map<NPC, Location> npcs = new HashMap<>();
            entities.forEach(entity -> {
                if (!npcRegistry.isNPC(entity)) return;
                npcs.put(npcRegistry.getNPC(entity), entity.getLocation());
            });

            npcs.forEach((npc, npcLocation) -> {
                NPCData data = plugin.npcs().getData(npc.getId());
                if (data == null) return;
                if (!data.hasActivationRange()) return;

                double range = data.getActivationRange();

                Pos3D npcPos = new Pos3D(npcLocation.getX(), npcLocation.getY(), npcLocation.getZ());
                Pos3D fromPos = new Pos3D(event.getFrom().getX(), event.getFrom().getY(), event.getFrom().getZ());
                Pos3D toPos = new Pos3D(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());

                if (Space3D.getDistance(fromPos, npcPos) <= range) return;
                if (Space3D.getDistance(toPos, npcPos) > range) return;

                runNPCActions(data, npc.getName(), player, false);
            });
        });
    }

    public void runNPCActions(NPCData data, String npcName, Player player, boolean click) {
        if (data.getActions().isEmpty()) return;
        Bukkit.getPluginManager().callEvent(new NPCInteractEvent(player, data, npcName, click ? NPCInteractEvent.Action.CLICK : NPCInteractEvent.Action.APPROACH));

        if (data.runAllActions()) {
            if (click) data.getActions().stream().filter(Action::isOnClick).forEach(action -> action.execute(plugin, data, npcName, player));
            else data.getActions().stream().filter(Action::isOnApproach).forEach(action -> action.execute(plugin, data, npcName, player));
        } else {
            if (data.useRandomOrder()) {
                data.getActions().get(random.nextInt(data.getActions().size())).execute(plugin, data, npcName, player);
            } else {
                if (playerIndexes.containsKey(player)) {
                    Integer index = playerIndexes.get(player).get(data.getId());
                    if (index == null) index = 0;

                    if (index >= data.getActions().size()) {
                        playerIndexes.get(player).put(data.getId(), 0);
                        index = 0;
                    }

                    data.getActions().get(index).execute(plugin, data, npcName, player);

                    playerIndexes.get(player).put(data.getId(), index + 1);
                } else {
                    playerIndexes.put(player, new HashMap<>());
                    playerIndexes.get(player).put(data.getId(), 1);

                    data.getActions().get(0).execute(plugin, data, npcName, player);
                }
            }
        }
    }
}
