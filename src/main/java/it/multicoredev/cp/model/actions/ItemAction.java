package it.multicoredev.cp.model.actions;

import com.google.gson.annotations.SerializedName;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import it.multicoredev.cp.CitizensPlus;
import it.multicoredev.cp.model.Action;
import it.multicoredev.cp.model.NPCData;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class ItemAction extends Action {
    private final String material;
    private final Integer amount;
    private final String name;
    private final List<String> lore;
    private final String nbt;
    @SerializedName("max_amount")
    private final Integer maxAmount;

    public ItemAction(Material material, int amount, String name, List<String> lore, String nbt, int maxAmount, boolean onClick, boolean onApproach) {
        super(Type.ITEM, onClick, onApproach);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.nbt = nbt;
        this.maxAmount = maxAmount;
    }

    public ItemAction(Material material, int amount, String name, List<String> lore, String nbt, int maxAmount, boolean onClick) {
        super(Type.ITEM, onClick);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.nbt = nbt;
        this.maxAmount = maxAmount;
    }

    public ItemAction(Material material, int amount, String name, List<String> lore, String nbt, int maxAmount) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.nbt = nbt;
        this.maxAmount = maxAmount;
    }

    public ItemAction(Material material, int amount, String name, List<String> lore, String nbt) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.nbt = nbt;
        this.maxAmount = null;
    }

    public ItemAction(Material material, int amount, String name, List<String> lore) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.nbt = null;
        this.maxAmount = null;
    }

    public ItemAction(Material material, int amount) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = amount;
        this.name = null;
        this.lore = null;
        this.nbt = null;
        this.maxAmount = null;
    }

    public ItemAction(Material material) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = 1;
        this.name = null;
        this.lore = null;
        this.nbt = null;
        this.maxAmount = null;
    }

    public ItemAction(Material material, String name, List<String> lore) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = 1;
        this.name = name;
        this.lore = lore;
        this.nbt = null;
        this.maxAmount = null;
    }

    public ItemAction(Material material, String name, List<String> lore, String nbt) {
        super(Type.ITEM);
        this.material = material.getKey().toString();
        this.amount = 1;
        this.name = name;
        this.lore = lore;
        this.nbt = nbt;
        this.maxAmount = null;
    }

    public String getMaterial() {
        return material;
    }

    public Integer getAmount() {
        return amount != null ? amount : 1;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getNbt() {
        return nbt;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public ItemStack getItemStack(String npcName, Player player) {
        Material mat = Material.matchMaterial(material);
        if (mat == null) return null;

        String na = name == null ? null : name.replace("{npc}", npcName)
                .replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{uuid}", player.getUniqueId().toString())
                .replace("{world}", player.getWorld().getName())
                .replace("{x}", DF.format(player.getLocation().getX()))
                .replace("{y}", DF.format(player.getLocation().getY()))
                .replace("{z}", DF.format(player.getLocation().getZ()));

        List<String> l = lore == null ? null : lore.stream().map(s -> s.replace("{npc}", npcName)
                .replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{uuid}", player.getUniqueId().toString())
                .replace("{world}", player.getWorld().getName())
                .replace("{x}", DF.format(player.getLocation().getX()))
                .replace("{y}", DF.format(player.getLocation().getY()))
                .replace("{z}", DF.format(player.getLocation().getZ()))).collect(Collectors.toList());

        String nb = nbt == null ? null : nbt.replace("{npc}", npcName)
                .replace("{player}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{uuid}", player.getUniqueId().toString())
                .replace("{world}", player.getWorld().getName())
                .replace("{x}", DF.format(player.getLocation().getX()))
                .replace("{y}", DF.format(player.getLocation().getY()))
                .replace("{z}", DF.format(player.getLocation().getZ()));

        ItemStack item = new ItemStack(mat, getAmount());
        if (name != null || (lore != null && !lore.isEmpty())) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                if (name != null) meta.setDisplayName(Chat.getTranslated(na));
                if (lore != null && !lore.isEmpty()) meta.setLore(Chat.getTranslated(l));
            }

            item.setItemMeta(meta);
        }

        try {
            if (nbt != null) {
                NBTItem nbti = new NBTItem(item);
                nbti.mergeCompound(new NBTContainer(nb));
                item = nbti.getItem();
            }
        } catch (Exception e) {
            Chat.warning("&e" + e.getMessage());
        }

        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemAction that = (ItemAction) o;
        return material.equals(that.material) && amount.equals(that.amount) && Objects.equals(name, that.name) && Objects.equals(lore, that.lore) && Objects.equals(nbt, that.nbt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, amount, name, lore, nbt);
    }

    @Override
    public void execute(CitizensPlus plugin, NPCData data, String npcName, Player player) {
        if (maxAmount != null) {
            Integer takenAmount = plugin.db().amount(player, hashCode());
            if (takenAmount == null) return;
            if (takenAmount >= maxAmount) return;
        }

        ItemStack item = getItemStack(npcName, player);
        if (item == null) return;

        if (!player.getInventory().addItem(item).isEmpty()) player.getWorld().dropItem(player.getLocation(), item);
    }
}
