package net.thenova.titan.spigot.module.randomtp.commands;

import net.thenova.titan.library.command.data.CommandContext;
import net.thenova.titan.library.command.data.CommandPermission;
import net.thenova.titan.spigot.command.SpigotCommand;
import net.thenova.titan.spigot.data.message.MessageHandler;
import net.thenova.titan.spigot.data.message.placeholders.Placeholder;
import net.thenova.titan.spigot.module.randomtp.handler.RTPHandler;
import net.thenova.titan.spigot.users.user.User;
import org.bukkit.World;

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
public final class CommandRTP extends SpigotCommand<User> implements CommandPermission<User> {

    public CommandRTP() {
        super("wild","wilderness", "rtp");
    }

    @Override
    public void execute(User user, CommandContext commandContext) {
        final World world = user.getPlayer().getWorld();
        if(RTPHandler.INSTANCE.getFile().get("world-permissions", Boolean.class)
                && !user.hasPermission("randomtp.world." + world.getName().toLowerCase())) {
            MessageHandler.INSTANCE.build("module.randomtp.no-permission-world")
                    .placeholder(new Placeholder("world", user.getName()))
                    .send(user);
            return;
        }

        RTPHandler.INSTANCE.handle(user.getPlayer());
    }

    @Override
    public boolean hasPermission(User user) {
        return user.hasPermission("titan.command.randomtp");
    }
}
