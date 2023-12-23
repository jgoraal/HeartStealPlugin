package jgor.heartstealplugin.auctions;

import jgor.heartstealplugin.auctions.MarketGui;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class MyPluginDataHandler {

    public static void saveData(String fileName) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        try (Writer writer = new FileWriter(fileName)) {
            yaml.dump(MarketGui.playerSellItemList, writer);
            yaml.dump(MarketGui.playerSellItemPrice, writer);
            yaml.dump(MarketGui.timeItemExpired, writer);
            yaml.dump(MarketGui.sellingPlayerItems, writer);
            yaml.dump(MarketGui.buyingPlayerItems, writer);
            yaml.dump(MarketGui.expiredItems, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(String fileName) {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);

        Yaml yaml = new Yaml(options);

        try (Reader reader = new FileReader(fileName)) {
            MarketGui.playerSellItemList = yaml.load(reader);
            MarketGui.playerSellItemPrice = yaml.load(reader);
            MarketGui.timeItemExpired = yaml.load(reader);
            MarketGui.sellingPlayerItems = yaml.load(reader);
            MarketGui.buyingPlayerItems = yaml.load(reader);
            MarketGui.expiredItems = yaml.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
