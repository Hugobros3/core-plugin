package xyz.chunkstories.coreplugin;

import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.server.Command;
import io.xol.chunkstories.api.plugin.server.Player;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.item.Item;
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
				player.sendMessage("#FF969BRemoving " + player.getControlledEntity().getInventory().size()
						+ " items from your inventory.");
				player.getControlledEntity().getInventory().clear();
			} else if (cmd.equals("give")) {
				if (a.length == 0) {
					player.sendMessage("#FF969BSyntax : /give <item> [amount] [to]");
					return true;
				}
				Item type = null;
				String[] info = null;
				int amount = 1;
				Player to = player;
				String itemCall = a[0];
				if(itemCall.contains(":"))
				{
					String[] itemCallS = itemCall.split(":");
					itemCall = itemCallS[0];
					info = new String[itemCallS.length - 1];
					for(int i = 0; i < itemCallS.length - 1; i++)
						info[i] = itemCallS[i+1];
				}
				
				type = ItemsList.getItemByName(itemCall);
				if (type == null) {
					try{
						type = ItemsList.get(Integer.parseInt(itemCall));
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
				ItemPile itemPile = new ItemPile(type, info);
				itemPile.amount = amount;
				//player.sendMessage("#FF969B" + to.getControlledEntity());
				to.getControlledEntity().getInventory().addItemPile(itemPile);
				player.sendMessage("#FF969BGave " + itemPile + " to " + to);
			}
		}
		else if (cmd.equals("tp")) {
			Player who = (Player) e;
			Location to = null;
			
			if(a.length == 1)
			{
				Player otherPlayer = this.getServer().getPlayer(a[0]);
				if(otherPlayer != null)
					to = otherPlayer.getLocation();
				else
					e.sendMessage("#FF8966Player not found : "+a[0]);
			}
			else if(a.length == 2)
			{
				who = this.getServer().getPlayer(a[0]);
				if(who == null)
					e.sendMessage("#FF8966Player not found : "+a[0]);
				
				Player otherPlayer = this.getServer().getPlayer(a[1]);
				if(otherPlayer != null)
					to = otherPlayer.getLocation();
				else
					e.sendMessage("#FF8966Player not found : "+a[1]);
			}
			else if(a.length == 3)
			{
				int x = Integer.parseInt(a[0]);
				int y = Integer.parseInt(a[1]);
				int z = Integer.parseInt(a[2]);
				
				to = new Location(who.getLocation().getWorld(), x, y, z);
			}
			else if(a.length == 4)
			{
				who = this.getServer().getPlayer(a[0]);
				if(who == null)
					e.sendMessage("#FF8966Player not found : "+a[0]);

				int x = Integer.parseInt(a[1]);
				int y = Integer.parseInt(a[2]);
				int z = Integer.parseInt(a[3]);
				
				to = new Location(who.getLocation().getWorld(), x, y, z);
			}
			
			if(who != null && to != null)
			{
				e.sendMessage("#FF8966Teleported to : "+to);
				who.setLocation(to);
			}
		}
		else if (cmd.equals("time")) {
			if(a.length == 1)
			{
				long newTime = Long.parseLong(a[0]);
				Player player = (Player) e;
				player.getLocation().getWorld().setTime(newTime);
				e.sendMessage("#82FFDBSet time to  :"+newTime);
			}
			else
				e.sendMessage("#82FFDBSyntax : /time [0-10000]");
		}
		return true;
	}
}
