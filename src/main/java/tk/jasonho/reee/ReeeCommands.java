package tk.jasonho.reee;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class ReeeCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //ReeeMain.getMain().getHelper().getLogger().debug("Info - " + event);
        String content = event.getMessage().getContentRaw().toLowerCase();

        if (content.contains(ReeeMain.getMain().getBot().getSelfUser().getId()) && content.contains("get") && content.contains("in") && content.contains("here")) {
            //ReeeMain.getMain().getHelper().getLogger().debug("Info - wow");
            if(event.getChannelType() != ChannelType.PRIVATE) {
                //ReeeMain.getMain().getHelper().getLogger().debug("Info - private");
                Guild guild = event.getMember().getGuild();
                GuildVoiceState voiceState = event.getMember().getVoiceState();
                if (voiceState.inVoiceChannel()) {
                    try {
                        event.getTextChannel().sendMessage("Alright man.").queue();
                    } catch (Exception e) { // any perm exceptions
                        e.printStackTrace();
                    }

                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - in channel");
                    guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - connected");

                    AudioPlayer audioplayer = ReeeVoiceAudioManager.get().getAudioPlayerManager().createPlayer();
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - player spawned");

                    AudioEventListener trackScheduler = new AudioEventAdapter() {
                        @Override
                        public void onPlayerPause(AudioPlayer player) {
                            super.onPlayerPause(player);
                        }

                        @Override
                        public void onPlayerResume(AudioPlayer player) {
                            super.onPlayerResume(player);
                        }

                        @Override
                        public void onTrackStart(AudioPlayer player, AudioTrack track) {
                            super.onTrackStart(player, track);
                        }

                        @Override
                        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                            super.onTrackEnd(player, track, endReason);

                            guild.getAudioManager().closeAudioConnection();
                        }

                        @Override
                        public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
                            exception.printStackTrace();
                        }

                        @Override
                        public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
                            super.onTrackStuck(player, track, thresholdMs);
                        }

                        @Override
                        public void onEvent(AudioEvent event) {
                            super.onEvent(event);
                        }

                        @Override
                        public int hashCode() {
                            return super.hashCode();
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return super.equals(obj);
                        }

                        @Override
                        protected Object clone() throws CloneNotSupportedException {
                            return super.clone();
                        }

                        @Override
                        public String toString() {
                            return super.toString();
                        }

                        @Override
                        protected void finalize() throws Throwable {
                            super.finalize();
                        }
                    };
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - spawn ts");
                    guild.getAudioManager().setSendingHandler(new ReeeSendHandler(audioplayer));
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - set");
                    audioplayer.addListener(trackScheduler);
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - listener set");

                    ReeeVoiceAudioManager.get().getAudioPlayerManager()
                            .loadItem("https://www.youtube.com/playlist?list=PLya__OBTLMkPQwGj-_xlnVb2-F_w_HN5i",
                                    new AudioLoadResultHandler() {
                                        @Override
                                        public void trackLoaded(AudioTrack track) {
                                            //ReeeMain.getMain().getHelper().getLogger().debug("Info - load - " + track);
                                            audioplayer.playTrack(track);
                                        }

                                        @Override
                                        public void playlistLoaded(AudioPlaylist playlist) {
                                            AudioTrack audioTrack = playlist.getTracks().get(new Random().nextInt(playlist.getTracks().size()));
                                            //ReeeMain.getMain().getHelper().getLogger().debug("Info - loads - " + audioTrack);
                                            audioplayer.playTrack(audioTrack);
                                        }

                                        @Override
                                        public void noMatches() {
                                            // err
                                        }

                                        @Override
                                        public void loadFailed(FriendlyException exception) {
                                            exception.printStackTrace();
                                        }
                                    });
                    //ReeeMain.getMain().getHelper().getLogger().debug("Info - made load");
                }
            }
        }
    }
}
