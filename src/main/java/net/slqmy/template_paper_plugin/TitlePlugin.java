package net.slqmy.template_paper_plugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.TitlePart;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.stream.Stream;

@DefaultQualifier(NonNull.class)
public final class TitlePlugin extends JavaPlugin implements Listener {

  private Component titleComponent;

  private List<TextComponent> changingSubtitleComponents;

  private long secondsBeforeChaning;

  @Override
  public void onEnable() {
    getDataFolder().mkdir();
    saveDefaultConfig();

    Bukkit.getPluginManager().registerEvents(this, this);

    FileConfiguration configuration = getConfig();

    titleComponent =  Component.text(ChatColor.translateAlternateColorCodes('&', configuration.getString("title")));

    changingSubtitleComponents = Stream.of((List<String>) configuration.getList("subtitle")).map((string) -> Component.text(ChatColor.translateAlternateColorCodes('&', ""))).toList();

    secondsBeforeChaning = configuration.getLong("seconds-before-changing");
  }

  @EventHandler
  public void onResourcePackLoad(PlayerResourcePackStatusEvent event) {
    PlayerResourcePackStatusEvent.Status status = event.getStatus();

    if (status.equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
      Player player = event.getPlayer();

      player.sendTitlePart(TitlePart.TITLE, titleComponent);

      new BukkitRunnable() {

        private int subTitleIndex = 0;

        @Override
        public void run() {

          player.sendTitlePart(TitlePart.SUBTITLE, changingSubtitleComponents.get(subTitleIndex));

          subTitleIndex++;
          subTitleIndex %= changingSubtitleComponents.size();
        }
        
      }.runTaskTimer(this, secondsBeforeChaning * 20L, secondsBeforeChaning * 20L);
    }
  }
}
