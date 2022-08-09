package it.multicoredev.cp.storage;

import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.mclib.db.SQLite;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.SQLException;

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
public class Database {
    private static final String TABLE = "items";
    private final SQLite db;

    public Database(File file) throws SQLException {
        db = new SQLite(file);

        db.createTable(new String[]{
                "player VARCHAR(128)",
                "item INT",
                "amount INT"
        }, TABLE);
    }

    public void insert(Player player, int item) {
        new Thread(() -> {
            try {
                if (db.rowExists(new String[]{"player", "item"}, new Object[]{player.getUniqueId(), item}, TABLE)) {
                    int amount = db.getInteger(new String[]{"player", "item"}, new Object[]{player.getUniqueId(), item}, "amount", TABLE);
                    db.set("amount", amount + 1, new String[]{"player", "item"}, new Object[]{player.getUniqueId(), item}, TABLE);
                } else {
                    db.addRow(new String[]{"player", "item", "amount"}, new Object[]{player.getUniqueId(), item, 1}, TABLE);
                }
            } catch (SQLException e) {
                Chat.warning("&e" + e.getMessage());
            }
        }).start();
    }

    public Integer amount(Player player, int item) {
        try {
            if (!db.rowExists(new String[]{"player", "item"}, new Object[]{player.getUniqueId(), item}, TABLE)) return 0;
            return db.getInteger(new String[]{"player", "item"}, new Object[]{player.getUniqueId(), item}, "amount", TABLE);
        } catch (SQLException e) {
            Chat.warning("&e" + e.getMessage());
            return null;
        }
    }
}
