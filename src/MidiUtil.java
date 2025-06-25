import java.util.HashMap;
import java.util.Map;

public class MidiUtil {
	private static final Map<String, Integer> NOTE_TO_MIDI = new HashMap<>();

	static {
		// 기본적인 음 이름 - 미디 번호 매핑 (C4 = 60)
		String[] notes = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
		for (int octave = 0; octave <= 9; octave++) {
			for (int i = 0; i < notes.length; i++) {
				String noteName = notes[i] + octave;
				int midiNumber = octave * 12 + i + 24; // C0 = 24로 보정
				NOTE_TO_MIDI.put(noteName, midiNumber);
			}
		}
	}

	public static int noteToMidi(String note) {
		Integer midi = NOTE_TO_MIDI.get(note);
		if (midi == null) {
			System.err.println("Unknown note: " + note);
			return 60; // 기본값 C4
		}
		return midi;
	}

	// MIDI 음표 번호 -> 음표 문자열 (예: 60 -> C4)
	public static String midiToNote(int midiNumber) {
		String[] notes = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
		int octave = (midiNumber / 12) - 1;
		int noteIndex = midiNumber % 12;
		return notes[noteIndex] + octave;
	}
	// ... 기존 noteToMidi(), midiToFrequency() 메서드 유지

	public static double midiToFrequency(int midiNumber) {
		return 440.0 * Math.pow(2, (midiNumber - 69) / 12.0);
	}
}
