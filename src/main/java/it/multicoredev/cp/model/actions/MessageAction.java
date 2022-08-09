package it.multicoredev.cp.model.actions;

import it.multicoredev.cp.CitizensPlus;
import it.multicoredev.cp.model.Action;
import it.multicoredev.cp.model.NPCData;
import it.multicoredev.mbcore.spigot.Chat;
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
public class MessageAction extends Action {
    private final String message;

    public MessageAction(String message, boolean onClick, boolean onApproach) {
        super(Type.MESSAGE, onClick, onApproach);
        this.message = message;
    }

    public MessageAction(String message, boolean onClick) {
        super(Type.MESSAGE, onClick);
        this.message = message;
    }

    public MessageAction(String message) {
        super(Type.MESSAGE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute(CitizensPlus plugin, NPCData data, String npcName, Player player) {
        String msg = data.getFormat() != null && !data.getFormat().trim().isEmpty() ? data.getFormat() : CitizensPlus.getFormat();
        msg = msg
                .replace("{message}", message)
                .replace("{npc}", npcName)
                .replace("{player}", player.getName())
                .replace("{displayname}", Chat.getDiscolored(player.getDisplayName()))
                .replace("{uuid}", player.getUniqueId().toString())
                .replace("{world}", player.getWorld().getName())
                .replace("{x}", DF.format(player.getLocation().getX()))
                .replace("{y}", DF.format(player.getLocation().getY()))
                .replace("{z}", DF.format(player.getLocation().getZ()));

        Chat.send(msg, player);
    }
}
