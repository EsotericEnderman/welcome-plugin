package net.slqmy.template_paper_plugin.util.types;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockLocation {

  private String worldName;

  private int x;
  private int y;
  private int z;

  public String getWorldName() {
    return worldName;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public BlockLocation(String worldName, int x, int y, int z) {
    this.worldName = worldName;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public BlockLocation(Location location) {
    this(location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
  }

  public BlockLocation(Block block) {
    this(block.getLocation());
  }
}
