package test.custom.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Calendar;

public class WeekPicker extends Calendar {
	private static final long serialVersionUID = 7513083343273393743L;
	private List<Date> _selectedDates;
	static {
		addClientEvent(WeekPicker.class, "onCustomSelect", CE_IMPORTANT|CE_REPEAT_IGNORE|CE_NON_DEFERRABLE);
	}
	public List<Date> getSelectedDates () {
		return _selectedDates;
	}
	private void updateSelectedDates (int beforeCnt) {
		_selectedDates = new ArrayList<Date>();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		// current selected date
		cal.setTime(getValue());
		// move to first day of the week
		cal.add(java.util.Calendar.DATE, (-1*beforeCnt));
		// add seven days to _selectedDates
		for (int i = 0; i < 7; i++) {
			_selectedDates.add(cal.getTime());
			cal.add(java.util.Calendar.DATE, 1);
		}
	}
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onCustomSelect")) {
			final Map<String, Object> data = request.getData();
			// get node count before selected date
			final Integer beforeCnt = (Integer)data.get("bcnt");
			// update selected dates
			updateSelectedDates(beforeCnt);
			// post event
			Events.postEvent("onCustomSelect", this, null);
		} else {
			super.service(request, everError);
		}
	}
}