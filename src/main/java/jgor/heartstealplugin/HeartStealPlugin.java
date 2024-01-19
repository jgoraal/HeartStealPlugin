package jgor.heartstealplugin;

import jgor.heartstealplugin.CraftingSpecialItemsGUI.CraftingSpecialItemsGUI;
import StuckCommand.StuckCommand;
import jgor.heartstealplugin.CraftingSpecialItemsGUI.GuiListener;
import jgor.heartstealplugin.UnstuckCommand.UnStuckCommand;
import jgor.heartstealplugin.auctions.AuctionCommand;
import jgor.heartstealplugin.auctions.AuctionListeners;
import jgor.heartstealplugin.auctions.MarketGui;
import jgor.heartstealplugin.fireSpreadCancel.FireSpreadCancel;
import jgor.heartstealplugin.restore.heart.command.RestoreHeartCommand;
import jgor.heartstealplugin.start.protection.StartProtection;
import jgor.heartstealplugin.stone.drop.StoneDrop;
import jgor.heartstealplugin.toggle.teleport.end.ToggleEndTeleport;
import jgor.heartstealplugin.vanish.Vanish;
import jgor.heartstealplugin.vanish.VanishListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
    private AuctionCommand auctionCommand;
    private final AuctionListeners auctionListeners = new AuctionListeners();
    private final FireSpreadCancel fireSpreadCancel = new FireSpreadCancel();
    private RestoreHeartCommand restoreHeartCommand;
    private final ToggleEndTeleport toggleEndTeleport = new ToggleEndTeleport();
    private final StoneDrop stoneDrop = new StoneDrop();
    private final StartProtection startProtection = new StartProtection();

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "Plugin został włączony!");
        StuckCommand.getStuckPlayers().clear();
        createRecipes();
        MarketGui.loadAllData();
        heartStealListener.loadConfigData();
        getServer().getPluginManager().registerEvents(heartStealListener, this);
        getServer().getPluginManager().registerEvents(redemptionItem, this);
        getServer().getPluginManager().registerEvents(stuckCommand, this);
        getServer().getPluginManager().registerEvents(guiListener, this);
        getServer().getPluginManager().registerEvents(vanishListener, this);
        getServer().getPluginManager().registerEvents(auctionListeners, this);
        getServer().getPluginManager().registerEvents(fireSpreadCancel, this);
        getServer().getPluginManager().registerEvents(toggleEndTeleport, this);
        getServer().getPluginManager().registerEvents(startProtection, this);
        //getServer().getPluginManager().registerEvents(stoneDrop, this);
        getCommand("stuck").setExecutor(stuckCommand);
        getCommand("undostuck").setExecutor(unStuckCommand);
        getCommand("crafting").setExecutor(craftingSpecialItemsGUI);
        getCommand("vanish").setExecutor(vanish);
        auctionCommand = new AuctionCommand(this);
        getCommand("rynek").setExecutor(auctionCommand);
        getCommand("rynek").setTabCompleter(auctionCommand);
        restoreHeartCommand = new RestoreHeartCommand(this);
        getCommand("serce").setExecutor(restoreHeartCommand);
        MarketGui.scheduleItemExpirationCheck();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(HeartStealPlugin.getInstance());
        closePlayerInventories();
        MarketGui.saveAllData();
        heartStealListener.saveConfigData();
    }

    public static HeartStealPlugin getInstance() {
        return getPlugin(HeartStealPlugin.class);
    }

    public void createRecipes() {
        HeartItem.createRecipe();
        RedemptionItem.createRecipe();
    }

    private void closePlayerInventories() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.closeInventory();
        }
    }

    public HeartStealListener getHeartStealListener() {
        return heartStealListener;
    }
    
}
