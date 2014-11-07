package com.instancedev.denmarktaxes;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.wargamer2010.signshop.events.SSMoneyEventType;
import org.wargamer2010.signshop.events.SSMoneyTransactionEvent;

public class Main extends JavaPlugin implements Listener {

	public static Economy econ = null;
	public boolean economy = true;

	double tax = 0.98D;

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);

		if (!setupEconomy()) {
			getLogger().severe(String.format("[%s] - No Economy (Vault) dependency found! Disabling Economy.", getDescription().getName()));
			economy = false;
		}

		this.getConfig().addDefault("config.tax", 0.02D);

		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		tax = 1D - this.getConfig().getDouble("config.tax");

	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	@EventHandler
	public void onTransaction(SSMoneyTransactionEvent event) {
		if (economy) {
			if (!event.isLeftClicking()) {
				if (event.getTransactionType() == SSMoneyEventType.GiveToOwner) {
					event.setPrice(event.getPrice() * tax);
				}
			}
		}
	}

}
