package jgor.heartstealplugin;

import jgor.heartstealplugin.CraftingSpecialItemsGUI.CraftingSpecialItemsGUI;
import StuckCommand.StuckCommand;
import jgor.heartstealplugin.CraftingSpecialItemsGUI.GuiListener;
import jgor.heartstealplugin.UnstuckCommand.UnStuckCommand;
import jgor.heartstealplugin.auctions.AuctionCommand;
import jgor.heartstealplugin.auctions.AuctionListeners;
import jgor.heartstealplugin.fireSpreadCancel.FireSpreadCancel;
import jgor.heartstealplugin.vanish.Vanish;
import jgor.heartstealplugin.vanish.VanishListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeartStealPlugin extends JavaPlugin {

    private final HeartStealListener heartStealListener = new HeartStealListener();
    private final RedemptionItem redemptionItem = new RedemptionItem();
    private final StuckCommand stuckCommand = new StuckCommand();
    private final UnStuckCommand unStuckCommand = new UnStuckCommand();
    private final CraftingSpecialItemsGUI craftingSpecialItemsGUI = new CraftingSpecialItemsGUI();
    private final GuiListener guiListener = new GuiListener();
    private final Vanish vanish = new Vanish();
    private final VanishListener vanishListener = new VanishListener();
    private final AuctionCommand auctionCommand = new AuctionCommand();
    private final AuctionListeners auctionListeners = new AuctionListeners();
    private final FireSpreadCancel fireSpreadCancel = new FireSpreadCancel();



    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "Plugin został włączony!");
        StuckCommand.getStuckPlayers().clear();
        createRecipes();
        heartStealListener.loadConfigData();
        getServer().getPluginManager().registerEvents(heartStealListener, this);
        getServer().getPluginManager().registerEvents(redemptionItem, this);
        getServer().getPluginManager().registerEvents(stuckCommand, this);
        getServer().getPluginManager().registerEvents(guiListener, this);
        getServer().getPluginManager().registerEvents(vanishListener, this);
        getServer().getPluginManager().registerEvents(auctionListeners, this);
        getServer().getPluginManager().registerEvents(fireSpreadCancel, this);
        getCommand("stuck").setExecutor(stuckCommand);
        getCommand("undostuck").setExecutor(unStuckCommand);
        getCommand("crafting").setExecutor(craftingSpecialItemsGUI);
        getCommand("vanish").setExecutor(vanish);
        getCommand("rynek").setExecutor(auctionCommand);
    }


    @Override
    public void onDisable() {
        heartStealListener.saveConfigData();
    }

    public static HeartStealPlugin getInstance() {
        return getPlugin(HeartStealPlugin.class);
    }

    public void createRecipes() {
        HeartItem.createRecipe();
        RedemptionItem.createRecipe();
    }
}
