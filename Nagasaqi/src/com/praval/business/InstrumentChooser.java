package com.praval.business;

import javax.sound.midi.Instrument;
import javax.swing.JComboBox;

public class InstrumentChooser extends JComboBox<String>{
	static DisplayAdapter displayAdapter;
	public InstrumentChooser(Instrument[] instruments,DisplayAdapter da) {
		displayAdapter=da;
		for(Instrument i: instruments)
			this.addItem("Instrument::"+i.getName());
			// TODO Auto-generated constructor stub
				//displayAdapter.changeInstrument(0);
	}
	
	@Override
	protected void selectedItemChanged() {
		// TODO Auto-generated method stub
		super.selectedItemChanged();
		try{
		displayAdapter.changeInstrument(this.getSelectedIndex());}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
