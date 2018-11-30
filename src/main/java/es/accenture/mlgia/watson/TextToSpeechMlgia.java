package es.accenture.mlgia.watson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextToSpeechMlgia {

	@Value("${watson.text.to.speech.username}")
	@Getter
	private String usuario;

	@Value("${watson.text.to.speech.password}")
	@Getter
	private String password;

	@Value("${language.model}")
	private String languageModel;

	TextToSpeech speechService;

	public byte[] getAudioFile(String text) throws IOException {
		log.debug("Se obtiene audio para el texto:" + text);
		speechService = new TextToSpeech(usuario, password);
		InputStream in = null;
		byte[] salida = null;
		byte[] encoded = null;
		InputStream stream = null;
		try {

			stream = speechService.synthesize(text, Voice.ES_ENRIQUE, AudioFormat.WAV).execute();
			in = WaveUtils.reWriteWaveHeader(stream);
			salida = IOUtils.toByteArray(in);
			encoded = Base64.getEncoder().encode(salida);
		} catch (Exception e) {
			log.debug("Se ha producio un error al obtener el audio." + e.getMessage());
			e.printStackTrace();
		} finally {
			in.close();
			stream.close();
		}

		log.debug("Se retorna audio");
		return encoded;
	}

}
