package pe.interseguro.siv.common.dto.request;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SMEAdjunto4Mail {
	String rutaWord;
	Map<String, Object> trama;
}