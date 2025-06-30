package piano;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Piano extends AbstractPiano {

	public Piano(String keyMap) {
		super(loadKeyNoteMap(keyMap), keyMap);
	}

	private static Map<String, String> loadKeyNoteMap(String keyMap) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// JSON 파일 경로를 실제 경로에 맞게 바꾸세요
			return mapper.readValue(new File("basic_keymaps/" + keyMap + ".json"), new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			e.printStackTrace();
			// 실패 시 빈 Map 반환 (필요시 수정)
			return Map.of();
		}
	}
}