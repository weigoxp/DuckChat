import java.io.Serializable;


public class ClientRequest implements Serializable {
	private int identifier = -1; // 32-bit message type identifier
	private byte[] userName = new byte[32]; // 32-byte user name
	private byte[] channelName = new byte[32]; //32-byte channel name
	private byte[] text = new byte[64]; // 64-byte textfield
	
	// login, join, leave or who request constructor
	public ClientRequest(int identifier, byte[] name) {
		this(identifier);
		if (identifier == 0) { //login
			userName = name;
		} else if (identifier == 3) { //leave
		
		} else if (identifier == 6) {// who
			
		}
	}
	
	// list or logout request constructor
	public ClientRequest(int identifier) {
		this.identifier = identifier;
	}
	
	// say request constructor
	public ClientRequest(int identifier, byte[] name, byte[] field) {
		this(identifier);
		this.channelName = name;
		this.text = field;
	}
	
	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public byte[] getUserName() {
		return userName;
	}

	public void setUserName(byte[] userName) {
		this.userName = userName;
	}

	public byte[] getChannelName() {
		return channelName;
	}

	public void setChannelName(byte[] channelName) {
		this.channelName = channelName;
	}

	public byte[] getText() {
		return text;
	}

	public void setText(byte[] text) {
		this.text = text;
	}
	//java serialization: http://www.tutorialspoint.com/java/java_serialization.htm
	
}