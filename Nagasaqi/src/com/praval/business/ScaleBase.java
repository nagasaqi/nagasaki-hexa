package com.praval.business;

import java.util.ArrayList;

public class ScaleBase {

	ArrayList<ScaleData> listScales=new ArrayList<ScaleData>();
	
	ScaleBase(){
		
		int[] bilawal = {60,62,64,65,67,69,71};//Bilawal
		int[] yaman = {60,62,64,66,67,69,71};//yaman
		int[] khamaj = {60,62,64,65,67,69,70};//Khamaj
		int[] bhairava = {60,61,63,65,67,68,70};//bhairava
		int[] purvi = {60,61,63,66,67,68,70};//purvi
		int[] marwa={60,61,63,66,67,69,70};//mrwa
		int[] kafi={60,62,63,65,67,69,70};//kafi
		int[] asav={60,62,63,65,67,68,70};//asavari
		int[] bhairavi={60,61,63,65,67,68,70};//bhairavi
		int[] todi = {60,61,63,66,67,68,71};//kafi
		listScales.add(new ScaleData(bilawal, "Bilawal"));
		listScales.add(new ScaleData(yaman, "Yaman"));
		listScales.add(new ScaleData(khamaj, "Khamaj"));
		listScales.add(new ScaleData(bhairava, "Bhairava"));
		listScales.add(new ScaleData(purvi, "Purvi"));
		listScales.add(new ScaleData(marwa, "Marwa"));
		listScales.add(new ScaleData(kafi, "Kafi"));
		listScales.add(new ScaleData(asav, "Asavari"));
		listScales.add(new ScaleData(bhairavi, "Bhairavi"));
		listScales.add(new ScaleData(todi, "Todi"));
	}

	public ArrayList<ScaleData> getListScales() {
		return listScales;
	}

	public void setListScales(ArrayList<ScaleData> listScales) {
		this.listScales = listScales;
	}
	
	public static String identifyNoteFromNumber(int num){
		String note="";
		switch(num%12){
		case 0:
			note="C";break;
		case 1:
			note="C#";break;
		case 2:
			note="D";break;
		case 3:
			note="D#";break;
		case 4:
			note="E";break;
		case 5:
			note="F";break;
		case 6:
			note="F#";break;
		case 7:
			note="G";break;
		case 8:
			note="G#";break;
		case 9:
			note="A";break;
		case 10:
			note="A#";break;
		case 11:
			note="B";break;	
		}
		return note;
	}
	
	
	
}
