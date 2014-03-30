package test;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import test.component.parameter.ComponentParameterInterceptor;

/** VM with new binding approach command
 * Tested with ZK 6.5.5
 * 
 * @author benbai123
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TestVM {
	private ListModelList _model;
	public ListModelList getModel () {
		if (_model == null) {
			List l = new ArrayList();
			for (int i = 0; i < 10; i++) {
				l.add(new Item("Name " + i));
			}
			_model = new ListModelList(l);
		}
		return _model;
	}
	/** Do command with data get from ComponentParameterInterceptor
	 */
	@Command
	public void showIndex () {
		List data = ComponentParameterInterceptor.getCommandData();
		Clients.alert(data.get(0).toString());
	}
	@Command
	public void showName () {
		List data = ComponentParameterInterceptor.getCommandData();
		Clients.alert(data.get(0).toString());
	}
	@Command
	public void showIndexAndName () {
		List data = ComponentParameterInterceptor.getCommandData();
		Clients.alert("Index: " + data.get(0)+ ", Name: " + data.get(1));
	}
	@Command
	@NotifyChange("model")
	public void delete () {
		List data = ComponentParameterInterceptor.getCommandData();
		_model.remove(data.get(0));
	}
}
