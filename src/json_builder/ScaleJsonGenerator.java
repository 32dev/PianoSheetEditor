package json_builder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

/**
 * Generates keyboard‑note mapping JSON files for all 12 major and 12 natural minor keys
 * based on existing C_major.json and C_minor.json templates.
 * <p>
 * Requirements:
 *  - Jackson databind library on the classpath
 *    (e.g. compile with:  javac -cp jackson-databind-2.17.0.jar ScaleJsonGenerator.java)
 *    and run with:       java  -cp .:jackson-databind-2.17.0.jar ScaleJsonGenerator
 *  - Place C_major.json and C_minor.json (exact format as the samples you provided)
 *    in the same working directory as this program.
 * <p>
 * The program will create files such as "C#_major.json", "G_minor.json", etc.
 */
public class ScaleJsonGenerator {

    private static final List<String> NOTE_NAMES = Arrays.asList(
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Load the reference templates (natural C major & C minor)
        Map<String, String> cMajor = mapper.readValue(
                new File("basic_keymaps/C_major.json"), new TypeReference<Map<String, String>>() {});
        Map<String, String> cMinor = mapper.readValue(
                new File("basic_keymaps/C_minor.json"), new TypeReference<Map<String, String>>() {});

        // Generate all 12 major and 12 minor key mappings
        for (int semitoneShift = 0; semitoneShift < 12; semitoneShift++) {
            String root = NOTE_NAMES.get(semitoneShift);
            writeScale(mapper, cMajor, semitoneShift, root + "_major.json");
            writeScale(mapper, cMinor, semitoneShift, root + "_minor.json");
        }

        System.out.println("✅ Finished generating 24 JSON files.");
    }

    /**
     * Transposes every note in the given template map by {@code shift} semitones
     * and writes the resulting map to {@code fileName} using pretty‑printed JSON.
     */
    private static void writeScale(ObjectMapper mapper, Map<String, String> template,
                                   int shift, String fileName) throws Exception {
        Map<String, String> transposed = new LinkedHashMap<>(); // preserve key order
        for (Map.Entry<String, String> entry : template.entrySet()) {
            transposed.put(entry.getKey(), transposeNote(entry.getValue(), shift));
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("keymaps/"+fileName), transposed);
    }

    /**
     * Transposes a single note label (e.g., "C#4") by {@code shift} semitones.
     * Supports octaves 0‑9. Returns the new note label (e.g., "D4").
     */
    private static String transposeNote(String note, int shift) {
        // Split pitch class and octave (last character is the octave digit)
        int octave = Character.getNumericValue(note.charAt(note.length() - 1));
        String pitch = note.substring(0, note.length() - 1); // e.g. "C#" or "A"

        int pitchClass = NOTE_NAMES.indexOf(pitch);
        if (pitchClass == -1) {
            throw new IllegalArgumentException("Unknown pitch: " + pitch);
        }

        int midiNumber = (octave + 1) * 12 + pitchClass + shift;
        int newOctave = midiNumber / 12 - 1;
        int newPitchClass = midiNumber % 12;

        return NOTE_NAMES.get(newPitchClass) + newOctave;
    }
}
