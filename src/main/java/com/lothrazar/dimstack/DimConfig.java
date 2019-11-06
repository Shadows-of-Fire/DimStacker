package com.lothrazar.dimstack;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = DimstackMod.MODID, category = DimstackMod.MODID + ".settings")
public class DimConfig {

  public List<PlayerTransmit> emitters = new ArrayList<>();
  private Configuration config;
  private String[] layers;

  public DimConfig(Configuration configuration) {
    this.config = configuration;
    config.load();
    syncConfig();
    if (config.hasChanged()) {
      config.save();
    }
  }

  @SubscribeEvent
  public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(DimstackMod.MODID)) {
      syncConfig();
    }
  }

  private void syncConfig() {
    String[] def = new String[] {
        //from overworld down to nether
        "0,-1,<,3,minecraft:obsidian,true,0,126,0",
        //from nether up to overworld
        "-1,0,>,200,minecraft:dirt,false,0,5,0",
        //overworld up to end
        "0,1,>,200,minecraft:ender_eye,true,0,10,0",
        //end down to overworld
        "1,0,<,3,minecraft:wool,true,0,200,0"
    };
    this.layers = config.getStringList("DimensionStack", DimstackMod.MODID, def,
        "Layers config.  Each line is one layer transition"
            + "from,to,compare,yLevel,key,x,y,z"
            + "from: start dimension where key and yLimit test is used"
            + "to: target dimension"
            + "compare:  true meansplayer.y > yLimit || false means player.y <= yLimit"
            + "key: Player must hold this in their main hand to trigger the teleport, while passing yLimit test");
    this.parseEmitters();
  }

  private void parseEmitters() {
    this.emitters = new ArrayList<>();
    for (String layer : this.layers) {
      try {
        PlayerTransmit t = this.parseLayer(layer);
        this.emitters.add(t);
      }
      catch (Exception e) {
        //
        DimstackMod.logger.error("Bad config " + layer, e);
      }
    }
  }

  private PlayerTransmit parseLayer(String layer) throws Exception {
    String[] lrs = layer.split(",");
    PlayerTransmit t = new PlayerTransmit();
    t.from = Integer.parseInt(lrs[0]);
    t.to = Integer.parseInt(lrs[1]);
    t.greaterThan = ">".equalsIgnoreCase(lrs[2]);
    t.yLimit = Integer.parseInt(lrs[3]);
    t.key = lrs[4];
    int x = Integer.parseInt(lrs[5]),
        y = Integer.parseInt(lrs[6]),
        z = Integer.parseInt(lrs[7]);
    t.pos = new BlockPos(x, y, z);
    return t;
  }
}
