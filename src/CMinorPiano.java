import java.util.HashMap;
import java.util.Map;

public class CMinorPiano extends AbstractPiano {
    public CMinorPiano() {
        super(getKeyNoteMap(), "C Minor Piano");
        setTitle("C Minor Piano");
    }

    private static Map<String, String> getKeyNoteMap() {
        Map<String, String> keyNoteMap = new HashMap<>();
        keyNoteMap.put("1", "C1");
        keyNoteMap.put("!", "C#1");
        keyNoteMap.put("2", "D1");
        keyNoteMap.put("@", "D#1");
        keyNoteMap.put("3", "D#1"); // Eb
        keyNoteMap.put("4", "F1");
        keyNoteMap.put("$", "F#1");
        keyNoteMap.put("5", "G1");
        keyNoteMap.put("%", "G#1");
        keyNoteMap.put("6", "G#1"); // Ab
        keyNoteMap.put("^", "A1");
        keyNoteMap.put("7", "A#1"); // Bb
        keyNoteMap.put("8", "C2");
        keyNoteMap.put("*", "C#2");
        keyNoteMap.put("9", "D2");
        keyNoteMap.put("(", "D#2");
        keyNoteMap.put("0", "D#2"); // Eb2
        keyNoteMap.put("q", "F2");
        keyNoteMap.put("Q", "F#2");
        keyNoteMap.put("w", "G2");
        keyNoteMap.put("W", "G#2");
        keyNoteMap.put("e", "G#2"); // Ab2
        keyNoteMap.put("E", "A2");
        keyNoteMap.put("r", "A#2"); // Bb2
        keyNoteMap.put("t", "C3");
        keyNoteMap.put("T", "C#3");
        keyNoteMap.put("y", "D3");
        keyNoteMap.put("Y", "D#3");
        keyNoteMap.put("u", "D#3"); // Eb3
        keyNoteMap.put("i", "F3");
        keyNoteMap.put("I", "F#3");
        keyNoteMap.put("o", "G3");
        keyNoteMap.put("O", "G#3");
        keyNoteMap.put("p", "G#3"); // Ab3
        keyNoteMap.put("P", "A3");
        keyNoteMap.put("a", "A#3"); // Bb3
        keyNoteMap.put("s", "C4");
        keyNoteMap.put("S", "C#4");
        keyNoteMap.put("d", "D4");
        keyNoteMap.put("D", "D#4");
        keyNoteMap.put("f", "D#4"); // Eb4
        keyNoteMap.put("g", "F4");
        keyNoteMap.put("G", "F#4");
        keyNoteMap.put("h", "G4");
        keyNoteMap.put("H", "G#4");
        keyNoteMap.put("j", "G#4"); // Ab4
        keyNoteMap.put("J", "A4");
        keyNoteMap.put("k", "A#4"); // Bb4
        keyNoteMap.put("l", "C5");
        keyNoteMap.put("L", "C#5");
        keyNoteMap.put("z", "D5");
        keyNoteMap.put("Z", "D#5");
        keyNoteMap.put("x", "D#5"); // Eb5
        keyNoteMap.put("c", "F5");
        keyNoteMap.put("C", "F#5");
        keyNoteMap.put("v", "G5");
        keyNoteMap.put("V", "G#5");
        keyNoteMap.put("b", "G#5"); // Ab5
        keyNoteMap.put("B", "A5");
        keyNoteMap.put("n", "A#5"); // Bb5
        keyNoteMap.put("m", "C6");
        keyNoteMap.put("M", "C#6");
        return keyNoteMap;
    }

    public static void main(String[] args) {
        new CMinorPiano();
    }
}