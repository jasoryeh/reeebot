package tk.jasonho.reee;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tk.jasonho.shortcuts.everything.log.LoggerManager;

public class ReeeCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LoggerManager logger = ReeeMain.getMain().getHelper().getLogger();

        Message message = event.getMessage();
        logger.debug("Info - " + event.getGuild().getName() + " : " + event.getAuthor().getAsMention() + " - " + message.getContentRaw());
        String content = message.getContentRaw().toLowerCase();

        SelfUser selfUser = ReeeMain.getMain().getBot().getSelfUser();
        if (content.contains(selfUser.getId())) {
            // memes
            if((content.contains("get") && content.contains("in")) || content.contains("come")) {
                ReeeMemes.handle(event);
            } else if (content.contains("play") && content.split(" ").length >= 3) {
                ReeePlayer.handle(event);
            }
        }
    }
}
