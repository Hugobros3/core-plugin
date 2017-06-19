package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.ServerInterface;
import xyz.chunkstories.coreplugin.CorePlugin;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class TpCommandHandler implements CommandHandler {

	CorePlugin corePlugin;
	
	public TpCommandHandler(CorePlugin corePlugin)
	{
		this.corePlugin = corePlugin;
	}
	
	@Override
	public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {
		
		if(!emitter.hasPermission("server.tp"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		
		if(!(emitter instanceof Player))
		{
			emitter.sendMessage("You need to be a player to use this command.");
			return true;
		}
		
		Player who = (Player)emitter;
		
		Location to = null;

		if (arguments.length == 1) {
			Player otherPlayer = getPlayer(arguments[0]);
			if (otherPlayer != null)
				to = otherPlayer.getLocation();
			else
				emitter.sendMessage("#FF8966Player not found : " + arguments[0]);
		} else if (arguments.length == 2) {
			who = getPlayer(arguments[0]);
			if (who == null)
				emitter.sendMessage("#FF8966Player not found : " + arguments[0]);

			Player otherPlayer = getPlayer(arguments[1]);
			if (otherPlayer != null)
				to = otherPlayer.getLocation();
			else
				emitter.sendMessage("#FF8966Player not found : " + arguments[1]);
		} else if (arguments.length == 3) {
			int x = Integer.parseInt(arguments[0]);
			int y = Integer.parseInt(arguments[1]);
			int z = Integer.parseInt(arguments[2]);

			to = new Location(who.getLocation().getWorld(), x, y, z);
		} else if (arguments.length == 4) {
			who = getPlayer(arguments[0]);
			if (who == null)
				emitter.sendMessage("#FF8966Player not found : " + arguments[0]);

			int x = Integer.parseInt(arguments[1]);
			int y = Integer.parseInt(arguments[2]);
			int z = Integer.parseInt(arguments[3]);

			to = new Location(who.getLocation().getWorld(), x, y, z);
		}

		if (who != null && to != null) {
			emitter.sendMessage("#FF8966Teleported to : " + to);
			who.setLocation(to);
			return true;
		}
		
		emitter.sendMessage("#FF8966Usage: /tp [who] (<x> <y> <z>)|(to)");
		
		return true;
	}

	private Player getPlayer(String string) {
		
		if(corePlugin.getPluginExecutionContext() instanceof ServerInterface)
			return ((ServerInterface)corePlugin.getPluginExecutionContext()).getPlayerByName(string);
		
		return null;
	}

}
