package xyz.chunkstories.coreplugin;

import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.server.Player;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.core.item.ItemVoxel;
import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.entity.interfaces.EntityWithInventory;
import io.xol.chunkstories.api.item.Item;
import io.xol.chunkstories.api.item.ItemType;
import io.xol.chunkstories.item.ItemPile;
import io.xol.chunkstories.item.ItemTypes;
import io.xol.chunkstories.server.Server;
import io.xol.chunkstories.voxel.Voxels;

//(c) 2015-2016 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class CorePlugin extends ChunkStoriesPlugin {
	public void onEnable() {
		System.out.println("Enabling Core plugin ... " + getServer());
	}

	public void onDisable() {
		System.out.println("Disabling Core plugin");
	}
	
	public boolean handleCommand(CommandEmitter e, Command cmd, String[] a) {
		if ((e instanceof Player)) {
			Player player = (Player) e;
			
			if (cmd.equals("clear") && e.hasPermission("server.admin")) {
				player.sendMessage("#FF969BRemoving " + ((EntityWithInventory) player.getControlledEntity()).getInventory().size()
						+ " items from your inventory.");
				((EntityWithInventory) player.getControlledEntity()).getInventory().clear();
				
			} else if (cmd.equals("give") && e.hasPermission("server.admin")) {
				if (a.length == 0) {
					player.sendMessage("#FF969BSyntax : /give <item> [amount] [to]");
					return true;
				}
				int amount = 1;
				Player to = player;
				
				String itemName = a[0];
				
				//Look for the item first
				ItemType type = ItemTypes.getItemTypeByName(itemName);
				if (type == null) {
					//Try me bitch
					try{
						type = ItemTypes.getItemTypeById(Integer.parseInt(itemName));
					}
					catch(NumberFormatException ex) { }
				}
				
				//If the type was found we are simply trying to spawn an item
				Item item = null;
				if(type != null)
					item = type.newItem();
				else
				{
					String voxelName = itemName;
					int voxelMeta = 0;
					if(voxelName.contains(":"))
					{
						voxelMeta = Integer.parseInt(voxelName.split(":")[1]);
						voxelName = voxelName.split(":")[0];
					}
					
					//Try to find a matching voxel
					Voxel voxel = Voxels.getVoxelTypeByName(itemName);
					if (voxel == null) {
						//Try me bitch
						try{
							voxel = Voxels.get(Integer.parseInt(itemName));
						}
						catch(NumberFormatException ex) { }
					}
					
					if(voxel != null)
					{
						//Spawn new itemPile in his inventory
						ItemVoxel itemVoxel = (ItemVoxel)ItemTypes.getItemTypeByName("item_voxel").newItem();
						itemVoxel.voxel = voxel;
						itemVoxel.voxelMeta = voxelMeta;
						
						item = itemVoxel;
					}
				}
				
				
				if (item == null) {
					player.sendMessage("#FF969BItem or voxel \"" + a[0] + " can't be found.");
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
				ItemPile itemPile = new ItemPile(item);
				itemPile.setAmount(amount);
				
				((EntityWithInventory) to.getControlledEntity()).getInventory().addItemPile(itemPile);
				player.sendMessage("#FF969BGave " + itemPile + " to " + to);
			}
			else if (cmd.equals("tp") && e.hasPermission("server.admin")) {
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
			else if (cmd.equals("time") && e.hasPermission("server.admin")) {
				if(a.length == 1)
				{
					long newTime = Long.parseLong(a[0]);
					player.getLocation().getWorld().setTime(newTime);
					e.sendMessage("#82FFDBSet time to  :"+newTime);
				}
				else
					e.sendMessage("#82FFDBSyntax : /time [0-10000]");
			}
			else if (cmd.equals("weather") && e.hasPermission("server.admin")) {
				if(a.length == 1)
				{
					float overcastFactor = Float.parseFloat(a[0]);
					player.getLocation().getWorld().setWeather(overcastFactor);
				}
				else
					e.sendMessage("#82FFDBSyntax : /weather [0.0 - 1.0]");
			}
		}
		return true;
	}
}
