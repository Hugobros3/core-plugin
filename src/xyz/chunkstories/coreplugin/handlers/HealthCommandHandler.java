package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.EntityLiving;
import io.xol.chunkstories.api.entity.interfaces.EntityFlying;
import io.xol.chunkstories.api.player.Player;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Heals */
public class HealthCommandHandler implements CommandHandler {

	private final ChunkStoriesPlugin plugin;
	
	public HealthCommandHandler(ChunkStoriesPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	// Lazy, why does Java standard lib doesn't have a clean way to do this tho
	// http://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	@Override
	public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {

		if (!(emitter instanceof Player)) {
			emitter.sendMessage("You need to be a player to use this command.");
			return true;
		}

		Player player = (Player) emitter;
		
		if(!emitter.hasPermission("self.sethealth"))
		{
			emitter.sendMessage("You don't have the permission.");
			return true;
		}
		
		if(arguments.length < 1 || !isNumeric(arguments[0]))
		{
			emitter.sendMessage("Syntax: /health <hp>");
			return true;
		}
		
		float health = Float.parseFloat(arguments[0]);
		
		Entity controlledEntity = player.getControlledEntity();
		if (controlledEntity != null && controlledEntity instanceof EntityLiving)
		{
			((EntityLiving)controlledEntity).setHealth(health);
			player.sendMessage("Health set to: " + health + "/"+((EntityLiving)controlledEntity).getMaxHealth());
			return true;
		}
		
		emitter.sendMessage("This action doesn't apply to your current entity.");
		
		return true;
	}

}
