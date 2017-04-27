package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.Player;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Handles the (re)spawn point of a world */
public class SpawnCommandHandler implements CommandHandler {

	private final ChunkStoriesPlugin plugin;
	
	public SpawnCommandHandler(ChunkStoriesPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {

		if (!(emitter instanceof Player)) {
			emitter.sendMessage("You need to be a player to use this command.");
			return true;
		}
		
		Player player = (Player) emitter;
		if(command.getName().equals("spawn"))
		{
			if(!emitter.hasPermission("world.spawn"))
			{
				emitter.sendMessage("You don't have the permission.");
				return true;
			}
			
			Location loc = player.getWorld().getDefaultSpawnLocation();
			player.setLocation(loc);
			
			emitter.sendMessage("#00FFD0Teleported to spawn");
			return true;
		}
		else if(command.getName().equals("setspawn"))
		{
			if(!emitter.hasPermission("world.spawn.set"))
			{
				emitter.sendMessage("You don't have the permission.");
				return true;
			}
			
			Location loc = player.getLocation();
			player.getWorld().setDefaultSpawnLocation(loc);
			
			emitter.sendMessage("#00FFD0Set default spawn to : "+loc);
			return true;
		}

		return false;
	}

}
