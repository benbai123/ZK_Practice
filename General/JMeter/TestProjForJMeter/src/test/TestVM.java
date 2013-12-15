package test;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class TestVM {
	private boolean _testStarted;
	private int _intOne;
	private int _intTwo;
	public boolean getTestStarted () {
		return _testStarted;
	}
	public int getIntOne () {
		return _intOne;
	}
	public int getIntTwo () {
		return _intTwo;
	}
	@Command
	@NotifyChange("testStarted")
	public void showTestItems () {
		_testStarted = true;
	}
	@Command
	@NotifyChange("intOne")
	public void increaseOne () {
		_intOne++;
	}
	@Command
	@NotifyChange("intTwo")
	public void increaseTwo () {
		_intTwo++;
	}
}
