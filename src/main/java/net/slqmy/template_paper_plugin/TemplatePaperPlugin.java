package net.slqmy.template_paper_plugin;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import net.slqmy.template_paper_plugin.commands.GiveCustomItemCommand;
import net.slqmy.template_paper_plugin.commands.PlaceCustomMultiblockCommand;
import net.slqmy.template_paper_plugin.commands.SetLanguageCommand;
import net.slqmy.template_paper_plugin.commands.SpawnCustomMultientityCommand;
import net.slqmy.template_paper_plugin.custom_item.CustomItem;
import net.slqmy.template_paper_plugin.custom_item.CustomItemManager;
import net.slqmy.template_paper_plugin.custom_multiblock.CustomMultiblock;
import net.slqmy.template_paper_plugin.custom_multiblock.CustomMultiblockManager;
import net.slqmy.template_paper_plugin.custom_multientity.CustomMultientity;
import net.slqmy.template_paper_plugin.custom_multientity.CustomMultientityManager;
import net.slqmy.template_paper_plugin.data.player.PlayerDataManager;
import net.slqmy.template_paper_plugin.file.FileManager;
import net.slqmy.template_paper_plugin.http_server.HttpServerManager;
import net.slqmy.template_paper_plugin.language.LanguageManager;
import net.slqmy.template_paper_plugin.language.Message;
import net.slqmy.template_paper_plugin.resource_pack.ResourcePackManager;

@DefaultQualifier(NonNull.class)
public final class TemplatePaperPlugin extends JavaPlugin {

  private FileManager fileManager;
  private PlayerDataManager playerDataManager;
  private LanguageManager languageManager;
  private ResourcePackManager resourcePackManager;
  private HttpServerManager httpServerManager;
  private CustomItemManager customItemManager;
  private CustomMultientityManager customMultientityManager;
  private CustomMultiblockManager customMultiblockManager;

  private final NamespacedKey customItemIdKey = new NamespacedKey(this, "custom_item_id");
  private final NamespacedKey customEntityIdKey = new NamespacedKey(this, "custom_entity_id");

  public FileManager getFileManager() {
    return fileManager;
  }

  public PlayerDataManager getPlayerDataManager() {
    return playerDataManager;
  }

  public LanguageManager getLanguageManager() {
    return languageManager;
  }

  public ResourcePackManager getResourcePackManager() {
    return resourcePackManager;
  }

  public HttpServerManager getHttpServerManager() {
    return httpServerManager;
  }

  public CustomItemManager getCustomItemManager() {
    return customItemManager;
  }

  public CustomMultientityManager getCustomMultientityManager() {
    return customMultientityManager;
  }

  public CustomMultiblockManager getCustomMultiblockManager() {
    return customMultiblockManager;
  }

  public NamespacedKey getCustomItemIdKey() {
    return customItemIdKey;
  }

  public NamespacedKey getCustomEntityIdKey() {
    return customEntityIdKey;
  }

  @Override
  public void onEnable() {
    getDataFolder().mkdir();
    saveDefaultConfig();

    CommandAPIBukkitConfig commandAPIConfig = new CommandAPIBukkitConfig(this);

    CommandAPI.onLoad(commandAPIConfig);
    CommandAPI.onEnable();

    fileManager = new FileManager(this);
    playerDataManager = new PlayerDataManager(this);
    if (Message.isEnabled()) {
      languageManager = new LanguageManager(this);
    }
    resourcePackManager = new ResourcePackManager(this);
    httpServerManager = new HttpServerManager(this);
    if (CustomItem.isEnabled()) {
      customItemManager = new CustomItemManager(this);
    }
    if (CustomMultientity.isEnabled()) {
      customMultientityManager = new CustomMultientityManager(this);
    }
    if (CustomMultiblock.isEnabled()) {
      customMultiblockManager = new CustomMultiblockManager(this);
    }

    if (Message.isEnabled()) {
      new SetLanguageCommand(this);
    }
    if (CustomItem.isEnabled()) {
      new GiveCustomItemCommand(this);
    }
    if (CustomMultientity.isEnabled()) {
      new SpawnCustomMultientityCommand(this);
    }
    if (CustomMultiblock.isEnabled()) {
      new PlaceCustomMultiblockCommand(this);
    }
  }

  @Override
  public void onDisable() {
    if (playerDataManager != null) {
      playerDataManager.save();
    }
    if (customMultiblockManager != null) {
      customMultiblockManager.save();
    }
  }
}
