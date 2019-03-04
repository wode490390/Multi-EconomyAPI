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

import java.io.File;
import java.util.LinkedHashMap;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

public class YamlProvider implements Provider {
	private Config file = null;
	private LinkedHashMap<String, Double> data = null;
	
	@SuppressWarnings({ "unchecked", "serial" })
	public void init(File path){
		file = new Config(new File(path, "Money.yml"), Config.YAML, new LinkedHashMap<String, Object>(){
			{
				put("version" , 2);
				put("money", new LinkedHashMap<String, Double>());
			}
		});
		
		LinkedHashMap<Object, Object> temp = (LinkedHashMap<Object, Object>) file.get("money");
		
		data = new LinkedHashMap<>();
		temp.forEach((key, money) -> {
			String username = key.toString();
			
			if(money instanceof Integer){
				data.put(username, ((Integer) money).doubleValue());
			}else if(money instanceof Double){
				data.put(username, (Double) money);
			}else if(money instanceof String){
				data.put(username, Double.parseDouble(money.toString()));
			}
		});
	}
	
	public void open(){
		
	}

	public void save(){
		file.set("money", data);
		file.save();
	}

	public void close(){
		this.save();
		
		file = null;
		data = null;
	}

	@Override
	public boolean accountExists(UUID player) {
		return accountExists(player.toString());
	}

	@Override
	public boolean accountExists(String player){
		player = player.toLowerCase();
		
		return data.containsKey(player);
	}

	@Override
	public boolean removeAccount(UUID player) {
		return removeAccount(player.toString());
	}

	@Override
	public boolean removeAccount(String player) {
		if (accountExists(player)) {
			data.remove(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean createAccount(UUID player, double defaultMoney) {
		if(!this.accountExists(player)){
			data.put(player.toString(), defaultMoney);
		}
		return false;
	}

	@Override
	public boolean setMoney(UUID player, double amount){
		if(data.containsKey(player.toString())){
			data.put(player.toString(), amount);
			return true;
		}
		return false;
	}


	@Override
	public boolean addMoney(UUID player, double amount) {
		if(data.containsKey(player.toString())){
			data.put(player.toString(), data.get(player.toString()) + amount);
			return true;
		}
		return false;
	}

	@Override
	public boolean reduceMoney(UUID player, double amount) {
		if(data.containsKey(player.toString())){
			data.put(player.toString(), data.get(player.toString()) - amount);
			return true;
		}
		return false;
	}

	@Override
	public double getMoney(UUID player) {
		if(data.containsKey(player.toString())){
			return data.get(player.toString());
		}
		return -1;
	}

	@Override
	public double getMoney(String player){
		player = player.toLowerCase();
		if(data.containsKey(player)){
			return data.get(player);
		}
		return -1;
	}
	
	public LinkedHashMap<String, Double> getAll(){
		return data;
	}
	
	public String getName(){
		return "Yaml";
	}
}
