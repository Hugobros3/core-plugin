package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.interfaces.EntityCreative;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.Player;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Handles creativity */
public class CreativeCommandHandler implements CommandHandler {

	private final ChunkStoriesPlugin plugin;
	
	public CreativeCommandHandler(ChunkStoriesPlugin plugin)
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
		
		if(!emitter.hasPermission("self.toggleCreative"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		
		Entity controlledEntity = player.getControlledEntity();
		if (controlledEntity != null && controlledEntity instanceof EntityCreative)
		{
			boolean state = ((EntityCreative) controlledEntity).getCreativeModeComponent().get();
			state = !state;
			player.sendMessage("Creative mode set to: " + state);
			((EntityCreative) controlledEntity).getCreativeModeComponent().set(state);
			return true;
		}
		
		emitter.sendMessage("This action doesn't apply to your current entity.");
		
		return true;
	}

}
