package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PianoMajorKeyMapGenerator {

	// 12음계
	private static final String[] CHROMATIC_SCALE = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

	// 키 입력 순서 (당신이 준 매핑 순서 기준)
	private static final String[] KEYBOARD_KEYS = { "1", "!", "2", "@", "3", "4", "$", "5", "%", "6", "^", "7", "8",
			"*", "9", "(", "0", "q", "Q", "w", "W", "e", "E", "r", "t", "T", "y", "Y", "u", "i", "I", "o", "O", "p",
			"P", "a", "s", "S", "d", "D", "f", "g", "G", "h", "H", "j", "J", "k", "l", "L", "z", "Z", "x", "c", "C",
			"v", "V", "b", "B", "n", "m", "M", };

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

		for (int j = 0; j <= 1; j++) {
			for (int i = 0; i < CHROMATIC_SCALE.length; i++) {
				String rootNote = CHROMATIC_SCALE[i] + j;
				Map<String, String> map = generateKeyToNoteMap(rootNote);
				File file = new File(folder, rootNote + "_major.json");

				try (FileWriter writer = new FileWriter(file)) {
					gson.toJson(map, writer);
					System.out.println("✔ Saved: " + file.getPath());
				} catch (IOException e) {
					System.err.println("❌ Failed to write file: " + file.getPath());
					e.printStackTrace();
				}
			}
		}

	}
}
