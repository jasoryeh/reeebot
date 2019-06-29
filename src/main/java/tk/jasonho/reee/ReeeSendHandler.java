package tk.jasonho.reee;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import lombok.Getter;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class ReeeSendHandler implements AudioSendHandler {

    @Getter
    private final AudioPlayer audioPlayer;

    /**
     * Also known as the AudioFrame from getAudioFrame()
     */
    @Getter
    private AudioFrame lastFrame;

    /**
     * @param audioPlayer Audio player to wrap.
     */
    public ReeeSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        provide();

        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        provide();

        byte[] data = lastFrame != null ? lastFrame.getData() : null;
        lastFrame = null;

        return ByteBuffer.wrap(data);
    }

    @Override
    public boolean isOpus() {
        return true;
    }

    private void provide() {
        lastFrame = lastFrame == null ? audioPlayer.provide() : lastFrame;
    }
}
