package it.multicoredev.cp.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import it.multicoredev.cp.CitizensPlus;
import it.multicoredev.cp.model.actions.CommandAction;
import it.multicoredev.cp.model.actions.DelayAction;
import it.multicoredev.cp.model.actions.ItemAction;
import it.multicoredev.cp.model.actions.MessageAction;
import org.bukkit.entity.Player;

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
@JsonAdapter(Action.Adapter.class)
public abstract class Action {
    private final String type;
    @SerializedName("on_click")
    protected Boolean onClick;
    @SerializedName("on_approach")
    protected Boolean onApproach;

    public Action(Type type, boolean onClick, boolean onApproach) {
        this.type = type.getName();
        this.onClick = onClick;
        this.onApproach = onApproach;
    }

    public Action(Type type, boolean onClick) {
        this(type, onClick, !onClick);
    }

    public Action(Type type) {
        this(type, true);
    }

    public boolean isOnClick() {
        return onClick != null ? onClick : false;
    }

    public boolean isOnApproach() {
        return onApproach != null ? onApproach : false;
    }

    public abstract void execute(CitizensPlus plugin, NPCData data, String npcName, Player player);

    public static class Adapter implements JsonSerializer<Action>, JsonDeserializer<Action> {

        @Override
        public JsonElement serialize(Action action, java.lang.reflect.Type type, JsonSerializationContext ctx) {
            Type t = Type.fromString(action.type);
            if (t != null) return ctx.serialize(action, t.getType());
            else return ctx.serialize(action);
        }

        @Override
        public Action deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext ctx) throws JsonParseException {
            if (!json.isJsonObject()) return null;
            JsonObject obj = json.getAsJsonObject();
            if (!obj.has("type")) return null;

            Type t = Type.fromString(obj.get("type").getAsString());
            if (t == null) return null;

            return ctx.deserialize(json, t.getType());
        }
    }

    public enum Type {
        MESSAGE("message", MessageAction.class),
        COMMAND("command", CommandAction.class),
        ITEM("item", ItemAction.class),
        DELAY("delay", DelayAction.class);

        private final String name;
        private final Class<? extends Action> type;

        Type(String name, Class<? extends Action> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<? extends Action> getType() {
            return type;
        }

        public static Type fromString(String name) {
            for (Type t : Type.values()) {
                if (t.getName().equals(name)) {
                    return t;
                }
            }
            return null;
        }
    }
}
