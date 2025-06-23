import java.util.HashMap;
import java.util.Map;

public class CMajorPiano extends AbstractPiano {
    public CMajorPiano() {
        super(getKeyNoteMap(), "C Major Piano");
        setTitle("C Major Piano");
    }

    private static Map<String, String> getKeyNoteMap() {
        Map<String, String> keyNoteMap = new HashMap<>();
        keyNoteMap.put("1", "C1");
        keyNoteMap.put("!", "C#1");
        keyNoteMap.put("2", "D1");
        keyNoteMap.put("@", "D#1");
        keyNoteMap.put("3", "E1");
        keyNoteMap.put("4", "F1");
        keyNoteMap.put("$", "F#1");
        keyNoteMap.put("5", "G1");
        keyNoteMap.put("%", "G#1");
        keyNoteMap.put("6", "A1");
        keyNoteMap.put("^", "A#1");
        keyNoteMap.put("7", "B1");
        keyNoteMap.put("8", "C2");
        keyNoteMap.put("*", "C#2");
        keyNoteMap.put("9", "D2");
        keyNoteMap.put("(", "D#2");
        keyNoteMap.put("0", "E2");
        keyNoteMap.put("q", "F2");
        keyNoteMap.put("Q", "F#2");
        keyNoteMap.put("w", "G2");
        keyNoteMap.put("W", "G#2");
        keyNoteMap.put("e", "A2");
        keyNoteMap.put("E", "A#2");
        keyNoteMap.put("r", "B2");
        keyNoteMap.put("t", "C3");
        keyNoteMap.put("T", "C#3");
        keyNoteMap.put("y", "D3");
        keyNoteMap.put("Y", "D#3");
        keyNoteMap.put("u", "E3");
        keyNoteMap.put("i", "F3");
        keyNoteMap.put("I", "F#3");
        keyNoteMap.put("o", "G3");
        keyNoteMap.put("O", "G#3");
        keyNoteMap.put("p", "A3");
        keyNoteMap.put("P", "A#3");
        keyNoteMap.put("a", "B3");
        keyNoteMap.put("s", "C4");
        keyNoteMap.put("S", "C#4");
        keyNoteMap.put("d", "D4");
        keyNoteMap.put("D", "D#4");
        keyNoteMap.put("f", "E4");
        keyNoteMap.put("g", "F4");
        keyNoteMap.put("G", "F#4");
        keyNoteMap.put("h", "G4");
        keyNoteMap.put("H", "G#4");
        keyNoteMap.put("j", "A4");
        keyNoteMap.put("J", "A#4");
        keyNoteMap.put("k", "B4");
        keyNoteMap.put("l", "C5");
        keyNoteMap.put("L", "C#5");
        keyNoteMap.put("z", "D5");
        keyNoteMap.put("Z", "D#5");
        keyNoteMap.put("x", "E5");
        keyNoteMap.put("c", "F5");
        keyNoteMap.put("C", "F#5");
        keyNoteMap.put("v", "G5");
        keyNoteMap.put("V", "G#5");
        keyNoteMap.put("b", "A5");
        keyNoteMap.put("B", "A#5");
        keyNoteMap.put("n", "B5");
        keyNoteMap.put("m", "C6");
        keyNoteMap.put("M", "C#6");
        return keyNoteMap;
    }

    public static void main(String[] args) {
        new CMajorPiano();
    }
}
