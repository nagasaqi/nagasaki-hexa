/**
 * 
 */
package com.nagasaqi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import com.nagasaqi.business.SimpleMIDIAdapter;

/**
 * @author praval
 *
 */
public class KeyBoardControllerSim extends JPanel implements EventListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3265850758468681904L;

	/**
	 * This simulates a synthesizer using keyboard
	 * TO_DO Also can have chord modes
	 */
	
	static long NUM_KEYS=8;
	static String[] MODES = {"real-world","false-world"}; 
	
	private static InstrumentAdapter insAdapter = null;
	
	static JComboBox<String> modeCombo = new JComboBox<String>();
	static ArrayList<JButton> vKeys = new ArrayList<JButton>();
	static AbstractAction keyPressed = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			JButton j = (JButton)event.getSource();
			j.setEnabled(!j.isEnabled());
			switch(Integer.parseInt(j.getText())){
				case 0:
					reset();
					break;
				case 9:
					specialattack();
					break;
				default:
					insAdapter.playKey(Integer.parseInt(j.getActionCommand()));
					j.setEnabled(!j.isEnabled());
			}
		}

		private void specialattack() {
			// TODO Auto-generated method stub
			SimpleMIDIAdapter.noteSlide(1);
			
		}

		private void reset() {
			// TODO Auto-generated method stub
			SimpleMIDIAdapter.allNotesOff();
		}
	};

	private JFrame frame;
	
	public KeyBoardControllerSim(InstrumentAdapter insAdapter){
		super();
		this.setInsAdapter(insAdapter);
		initiate();
	}
	
	private void initiate(){	
		setPreferredSize(new Dimension(200,200));
		this.setBackground(Color.GRAY);
		this.setLayout(new GridLayout());
		for(String s:MODES){
			modeCombo.addItem(s);
		}
		modeCombo.setEnabled(true);
		modeCombo.setBounds(0, 0, 200, 20);
		modeCombo.setBackground(Color.WHITE);
		modeCombo.setForeground(Color.DARK_GRAY);
		modeCombo.setFont(getFont());
		
		int[] events= new int[12];
		int reset=java.awt.event.KeyEvent.VK_0;
		int end=java.awt.event.KeyEvent.VK_9;
		//
		events[0]=reset;
		events[9]=end;
		
		for(int x=1;x<9;x++){
			events[x]=reset+x;
		}
	
		Map<Integer, Character> keyTochar = new HashMap<Integer, Character>();
		keyTochar.put(events[0], new Character('0'));
		keyTochar.put(events[1], new Character('1'));
		keyTochar.put(events[2], new Character('2'));
		keyTochar.put(events[3], new Character('3'));
		keyTochar.put(events[4], new Character('4'));
		keyTochar.put(events[5], new Character('5'));
		keyTochar.put(events[6], new Character('6'));
		keyTochar.put(events[7], new Character('7'));
		keyTochar.put(events[8], new Character('8'));
		keyTochar.put(events[9], new Character('9'));
		
		BufferedImage logo = null;
		try {
			logo = ImageIO.read(this.getClass().getResource("key.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<NUM_KEYS+2;i++)
		{
			JButton j = new JButton(""+i,new ImageIcon(logo));
			j.setVisible(true);
			j.setForeground(Color.GRAY);
			j.setEnabled(true);
			j.setBounds(0, 0, this.getWidth()/20, this.getWidth()/20);
			j.setBackground(Color.WHITE);
			j.setForeground(Color.BLUE);
			j.setFont(getFont());

			j.addActionListener(keyPressed);
			try {
				
				j.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
						.put(javax.swing.KeyStroke.
								getKeyStroke(keyTochar.get(events[i]).charValue(),0),"released");
			} catch (Exception e) {
				e.printStackTrace();
			}
			j.getActionMap().put("released", keyPressed);
			add(j, 0, -1);;//to jPanel
			vKeys.add(j); //to_DO vKeys.add(new JButton(string text, icon arg0))
			
		}
		this.add(modeCombo);
		this.setVisible(true);
		this.frame=new JFrame("KeyBD[0-9]");
		frame.setAlwaysOnTop(true);
		frame.setContentPane(this);
		frame.setBounds(200,420, 800, 100);
		frame.show();
	}
	
	public KeyBoardControllerSim(int numkeys) {
		// TODO Auto-generated constructor stub
		KeyBoardControllerSim.NUM_KEYS=numkeys;
		initiate();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
	}

	/**
	 * @return the insAdapter
	 */
	public static InstrumentAdapter getInsAdapter() {
		return insAdapter;
	}

	/**
	 * @param insAdapter the insAdapter to set
	 */
	public static void setInsAdapter(InstrumentAdapter insAdapter) {
		KeyBoardControllerSim.insAdapter = insAdapter;
	}

}
