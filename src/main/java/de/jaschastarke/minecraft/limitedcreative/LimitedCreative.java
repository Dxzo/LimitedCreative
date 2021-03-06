package de.jaschastarke.minecraft.limitedcreative;

import de.jaschastarke.I18n;
import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.PluginLang;
import de.jaschastarke.bukkit.lib.configuration.command.ConfigCommand;
import de.jaschastarke.bukkit.lib.modules.AdditionalBlockBreaks;
import de.jaschastarke.bukkit.lib.modules.InstantHangingBreak;
import de.jaschastarke.utils.ClassDescriptorStorage;

public class LimitedCreative extends Core {
    protected Config config = null;
    protected MainCommand command = null;
    
    @Override
    public void onInitialize() {
        super.onInitialize();
        config = new Config(this);
        
        setLang(new PluginLang("lang/messages", this));
        
        command = new MainCommand(this);
        ConfigCommand cc = new ConfigCommand(config, Permissions.CONFIG);
        cc.setPackageName(this.getName() + " - " + this.getLocale().trans(cc.getPackageName()));
        command.registerCommand(cc);
        commands.registerCommand(command);
        
        Hooks.inizializeHooks(this);
        
        modules.addSharedModule(new AdditionalBlockBreaks(this));
        modules.addSharedModule(new InstantHangingBreak(this));
        modules.addSharedModule(new FeatureBlockItemSpawn(this));
        addModule(new FeatureSwitchGameMode(this));
        addModule(new ModInventories(this));
        addModule(new ModCreativeLimits(this));
        addModule(new ModRegions(this));
        addModule(new ModCmdBlocker(this));
        addModule(new ModGameModePerm(this));
        addModule(new ModBlockStates(this));
        addModule(new FeatureMetrics(this)).setDefaultEnabled(config.getMetrics());
        
        listeners.addListener(new DependencyListener(this));
        
        config.saveDefault();
        
        try {
            Class.forName("de.jaschastarke.hooking.CaptainHook");
        } catch(Exception e) {}
    }
    
    @Override
    public ClassDescriptorStorage getDocCommentStorage() {
        if (cds == null) {
            cds = new ClassDescriptorStorage();
            cds.getResourceBundle().addResourceBundle("lang.doccomments");
        }
        return cds;
    }

    @Override
    public boolean isDebug() {
        return config.getDebug();
    }
    
    public Config getPluginConfig() {
        return config;
    }

    public I18n getLocale() {
        return getLang();
    }

    public MainCommand getMainCommand() {
        return command;
    }
}
