package com.nagasaqi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputListener;

import com.nagasaqi.business.PressHistory;
import com.nagasaqi.business.ScaleBase;
import com.nagasaqi.business.SimpleMIDIAdapter;

public class InstrumentAdapter extends JPanel implements MouseInputListener,
		MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Boolean> pressMemory;

	private SimpleMIDIAdapter simpleMIDIAdapter = null;

	private ScaleBase scaleBase;

	private PressHistory pressHistory;

	private DisplayAdapter display;

	private KeyBoardControllerSim keySim;
	private static JPanel jp;

	@SuppressWarnings("deprecation")
	public InstrumentAdapter() {
		this.setLayout(new BorderLayout());
		jp = this;

		// Register for mouse-wheel events on the text area.
		pressMemory = new ArrayList<Boolean>();
		pressMemory.add(false);
		pressMemory.add(false);
		pressMemory.add(false);
		printpressMemory();
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		setPreferredSize(new Dimension(400, 400));
		this.keySim = new KeyBoardControllerSim(this);
		int channel = 0; // 0 is a piano, 9 is percussion, other channels are
							// for other instruments
		int volume = 80; // between 0 et 127
		int duration = 50; // in milliseconds
		simpleMIDIAdapter = SimpleMIDIAdapter.getInstanceOf(channel, volume,
				duration);
		pressHistory = PressHistory.getInstanceOf();
		scaleBase = new ScaleBase();
		display = new DisplayAdapter();
		display.setInstrumentAdapter(this);

		// to do
		// display.setOpacity(0.5f);
		BufferedImage logo = null;
		try {
			logo = ImageIO.read(this.getClass().getResource("nagasaqi.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel icon = new JLabel(new ImageIcon(logo));
		icon.setLayout(new BorderLayout());
		icon.setBounds(0, 0, 100, 100);

		icon.setVisible(true);
		this.add(icon);
		display.show();
	}

	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Instrument Adapter:");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create and set up the content pane.
		JComponent newContentPane = new InstrumentAdapter();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO alternate method -
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("button-" + e.getButton() + " Pressed");
		SimpleMIDIAdapter.allNotesOff();
		switch (e.getButton()) {
		case 1:
			pressMemory.set(0, !pressMemory.get(0));
			break;
		case 2:
			pressMemory.set(1, !pressMemory.get(1));
			break;
		case 3:
			pressMemory.set(2, !pressMemory.get(2));
			break;
		}
		printpressMemory();
		pressHistory.add(pressMemory);
		drawVisual(false, e.getX(), e.getY());

		play();
	}

	public synchronized void playKey(int note){
		switch (note) {
		
		case 0:
			simpleMIDIAdapter.allNotesOff();
			break;
		case 8:
			SimpleMIDIAdapter
			.noteOn(scaleBase.getListScales()
					.get(display.getScalenum()).frequencies[0]+12,80);
			break;
		case 9:
			simpleMIDIAdapter.noteSlide(1);
			break;
		default:
			SimpleMIDIAdapter
			.noteOn(scaleBase.getListScales()
					.get(display.getScalenum()).frequencies[note-1],80);
		}
	}
	
	private synchronized void play() {
		System.out.println("Played");
		long diff = pressHistory.getGapFromHistory();

		System.out.println("Diff:" + diff);

		if (diff < 80) {
			// impleMIDIAdapter.allNotesOff();
			// SimpleMIDIAdapter.noteOff(scaleBase.getListScales()
			// .get(display.getScalenum()).frequencies[mapInput(getSingleNoteNumber(pressHistory.getModifiedState()))],
			// 80);
			SimpleMIDIAdapter
					.noteOn(scaleBase.getListScales()
							.get(display.getScalenum()).frequencies[mapInput(getSingleNoteNumber(pressMemory))],
							80);

			// color magic

			display.btnNotes.get(mapInput(getSingleNoteNumber(pressMemory)))
					.setEnabled(false);
			System.out
					.println(mapInput(getSingleNoteNumber(pressMemory))
							+ ","
							+ scaleBase.getListScales().get(
									display.getScalenum()).frequencies[mapInput(getSingleNoteNumber(pressMemory))]);
		} else {
			// SimpleMIDIAdapter.allNotesOff();
			SimpleMIDIAdapter
					.noteOn(scaleBase.getListScales()
							.get(display.getScalenum()).frequencies[mapInput(getSingleNoteNumber((pressHistory
							.getMostRecentState())))], 80);
			// color magic
			display.btnNotes.get(
					mapInput(getSingleNoteNumber((pressHistory
							.getModifiedState())))).setEnabled(false);

			System.out
					.println(mapInput(getSingleNoteNumber((pressHistory
							.getMostRecentState())))
							+ ","
							+ scaleBase.getListScales().get(
									display.getScalenum()).frequencies[mapInput(getSingleNoteNumber((pressHistory
									.getModifiedState())))]);
		}

	}

	public int getSingleNoteNumber(ArrayList<Boolean> reg) {
		int nonum = 0;
		int count = 2;
		for (Boolean b : reg) {
			if (b) {
				nonum = nonum + pow2(count);
			}
			count--;
		}
		return nonum;
	}

	@SuppressWarnings("static-access")
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("button-" + e.getButton() + " Released");
		switch (e.getButton()) {
		case 1:
			pressMemory.set(0, false);
			break;
		case 2:
			pressMemory.set(1, false);
			break;
		case 3:
			pressMemory.set(2, false);
			break;
		}
		printpressMemory();
		for (JButton jb : display.btnNotes) {
			jb.setEnabled(true);
		}
		simpleMIDIAdapter.resetBend();
		shiftList(display.btnListBend, 0, true);
		simpleMIDIAdapter.allNotesOff();
		drawVisual(true, e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("static-access")
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

		System.out.println("I moved:" + e.getPreciseWheelRotation());
		System.out.println("I moved:" + e.getWheelRotation());
		System.out.println("@:" + e.getWhen());
		@SuppressWarnings("unused")
		Date d = new Date(e.getWhen());
		System.out.println();
		if (this.isAnyButtonDown()) {
			shiftList(display.btnListBend, e.getWheelRotation(), true);
			shiftList(display.btnListBend, e.getWheelRotation(), false);
			switch (numButtonDown()) {
			case 1:
				// Mode semitone up down
				simpleMIDIAdapter.noteSlide(e.getWheelRotation());
				break;
			case 2:
				// Mode 2 //Mode semitone up down
				simpleMIDIAdapter.noteSlide(e.getWheelRotation());
				break;
			case 3:
				// Mode 3 //Mode semitone up down
				simpleMIDIAdapter.noteSlide(e.getWheelRotation());
				break;
			case 0:
				// Illegal state
			}
		} else {
			// Bend Mode
			// Mode +/-
			System.out.println("in up/down:");
			if (e.getWheelRotation() < 0) {
				scaleBase.getListScales().get(display.getScalenum()).frequencies = shiftarray(
						scaleBase.getListScales().get(display.getScalenum()).frequencies,
						-1);

				@SuppressWarnings("unused")
				int shift = e.getWheelRotation();
				if (display.getKeynum() == 0) {
					display.setKeynum((Math.abs(display.getKeynum()) + 11) % 12);
				} else {
					display.setKeynum(Math.abs((display.getKeynum()) - 1) % 12);
				}
				display.cboKey.setSelectedIndex(display.getKeynum());
			} else {
				scaleBase.getListScales().get(display.getScalenum()).frequencies = shiftarray(
						scaleBase.getListScales().get(display.getScalenum()).frequencies,
						1);

				if (display.getKeynum() == 11) {
					display.setKeynum((Math.abs(display.getKeynum()) + 1) % 12);
				} else {
					display.setKeynum(Math.abs((display.getKeynum()) + 1) % 12);
				}
				display.cboKey.setSelectedIndex(display.getKeynum());
			}

			refreshDataKeyScale();
		}

	}

	private void shiftList(ArrayList<JButton> btnListBend, int wheelRotation,
			boolean resetFLag) {
		// TODO Auto-generated method stub
		for (JButton j : btnListBend) {
			if (!resetFLag) {
				j.setText(Integer.toString(Integer.parseInt(j.getText())
						+ wheelRotation));
			} else {
				j.setText(Integer.toString(1 - btnListBend.indexOf(j)));
			}
		}
	}

	public void refreshDataKeyScale() {
		int tuning = scaleBase.getListScales().get(display.getScalenum()).frequencies[0];
		int octave = tuning / 12;
		display.txtOctave.setText("Octave:" + octave);
		display.txtRoot.setText((String) display.cboKey.getSelectedItem()
				+ octave);
		display.refreshTuning(scaleBase.getListScales().get(
				display.getScalenum()).frequencies);
	}

	public ArrayList<Boolean> getPressMemory() {
		return pressMemory;
	}

	public void setPressMemory(ArrayList<Boolean> pressMemory) {
		this.pressMemory = pressMemory;
	}

	public void printpressMemory() {
		System.out.println("[");
		for (Boolean b : pressMemory) {
			System.out.print(b ? 1 : 0);
		}
		System.out.println("]");
	}

	private boolean isAnyButtonDown() {
		boolean b = false;
		for (boolean bt : pressMemory)
			b = bt || b;
		return b;
	}

	private int numButtonDown() {
		int b = 0;
		for (boolean bt : pressMemory)
			if (bt) {
				b++;
			}
		return b;
	}

	public ScaleBase getScaleBase() {
		return scaleBase;
	}

	public void setScaleBase(ScaleBase scaleBase) {
		this.scaleBase = scaleBase;
	}

	static int pow2(int n) {
		int ret = 1;
		for (int x = 0; x < n; x++) {
			ret = ret * 2;
		}
		return ret;
	}

	public static int mapInput(int x) {
		int ret = 0;
		switch (x) {
		case 1:
			ret = 0;
			break;
		case 2:
			ret = 1;
			break;
		case 4:
			ret = 2;
			break;
		case 3:
			ret = 3;
			break;
		case 5:
			ret = 4;
			break;
		case 6:
			ret = 5;
			break;
		case 7:
			ret = 6;
			break;
		default:
			ret = 0;
		}
		return ret;
	}

	public static int inverseInputMap(int x) {
		int ret = 0;
		switch (x) {
		case 0:
			ret = 1;
			break;
		case 1:
			ret = 2;
			break;
		case 2:
			ret = 4;
			break;
		case 3:
			ret = 3;
			break;
		case 4:
			ret = 5;
			break;
		case 5:
			ret = 6;
			break;
		case 6:
			ret = 7;
			break;
		default:
			ret = 0;
		}
		return ret;
	}

	int[] shiftarray(int[] arr, int amt) {
		int[] x = arr;
		for (int i = 0; i < arr.length; i++) {
			x[i] = arr[i] + amt;
		}
		return x;
	}

	@SuppressWarnings("static-access")
	public void setInstrument(int instr) {
		simpleMIDIAdapter.setInstrument(instr);
	}

	public void drawVisual(final boolean reset, final int x, final int y) {
		@SuppressWarnings("static-access")
		final Graphics graphics = this.jp.getGraphics();
		if (!reset){
			graphics.setColor(Color.WHITE);
			graphics.drawOval(x-10, y-10, 20,5 );
			graphics.fillOval(x-10, y-10, 20,5 );
			
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.drawOval(x-20, y-30, 40,15 );
			graphics.fillOval(x-20, y-30, 40,15 );
			
			graphics.setColor(Color.DARK_GRAY);
			graphics.drawOval(x-40, y-60, 80,25 );
			graphics.fillOval(x-40, y-60, 80,25 );
			graphics.drawString("@", x-10, y);}
		else
			this.paintAll(this.getGraphics());
	}
}
