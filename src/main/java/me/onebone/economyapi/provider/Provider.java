package me.onebone.economyapi.provider;

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
import cn.nukkit.Player;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.UUID;

public interface Provider {
	void init(File path);
	
	void open();
	void save();
	void close();
	
	boolean accountExists(UUID player);
	boolean accountExists(String player);

	boolean removeAccount(UUID player);
	boolean removeAccount(String player);

	boolean createAccount(UUID player, double defaultMoney);
	
	boolean setMoney(UUID player, double amount);
	
	boolean addMoney(UUID player, double amount);
	
	boolean reduceMoney(UUID player, double amount);
	
	double getMoney(UUID player);
	double getMoney(String player);
	
	LinkedHashMap<String, Double> getAll();
	
	String getName();
}
