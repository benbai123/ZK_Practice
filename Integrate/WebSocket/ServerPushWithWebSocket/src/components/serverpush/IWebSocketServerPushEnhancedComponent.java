package components.serverpush;

import impl.serverpush.Binding;
import components.IWebSocketEnhancedComponent;

/** Tested with ZK 6.5.2<br>
 * 
 * Define a "WebSocket Enhanced Component -- ServerPush type"
 * for ServerPush pattern
 * 
 * @author benbai123
 *
 */
public interface IWebSocketServerPushEnhancedComponent extends IWebSocketEnhancedComponent {
	/** Add Binding for specific field/context pair
	 * 
	 * The added Binding will be stored to a Binding List of a Component
	 * 
	 * @param field field to bind
	 * @param context context to bind with field
	 * @return the Added Binding
	 */
	public Binding addSocketContextBinding (String field, String context);
	/** Remove Binding for specific field/context pair
	 * 
	 * Remove it from Binding List of a Component
	 * 
	 * @param field field of field/context pair to remove
	 * @param context context of field/context pair to remove
	 */
	public void removeSocketContextBinding (String field, String context);
}
