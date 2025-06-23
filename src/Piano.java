import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Piano extends JFrame {
    private Map<String, PianoKey> keys = null;
    private JTextArea textArea = null;
    private JScrollPane scrollPane = null;
    private long startTime = -1;
    private long lastKeyTime = -1;
    private BufferedWriter logWriter = null;
    private String baseDir;

    // MIDI 관련 변수
    private MidiDevice midiDevice = null;
    private Receiver midiReceiver = null;
    private final int MIDI_CHANNEL = 0; // 0부터 시작, 채널 1

    public Piano() {
        try {
            // 현재 시각 문자열 생성
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = now.format(formatter);

            // 폴더 생성
            baseDir = timestamp;
            File dir = new File(baseDir);
            if (!dir.exists())
                dir.mkdirs();

            // 로그 파일 열기
            String logFileName = baseDir + File.separator + "piano_log_" + timestamp + ".txt";
            logWriter = new BufferedWriter(new FileWriter(logFileName, true));

            System.out.println("Logging to file: " + logFileName);

            // MIDI 장치 초기화 (LoopMIDI 포트 이름 입력)
            setupMidiDevice("loopMIDI Port"); 

        } catch (IOException | MidiUnavailableException e) {
            e.printStackTrace();
        }

        // UI 구성
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());
        keys = new HashMap<>();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveTextAreaContentToFile();
                dispose();
                System.exit(0);
            }
        });

        setTitle("Qwerty Piano");
        setSize(200, 300);
        setLocationRelativeTo(null);
        add(scrollPane, BorderLayout.CENTER);

        // 키맵 초기화
        addKeys();

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String keyChar = String.valueOf(e.getKeyChar());
                PianoKey key = keys.get(keyChar);
                if (key != null) {
                    long currentTime = System.currentTimeMillis();

                    if (startTime < 0) {
                        startTime = currentTime;
                        lastKeyTime = currentTime;
                    }
                    long elapsed = currentTime - startTime;
                    long delta = currentTime - lastKeyTime;

                    int spacesToAdd = (int) (delta / 125);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < spacesToAdd; i++) {
                        sb.append(" ");
                    }
                    textArea.append(sb.toString());

                    // MIDI Note On 전송
                    sendNoteOn(key.getMidiNumber());

                    lastKeyTime = currentTime;

                    String logLine = "{key: \"" + keyChar + "\", note: \"" + key.getNoteId() + "\", time :" + elapsed + "}, ";
                    System.out.println(logLine);
                    writeLog(logLine);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String keyChar = String.valueOf(e.getKeyChar());
                PianoKey key = keys.get(keyChar);
                if (key != null) {
                    // MIDI Note Off 전송
                    sendNoteOff(key.getMidiNumber());
                }
            }
        });

        setVisible(true);
    }

    // LoopMIDI 포트 연결
    private void setupMidiDevice(String portName) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            if (info.getName().equals(portName)) {
                midiDevice = MidiSystem.getMidiDevice(info);
                break;
            }
        }

        if (midiDevice == null) {
            System.err.println("LoopMIDI port '" + portName + "' not found.");
            return;
        }

        midiDevice.open();
        midiReceiver = midiDevice.getReceiver();
        System.out.println("Connected to MIDI device: " + portName);
    }

    private void sendNoteOn(int midiNote) {
        if (midiReceiver == null) return;
        try {
            ShortMessage onMessage = new ShortMessage();
            onMessage.setMessage(ShortMessage.NOTE_ON, MIDI_CHANNEL, midiNote, 100);
            midiReceiver.send(onMessage, -1);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void sendNoteOff(int midiNote) {
        if (midiReceiver == null) return;
        try {
            ShortMessage offMessage = new ShortMessage();
            offMessage.setMessage(ShortMessage.NOTE_OFF, MIDI_CHANNEL, midiNote, 0);
            midiReceiver.send(offMessage, -1);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void saveTextAreaContentToFile() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = now.format(formatter);

            String filename = baseDir + File.separator + "sheet_log_" + timestamp + ".txt";

            String content = textArea.getText();
            StringBuilder wrapped = new StringBuilder();
            int count = 0;
            for (int i = 0; i < content.length(); i++) {
                wrapped.append(content.charAt(i));
                count++;
                if (count >= 40) {
                    wrapped.append('\n');
                    count = 0;
                }
            }

            Files.write(Paths.get(filename), wrapped.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println("TextArea 내용이 파일로 저장됨: " + filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeLog(String logLine) {
        if (logWriter != null) {
            try {
                logWriter.write(logLine);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (logWriter != null) {
            try {
                logWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (midiReceiver != null) {
            midiReceiver.close();
        }
        if (midiDevice != null && midiDevice.isOpen()) {
            midiDevice.close();
        }
    }

    public void addKey(String qwertyKey, PianoKey pianoKey) {
        this.keys.put(qwertyKey, pianoKey);
    }

    public void addKeys() {
        Map<String, String> keyNoteMap = new HashMap<String, String>() {
            {
                put("1", "C2");
                put("!", "C#2");
                put("2", "D2");
                put("@", "D#2");
                put("3", "E2");
                put("4", "F2");
                put("$", "F#2");
                put("5", "G2");
                put("%", "G#2");
                put("6", "A2");
                put("^", "A#2");
                put("7", "B2");
                put("8", "C3");
                put("*", "C#3");
                put("9", "D3");
                put("(", "D#3");
                put("0", "E3");
                put("q", "F3");
                put("Q", "F#3");
                put("w", "G3");
                put("W", "G#3");
                put("e", "A3");
                put("E", "A#3");
                put("r", "B3");
                put("t", "C4");
                put("T", "C#4");
                put("y", "D4");
                put("Y", "D#4");
                put("u", "E4");
                put("i", "F4");
                put("I", "F#4");
                put("o", "G4");
                put("O", "G#4");
                put("p", "A4");
                put("P", "A#4");
                put("a", "B4");
                put("s", "C5");
                put("S", "C#5");
                put("d", "D5");
                put("D", "D#5");
                put("f", "E5");
                put("g", "F5");
                put("G", "F#5");
                put("h", "G5");
                put("H", "G#5");
                put("j", "A5");
                put("J", "A#5");
                put("k", "B5");
                put("l", "C6");
                put("L", "C#6");
                put("z", "D6");
                put("Z", "D#6");
                put("x", "E6");
                put("c", "F6");
                put("C", "F#6");
                put("v", "G6");
                put("V", "G#6");
                put("b", "A6");
                put("B", "A#6");
                put("n", "B6");
                put("m", "C7");
                put("M", "C#7");
            }
        };
        for (Map.Entry<String, String> entry : keyNoteMap.entrySet()) {
            String key = entry.getKey();
            String note = entry.getValue();
            int midi = MidiUtil.noteToMidi(note);
            double freq = MidiUtil.midiToFrequency(midi);
            addKey(key, new PianoKey(note, midi, freq));
        }
    }

    public void printKeys() {
        for (Map.Entry<String, PianoKey> entry : keys.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}
