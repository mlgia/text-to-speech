package es.accenture.mlgia.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.accenture.mlgia.audio.Audio;
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

    @Autowired
    TextToSpeechMlgia textToSpeech;

    @RequestMapping(value = "/audiojava", method = RequestMethod.POST)
	public void getAudioJava(HttpServletResponse response,
			@RequestBody InputTextToSpeechDTO text) throws Exception
	{
		response.setHeader("Content-Disposition","attachment;filename=audio.wav");
		response.setContentType("audio/wave");

		Audio.getStream(response.getOutputStream(),text.getText());
	}


    @RequestMapping(value = "/audio", method = RequestMethod.POST)
   	public TextToSpeechDTO  getAudioWatson(HttpServletResponse response,
   			@RequestBody InputTextToSpeechDTO text) throws Exception
	{
    	log.debug("Llamada a Audio con texto:" + text);
    	if (text != null)
    	{
    		response.setHeader("Content-Disposition","attachment;filename=audio.wav");
    		response.setContentType("audio/wave");

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
