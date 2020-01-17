package tk.jasonho.reee;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class ReeeVoiceAudioManager {

    private Map<String, AudioPlayer> players = new HashMap<>();

    private static ReeeVoiceAudioManager instance;

    @Getter
    private AudioPlayerManager audioPlayerManager;

    private ReeeVoiceAudioManager() {
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

        instance = this;
    }

    public static ReeeVoiceAudioManager get() {
        return instance == null ? new ReeeVoiceAudioManager() : instance;
    }

    public AudioPlayer cleanResetAndGet(String id) {
        if(players.containsKey(id)) {
            AudioPlayer player = players.remove(id);
            if(player != null) {
                ReeeMain.getMain().getHelper().getLogger().info("Destroying player cleanly.");
                Guild guildById = ReeeMain.getMain().getBot().getGuildById(id);
                if(guildById != null) {
                    guildById.getAudioManager().closeAudioConnection();
                }
                player.stopTrack();
                player.destroy();
                ReeeMain.getMain().getHelper().getLogger().info("Got rid of player for " + id);
            }
        }
        players.remove(id);

        return getPlayer(id);
    }

    public AudioPlayer getPlayer(String id) {
        if (players.containsKey(id)) {
            return players.get(id);
        } else {
            AudioPlayer player = instance.getAudioPlayerManager().createPlayer();
            players.put(id, player);
            return player;
        }
    }
}
