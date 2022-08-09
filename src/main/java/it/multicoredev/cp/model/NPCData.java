package it.multicoredev.cp.model;

import com.google.gson.annotations.SerializedName;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import java.util.ArrayList;
import java.util.Arrays;
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
public class NPCData {
    private final Integer id;
    private String name;
    private String format;
    @SerializedName("run_all_actions")
    private Boolean runAllActions;
    @SerializedName("random_order")
    private Boolean randomOrder;
    @SerializedName("activation_range")
    private Double activationRange;
    private final List<Action> actions;

    public NPCData(int id, String format, boolean runAllActions, boolean randomOrder, Double activationRange, List<Action> actions) {
        this.id = id;
        this.format = format;
        this.runAllActions = runAllActions;
        this.randomOrder = randomOrder;
        this.activationRange = activationRange;
        this.actions = actions;

        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        if (npc != null) this.name = npc.getName();
    }

    public NPCData(int id, String format, boolean runAllActions, boolean randomOrder, Double activationRange, Action... actions) {
        this(id, format, runAllActions, randomOrder, activationRange, new ArrayList<>(Arrays.asList(actions)));
    }

    public NPCData(int id, String format, boolean runAllActions, boolean randomOrder, Double activationRange) {
        this(id, format, runAllActions, randomOrder, activationRange, new ArrayList<>());
    }

    public NPCData(int id, String format, boolean runAllActions, Double activationRange, List<Action> actions) {
        this(id, format, runAllActions, false, activationRange, actions);
    }

    public NPCData(int id, String format, boolean runAllActions, List<Action> actions) {
        this(id, format, runAllActions, false, null, actions);
    }

    public NPCData(int id, String format, boolean runAllActions, Double activationRange, Action... actions) {
        this(id, format, runAllActions, false, activationRange, new ArrayList<>(Arrays.asList(actions)));
    }

    public NPCData(int id, String format, boolean runAllActions, Action... actions) {
        this(id, format, runAllActions, false, null, new ArrayList<>(Arrays.asList(actions)));
    }

    public NPCData(int id, boolean runAllActions, boolean randomOrder, Double activationRange, List<Action> actions) {
        this(id, null, runAllActions, randomOrder, activationRange, actions);
    }

    public NPCData(int id, boolean runAllActions, boolean randomOrder, List<Action> actions) {
        this(id, null, runAllActions, randomOrder, null, actions);
    }

    public NPCData(int id, boolean runAllActions, boolean randomOrder, Double activationRange, Action... actions) {
        this(id, null, runAllActions, randomOrder, activationRange, new ArrayList<>(Arrays.asList(actions)));
    }

    public NPCData(int id, boolean runAllActions, boolean randomOrder, Action... actions) {
        this(id, null, runAllActions, randomOrder, null, new ArrayList<>(Arrays.asList(actions)));
    }

    public NPCData(int id, String format, boolean runAllActions, Double activationRange) {
        this(id, format, runAllActions, false, activationRange, new ArrayList<>());
    }

    public NPCData(int id, String format, boolean runAllActions) {
        this(id, format, runAllActions, false, null, new ArrayList<>());
    }

    public NPCData(int id, boolean runAllActions, Double activationRange) {
        this(id, null, runAllActions, false, activationRange, new ArrayList<>());
    }

    public NPCData(int id, boolean runAllActions) {
        this(id, null, runAllActions, false, null, new ArrayList<>());
    }

    public NPCData(int id, Double activationRange) {
        this(id, null, false, false, activationRange, new ArrayList<>());
    }

    public NPCData(int id) {
        this(id, null, false, false, null, new ArrayList<>());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public NPCData setFormat(String format) {
        this.format = format;
        return this;
    }

    public boolean runAllActions() {
        return runAllActions != null ? runAllActions : false;
    }

    public NPCData setRunAllActions(boolean runAllActions) {
        this.runAllActions = runAllActions;
        return this;
    }

    public boolean useRandomOrder() {
        return randomOrder != null ? randomOrder : false;
    }

    public NPCData setRandomOrder(boolean randomOrder) {
        this.randomOrder = randomOrder;
        return this;
    }

    public Double getActivationRange() {
        return activationRange;
    }

    public boolean hasActivationRange() {
        return activationRange != null;
    }

    public NPCData setActivationRange(Double activationRange) {
        this.activationRange = activationRange;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public NPCData addAction(Action action) {
        actions.add(action);
        return this;
    }

    public NPCData removeAction(Action action) {
        actions.remove(action);
        return this;
    }
}
