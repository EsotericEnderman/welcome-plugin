package dev.enderman.commissions.minecraft.plugins.welcome;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.UUID;
import java.time.Duration;

@DefaultQualifier(NonNull.class)
public final class WelcomePlugin extends JavaPlugin implements Listener {

  private final List<UUID> resourcePackLoadedPlayers = new ArrayList<>();
  private final List<UUID> joinedPlayers = new ArrayList<>();;

  private @Nullable Component titleComponent;
  private @Nullable List<TextComponent> changingSubtitleComponents;
  private long secondsBeforeChanging;

  private long firstSubtitleFadeInTime;
  private long lastSubtitleFadeOutTime;

  private long fadeInTime;
  private long stayTime;
  private long fadeOutTime;

  @Override
  @SuppressWarnings({"SuspiciousToArrayCall"})
  public void onEnable() {
    getDataFolder().mkdir();
    saveDefaultConfig();

    Bukkit.getPluginManager().registerEvents(this, this);

    FileConfiguration configuration = getConfig();

    titleComponent = Component.text(ChatColor.translateAlternateColorCodes('&', configuration.getString("title", "Title (can be changed in config.yml)")));
    changingSubtitleComponents = Stream.of((configuration.getList("subtitle", List.of())).toArray(String[]::new))
        .map((string) -> Component.text(ChatColor.translateAlternateColorCodes('&', string))).toList();
    secondsBeforeChanging = configuration.getLong("seconds-before-changing", 5L);

    firstSubtitleFadeInTime = configuration.getLong("first-subtitle-fade-in-time", 0L);
    lastSubtitleFadeOutTime = configuration.getLong("last-subtitle-fade-out-time", 0L);

    fadeInTime = configuration.getLong("fade-in-time", 0L);
    stayTime = configuration.getLong("stay-time", 10L);
    fadeOutTime = configuration.getLong("fade-out-time", 0L);
  }

  @EventHandler
  public void onResourcePackLoad(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID playerUuid = player.getUniqueId();

    joinedPlayers.add(playerUuid);

    if (resourcePackLoadedPlayers.contains(playerUuid)) {
      new TitleRunnable(this, player);
    }
  }

  @EventHandler
  public void onResourcePackLoad(PlayerResourcePackStatusEvent event) {
    if (event.getStatus() != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
      return;
    }

    Player player = event.getPlayer();
    UUID playerUuid = player.getUniqueId();

    resourcePackLoadedPlayers.add(playerUuid);

    if (joinedPlayers.contains(playerUuid)) {
      new TitleRunnable(this, player);
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID playerUuid = player.getUniqueId();

    joinedPlayers.remove(playerUuid);
    resourcePackLoadedPlayers.remove(playerUuid);
  }

  private final class TitleRunnable extends BukkitRunnable {

    private final Player player;

    private int subtitleIndex = 0;

    public TitleRunnable(WelcomePlugin plugin, Player player) {
      this.player = player;

      runTaskTimer(plugin, 0L, secondsBeforeChanging * 20L);
    }

    @Override
    public void run() {
      if (!player.isOnline()) {
        cancel();
        return;
      }

      assert titleComponent != null;
      assert changingSubtitleComponents != null;

      player.sendTitlePart(TitlePart.TITLE, titleComponent);
      player.sendTitlePart(TitlePart.SUBTITLE, changingSubtitleComponents.get(subtitleIndex));
      player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(subtitleIndex == 0 ? firstSubtitleFadeInTime : fadeInTime), Duration.ofSeconds(stayTime), Duration.ofSeconds(subtitleIndex == changingSubtitleComponents.size() - 1 ? lastSubtitleFadeOutTime : fadeOutTime)));

      subtitleIndex++;
      if (subtitleIndex == changingSubtitleComponents.size()) {
        cancel();
      }
    }
  }
}
