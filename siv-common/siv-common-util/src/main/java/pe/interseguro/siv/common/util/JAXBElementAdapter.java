package pe.interseguro.siv.common.util;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Adaptador para JAXBElement to JSON (gson)
 * 
 * @author ti-is
 *
 */
public class JAXBElementAdapter extends TypeAdapter<JAXBElement> {

    @Override
    public void write(JsonWriter jsonWriter, JAXBElement jaxbElement) throws IOException {
        
    	if (jaxbElement == null) {
            jsonWriter.nullValue();
            return;
        }
    	
        JAXBElement e = jaxbElement;        
        jsonWriter.value(e.getValue() != null ? e.getValue().toString() : "");
        
    }

    @Override
    public JAXBElement read(JsonReader jsonReader) throws IOException {
        return null;
    }
    
}
