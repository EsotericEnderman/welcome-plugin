package net.slqmy.template_paper_plugin.file;

import java.io.File;
import java.io.IOException;

import net.slqmy.template_paper_plugin.TemplatePaperPlugin;

public class FileManager {

  private final TemplatePaperPlugin plugin;

  public FileManager(TemplatePaperPlugin plugin) {
    this.plugin = plugin;
  }

  public File saveResourceFileFolder(String resourceFileFolderPath, boolean shouldReplaceExistingFiles) {
    try {
      FileUtil.getResourceFileFolderResourceFilePathsRecursively(resourceFileFolderPath).forEach((resourceFilePath) -> {
        plugin.saveResource(resourceFilePath, shouldReplaceExistingFiles);
      });
      return new File(plugin.getDataPath() + File.separator + resourceFileFolderPath);
    } catch (IOException exception) {
      exception.printStackTrace();
      return null;
    }
  }

  public File saveResourceFileFolder(String resourceFileFolderPath) {
    return saveResourceFileFolder(resourceFileFolderPath, true);
  }
}
