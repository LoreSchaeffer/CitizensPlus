package it.multicoredev.cp;

import it.multicoredev.cp.listeners.ActionListener;
import it.multicoredev.cp.listeners.NPCSelectListener;
import it.multicoredev.cp.storage.Database;
import it.multicoredev.cp.storage.NPCs;
import it.multicoredev.mbcore.spigot.pmc.PluginMessageChannel;
import it.multicoredev.mclib.json.GsonHelper;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

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
public class CitizensPlus extends JavaPlugin {
    public static final DecimalFormat DF = new DecimalFormat("#.0");
    private static final GsonHelper GSON = new GsonHelper();
    private static NPCs npcs;
    private Database db;
    private PluginMessageChannel pmc;

    private Map<Player, Integer> selectedNPCs;

    @Override
    public void onEnable() {
        selectedNPCs = new HashMap<>();

        if (!initStorage()) {
            onDisable();
            return;
        }

        pmc = new PluginMessageChannel(this);
        pmc.registerDefault();

        getServer().getPluginManager().registerEvents(new NPCSelectListener(this), this);
        getServer().getPluginManager().registerEvents(new ActionListener(this), this);

        NPCPCommand cmd = new NPCPCommand(this);
        getCommand("npcp").setExecutor(cmd);
        getCommand("npcp").setTabCompleter(cmd);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        selectedNPCs.clear();

        System.gc();
    }

    private boolean initStorage() {
        try {
            if (!getDataFolder().exists() || !getDataFolder().isDirectory()) {
                if (!getDataFolder().mkdir()) throw new IOException("Cannot create plugins folder");
            }

            npcs = GSON.autoload(new File(getDataFolder(), "npcs.json"), new NPCs().init(), NPCs.class);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            db = new Database(new File(getDataFolder(), "users.db"));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getFormat() {
        return npcs.getFormat();
    }

    boolean saveStorage() {
        try {
            GSON.save(npcs, new File(getDataFolder(), "npcs.json"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public NPCs npcs() {
        return npcs;
    }

    public Database db() {
        return db;
    }

    public PluginMessageChannel pmc() {
        return pmc;
    }

    public void selectNPC(Player player, NPC npc) {
        selectedNPCs.put(player, npc.getId());
    }

    public boolean hasSelectedNPC(Player player) {
        return selectedNPCs.containsKey(player);
    }

    public int getSelectedNPC(Player player) {
        return selectedNPCs.get(player);
    }
}
