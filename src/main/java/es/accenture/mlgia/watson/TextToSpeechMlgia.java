package es.accenture.mlgia.watson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions.Voice;
import com.ibm.watson.text_to_speech.v1.model.Voices;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextToSpeechMlgia {
	
	@Value("${watson.apikey}")
	@Getter
	private String apikey;
	
	@Value("${watson.endpoint}")
	@Getter
	private String endpoint;
	
	@Value("${watson.voice}")
	private String voice;
	
	public byte[] getAudioFile(String text) throws IOException {
		log.debug("getAudioFile() init");
		
		IamOptions options = new IamOptions.Builder()
			    .apiKey(apikey)
			    .build();
		
		TextToSpeech service = new TextToSpeech(options);
		service.setEndPoint(endpoint);
		
		if (text.contains(":")) {
			text = text.substring(0, text.indexOf(":"));
		}
		
		SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
				  .text(text)
				  .voice(Voice.ES_ES_LAURAVOICE)
				  .accept(SynthesizeOptions.Accept.AUDIO_MP3)
				  .build();
		
		InputStream stream = service.synthesize(synthesizeOptions).execute().getResult();
		InputStream in = WaveUtils.reWriteWaveHeader(stream);
		stream.close();
		  
		return IOUtils.toByteArray(in);
	}
	
	private void saveAudio(InputStream in, String filename) {
		OutputStream out;
		try {
			out = new FileOutputStream(filename);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getVoices() throws IOException {
		
		IamOptions options = new IamOptions.Builder()
			    .apiKey(apikey)
			    .build();
		TextToSpeech service = new TextToSpeech(options);
		service.setEndPoint(endpoint);
		
		Voices voices = service.listVoices().execute().getResult();
		//System.out.println(voices);
		return voices.toString();
	}
	
	public void getAudioFileAndSaveInLocal(String text) throws IOException {
		log.debug("getAudioFileAndSaveInLocal() init");
		
		IamOptions options = new IamOptions.Builder()
			    .apiKey(apikey)
			    .build();
		
		TextToSpeech service = new TextToSpeech(options);
		service.setEndPoint(endpoint);
		
		if (text.contains(":")) {
			text = text.substring(0, text.indexOf(":"));
		}
		
		SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
				  .text(text)
				  .voice(Voice.ES_ES_LAURAVOICE)
				  .accept(SynthesizeOptions.Accept.AUDIO_MP3)
				  .build();
		
		InputStream stream = service.synthesize(synthesizeOptions).execute().getResult();
		InputStream in = WaveUtils.reWriteWaveHeader(stream);
		stream.close();
		
		saveAudio(in, "test_text_to_speech.mp3");
		in.close();
	}

}
