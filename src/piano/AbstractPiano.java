package piano;

import java.awt.BorderLayout;
import java.awt.Font;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import midi.MidiUtil;

public abstract class AbstractPiano extends JFrame {
	protected Map<String, PianoKey> keys = null;
	protected JTextArea textArea = null;
	protected JScrollPane scrollPane = null;
	protected long startTime = -1;
	protected long lastKeyTime = -1;
	protected BufferedWriter logWriter = null;
	protected String baseDir;

	protected MidiDevice midiDevice = null;
	protected Receiver midiReceiver = null;
	protected final int MIDI_CHANNEL = 0;

	protected Set<String> pressedKeys = new HashSet<>();

	public AbstractPiano(Map<String, String> keyNoteMap, String title) {
		try {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
			String timestamp = now.format(formatter);

			baseDir = "log" + File.separator + timestamp;
			File dir = new File(baseDir);
			if (!dir.exists())
				dir.mkdirs();

			String logFileName = baseDir + File.separator + "piano_log_" + timestamp + ".txt";
			logWriter = new BufferedWriter(new FileWriter(logFileName, true));

			System.out.println("Logging to file: " + logFileName);

			setupMidiDevice("loopMIDI Port");
		} catch (IOException | MidiUnavailableException e) {
			e.printStackTrace();
		}

		textArea = new JTextArea();
//        textArea.setFont(new Font("Consolas", Font.PLAIN, 24));
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
//				System.exit(0);
			}
		});

		setTitle(title);
		setSize(300, 300);
		setLocationRelativeTo(null);
		add(scrollPane, BorderLayout.CENTER);

		addKeys(keyNoteMap);

		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String keyChar = String.valueOf(e.getKeyChar());

				if (pressedKeys.contains(keyChar))
					return; // 중복 입력 방지
				pressedKeys.add(keyChar);

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
					for (int i = 0; i < spacesToAdd; i++)
						sb.append(" ");
					textArea.append(sb.toString());

					sendNoteOn(key.getMidiNumber());

					lastKeyTime = currentTime;

					String logLine = "{key: \"" + keyChar + "\", midi : \"" + key.getMidiNumber() + "\" note: \""
							+ key.getNoteId() + "\", time :" + elapsed + "}, ";
					System.out.println(logLine);
					writeLog(logLine);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String keyChar = String.valueOf(e.getKeyChar());
				pressedKeys.remove(keyChar);

				PianoKey key = keys.get(keyChar);
				if (key != null) {
					sendNoteOff(key.getMidiNumber());
				}
			}
		});

		setVisible(true);
	}

	protected void setupMidiDevice(String portName) throws MidiUnavailableException {
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

	protected void sendNoteOn(int midiNote) {
		if (midiReceiver == null)
			return;
		try {
			ShortMessage onMessage = new ShortMessage();
			onMessage.setMessage(ShortMessage.NOTE_ON, MIDI_CHANNEL, midiNote, 100);
			midiReceiver.send(onMessage, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	protected void sendNoteOff(int midiNote) {
		if (midiReceiver == null)
			return;
		try {
			ShortMessage offMessage = new ShortMessage();
			offMessage.setMessage(ShortMessage.NOTE_OFF, MIDI_CHANNEL, midiNote, 0);
			midiReceiver.send(offMessage, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	protected void saveTextAreaContentToFile() {
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

	protected void writeLog(String logLine) {
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
		if (midiReceiver != null)
			midiReceiver.close();
		if (midiDevice != null && midiDevice.isOpen())
			midiDevice.close();
	}

	protected void addKey(String qwertyKey, PianoKey pianoKey) {
		keys.put(qwertyKey, pianoKey);
	}

	protected void addKeys(Map<String, String> keyNoteMap) {
		for (Map.Entry<String, String> entry : keyNoteMap.entrySet()) {
			String key = entry.getKey();
			String note = entry.getValue();
			int midi = MidiUtil.noteToMidi(note);
			double freq = MidiUtil.midiToFrequency(midi);
			addKey(key, new PianoKey(note, midi, freq));
		}
	}
}
