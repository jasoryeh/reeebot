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
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class ReeeMemes {

    public static void handle(MessageReceivedEvent event) {
        if(event.getChannelType() != ChannelType.PRIVATE) {
            Guild guild = event.getMember().getGuild();
            GuildVoiceState voiceState = event.getMember().getVoiceState();
            if (voiceState != null && voiceState.inVoiceChannel()) {
                AudioPlayer audioplayer = ReeeVoiceAudioManager.get().cleanResetAndGet(guild.getId());

                try {
                    event.getTextChannel().sendMessage("Alright man.").queue(ReeeUtilities.autoDelete());
                } catch (Exception e) { /* any perm exceptions */ e.printStackTrace(); }

                guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());

                AudioEventListener trackScheduler = new AudioEventAdapter() {
                    @Override
                    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                        guild.getAudioManager().closeAudioConnection();
                        AudioTrackInfo info = track.getInfo();
                        event.getTextChannel().sendMessage("That snippet was: `"
                                + info.title + "` by " + info.author + " at " + info.uri).queue();
                    }
                };

                guild.getAudioManager().setSendingHandler(new ReeeSendHandler(audioplayer));
                audioplayer.addListener(trackScheduler);

                ReeeVoiceAudioManager.get().getAudioPlayerManager()
                        .loadItem("https://www.youtube.com/playlist?list=PLya__OBTLMkPQwGj-_xlnVb2-F_w_HN5i",
                                new AudioLoadResultHandler() {
                                    @Override
                                    public void trackLoaded(AudioTrack track) {
                                        audioplayer.playTrack(track);
                                    }

                                    @Override
                                    public void playlistLoaded(AudioPlaylist playlist) {
                                        AudioTrack audioTrack = playlist.getTracks().get(new Random().nextInt(playlist.getTracks().size()));
                                        audioplayer.playTrack(audioTrack);
                                    }

                                    @Override public void noMatches() {/* err */event.getTextChannel().sendMessage("Something went wrong... I couldn't find anything for you.").queue(); }
                                    @Override public void loadFailed(FriendlyException exception) { event.getTextChannel().sendMessage("Something went wrong... check back later.").queue();exception.printStackTrace(); }
                                });
            } else {
                event.getTextChannel().sendMessage("Yo you're not in a voice channel!").queue(ReeeUtilities.autoDelete());
            }
        }
    }
}
