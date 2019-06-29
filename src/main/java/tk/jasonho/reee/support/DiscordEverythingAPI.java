package tk.jasonho.reee.support;

import tk.jasonho.reee.ReeeMain;
import tk.jasonho.shortcuts.everything.EverythingAPI;
import tk.jasonho.shortcuts.everything.log.LoggerManager;
import tk.jasonho.shortcuts.everything.yaml.YamlConfiguration;
import tk.jasonho.shortcuts.everything.yaml.YamlConfigurationObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DiscordEverythingAPI extends EverythingAPI {

    @Override
    public void enable() { }

    @Override
    public void disable() { }

    @Override
    public LoggerManager getLogger() {
        return new DiscordLoggerManager();
    }

    private YamlConfigurationObject config;

    @Override
    public YamlConfigurationObject getConfig() {
        if(this.config != null) {
            return this.config;
        }

        try {
            File config = new File(this.getDataFolder().getAbsolutePath() + File.separator + "config.yml");

            if(!config.exists()) {
                // clear it just in case
                config.delete();

                // new.
                if(!config.createNewFile()) {
                    // can't create new file, maybe try to overwrite.
                    this.getLogger().warn("We were unable to write a new file to disk, we will try overwriting.");
                }

                // write
                InputStream is = ReeeMain
                        .class
                        .getClassLoader()
                        .getResourceAsStream("config.yml");
                FileOutputStream fos = new FileOutputStream(config);

                final byte[] bytes = new byte[8192];
                for(int read; (read = is.read(bytes)) != -1;) {
                    fos.write(bytes, 0, read);
                }

                fos.close();
                is.close();

                this.getLogger().info("Successfully made a new config file.");
            }

            this.config = YamlConfiguration.load(config);
            return this.config;
        } catch(IOException e) {
            e.printStackTrace();
            this.getLogger().error("Unable to create a config file @ "
                    + this.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
            ReeeMain.exit(true);
        }
        return this.config;
    }

    @Override
    public YamlConfigurationObject getLangConfig() {
        // TODO: language.
        return null;
    }

    @Override
    public File getDataFolder() {
        return new File(".");
    }
}
