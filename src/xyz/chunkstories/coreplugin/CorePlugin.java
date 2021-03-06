package xyz.chunkstories.coreplugin;

import io.xol.chunkstories.api.GameContext;
import io.xol.chunkstories.api.client.ClientInterface;
import io.xol.chunkstories.api.plugin.ChunkStoriesPlugin;
import io.xol.chunkstories.api.plugin.PluginInformation;
import io.xol.chunkstories.api.plugin.commands.Command;
import io.xol.chunkstories.api.plugin.commands.CommandEmitter;
import io.xol.chunkstories.api.plugin.commands.CommandHandler;
import io.xol.chunkstories.api.server.ServerInterface;
import io.xol.chunkstories.api.world.WorldMaster;

import xyz.chunkstories.coreplugin.handlers.*;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class CorePlugin extends ChunkStoriesPlugin {
	public CorePlugin(PluginInformation pluginInformation, GameContext pluginExecutionContext) {
		super(pluginInformation, pluginExecutionContext);

		//Only activates if we are a server or a master world
		if (pluginExecutionContext instanceof ServerInterface || (pluginExecutionContext instanceof ClientInterface
				&& ((ClientInterface) pluginExecutionContext).getWorld() instanceof WorldMaster)) {
			
			this.getPluginManager().registerCommandHandler("tp", new TpCommandHandler(this));
			this.getPluginManager().registerCommandHandler("give", new GiveCommandHandler(this.pluginExecutionContext.getContent()));
			this.getPluginManager().registerCommandHandler("clear", new ClearCommandHandler());
			this.getPluginManager().registerCommandHandler("time", new TimeCommandHandler());
			this.getPluginManager().registerCommandHandler("weather", new WeatherCommandHandler());
			this.getPluginManager().registerCommandHandler("spawn", new SpawnCommandHandler(this));
			this.getPluginManager().registerCommandHandler("setspawn", new SpawnCommandHandler(this));
			this.getPluginManager().registerCommandHandler("spawnEntity", new SpawnEntityCommandHandler(this));
			this.getPluginManager().registerCommandHandler("fly", new FlyCommandHandler(this));
			this.getPluginManager().registerCommandHandler("creative", new CreativeCommandHandler(this));
			this.getPluginManager().registerCommandHandler("food", new FoodCommandHandler(this));
			this.getPluginManager().registerCommandHandler("health", new HealthCommandHandler(this));
		}
		else
		{
			System.out.println("Disabled because of ");
			//If the plugin is disabled, we make sure all commands return false and are forwarded 
			for(Command command : pluginInformation.getCommands())
			{
				this.getPluginManager().registerCommandHandler(command.getName(), new FakeCommandHandler());
			}
			
			/*this.getPluginManager().registerCommandHandler("tp", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("give", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("clear", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("time", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("weather", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("spawn", new FakeCommandHandler());
			this.getPluginManager().registerCommandHandler("setSpawn", new FakeCommandHandler());*/
		}
	}

	// Returns false to let know the command handler to forward it to whatever's next.
	class FakeCommandHandler implements CommandHandler {

		@Override
		public boolean handleCommand(CommandEmitter emitter, Command command, String[] arguments) {
			return false;
		}
		
	}
	
	public void onEnable() {
		System.out.println("Enabling Core plugin ... ");
	}

	public void onDisable() {
		System.out.println("Disabling Core plugin");
	}
}
