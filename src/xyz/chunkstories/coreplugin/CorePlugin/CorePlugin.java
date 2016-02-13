package xyz.chunkstories.coreplugin.CorePlugin;

import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.server.Command;
import io.xol.chunkstories.api.plugin.server.Player;
import io.xol.chunkstories.item.Item;
import io.xol.chunkstories.item.ItemPile;
import io.xol.chunkstories.item.ItemsList;
import io.xol.chunkstories.server.Server;
import io.xol.chunkstories.server.tech.CommandEmitter;

public class CorePlugin extends ChunkStoriesPlugin {
	public void onEnable() {
		System.out.println("Enabling Core plugin ... " + getServer());
	}

	public void onDisable() {
		System.out.println("Disabling Core plugin");
	}

	public boolean handleCommand(CommandEmitter e, Command cmd, String[] a, String rawText) {
		if ((e instanceof Player)) {
			Player player = (Player) e;
			if (cmd.equals("clear")) {
				player.sendMessage("#FF969BRemoving " + player.getControlledEntity().inventory.size()
						+ " items from your inventory.");
				player.getControlledEntity().inventory.clear();
			} else if (cmd.equals("give")) {
				if (a.length == 0) {
					player.sendMessage("#FF969BSyntax : /give <item> [amount] [to]");
					return true;
				}
				Item type = null;
				int amount = 1;
				Player to = player;
				type = ItemsList.getItemByName(a[0]);
				if (type == null) {
					try{
						type = ItemsList.get(Integer.parseInt(a[0]));
					}
					catch(NumberFormatException ex)
					{
						
					}
				}
				if (type == null) {
					player.sendMessage("#FF969BItem \"" + a[0] + " can't be found.");
					return true;
				}
				if (a.length >= 2) {
					amount = Integer.parseInt(a[1]);
				}
				if (a.length >= 3) {
					to = Server.getInstance().getPlayer(a[2]);
				}
				if (to == null) {
					player.sendMessage("#FF969BPlayer \"" + a[2] + " can't be found.");
					return true;
				}
				ItemPile itemPile = new ItemPile(type, amount);
				//player.sendMessage("#FF969B" + to.getControlledEntity());
				to.getControlledEntity().inventory.addItemPile(itemPile);
				player.sendMessage("#FF969BGave " + itemPile + " to " + to);
			}
		}
		return true;
	}
}
