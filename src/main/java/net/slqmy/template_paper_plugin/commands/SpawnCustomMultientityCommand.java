package net.slqmy.template_paper_plugin.commands;

import java.util.stream.Stream;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.StringArgument;
import net.slqmy.template_paper_plugin.TemplatePaperPlugin;
import net.slqmy.template_paper_plugin.custom_multientity.CustomMultientity;
import net.slqmy.template_paper_plugin.language.Message;

public class SpawnCustomMultientityCommand extends CommandAPICommand {

  public SpawnCustomMultientityCommand(TemplatePaperPlugin plugin) {
    super("spawn-custom-multientity");

    String customMultientityArgumentNodeName = "custom-multientity-id";

    String[] customMultientityIds = Stream.of(CustomMultientity.values()).map((customMultientity) -> customMultientity.name()).toArray(String[]::new);

    Argument<CustomMultientity> customMultientityArgument = new CustomArgument<>(new StringArgument(customMultientityArgumentNodeName), (info) -> {
      String input = info.currentInput();

      try {
        return CustomMultientity.valueOf(input);
      } catch (IllegalArgumentException exception) {
        throw CustomArgumentException.fromAdventureComponent(plugin.getLanguageManager().getMessage(Message.UNKNOWN_CUSTOM_MULTIENTITY, info.sender(), input));
      }
    }).includeSuggestions(ArgumentSuggestions.strings(customMultientityIds));

    executesPlayer((info) -> {
      CustomMultientity multientity = (CustomMultientity) info.args().get(customMultientityArgumentNodeName);

      plugin.getCustomMultientityManager().spawnEntity(multientity, info.sender().getLocation());
    });

    withPermission(CommandPermission.OP);
    withArguments(customMultientityArgument);

    register(plugin);
  }
}
