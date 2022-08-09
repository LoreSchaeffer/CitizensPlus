package it.multicoredev.cp.storage;

import it.multicoredev.cp.model.NPCData;
import it.multicoredev.mclib.json.JsonConfig;

import java.util.ArrayList;
import java.util.List;

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
public class NPCs extends JsonConfig {
    private String format;
    private List<NPCData> npcs;

    @Override
    public NPCs init() {
        if (format == null) format = "&e&l{npc}&h >&r {message}";
        if (npcs == null) npcs = new ArrayList<>();

        return this;
    }

    public String getFormat() {
        return format;
    }

    public NPCData getData(int id) {
        for (NPCData npc : npcs) {
            if (npc.getId() == id) return npc;
        }

        return null;
    }

    public void addData(NPCData npc) {
        npcs.removeIf(n -> n.getId().equals(npc.getId()));
        npcs.add(npc);
    }

    public void removeData(int id) {
        npcs.removeIf(npc -> npc.getId() == id);
    }
}
