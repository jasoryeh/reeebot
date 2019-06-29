package tk.jasonho.reee;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReeeBot extends ListenerAdapter {
    @Getter
    private final JDA bot;
    @Getter
    private final ReeeMain main;

    public ReeeBot(JDA bot, ReeeMain main) {
        this.bot = bot;
        this.main = main;
    }

    @Override
    public void onReady(ReadyEvent re) {
        ReeeMain.getMain().getHelper().getLogger().debug("Bot connected - " + bot.getSelfUser().getName()
                + " | Connected to - " + bot.getGuilds().size());
        //ReeeMain.getMain().getHelper().getLogger().debug("Info - ready");
        bot.addEventListener(new ReeeCommands());
    }

}
