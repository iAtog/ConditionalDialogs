package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class ProxyCommandMethod extends DialogMethod {

	private CharacterDialoguePlugin main;
	
	public ProxyCommandMethod(CharacterDialoguePlugin main) {
		super("proxy_command");
		this.main = main;
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		String[] args = arg.split(",");
		String command = null;
		String channel = null;

		if(args.length == 1) {
			command = args[0];
			channel = "BungeeCord";
		} else if(args.length == 2) {
			command = args[0];
			channel = args[1];
		} else {
			return;
		}
		
		if(command.startsWith("/")) {
			command = command.substring(1);
		}
		
		out.writeUTF(command);
		out.writeUTF("proxy command execution by characterdialogue");

		player.sendPluginMessage(main, channel, out.toByteArray());
	}
}
