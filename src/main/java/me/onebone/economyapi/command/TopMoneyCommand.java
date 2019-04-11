package me.onebone.economyapi.command;

/*
 * EconomyAPI: Core of economy system for Nukkit
 * Copyright (C) 2016  onebone <jyc00410@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import cn.nukkit.IPlayer;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;

import java.util.*;

public class TopMoneyCommand extends Command {
    private EconomyAPI plugin;

    public TopMoneyCommand(EconomyAPI plugin) {
        super("topmoney", "Shows top money of this server", "/topmoney [page]", new String[]{"baltop", "balancetop"});

        this.plugin = plugin;
    }

    private static String getName(String possibleUuid) {
        UUID uuid;
        try {
            uuid = UUID.fromString(possibleUuid);
        } catch (Exception e) {
            return possibleUuid;
        }

        IPlayer player = Server.getInstance().getOfflinePlayer(uuid);
        if (player != null && player.getName() != null) {
            return player.getName();
        }
        return possibleUuid;
    }

    @Override
    public boolean execute(final CommandSender sender, String label, final String[] args) {
        if (!this.plugin.isEnabled()) return false;
        if (!sender.hasPermission("economyapi.command.topmoney")) {
            sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
            return false;
        }

        try {
            final LinkedHashMap<String, Double> money = plugin.getAllMoney();
            final Set<String> players = money.keySet();
            final int page = args.length > 0 ? Math.max(1, Math.min(Integer.parseInt(args[0]), players.size())) : 1;
            sender.getServer().getScheduler().scheduleTask(() -> {
                List<String> list = new LinkedList<>(money.keySet());

                list.sort((s1, s2) -> Double.compare(money.get(s2), money.get(s1)));

                StringBuilder output = new StringBuilder();
                output.append(plugin.getMessage("topmoney-tag", new String[]{Integer.toString(page), Integer.toString(((players.size() + 6) / 5))}, sender)).append("\n");

                if (page == 1) {
                    double total = 0;
                    for (double val : money.values()) {
                        total += val;
                    }
                    output.append(plugin.getMessage("topmoney-total", new String[]{EconomyAPI.MONEY_FORMAT.format(total)}, sender)).append("\n\n");
                }

                int duplicate = 0;
                double prev = -1D;
                for (int n = 0; n < list.size(); n++) {
                    double m = money.get(list.get(n));
                    if (m == prev) duplicate++;
                    else duplicate = 0;
                    prev = m;
                    int current = (int) Math.ceil((double) (n + 1) / 5);
                    if (page == current) {
                        output.append(plugin.getMessage("topmoney-format", new String[]{Integer.toString(n + 1 - duplicate), getName(list.get(n)), EconomyAPI.MONEY_FORMAT.format(m)}, sender)).append("\n");
                    } else if (page < current) {
                        break;
                    }
                }
                output.substring(0, output.length() - 1);

                sender.sendMessage(output.toString());
            });
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + "Please provide a number.");
        }
        return true;
    }
}
