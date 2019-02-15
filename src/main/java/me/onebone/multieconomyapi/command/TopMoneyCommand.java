package me.onebone.multieconomyapi.command;

/*
 * MultiEconomyAPI: Core of economy system for Nukkit
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
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import me.onebone.multieconomyapi.MultiEconomyAPI;

public class TopMoneyCommand extends Command {

    private final MultiEconomyAPI plugin;

    public TopMoneyCommand(MultiEconomyAPI plugin) {
        super("mtopmoney", "Shows top money of this server", "/topmoney [page]");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, String label, final String[] args) {
        if (!this.plugin.isEnabled()) {
            return false;
        }
        if (!sender.hasPermission("multieconomyapi.command.topmoney")) {
            sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
            return false;
        }

        try {
            final LinkedHashMap<String, Double> money = plugin.getAllMoney();
            final Set<String> players = money.keySet();
            final int page = args.length > 0 ? Math.max(1, Math.min(Integer.parseInt(args[0]), players.size())) : 1;
            new Thread() {
                @Override
                public void run() {
                    List<String> list = new LinkedList<>();
                    money.keySet().forEach((player) -> {
                        list.add(player);
                    });

                    Collections.sort(list, (String s1, String s2) -> {
                        double one = money.get(s1);
                        double two = money.get(s2);
                        return one < two ? 1 : one > two ? -1 : 0;
                    });

                    StringBuilder output = new StringBuilder();
                    output.append(plugin.getMessage("topmoney-tag", new String[]{Integer.toString(page), Integer.toString(((players.size() + 6) / 5))}, sender)).append("\n");

                    int duplicate = 0;
                    double prev = -1D;
                    for (int n = 0; n < list.size(); n++) {
                        double m = money.get(list.get(n));
                        if (m == prev) {
                            duplicate++;
                        } else {
                            duplicate = 0;
                        }
                        prev = m;
                        int current = (int) Math.ceil((double) (n + 1) / 5);
                        if (page == current) {
                            output.append(plugin.getMessage("topmoney-format", new String[]{Integer.toString(n + 1 - duplicate), list.get(n), Double.toString(m)}, sender)).append("\n");
                        } else if (page < current) {
                            break;
                        }
                    }
                    output.substring(0, output.length() - 1);

                    if (sender != null) {
                        sender.sendMessage(output.toString());
                    }
                }
            }.start();
        } catch (NumberFormatException e) {
            sender.sendMessage(TextFormat.RED + "Please provide a number.");
        }
        return true;
    }
}
