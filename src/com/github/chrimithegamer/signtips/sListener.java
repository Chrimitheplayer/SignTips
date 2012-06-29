package com.github.chrimithegamer.signtips;

import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class sListener implements Listener {
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		// Variables
		Permission permissions = SignTips.permissions;
		HashMap<Player, String> promoting = SignTips.promoting;
		HashMap<Player, String> messaging = SignTips.messaging;
		HashMap<Player, String> commanding = SignTips.commanding;
		HashMap<Player, Location> warping = SignTips.warping;
		HashMap<Block, String> promotions = SignTips.promotions;
		HashMap<Block, String> messages = SignTips.messages;
		HashMap<Block, String> commands = SignTips.commands;
		HashMap<Block, Location> warps = SignTips.warps;
		List<Player> removing = SignTips.removing;
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		// Returns
		if (!(action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;
		if (!(block.getType().equals(Material.SIGN_POST) || block.getType().equals(Material.WALL_SIGN))) return;
		
		// Set Promotions
		if (promoting.containsKey(player)) {
			promotions.put(block, promoting.get(player));
			player.sendMessage(ChatColor.GREEN + "This sign will now promote users to " + promoting.get(player));
			promoting.remove(player);
			SignTips.save();
			return;
		}
		
		// Set Messages
		if (messaging.containsKey(player)) {
			messages.put(block, messaging.get(player));
			player.sendMessage(ChatColor.GREEN + "This sign will now send players that message!");
			messaging.remove(player);
			SignTips.save();
			return;
		}
		
		// Set Commands
		if (messaging.containsKey(player)) {
			messages.put(block, commanding.get(player));
			player.sendMessage(ChatColor.GREEN + "This sign will now execute the command " + commanding.get(player));
			commanding.remove(player);
			SignTips.save();
			return;
		}
		
		// Set Warps
		if (warping.containsKey(player)) {
			warps.put(block, warping.get(player));
			player.sendMessage(ChatColor.GREEN + "This sign will now warp players to the set location!");
			warping.remove(player);
			SignTips.save();
			return;
		}
		
		// Remove Promotions
		if (removing.contains(player)) {
			promotions.remove(block);
			messages.remove(block);
			commands.remove(block);
			warps.remove(block);
			player.sendMessage(ChatColor.GREEN + "This sign will no longer promote users.");
			removing.remove(player);
			SignTips.save();
			return;
		}
		
		// Receive Promotion
		if (promotions.containsKey(block)) {
			if (!permissions.has(player, "signtips.use")) return;
			String group = promotions.get(block);
			permissions.playerAddGroup(player, group);
			player.sendMessage(ChatColor.GREEN + "You are now a member of " + group + "!");
		}
		
		// Receive Message
		if (messages.containsKey(block)) {
			if (!permissions.has(player, "signtips.use")) return;
			String message = messages.get(block);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		
		// Receive Command
		if (commands.containsKey(block)) {
			if (!permissions.has(player, "signtips.use")) return;
		}
		
		// Receive Warp
		if (warps.containsKey(block)) {
			if (permissions.has(player, "signtips.use")) player.teleport(warps.get(block));
		}
	}
}
