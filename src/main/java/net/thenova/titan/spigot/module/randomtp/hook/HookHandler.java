package net.thenova.titan.spigot.module.randomtp.hook;

import de.arraying.kotys.JSON;
import lombok.Getter;
import net.thenova.titan.library.Titan;
import net.thenova.titan.spigot.TitanSpigot;
import net.thenova.titan.spigot.module.randomtp.handler.RTPHandler;
import net.thenova.titan.spigot.module.randomtp.hook.hooks.HookFactions;
import net.thenova.titan.spigot.module.randomtp.hook.hooks.HookFactionsUUID;
import net.thenova.titan.spigot.module.randomtp.hook.hooks.HookTowny;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public enum HookHandler {
    INSTANCE;

    @Getter private final Set<RegionHook> hooks = new HashSet<>();

    public void load() {
        final JSON hooksJSON = RTPHandler.INSTANCE.getFile().getJSON().json("hooks");
        final PluginManager manager = TitanSpigot.INSTANCE.getPlugin().getServer().getPluginManager();

        if(hooksJSON.bool("towny")) {
            if(manager.isPluginEnabled("Towny")) {
                this.loadHook(new HookTowny());
            } else {
                Titan.INSTANCE.getLogger().info("[RandomTP] [Hooks] - Towny hook is enabled but Towny plugin could not be found.");
            }
        }

        if(hooksJSON.bool("factions")) {
            if(manager.isPluginEnabled("Factions")) {
                if(manager.getPlugin("Factions").getDescription().getAuthors().contains("drtshock")) {
                    this.loadHook(new HookFactionsUUID());
                } else {
                    this.loadHook(new HookFactions());
                }
            } else {
                Titan.INSTANCE.getLogger().info("[RandomTP] [Hooks] - Factions hook is enabled but Factions plugin could not be found.");
            }
        }
    }

    /**
     * Load in a hook if the plugin is found.
     * @param hooks - Plugin hooks
     */
    private void loadHook(RegionHook... hooks) {
        this.hooks.addAll(Arrays.asList(hooks));
    }
}
