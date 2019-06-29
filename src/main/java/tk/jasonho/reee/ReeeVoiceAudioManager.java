package tk.jasonho.reee;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lombok.Getter;

public class ReeeVoiceAudioManager {
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
}
