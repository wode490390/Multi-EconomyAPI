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
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.onebone.multieconomyapi.MultiEconomyAPI;

public class TakeMoneyCommand extends Command {

    private final MultiEconomyAPI plugin;

    public TakeMoneyCommand(MultiEconomyAPI plugin) {
        super("mtakemoney", "Takes money from player", "/takemoney <player> <amount>");

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            return false;
        }
        if (!sender.hasPermission("multieconomyapi.command.takemoney")) {
            sender.sendMessage(TextFormat.RED + "You don't have permission to use this command.");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(TextFormat.RED + "Usage: " + this.getUsage());
            return true;
        }
        String player = args[0];

        Player p = this.plugin.getServer().getPlayer(player);
        if (p != null) {
            player = p.getName();
        }
        try {
            double amount = Double.parseDouble(args[1]);
            if (amount < 0) {
                sender.sendMessage(this.plugin.getMessage("takemoney-invalid-number", sender));
                return true;
            }

            int result = this.plugin.reduceMoney(player, amount);
            switch (result) {
                case MultiEconomyAPI.RET_INVALID:
                    sender.sendMessage(this.plugin.getMessage("takemoney-player-lack-of-money", new String[]{player, Double.toString(amount), Double.toString(this.plugin.myMoney(player))}, sender));
                    return true;
                case MultiEconomyAPI.RET_NO_ACCOUNT:
                    sender.sendMessage(this.plugin.getMessage("player-never-connected", new String[]{player}, sender));
                    return true;
                case MultiEconomyAPI.RET_CANCELLED:
                    sender.sendMessage(this.plugin.getMessage("takemoney-failed", new String[]{player}, sender));
                    return true;
                case MultiEconomyAPI.RET_SUCCESS:
                    sender.sendMessage(this.plugin.getMessage("takemoney-took-money", new String[]{player, Double.toString(amount)}, sender));
                    if (p instanceof Player) {
                        p.sendMessage(this.plugin.getMessage("takemoney-money-taken", new String[]{Double.toString(amount)}, sender));
                    }
                    return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(this.plugin.getMessage("takemoney-must-be-number", sender));
        }
        return true;
    }
}
