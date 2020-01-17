package tk.jasonho.reee;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;
import java.util.regex.Pattern;

public class ReeePlayer {

    public static void handle(MessageReceivedEvent event) {
        Guild guild = event.getMember().getGuild();
        GuildVoiceState voiceState = event.getMember().getVoiceState();
        if (voiceState != null && voiceState.inVoiceChannel()) {
            AudioPlayer audioplayer = ReeeVoiceAudioManager.get().cleanResetAndGet(guild.getId());

            String request = event.getMessage().getContentRaw()
                    .replaceFirst(Pattern.quote("<@!" + ReeeMain.getMain().getBot().getSelfUser().getId() + "> play "), "");
            ReeeMain.getMain().getHelper().getLogger().info("Request: \"" + request + "\"");
            try {
                event.getTextChannel().sendMessage("Popping in to play your beats...").queue(ReeeUtilities.autoDelete());
            } catch (Exception e) { /* any perm exceptions */ e.printStackTrace(); }


            guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
            AudioEventListener trackScheduler = new AudioEventAdapter() {
                @Override
                public void onTrackStart(AudioPlayer player, AudioTrack track) {
                    if(!guild.getAudioManager().isConnected()) {
                        guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                    }
                    AudioTrackInfo info = track.getInfo();
                    event.getTextChannel().sendMessage("Playing: `" + info.title + "` by " + info.author + " at " + info.uri).queue();
                }

                @Override
                public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                    guild.getAudioManager().closeAudioConnection();
                    AudioTrackInfo info = track.getInfo();
                    event.getTextChannel().sendMessage("Finished playing `" + info.title + "`. See ya!").queue();
                }
            };

            guild.getAudioManager().setSendingHandler(new ReeeSendHandler(audioplayer));
            audioplayer.addListener(trackScheduler);

            ReeeMain.getMain().getHelper().getLogger().info("Request: \"" + request + "\"");
            ReeeVoiceAudioManager.get().getAudioPlayerManager()
                    .loadItem(request,
                            new AudioLoadResultHandler() {
                                @Override
                                public void trackLoaded(AudioTrack track) {
                                    audioplayer.playTrack(track);
                                }

                                @Override
                                public void playlistLoaded(AudioPlaylist playlist) {
                                    event.getTextChannel().sendMessage("Playlists are not currently supported. Playing one random song instead.");
                                    AudioTrack audioTrack = playlist.getTracks().get(new Random().nextInt(playlist.getTracks().size()));
                                    audioplayer.playTrack(audioTrack);
                                }

                                @Override public void noMatches() { /* err */ event.getTextChannel().sendMessage("Something went wrong... I couldn't find anything for you.").queue(); }
                                @Override public void loadFailed(FriendlyException exception) { event.getTextChannel().sendMessage("Something went wrong... check back later.").queue(); exception.printStackTrace(); }
                            });
        } else {
            event.getTextChannel().sendMessage("Yo you're not in a voice channel!").queue(ReeeUtilities.autoDelete());
        }
    }

}
