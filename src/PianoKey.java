public class PianoKey {
	private String noteId;
	private double frequency;
	private int midiNumber;
	private int octave;
	@Override
	public String toString() {
	    return String.format("%s (MIDI: %d, %.2fHz)", noteId, midiNumber, frequency);
	}
	public PianoKey(String noteId, int midiNumber, double frequency) {
		this.noteId = noteId;
		this.midiNumber = midiNumber;
		this.frequency = frequency;

	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public int getMidiNumber() {
		return midiNumber;
	}

	public void setMidiNumber(int midiNumber) {
		this.midiNumber = midiNumber;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

}