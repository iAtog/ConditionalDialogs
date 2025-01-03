package me.iatog.characterdialogue.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Usage;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.command.object.CSubCommand;
import me.iatog.characterdialogue.command.object.CommandInfo;
import me.iatog.characterdialogue.libraries.ItemManager;
import me.iatog.characterdialogue.util.AdventureUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.iatog.characterdialogue.util.TextUtils.colorize;

@Command(
      names = "item"
)
public class ItemCommands extends CSubCommand implements CommandClass {

    private final CharacterDialoguePlugin main = CharacterDialoguePlugin.getInstance();

    public ItemCommands() {
        super();
    }

    public void addCommands() {
        addCommand("characterd item gui", "", "Open the items gui");
        addCommand("characterd item save", "<newName>", "Save the item in your main hand");
        addCommand("characterd item give", "<name>", "Get an item");

    }

    @Command(names = "", desc = "Main command")
    public void mainCommand(CommandSender sender) {
        mainCommandLogic(main, sender);
    }

    @Command(
          names = "gui",
          permission = "characterdialogue.command.item.gui"
    )
    public void seeGui(@Sender Player player) {
        AdventureUtil.sendMessage(player, main.language("command.gui.success", "items"));
        main.getGUIFactory().getGui("items").load(player);
    }

    @Usage("<id>")
    @Command(
          names = "save",
          permission = "characterdialogue.command.item.save"
    )
    public void saveItem(@Sender Player player, @OptArg("") String id) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if(itemInHand.getAmount() <= 0 || itemInHand.getType() == Material.AIR) {
            AdventureUtil.sendMessage(player, main.language(true, "command.item.no-hand"));
            return;
        }

        if(id == null || id.isEmpty()) {
            AdventureUtil.sendMessage(player, main.language(true, "command.item.no-id"));
            return;
        }

        ItemManager manager = main.getServices().getItemManager();

        if(manager.existsItem(id)) {
            AdventureUtil.sendMessage(player, main.language(true, "command.item.already-exists"));
            return;
        }

        manager.saveItem(id, itemInHand);
        AdventureUtil.sendMessage(player, main.language(true, "command.item.success"));
    }

    @Usage("<id> [player]")
    @Command(
          names = "give",
          permission = "characterdialogue.command.item.give"
    )
    public void giveItem(CommandSender sender, ItemStack item, @OptArg Player playerOpt) {
        if(item == null) {
            AdventureUtil.sendMessage(sender, main.language(true, "command.item.not-found"));
            return;
        }

        Player target = null;

        if (playerOpt == null) {
            if (!(sender instanceof Player player)) {
                AdventureUtil.sendMessage(sender, main.language(true, "command.dialogue.console"));
                return;
            }

            target = player;
        } else {
            target = playerOpt;
        }

        target.getInventory().addItem(item);
        AdventureUtil.sendMessage(sender, main.language(true, "command.item.give-success"));
    }
}
