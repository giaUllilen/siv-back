package pe.interseguro.siv.common.dto.response;

import lombok.Data;

import java.io.ByteArrayInputStream;

@Data
public class AcpByteArrayInput {
	private String numeroDocumento;
	private ByteArrayInputStream acpFile;
}
