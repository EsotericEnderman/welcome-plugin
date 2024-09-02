package net.slqmy.template_paper_plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Stream;

@DefaultQualifier(NonNull.class)
public final class TitlePlugin extends JavaPlugin implements Listener {

  private final MiniMessage miniMessage = MiniMessage.miniMessage();

  private Component titleComponent;

  private List<TextComponent> changingSubtitleComponents;

  private int secondsBeforeChaning;

  @Override
  public void onEnable() {
    getDataFolder().mkdir();
    saveDefaultConfig();

    FileConfiguration configuration = getConfig();

    titleComponent =  Component.text(ChatColor.translateAlternateColorCodes('&', configuration.getString("title")));

    changingSubtitleComponents = Stream.of((List<String>) configuration.getList("subtitle")).map((string) -> Component.text(ChatColor.translateAlternateColorCodes('&', ""))).toList();

    secondsBeforeChaning = configuration.getInt("seconds-before-changing");
  }

  @EventHandler
  public void onResourcePackLoad(PlayerResourcePackStatusEvent event) {
    PlayerResourcePackStatusEvent.Status status = event.getStatus();

    if (status.equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
      Player player = event.getPlayer();

      player.sendMessage(titleComponent);
    }
  }
}
