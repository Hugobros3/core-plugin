package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.Content;
import io.xol.chunkstories.api.entity.interfaces.EntityWithInventory;
import io.xol.chunkstories.api.item.Item;
import io.xol.chunkstories.api.item.ItemType;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.Player;
import io.xol.chunkstories.api.server.ServerInterface;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.core.item.ItemVoxel;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class GiveCommandHandler implements CommandHandler {

	public GiveCommandHandler(Content gameContent)
	{
		this.gameContent = gameContent;
	}
	
	private final Content gameContent;
	
	@Override
	public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {
		if(!emitter.hasPermission("server.tp"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		if (!(emitter instanceof Player)) {
			emitter.sendMessage("You need to be a player to use this command.");
			return true;
		}

		Player player = (Player) emitter;

		if (arguments.length == 0) {
			player.sendMessage("#FF969BSyntax : /give <item> [amount] [to]");
			return true;
		}

		int amount = 1;
		Player to = player;

		String itemName = arguments[0];

		// Look for the item first
		ItemType type = gameContent.items().getItemTypeByName(itemName);
		if (type == null) {
			// Try me bitch
			try {
				type = gameContent.items().getItemTypeById(Integer.parseInt(itemName));
			} catch (NumberFormatException ex) {
			}
		}

		// If the type was found we are simply trying to spawn an item
		Item item = null;
		if (type != null)
			item = type.newItem();
		else {
			String voxelName = itemName;
			int voxelMeta = 0;
			if (voxelName.contains(":")) {
				voxelMeta = Integer.parseInt(voxelName.split(":")[1]);
				voxelName = voxelName.split(":")[0];
			}

			// Try to find a matching voxel
			Voxel voxel = gameContent.voxels().getVoxelByName(itemName);
			if (voxel == null) {
				// Try me bitch
				try {
					voxel = gameContent.voxels().getVoxelById(Integer.parseInt(itemName));
				} catch (NumberFormatException ex) {
				}
			}

			if (voxel != null) {
				// Spawn new itemPile in his inventory
				ItemVoxel itemVoxel = (ItemVoxel) gameContent.items().getItemTypeByName("item_voxel").newItem();
				itemVoxel.voxel = voxel;
				itemVoxel.voxelMeta = voxelMeta;

				item = itemVoxel;
			}
		}

		if (item == null) {
			player.sendMessage("#FF969BItem or voxel \"" + arguments[0] + " can't be found.");
			return true;
		}

		if (arguments.length >= 2) {
			amount = Integer.parseInt(arguments[1]);
		}
		if (arguments.length >= 3) {
			if(gameContent instanceof ServerInterface)
				to = ((ServerInterface)gameContent).getPlayerByName(arguments[2]);
			else {
				player.sendMessage("#FF969BThis is a singleplayer world - there are no other players");
				return true;
			}
		}
		if (to == null) {
			player.sendMessage("#FF969BPlayer \"" + arguments[2] + " can't be found.");
			return true;
		}
		ItemPile itemPile = new ItemPile(item);
		itemPile.setAmount(amount);

		((EntityWithInventory) to.getControlledEntity()).getInventory().addItemPile(itemPile);
		player.sendMessage("#FF969BGave " + (amount > 1 ? amount + "x " : "" ) + "#4CFF00" + itemPile.getItem().getName() + " #FF969Bto " + to.getDisplayName());

		return true;
	}

}
