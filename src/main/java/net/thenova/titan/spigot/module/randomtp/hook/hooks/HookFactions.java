package net.thenova.titan.spigot.module.randomtp.hook.hooks;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;
import net.thenova.titan.spigot.module.randomtp.hook.RegionHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
public final class HookFactions implements RegionHook {

    @Override
    public final boolean check(final Player player, final Location location) {
        return BoardColl.get().getFactionAt(PS.valueOf(location)) == FactionColl.get().getNone();
    }
}
