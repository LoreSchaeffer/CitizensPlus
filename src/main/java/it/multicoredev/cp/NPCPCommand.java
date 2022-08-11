package it.multicoredev.cp;

import it.multicoredev.cp.model.NPCData;
import it.multicoredev.cp.model.actions.CommandAction;
import it.multicoredev.cp.model.actions.DelayAction;
import it.multicoredev.cp.model.actions.ItemAction;
import it.multicoredev.cp.model.actions.MessageAction;
import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mbcore.spigot.util.TabCompleterUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright © 2022 by Lorenzo Magni
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
public class NPCPCommand implements CommandExecutor, TabCompleter {
    private final CitizensPlus plugin;
    private final NPCRegistry npcRegistry;

    public NPCPCommand(CitizensPlus plugin) {
        this.plugin = plugin;
        this.npcRegistry = CitizensAPI.getNPCRegistry();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("npcp.command")) {
            Chat.send("&4Insufficient permissions!", sender, true);
            return true;
        }

        if (!(sender instanceof Player)) {
            Chat.send("&4You must be a player to run this command!", sender, true);
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) return true;

        if (args[0].equalsIgnoreCase("reload")) {
            long millis = System.currentTimeMillis();
            plugin.onDisable();
            plugin.onEnable();
            Chat.send("&2CitizensPlus reloaded in " + (System.currentTimeMillis() - millis) + "ms", player);
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2 && plugin.hasSelectedNPC(player)) {
                Chat.send("&4You must select an NPC!", player);
                return true;
            }

            List<Integer> selectedNPCs = new ArrayList<>();

            if (args.length < 2) {
                selectedNPCs.add(plugin.getSelectedNPC(player));
            } else {
                try {
                    selectedNPCs.add(Integer.parseInt(args[1]));
                } catch (NumberFormatException ignored) {
                    String selector = args[1];
                    if (selector.contains("-")) {
                        String[] split = selector.split("-");
                        int min = Integer.parseInt(split[0]);
                        int max = Integer.parseInt(split[1]);
                        for (int i = min; i <= max; i++) {
                            selectedNPCs.add(i);
                        }
                    } else if (selector.startsWith("<")) {
                        int max = Integer.parseInt(selector.substring(1));
                        for (int i = 0; i <= max; i++) {
                            selectedNPCs.add(i);
                        }
                    } else {
                        Chat.send("&4Invalid selector!", player);
                        return true;
                    }
                }
            }

            selectedNPCs.removeIf(id -> npcRegistry.getById(id) == null);

            if (selectedNPCs.isEmpty()) {
                Chat.send("&4No NPCs found!", player);
                return true;
            }

            selectedNPCs.forEach(id -> {
                if (plugin.npcs().getData(id) == null) plugin.npcs().addData(new NPCData(id));
            });

            if (plugin.saveStorage()) Chat.send("&2Done!", player);
            else Chat.send("&4Failed to save NPCs!", player);

            return true;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2 && plugin.hasSelectedNPC(player)) {
                Chat.send("&4You must select an NPC!", player);
                return true;
            }

            List<Integer> selectedNPCs = new ArrayList<>();

            if (args.length < 2) {
                selectedNPCs.add(plugin.getSelectedNPC(player));
            } else {
                try {
                    selectedNPCs.add(Integer.parseInt(args[1]));
                } catch (NumberFormatException ignored) {
                    String selector = args[1];
                    if (selector.contains("-")) {
                        String[] split = selector.split("-");
                        int min = Integer.parseInt(split[0]);
                        int max = Integer.parseInt(split[1]);
                        for (int i = min; i <= max; i++) {
                            selectedNPCs.add(i);
                        }
                    } else if (selector.startsWith("<")) {
                        int max = Integer.parseInt(selector.substring(1));
                        for (int i = 0; i <= max; i++) {
                            selectedNPCs.add(i);
                        }
                    } else {
                        Chat.send("&4Invalid selector!", player);
                        return true;
                    }
                }
            }

            selectedNPCs.removeIf(id -> npcRegistry.getById(id) == null);

            if (selectedNPCs.isEmpty()) {
                Chat.send("&4No NPCs found!", player);
                return true;
            }

            selectedNPCs.forEach(id -> plugin.npcs().removeData(id));

            if (plugin.saveStorage()) Chat.send("&2Done!", player);
            else Chat.send("&4Failed to save NPCs!", player);

            return true;
        } else if (args[0].equalsIgnoreCase("createExample")) {
            plugin.npcs().addData(new NPCData(
                    plugin.createExampleNpc(player.getLocation()),
                    "If this is null the default format will be used",
                    false,
                    false,
                    2.0,
                    new CommandAction("say Hello World!", false, false, true, false),
                    new DelayAction(1000, true, false),
                    new ItemAction(Material.DIAMOND, 1, "Diamond", Collections.singletonList("This is the lore"), "{}", 1, false),
                    new MessageAction("&6Hello World!", true, false)
            ));

            if (plugin.saveStorage()) Chat.send("&2Done!", player);
            else Chat.send("&4Failed to save example NPCs!", player);

            return true;
        }

        Chat.send("&cIncorrect usage!", player);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("npcp.command")) return null;

        if (args.length == 1) return TabCompleterUtil.getCompletions(args[0], "reload", "create", "remove", "createExample");
        else if (args.length == 2 && (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove")))
            return TabCompleterUtil.getCompletions(args[1], "0", "1", "5", "10", "1-10", "<50");

        return null;
    }
}
