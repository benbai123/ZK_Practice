package custom.zk.components.quicknote.Data;

/** Java bean that represent a text note block
 * 
 * @author benbai123
 *
 */
public class TextNoteData {
	private int _posX; // left
	private int _posY; // top
	private int _width; // width
	private int _height; // height
	private String _text; // text

	// constructor that construct with each attributes
	public TextNoteData (int posX, int posY, int width, int height, String text) {
		_posX = posX;
		_posY = posY;
		_width = width;
		_height = height;
		_text = text;
	}
	// constructor that construct with another data bean
	public TextNoteData (TextNoteData dataToCopy) {
		_posX = dataToCopy.getPosX();
		_posY = dataToCopy.getPosY();
		_width = dataToCopy.getWidth();
		_height = dataToCopy.getHeight();
		_text = dataToCopy.getText();
	}
	// setters, getters
	public void setPosX (int posX) {
		_posX = posX;
	}
	public int getPosX () {
		return _posX;
	}
	public void setPosY (int posY) {
		_posY = posY;
	}
	public int getPosY () {
		return _posY;
	}
	public void setWidth (int width) {
		_width = width;
	}
	public int getWidth () {
		return _width;
	}
	public void setHeight (int height) {
		_height = height;
	}
	public int getHeight () {
		return _height;
	}
	public void setText (String text) {
		_text = text;
	}
	public String getText () {
		return _text;
	}
}
