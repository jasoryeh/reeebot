package tk.jasonho.reee;

import net.dv8tion.jda.api.entities.Message;
import tk.jasonho.shortcuts.everything.log.LoggerManager;

import java.util.function.Consumer;

public class ReeeUtilities {

    public static Consumer<Message> autoDelete() {
        return new Consumer<Message>() {
            @Override
            public void accept(Message message) {
                try {
                    LoggerManager logger = ReeeMain.getMain().getHelper().getLogger();
                    logger.info(message.getId() + " queued to be deleted.");
                    Thread.sleep(10000);
                    logger.info(message.getId() + " deleted.");
                    message.delete().queue();
                } catch(InterruptedException e) {
                    // don't delete message, but report error
                    e.printStackTrace();
                }
            }
        };
    }

}
