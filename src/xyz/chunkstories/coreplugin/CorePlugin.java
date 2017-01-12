package xyz.chunkstories.coreplugin;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.PluginInformation;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;

import xyz.chunkstories.coreplugin.handlers.*;

//(c) 2015-2016 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class CorePlugin extends ChunkStoriesPlugin {
	public CorePlugin(PluginInformation pluginInformation, GameContext pluginExecutionContext) {
		super(pluginInformation, pluginExecutionContext);
	
		this.getPluginManager().registerCommandHandler("tp", new TpCommandHandler(this));
		this.getPluginManager().registerCommandHandler("give", new GiveCommandHandler(this.pluginExecutionContext.getContent()));
		this.getPluginManager().registerCommandHandler("clear", new ClearCommandHandler());
		this.getPluginManager().registerCommandHandler("time", new TimeCommandHandler());
		this.getPluginManager().registerCommandHandler("weather", new WeatherCommandHandler());
	}

	public void onEnable() {
		System.out.println("Enabling Core plugin ... ");
	}

	public void onDisable() {
		System.out.println("Disabling Core plugin");
	}

	public boolean handleCommand(CommandEmitter emitter, Command cmd, String[] arguments) {
		emitter.sendMessage("Should not happen :/");
		return true;
	}
}
