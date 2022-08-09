package it.multicoredev.cp.model.actions;

import it.multicoredev.cp.CitizensPlus;
import it.multicoredev.cp.model.Action;
import it.multicoredev.cp.model.NPCData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static it.multicoredev.cp.CitizensPlus.DF;

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
public class CommandAction extends Action {
    private final String command;
    private final boolean console;
    private final boolean bungeecord;

    public CommandAction(String command, boolean console, boolean bungeecord, boolean onCLick, boolean onApproach) {
        super(Type.COMMAND, onCLick, onApproach);
        this.command = command;
        this.console = console;
        this.bungeecord = bungeecord;
    }

    public CommandAction(String command, boolean console, boolean bungeecord, boolean onCLick) {
        super(Type.COMMAND, onCLick);
        this.command = command;
        this.console = console;
        this.bungeecord = bungeecord;
    }

    public CommandAction(String command, boolean console, boolean bungeecord) {
        super(Type.COMMAND);
        this.command = command;
        this.console = console;
        this.bungeecord = bungeecord;
    }

    public CommandAction(String command, boolean console) {
        super(Type.COMMAND);
        this.command = command;
        this.console = console;
        this.bungeecord = false;
    }

    public CommandAction(String command) {
        super(Type.COMMAND);
        this.command = command;
        this.console = false;
        this.bungeecord = false;
    }

    public String getCommand() {
        return command;
    }

    public boolean isConsole() {
        return console;
    }

    public boolean isBungeecord() {
        return bungeecord;
    }

    @Override
    public void execute(CitizensPlus plugin, NPCData data, String npcName, Player player) {
        String cmd = command
                .replace("{npc}", npcName)
                .replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{uuid}", player.getUniqueId().toString())
                .replace("{world}", player.getWorld().getName())
                .replace("{x}", DF.format(player.getLocation().getX()))
                .replace("{y}", DF.format(player.getLocation().getY()))
                .replace("{z}", DF.format(player.getLocation().getZ()));

        if (!bungeecord) {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                if (!console) Bukkit.dispatchCommand(player, cmd);
                else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                return null;
            });
        } else {
            if (!console) plugin.pmc().playerCommand(player, cmd);
            else plugin.pmc().consoleCommand("BUNGEECORD", cmd);
        }
    }
}
