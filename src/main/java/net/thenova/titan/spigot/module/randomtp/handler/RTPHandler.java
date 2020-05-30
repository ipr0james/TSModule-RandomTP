package net.thenova.titan.spigot.module.randomtp.handler;

import de.arraying.kotys.JSON;
import net.thenova.titan.core.message.MessageHandler;
import net.thenova.titan.core.message.placeholders.Placeholder;
import net.thenova.titan.core.module.data.JSONFileModuleData;
import net.thenova.titan.library.file.FileHandler;
import net.thenova.titan.library.file.json.JSONFile;
import net.thenova.titan.library.util.UNumber;
import net.thenova.titan.library.util.URandom;
import net.thenova.titan.library.util.cooldown.Cooldown;
import net.thenova.titan.spigot.TitanPluginSpigot;
import net.thenova.titan.spigot.compatibility.compat.XMaterial;
import net.thenova.titan.spigot.message.RecipientSpigot;
import net.thenova.titan.spigot.module.randomtp.handler.data.RTPWorldData;
import net.thenova.titan.spigot.module.randomtp.hook.HookHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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
public enum RTPHandler {
    INSTANCE;

    private static final String COOLDOWN_KEY = "random-tp";

    private final Map<String, RTPWorldData> worldData = new HashMap<>();
    private JSONFile file;

    private boolean allowWater;
    private int cooldown;

    public final void load() {
        this.file = FileHandler.INSTANCE.loadJSONFile(new JSONFileModuleData("randomtp"));

        this.cooldown = this.file.get("cooldown", Integer.class);
        this.allowWater = this.file.get("allow-water", Boolean.class);

        final JSON worlds = this.file.get("worlds", JSON.class);
        worlds.raw().keySet().forEach(key -> this.worldData.put(key, worlds.json(key).marshal(RTPWorldData.class)));

        Bukkit.getScheduler().runTask(TitanPluginSpigot.getPlugin(), () -> HookHandler.INSTANCE.load(this.file.getJSON()));
    }

    /**
     * Handle the player executing the RTP command.
     *
     * @param player - Player
     */
    public final void handle(final Player player) {
        final RecipientSpigot recipient = new RecipientSpigot(player);
        final World world = player.getWorld();
        if(this.file.get("world-permissions", Boolean.class)
                && !(player.hasPermission("randomtp.world." + world.getName().toLowerCase())
                    || player.hasPermission("randomtp.admin"))) {
            MessageHandler.INSTANCE.build("module.randomtp.no-permission-world")
                    .placeholder(new Placeholder("world", player.getName()))
                    .send(recipient);
            return;
        }

        final RTPWorldData data = this.worldData.get(player.getWorld().getName());
        if(data == null) {
            MessageHandler.INSTANCE.build("randomtp.not-enabled").send(recipient);
            return;
        }


        if(Cooldown.inCooldown(player.getUniqueId().toString(), COOLDOWN_KEY)
                && !player.isOp()
                && !player.hasPermission("randomtp.bypass")) {
            MessageHandler.INSTANCE.build("module.randomtp.on-cooldown")
                    .placeholder(new Placeholder("duration", UNumber.getTimeShort(Cooldown.get(player.getUniqueId().toString(), COOLDOWN_KEY))))
                    .send(recipient);
            return;
        }

        MessageHandler.INSTANCE.build("module.randomtp.teleport.teleporting").send(recipient);

        final int spawnX = player.getWorld().getSpawnLocation().getBlockX();
        final int spawnZ = player.getWorld().getSpawnLocation().getBlockZ();

        int count = 0;
        int x, y, z;
        do {
            count++;

            x = URandom.r(spawnX + data.getMin(), spawnX + data.getMax());
            z = URandom.r(spawnZ + data.getMin(), spawnZ + data.getMax());
            if (URandom.nextBoolean()) {
                x = -x;
            }
                
            if (URandom.nextBoolean()) {
                z = -z;
            }

            if (count == 100) {
                MessageHandler.INSTANCE.build("module.randomtp.teleport.failed").send(recipient);
                return;
            }
        } while ((y = teleportCheck(player, x, z)) == -1);

        new Cooldown(player.getUniqueId().toString(), COOLDOWN_KEY, cooldown);
        player.teleport(new Location(player.getWorld(), x + 0.5, y, z + 0.5));
        MessageHandler.INSTANCE.build("module.randomtp.teleport.success")
                .placeholder(new Placeholder("x", x),
                        new Placeholder("y", y),
                        new Placeholder("z", z))
                .send(recipient);
    }

    /**
     * Check the given location is safe for the user to teleport to
     *
     * @param player - Player
     * @param x - X coord
     * @param z - Z coord
     * @return - Y coord
     */
    private int teleportCheck(final Player player, final int x, final int z) {
        final World world = player.getWorld();
        final int y = this.getY(world, x, z);

        if(y == 0) {
            return -1;
        }

        final Block highest = world.getBlockAt(x, y, z);
        final XMaterial material = XMaterial.fromMaterial(highest.getType());

        if((XMaterial.fromMaterial(highest.getRelative(BlockFace.DOWN).getType()) == XMaterial.WATER && !this.allowWater)
                || HookHandler.INSTANCE.getHooks().stream()
                    .anyMatch(hook -> !hook.check(player, new Location(player.getWorld(), x, y, z)))) {
            return -1;
        }

        return (material == XMaterial.AIR)
                || (material == XMaterial.WATER && this.allowWater)
                || (material == XMaterial.COBWEB) ? y : -1;
    }

    /**
     * Get valid Y coord between worlds
     *
     * @param world - World
     * @param x - X coord
     * @param z - Z coord
     * @return - Integer
     */
    private int getY(final World world, final int x, final int z) {
        if(world.getEnvironment() == World.Environment.NETHER) {
            for (int i = 124; i > 2; i--) {
                if ((world.getBlockAt(x, i, z).isEmpty()) &&
                        (world.getBlockAt(x, i - 1, z).isEmpty()) &&
                        (!world.getBlockAt(x, i - 2, z).isEmpty()) &&
                        (!world.getBlockAt(x, i - 2, z).isLiquid())) {
                    return i;
                }
            }
        } else {
            return world.getHighestBlockYAt(x, z);
        }
        return 0;
    }
}
