package tk.jasonho.reee;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.jasonho.reee.support.DiscordEverythingAPI;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;

public class ReeeMain {

    @Getter
    private static ReeeMain main;

    public static void main(String[] args) {
        // go go go.
        main = new ReeeMain();
    }

    @Getter
    private DiscordEverythingAPI helper;
    @Getter
    private JDA bot;


    public ReeeMain() {
        main = this;

        // helper stuff
        this.helper = new DiscordEverythingAPI();

        // build bot.
        String token;
        if(System.getenv("BOT_TOKEN") != null || System.getenv("BOT_TOKEN") != "") {
            token = System.getenv("BOT_TOKEN");
        } else {
            token = this.helper.getConfig().getString("token", null);
        }

        this.helper.getLogger().info("JDA Login...");
        this.bot = startBot(token, ReeeBot.class);

        // now we wait :)
        this.helper.getLogger().info("Waiting for JDA login...");
    }

    private static JDA startBot(String token, Class<? extends ListenerAdapter> readyCallback) {
        try {
            //ReeeMain.getMain().getHelper().getLogger().info("Info - starting");
            JDA jda = new JDABuilder(token).build();
            jda.addEventListener(readyCallback.getDeclaredConstructor(JDA.class, ReeeMain.class).newInstance(jda, main));
            //ReeeMain.getMain().getHelper().getLogger().debug("Info - ya");

            return jda;
        } catch(LoginException e) {
            getMain().getHelper().getLogger().error("Unable to start bot: " + e.getMessage());
            e.printStackTrace();
            exit(true);
        } catch(IllegalAccessException|InstantiationException|InvocationTargetException|NoSuchMethodException e) {
            getMain().getHelper().getLogger().error("Unable to start bot, cannot find entry point.");
            e.printStackTrace();
            exit(true);
        }

        return null;
    }

    public static void exit(boolean err) {
        System.exit(err ? 1 : 0);
    }
}
