package net.thenova.titan.spigot.module.randomtp;

import net.thenova.titan.core.message.MessageHandler;
import net.thenova.titan.core.users.user.module.UserModule;
import net.thenova.titan.library.command.data.Command;
import net.thenova.titan.library.database.connection.IDatabase;
import net.thenova.titan.library.database.sql.table.DatabaseTable;
import net.thenova.titan.spigot.module.SpigotModule;
import net.thenova.titan.spigot.module.randomtp.commands.CommandRandomTP;
import net.thenova.titan.spigot.module.randomtp.handler.RTPHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;

/**
 * Copyright 2019 ipr0james
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class RandomTP implements SpigotModule {

    @Override
    public final String name() {
        return "RandomTP";
    }

    @Override
    public final void load() {
        RTPHandler.INSTANCE.load();
    }

    @Override
    public final void messages(final MessageHandler handler) {
        handler.add("module.randomtp.not-enabled", "%prefix.error% RandomTP is not enabled for this world.");
        handler.add("module.randomtp.on-cooldown", "%prefix.info% You must wait &d%duration% &7before you can use this again.");

        handler.add("module.randomtp.teleport.teleporting", "%prefix.info% Teleporting you to a safe place...");
        handler.add("module.randomtp.teleport.failed", "%prefix.error% Could not find a safe locations location. Please try again.");
        handler.add("module.randomtp.teleport.success", "You've been teleported to &dX: %x% Y: %y% Z: %z%&7!");

        handler.add("module.randomtp.no-permission-world", "%prefix.error% You do not have permission to use RandomTP in &c%world%&7.");
    }

    @Override
    public final void reload() {
        RTPHandler.INSTANCE.load();
    }

    @Override
    public final void shutdown() { }

    @Override
    public final IDatabase database() {
        return null;
    }

    @Override
    public final List<DatabaseTable> tables() {
        return null;
    }

    @Override
    public final List<Listener> listeners() {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public final List<Command> commands() {
        return Collections.singletonList(new CommandRandomTP());
    }

    @Override
    public List<Class<? extends UserModule>> user() {
        return null;
    }
}
