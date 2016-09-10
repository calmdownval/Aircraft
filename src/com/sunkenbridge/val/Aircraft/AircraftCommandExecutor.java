package com.sunkenbridge.val.Aircraft;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sunkenbridge.val.Aircraft.Executors.*;

public class AircraftCommandExecutor implements CommandExecutor {
	
    private AircraftPlugin plugin;
    
    private static HashMap<String, AircraftExecutor> executors;
    
    static {
    	
    	executors = new HashMap<String, AircraftExecutor>();
    	executors.put("pilot", new PilotExecutor());
    	executors.put("copilot", new CopilotExecutor());
    	executors.put("unpilot", new UnpilotExecutor());
    	executors.put("stop", new StopExecutor());
    	executors.put("noclip", new NoclipExecutor());
    	executors.put("ground", new GroundExecutor());
    	executors.put("board", new BoardExecutor());
    	executors.put("speed", new SpeedExecutor());
    	executors.put("tp", new TpExecutor());
    	
    	DeleteExecutor delete = new DeleteExecutor();
    	executors.put("delete", delete);
    	executors.put("destroy", delete);
    	
    	HelpExecutor help = new HelpExecutor();
    	executors.put("help", help);
    	executors.put("?", help);
    }
    
    
    public AircraftCommandExecutor(AircraftPlugin plugin) {
    	
    	// register commands
    	plugin.getCommand("aircraft").setExecutor(this);
    	plugin.getCommand("ac").setExecutor(this);
        
    	// plugin
    	this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
		// check whether sender is a player
    	Player player;
    	if (sender instanceof Player)
    		player = (Player) sender;
    	
    	// quit if sender is not recognized as a player
    	else
    		return false;
    	
    	// check if the player is an OP
    	if (!player.isOp()) {
    	
    		plugin.sendMessage(player, AircraftPlugin.LANG_MUST_BE_OP);
    		return true;
    	}
    	
    	// get executor
    	String arg = (args.length > 0) ? args[0].toLowerCase() : "pilot";
    	AircraftExecutor executor = executors.get(arg);
    	
    	if (executor == null) {
    		
    		plugin.sendMessage(player, AircraftPlugin.LANG_COMMAND_INVALID);
    		return true;
    	}
    	
    	// check the number of arguments
    	int count = Math.max(args.length - 1, 0);
    	if (count < executor.getMinArgumentCount() || count > executor.getMaxArgumentCount()) {
    		
    		plugin.sendMessage(player, AircraftPlugin.LANG_WRONG_ARGN);
    		return true;
    	}
    	
    	// execute
    	String[] userArgs = (count > 0) ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
    	return executor.execute(plugin, player, userArgs);
	}

}