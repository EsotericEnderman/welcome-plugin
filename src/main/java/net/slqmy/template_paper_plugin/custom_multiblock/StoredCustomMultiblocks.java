package net.slqmy.template_paper_plugin.custom_multiblock;

import java.util.ArrayList;
import java.util.List;

public class StoredCustomMultiblocks {
  private List<StoredCustomMultiblock> storedCustomMultiblocks = new ArrayList<>();

  public List<StoredCustomMultiblock> getStoredCustomMultiblocks() {
    return storedCustomMultiblocks;
  }

  public void addCustomMultiblock(StoredCustomMultiblock addedCustomMultiblocks) {
    storedCustomMultiblocks.add(addedCustomMultiblocks);
  }
}
