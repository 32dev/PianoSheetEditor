import javax.sound.midi.*;

public class MidiSender {
    private MidiDevice outputDevice;
    private Receiver receiver;

    public MidiSender(String targetDeviceNameContains) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            // LoopMIDI 포트 찾기
            if (info.getName().contains(targetDeviceNameContains) && device.getMaxReceivers() != 0) {
                outputDevice = device;
                outputDevice.open();
                receiver = outputDevice.getReceiver();
                System.out.println("Connected to: " + info.getName());
                break;
            }
        }
        if (receiver == null) {
            throw new MidiUnavailableException("MIDI output device not found");
        }
    }

    public void sendNoteOn(int channel, int note, int velocity) {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
            receiver.send(message, -1);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void sendNoteOff(int channel, int note) {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_OFF, channel, note, 0);
            receiver.send(message, -1);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (receiver != null) {
            receiver.close();
        }
        if (outputDevice != null && outputDevice.isOpen()) {
            outputDevice.close();
        }
    }
}
