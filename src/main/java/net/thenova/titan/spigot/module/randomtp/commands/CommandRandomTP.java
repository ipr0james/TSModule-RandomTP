package net.thenova.titan.spigot.module.randomtp.commands;

import net.thenova.titan.library.command.data.CommandContext;
import net.thenova.titan.library.command.data.CommandPermission;
import net.thenova.titan.spigot.command.SpigotCommand;
import net.thenova.titan.spigot.module.randomtp.handler.RTPHandler;
import net.thenova.titan.spigot.users.SpigotUser;

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
public final class CommandRandomTP extends SpigotCommand<SpigotUser> implements CommandPermission<SpigotUser> {

    public CommandRandomTP() {
        super("randomtp","rtp", "wilderness", "wild");
    }

    @Override
    public final void execute(final SpigotUser user, final CommandContext commandContext) {
        RTPHandler.INSTANCE.handle(user.getPlayer());
    }

    @Override
    public final boolean hasPermission(final SpigotUser user) {
        return user.hasPermission("titan.command.randomtp");
    }
}
