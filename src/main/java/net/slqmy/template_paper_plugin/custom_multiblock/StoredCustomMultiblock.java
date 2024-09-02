package net.slqmy.template_paper_plugin.custom_multiblock;

import java.util.List;
import java.util.Map.Entry;

import net.slqmy.template_paper_plugin.util.types.BlockLocation;

public class StoredCustomMultiblock {
  private final List<List<BlockLocation>> blockLocations;
  private final CustomMultiblock multiblockId;

  public List<List<BlockLocation>> getBlockLocations() {
    return blockLocations;
  }

  public CustomMultiblock getMultiblockId() {
    return multiblockId;
  }

  public StoredCustomMultiblock(List<List<BlockLocation>> blockLocations, CustomMultiblock multiblockId) {
    this.blockLocations = blockLocations;
    this.multiblockId = multiblockId;
  }

  public StoredCustomMultiblock(Entry<CustomMultiblock, AbstractCustomMultiblock> multiblockInfo) {  
    this(multiblockInfo.getValue().getMultiblocks(), multiblockInfo.getKey());
  } 
}
