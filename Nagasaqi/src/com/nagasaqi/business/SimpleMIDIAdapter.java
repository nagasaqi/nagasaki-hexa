package com.nagasaqi.business;

import java.security.AllPermission;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

@SuppressWarnings("unused")
public class SimpleMIDIAdapter {

	private static SimpleMIDIAdapter smadp = null;
	private static Synthesizer synth;
	private static int instrument = 0;
	static int channel = 0;
	static int volume = 80;
	static int duration = 200; // in milliseconds
	static int deflt_bend = 8193;
	static int BEND_UNIT = 4096; // equal to one semitone
	int flute = 79;// test instrument for program change a.k.a instrument

	@SuppressWarnings("static-access")
	private SimpleMIDIAdapter(int channel, int volume, int duration) {
		super();
		// TODO Auto-generated constructor stub
		this.channel = channel;
		this.volume = volume;
		this.duration = duration;

		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();

			MidiChannel channels[] = synth.getChannels();
			Soundbank sb = synth.getDefaultSoundbank();
			synth.loadAllInstruments(sb);

			Instrument[] ins = synth.getLoadedInstruments();
			int count = 0;
			for (Instrument mc : ins) {
				System.out.println(count++ + ":" + mc.getName());
			}

			channels[0].programChange(ins[0].getPatch().getBank(), ins[0]
					.getPatch().getProgram());
			channels[channel].setPitchBend(16383);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static SimpleMIDIAdapter getInstanceOf(int channel, int volume,
			int duration) {

		if (smadp == null) {
			smadp = new SimpleMIDIAdapter(channel, volume, duration);
		}

		return smadp;

	}

	public static void soundOff(int pitch, int volume) {
		MidiChannel[] channels = synth.getChannels();
		channels[channel].allSoundOff();
	}

	public static void noteOn(int pitch, int volume) {

		MidiChannel[] channels = synth.getChannels();
		channels[channel].noteOn(pitch, volume);

	}

	public static void noteOff(int pitch, int volume) {
		MidiChannel[] channels = synth.getChannels();
		channels[channel].noteOff(pitch, volume);
	}

	public static void allNotesOff() {
		MidiChannel[] channels = synth.getChannels();
		channels[channel].allNotesOff();
	}

	public static void noteSlide(int amount) {
		// TBD - Only bends note one semitone up down
		MidiChannel[] channels = synth.getChannels();
		int bend = deflt_bend;
		switch (amount) {
		case -1:
			bend = deflt_bend - BEND_UNIT;
			break;
		case +1:
			bend = deflt_bend + BEND_UNIT;
			break;
		case +2:
			bend = deflt_bend + 2 * BEND_UNIT;
			break;
		case -2:
			bend = deflt_bend + 2 * BEND_UNIT;
			break;
		default:
			bend = deflt_bend;
		}
		channels[channel].setPitchBend(bend);
		System.out.println("SET Pitch Bend:" + (bend) + ","
				+ channels[channel].getPitchBend());
	}

	public void resetBend() {
		// TODO Auto-generated method stub
		MidiChannel[] channels = synth.getChannels();
		channels[channel].setPitchBend(deflt_bend);
	}

	public static int getInstrument() {
		return instrument;
	}

	public static void setInstrument(int instrument) {
		SimpleMIDIAdapter.instrument = instrument;
		Instrument[] ins = synth.getLoadedInstruments();
		synth.getChannels()[0].programChange(ins[instrument].getPatch()
				.getBank(), ins[instrument].getPatch().getProgram());
		synth.getChannels()[0].setPitchBend(16383);
	}

	public static Instrument[] getInstrumentList() {
		return synth.getLoadedInstruments();
	}

}
