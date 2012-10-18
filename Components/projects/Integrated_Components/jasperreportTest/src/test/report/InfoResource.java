package test.report;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;

public class InfoResource implements JRDataSource {
	private List<FoodInfo> dataList = new ArrayList<FoodInfo>();
	private int index = -1;
	public static void main(String args[]) {
		generateTemplateSrc();
	}
	/**
	 * compile the template.jrxml to generate template.jasper
	 */
	public static void generateTemplateSrc() {
		try {
			JasperCompileManager.compileReportToFile("WebContent/WEB-INF/template/template.jrxml", "WebContent/WEB-INF/template/template.jasper");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public InfoResource()
	{
		// datas to report
		dataList.add(new FoodInfo("pizza", 1000, 200));
		dataList.add(new FoodInfo("ham",  500, 300));
		dataList.add(new FoodInfo("banana", 200, 20));
	}

	/**
	 * method defined in interface JRDataSource,
	 * return whether has next data
	 */
	public boolean next() throws JRException
	{
		index++;
		return (index < dataList.size());
	}
	/**
	 * method defined in interface JRDataSource,
	 * return value based on field name
	 */
	public Object getFieldValue(JRField field) throws JRException
	{
		Object value = null;
		
		String fieldName = field.getName();
		
		if ("product_name".equals(fieldName))
		{
			value = dataList.get(index).getName();
		}
		else if ("cal".equals(fieldName))
		{
			value = dataList.get(index).getCal();
		}
		else if ("price".equals(fieldName))
		{
			value = dataList.get(index).getPrice();
		}
		
		return value;
	}
}
/**
 * the data class
 *
 */
class FoodInfo {
	private String name;
	private int cal;
	private int price;
	
	public FoodInfo() {
		
	}
	public FoodInfo(String name, int cal, int price) {
		this.name = name;
		this.cal = cal;
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setCal(int cal) {
		this.cal = cal;
	}
	public int getCal() {
		return cal;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return price;
	}
}