package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PianoMinorKeyMapGenerator_MidiNumKey {

	// 12음계
	private static final String[] CHROMATIC_SCALE = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	// 키 입력 순서 (당신이 준 매핑 순서 기준)
	private static final String[] KEYBOARD_KEYS = { "1", "!", "2", "3", "#", "4", "$", "5", "6", "^", "7", "&", "8",
			"*", "9", "0", ")", "q", "Q", "w", "e", "E", "r", "R", "t", "T", "y", "u", "U", "i", "I", "o", "p", "P",
			"a", "A", "s", "S", "d", "f", "F", "g", "G", "h", "j", "J", "k", "K", "l", "L", "z", "x", "X", "c", "C",
			"v", "b", "B", "n", "N", "m", "M", };

	/**
	 * 주어진 시작 음(rootNote, 예: "D1")으로부터 전체 키 입력에 대한 음계 매핑을 생성합니다.
	 * 
	 * @param rootNote 예: "C1", "D1", "F#2" 등
	 * @return Map<String, String> 형식의 키-음 매핑
	 */
	public static Map<String, String> generateKeyToNoteMap(String rootNote) {
		Map<String, String> keyToNoteMap = new LinkedHashMap<>();

		// 시작음 분석 (예: "D1" -> "D", 옥타브 1)
		String pitch = rootNote.replaceAll("[0-9]", ""); // D
		int octave = Integer.parseInt(rootNote.replaceAll("[^0-9]", "")); // 1

		// 시작 인덱스 찾기
		int startIndex = Arrays.asList(CHROMATIC_SCALE).indexOf(pitch);
		if (startIndex == -1) {
			throw new IllegalArgumentException("Invalid root note: " + rootNote);
		}

		int noteIndex = startIndex;
		int currentOctave = octave;

		for (String key : KEYBOARD_KEYS) {
			String noteName = CHROMATIC_SCALE[noteIndex] + currentOctave;
			keyToNoteMap.put(key, noteName);

			// 다음으로 이동
			noteIndex++;
			if (noteIndex >= CHROMATIC_SCALE.length) {
				noteIndex = 0;
				currentOctave++;
			}
		}

		return keyToNoteMap;
	}

	// 테스트
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File folder = new File("basic_keymaps");
		if (!folder.exists()) {
			folder.mkdirs(); // 폴더 없으면 생성
		}

		for (int j = 0; j <= 3; j++) {
			for (int i = 0; i < CHROMATIC_SCALE.length; i++) {
				String rootNote = CHROMATIC_SCALE[i] + j;
				Map<String, String> map = generateKeyToNoteMap(rootNote);
				File file = new File(folder, rootNote + "_minor.json");

				try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {

					gson.toJson(map, writer); // map은 JSON으로 저장할 내용

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
