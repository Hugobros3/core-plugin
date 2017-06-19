package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.interfaces.EntityFlying;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Regulates flying */
public class FlyCommandHandler implements CommandHandler {

	private final ChunkStoriesPlugin plugin;
	
	public FlyCommandHandler(ChunkStoriesPlugin plugin)
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
		
		if(!emitter.hasPermission("self.toggleFly"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		
		Entity controlledEntity = player.getControlledEntity();
		if (controlledEntity != null && controlledEntity instanceof EntityFlying)
		{
			boolean state = ((EntityFlying) controlledEntity).getFlyingComponent().get();
			state = !state;
			player.sendMessage("Flying mode set to: " + state);
			((EntityFlying) controlledEntity).getFlyingComponent().set(state);
			return true;
		}
		
		emitter.sendMessage("This action doesn't apply to your current entity.");
		
		return true;
	}

}
