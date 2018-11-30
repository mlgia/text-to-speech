package es.accenture.mlgia.audio;

import java.io.OutputStream;
import java.util.Locale;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import marytts.LocalMaryInterface;

public class Audio
{
	public static void getStream(OutputStream input,String text) throws Exception
	{
		LocalMaryInterface mary = null;
		mary = new LocalMaryInterface();

		AudioInputStream audio = null;

		mary.setLocale(Locale.US);
		//mary.setLocale(Locale.FRENCH);
		mary.setVoice("cmu-rms-hsmm");

		audio = mary.generateAudio(text);

		AudioSystem.write(audio, AudioFileFormat.Type.WAVE, input);

		//return audio;
	}
}
