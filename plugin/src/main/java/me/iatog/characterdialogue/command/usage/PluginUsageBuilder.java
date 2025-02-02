package me.iatog.characterdialogue.command.usage;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.usage.UsageBuilder;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.util.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class PluginUsageBuilder implements UsageBuilder {

    private final CharacterDialoguePlugin main;

    public PluginUsageBuilder(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @Override
    public Component getUsage(CommandContext commandContext) {
        TextComponent usage = (TextComponent) commandContext.getCommand().getUsage();
        StringBuilder fullCommand = new StringBuilder("/");

        if(usage == null) usage = Component.text("");

        for(String label : commandContext.getLabels()) {
            fullCommand.append(label).append(" ");
        }

        return AdventureUtil.minimessage(main.language("command.usage"),
              AdventureUtil.placeholder("command", fullCommand.toString().trim()),
              AdventureUtil.placeholder("args", usage.content().trim()));
    }
}
