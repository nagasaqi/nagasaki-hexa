package in.nagasaqi.business;

import java.util.ArrayList;
import java.util.Date;

public class PressHistory {

	static PressHistory pressHistory = null;

	class HistoryItem {
		long timestamp;
		ArrayList<Boolean> pressMemory;

		public HistoryItem(ArrayList<Boolean> reg) {
			timestamp = new Date().getTime();
			pressMemory = reg;
		}

	}

	public static PressHistory getInstanceOf() {
		if (pressHistory == null) {
			return new PressHistory();
		}
		return pressHistory;
	}

	static ArrayList<HistoryItem> history;

	private PressHistory() {
		// TODO Auto-generated constructor stub
		history = new ArrayList<PressHistory.HistoryItem>();
	}

	public void add(ArrayList<Boolean> reg) {
		history.add(new HistoryItem(reg));
	}

	public long getGapFromHistory() {
		if (history.size() != 1) {
			return history.get(history.size() - 1).timestamp
					- history.get(history.size() - 2).timestamp;
		}
		return 0;
	}

	public ArrayList<Boolean> getModifiedState() {
		ArrayList<Boolean> tmp1 = null;
		ArrayList<Boolean> tmp2 = null;
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		if (history.size() != 1) {
			tmp1 = history.get(history.size() - 2).pressMemory;
			tmp2 = history.get(history.size() - 1).pressMemory;
			for (Boolean b : tmp2) {
				if (b != tmp1.get(tmp2.indexOf(b))) {
					ret.add(b != tmp1.get(tmp2.indexOf(b)));
				} else {
					ret.add(b);
				}
			}
		}
		return ret;
	}

	public ArrayList<Boolean> getMostRecentState() {
		return history.get(history.size() - 1).pressMemory;
	}

}
