package in.nagasaqi.business;

public class ScaleData {

	public int[] frequencies;
	String name;

	public ScaleData(int[] frequencies, String name) {
		super();
		this.frequencies = frequencies;
		this.name = name;
	}

	public int[] getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(int[] frequencies) {
		this.frequencies = frequencies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
