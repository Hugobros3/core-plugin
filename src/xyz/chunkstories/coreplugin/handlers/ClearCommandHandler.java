package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.entity.interfaces.EntityWithInventory;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.Player;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Removes all items from inventory */
public class ClearCommandHandler implements CommandHandler {

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

		player.sendMessage(
				"#FF969BRemoving " + ((EntityWithInventory) player.getControlledEntity()).getInventory().size()
						+ " items from your inventory.");
		((EntityWithInventory) player.getControlledEntity()).getInventory().clear();

		return true;
	}

}
