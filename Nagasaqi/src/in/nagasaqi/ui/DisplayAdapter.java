package in.nagasaqi.ui;

import in.nagasaqi.business.InstrumentChooser;
import in.nagasaqi.business.ScaleBase;
import in.nagasaqi.business.ScaleData;
import in.nagasaqi.business.SimpleMIDIAdapter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DisplayAdapter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1327212225623326489L;
	public InstrumentAdapter instrumentAdapter;
	public int keynum = 0;
	public int scalenum = 0;
	final JComboBox<String> cboKey;
	final JComboBox<ScaleData> cboScale;
	final JTextField txtOctave;
	final JTextField txtRoot;
	final ArrayList<JButton> btnNotes;
	final ArrayList<ArrayList<JButton>> btnListKeys;
	final ArrayList<JButton> btnListBend;

	public InstrumentAdapter getInstrumentAdapter() {
		return instrumentAdapter;
	}

	public void setInstrumentAdapter(InstrumentAdapter instrument) {
		this.instrumentAdapter = instrument;
	}

	public int getKeynum() {
		return keynum;
	}

	public void setKeynum(int keynum) {
		this.keynum = keynum;
	}

	public int getScalenum() {
		return scalenum;
	}

	public void setScalenum(int scalenum) {
		this.scalenum = scalenum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DisplayAdapter() {
		// TODO Auto-generated constructor stub

		setTitle("Display Adapter.");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(470, 0, 400, 400);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.DARK_GRAY);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDisplay = new JLabel();
		lblDisplay.setEnabled(false);
		lblDisplay.setBounds(0, 0, 480, 20);
		lblDisplay.setBackground(Color.WHITE);
		lblDisplay.setForeground(Color.BLUE);
		lblDisplay.setFont(getFont());
		lblDisplay
				.setText("NagaSaqi Synth:"
						+ "                                                                                                "
						+ "Controller" + "::");
		contentPane.add(lblDisplay);

		JLabel lblKeyIndicator = new JLabel("Key Setting:");
		lblKeyIndicator.setEnabled(false);
		lblKeyIndicator.setBounds(80, 20, 146, 24);
		lblKeyIndicator.setBackground(Color.WHITE);
		lblKeyIndicator.setForeground(Color.BLUE);
		lblDisplay.setFont(getFont());
		contentPane.add(lblKeyIndicator);

		cboKey = new JComboBox<String>();
		cboKey.setEnabled(false);
		cboKey.setBounds(80, 40, 146, 24);
		cboKey.setBackground(Color.WHITE);
		cboKey.setForeground(Color.BLACK);
		cboKey.addItem("C");
		cboKey.addItem("C#");
		cboKey.addItem("D");
		cboKey.addItem("D#");
		cboKey.addItem("E");
		cboKey.addItem("F");
		cboKey.addItem("F#");
		cboKey.addItem("G");
		cboKey.addItem("G#");
		cboKey.addItem("A");
		cboKey.addItem("A#");
		cboKey.addItem("B");
		contentPane.add(cboKey);

		cboKey.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getItem().equals(cboKey.getSelectedItem())) {
					keynum = cboKey.getSelectedIndex();
				}
			}
		});

		JLabel lblKeyIndicator1 = new JLabel("Scale Setting:");
		lblKeyIndicator1.setEnabled(false);
		lblKeyIndicator1.setBounds(226, 20, 146, 24);
		lblKeyIndicator1.setBackground(Color.WHITE);
		lblKeyIndicator1.setForeground(Color.BLUE);
		lblKeyIndicator1.setFont(getFont());
		contentPane.add(lblKeyIndicator1);

		cboScale = new JComboBox<ScaleData>();
		cboScale.setEnabled(true);
		cboScale.setBounds(226, 40, 146, 24);
		for (ScaleData sc : new ScaleBase().listScales) {
			cboScale.addItem(sc);
		}
		contentPane.add(cboScale);
		cboScale.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if (e.getItem().equals(cboScale.getSelectedItem())) {
					scalenum = cboScale.getSelectedIndex();
					cboKey.setSelectedIndex(0);// select one
					instrumentAdapter.refreshDataKeyScale();
					// tbd
				}
			}
		});

		txtOctave = new JTextField("Octave");
		txtOctave.setBackground(Color.GRAY);
		txtOctave.setBounds(80, 60, 144, 30);
		txtOctave.setVisible(true);
		txtRoot = new JTextField("Root");
		txtRoot.setBackground(Color.GRAY);
		txtRoot.setBounds(226, 60, 144, 30);
		txtRoot.setVisible(true);
		contentPane.add(txtOctave);
		contentPane.add(txtRoot);

		InstrumentChooser ic = new InstrumentChooser(
				SimpleMIDIAdapter.getInstrumentList(), this);
		ic.setEnabled(true);
		ic.setBounds(80, 100, 146 * 2, 16);
		contentPane.add(ic);

		@SuppressWarnings("unused")
		Graphics g = contentPane.getGraphics();
		btnNotes = new ArrayList<JButton>();

		btnListKeys = new ArrayList<ArrayList<JButton>>();
		for (int i = 0; i < 7; i++) {
			JButton jb = new JButton();
			jb.setBounds(40 + i * 40 + 40, 120, 55, 40);
			jb.setBackground(Color.WHITE);
			jb.setForeground(Color.BLACK);
			jb.setText(ScaleBase
					.identifyNoteFromNumber(new ScaleBase().listScales.get(0).frequencies[i]));
			jb.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					for (JButton jb : btnNotes) {
						if (!(btnNotes.indexOf(jb) > btnNotes.indexOf(this))) {
							jb.setEnabled(true);
						}

					}
				}
			});
			btnNotes.add(jb);

			ArrayList<JButton> keys = new ArrayList<JButton>();

			for (int j = 0; j < 3; j++) {
				JButton jbt = new JButton();
				if (toBinary(InstrumentAdapter.inverseInputMap(i)).get(j))
					jbt.setBackground(Color.WHITE);
				else {
					jbt.setBackground(Color.BLACK);
				}

				jbt.setBounds(40 + i * 40 + 40 + j * 10 + 10, 160, 10, 80);
				keys.add(jbt);
				contentPane.add(jbt);
			}
			btnListKeys.add(keys);
			contentPane.add(jb);
			jb.setVisible(true);
		}

		btnListBend = new ArrayList<JButton>();
		for (int i = 0; i < 3; i++) {
			JButton j = new JButton(i - 1 + "");
			j.setBackground(Color.BLACK);
			j.setForeground(Color.WHITE);
			j.setBounds(40 + i * 80 + 70, 270, 50, 50);
			contentPane.add(j);
			btnListBend.add(j);

		}
		btnListBend.get(0).setVisible(false);
		btnListBend.get(2).setVisible(false);
		btnListBend.get(1).setFont(new Font(Font.SANS_SERIF, 16, 16));
		JPanel jp = new JPanel();
		jp.setBounds(180, 260, 70, 70);
		jp.setBackground(Color.darkGray);
		jp.add(btnListBend.get(0));
		contentPane.add(jp);

	}

	@SuppressWarnings("static-access")
	public void refreshTuning(int[] freq) {
		int count = 0;
		for (int x : freq) {
			// btnNotes.get(count++).setText("XXX");
			System.out.println("N"
					+ count
					+ "_"
					+ instrumentAdapter.getScaleBase()
							.identifyNoteFromNumber(x));

			btnNotes.get(count++).setText(
					instrumentAdapter.getScaleBase().identifyNoteFromNumber(x));
		}

	}

	ArrayList<Boolean> toBinary(int x) {
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		String str = Integer.toString(x, 2);
		switch (str.length()) {
		case 1:
			str = "00" + str;
			break;
		case 2:
			str = "0" + str;
			break;
		}
		for (int i = str.length() - 1; i > -1; i--) {
			System.out.println(str);
			if (str.substring(2 - i, 2 - i + 1).equals("1")) {
				ret.add(false);
			} else
				ret.add(true);
		}
		return ret;
	}

	public void changeInstrument(int selectedIndex) {
		// TODO Auto-generated method stub
		this.instrumentAdapter.setInstrument(selectedIndex);
	}
}