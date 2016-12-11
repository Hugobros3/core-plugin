package xyz.chunkstories.coreplugin.handlers;

import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.Player;

//(c) 2015-2016 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class WeatherCommandHandler implements CommandHandler {

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

		if (arguments.length == 1) {
			float overcastFactor = Float.parseFloat(arguments[0]);
			player.getLocation().getWorld().setWeather(overcastFactor);
		} else
			emitter.sendMessage("#82FFDBSyntax : /weather [0.0 - 1.0]");
		return false;
	}

}
