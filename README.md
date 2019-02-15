# Multi-EconomyAPI
Core of multi-economy system for Nukkit

## Commands
 - /mmymoney
 - /mseemoney
 - /mgivemoney
 - /mtakemoney
 - /mtopmoney
 - /msetmoney
 - /mpay

## Permissions
- multieconomyapi
	- multieconomyapi.command
		- multieconomyapi.command.mymoney
		- multieconomyapi.command.givemoney `OP`
		- multieconomyapi.command.takemoney `OP`
		- multieconomyapi.command.setmoney `OP`
		- multieconomyapi.command.topmoney
		- multieconomyapi.command.seemoney
		- multieconomyapi.command.pay

## For developers

Developers can access to Multi-EconomyAPI's API by using:
```java
MultiEconomyAPI.getInstance().myMoney(player);
MultiEconomyAPI.getInstance().reduceMoney(player, amount);
MultiEconomyAPI.getInstance().addMoney(player, amount);
```
