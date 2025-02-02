package me.iatog.characterdialogue.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGUI extends GUI {

    private final Gui gui;

    public MainGUI(CharacterDialoguePlugin main) {
        super("main");

        Gui gui = Gui.gui()
              .title(AdventureUtil.minimessage("<red><bold>CharacterDialogue Menu"))
              .disableAllInteractions()
              .rows(3)
              .create();

        gui.getFiller().fill(
              ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
                    .name(Component.empty())
                    .asGuiItem()
        );

        gui.setItem(2, 2, ItemBuilder.from(Material.WRITABLE_BOOK)
              .name(AdventureUtil.minimessage("<green><bold>Dialogues").decoration(TextDecoration.ITALIC, false))
              .lore(buildLore("<empty>", "<gray>See all dialogues."))
              .asGuiItem(event -> main.getGUIFactory().getGui("dialogues").load((Player) event.getWhoClicked())));

        gui.setItem(2, 4,
              ItemBuilder.from(Material.ENCHANTED_GOLDEN_APPLE)
                    .name(AdventureUtil.minimessage("<gold><bold>Choices").decoration(TextDecoration.ITALIC, false))
                    .lore(Component.text(""))
                    .asGuiItem());

        gui.setItem(2, 6,
              ItemBuilder.from(Material.REDSTONE_TORCH)
                    .name(AdventureUtil.minimessage("&8&lConfiguration").decoration(TextDecoration.ITALIC, false))
                    .lore(Component.text(""))
                    .asGuiItem((event) -> {

                    }));

        gui.setItem(2, 8,
              ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZjMWE3OWVjZTAwMjczN2ZhNDBmMjBjNTVjYTYxOGY4YTUzOTFmNDViMGQ3M2M3OTM1NzI1Y2QxNzhiY2IifX19")
                    .name(AdventureUtil.minimessage("<yellow><bold>NPCs").decoration(TextDecoration.ITALIC, false))
                    .lore()
                    .asGuiItem((event) -> {

                    }));


        setupObservers(gui);
        this.gui = gui;
    }

    @Override
    public void load(Player player) {
        if(!player.getName().equals("aatog")) {
            player.sendMessage("Gui not available");
            return;
        }

        gui.open(player);
    }
}
