package es.accenture.mlgia.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.accenture.mlgia.dto.InputTextToSpeechDTO;
import es.accenture.mlgia.dto.TextToSpeechDTO;
import es.accenture.mlgia.watson.TextToSpeechMlgia;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TextToSpeechController    {

	@Value("${mensaje.informacion}")
	@Getter private String sMensajeInfo;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info() {
    	log.debug("Inicio info()");
    	return sMensajeInfo;
    }
    
    @RequestMapping(value = "/voices", method = RequestMethod.GET)
    public String getVoices() {
    	log.debug("get Voices from API");
    	String voices;
		try {
			voices = textToSpeech.getVoices();
			return voices;
		} catch (IOException e) {
			log.error("Error recuperando las voces");
		}
		return null;
    }
    
    @RequestMapping(value = "/audio2", method = RequestMethod.GET)
    public String getAudio() {
    	try {
			textToSpeech.getAudioFile("Hola, esto es una prueba");
		} catch (IOException e) {
			log.error("Error obteniendo el audio");
		}
    	return "Peticion hecha. Comprueba el audio guardado";
    }
    
    @RequestMapping(value = "/audio3/{text}", method = RequestMethod.GET)
    public String getAudio(@PathVariable("text") String text) {
    	try {
			textToSpeech.getAudioFileAndSaveInLocal(text);
		} catch (IOException e) {
			log.error("Error obteniendo el audio");
		}
    	return "Peticion hecha. Comprueba el audio guardado";
    }

    @Autowired
    TextToSpeechMlgia textToSpeech;

    @RequestMapping(value = "/audio", method = RequestMethod.POST)
   	public TextToSpeechDTO  getAudioWatson(HttpServletResponse response,
   			@RequestBody InputTextToSpeechDTO text) throws Exception {
    	log.debug("Llamada a Audio con texto:" + text);
    	if (text != null) {
 
    		byte[] fileOutput = textToSpeech.getAudioFile(text.getText());

    		TextToSpeechDTO textToSpeech = TextToSpeechDTO.builder()
												.message(fileOutput)
												.build();
    		return textToSpeech;
    	} else {
    		log.error("El valor de entrada es vacío");
    		return null;
    	}
	}

}
