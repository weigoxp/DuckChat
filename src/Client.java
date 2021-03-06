import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
	
	private static DatagramSocket clientSocket = null;
	private static ArrayList <String> myChannel = new ArrayList<String> (); 
	private static String currentChannel = "Common";
	private static InetAddress serverAddress;
	private static int serverPort;
	private static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	public static void main (String[] args) {
		
		// TODO add validation methods to Utilities.java and invoke them here
		// to see if serverAddress and port are valid
		
		try {
			serverAddress = InetAddress.getLocalHost(); // TODO needs a real address (arg[0])
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		serverPort = Integer.parseInt(args[1]);
		String username = args[2];
		
		
		// set up a clientSocket to send and receive data
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		
		// start listening to server's response 
		ResponseListener responseListener = new ResponseListener(threadExecutor, clientSocket);
		threadExecutor.execute(responseListener);
		
		// send the login request
		byte[] adjustedUsername = Utilities.fillInByteArray(username, 32);
		ClientRequest loginRequest = new ClientRequest(0, adjustedUsername);
		sendClientRequest(loginRequest);
		
		// start processing the user's command
		Scanner console = new Scanner(System.in);
		while (console.hasNextLine()){
			String userInput = console.nextLine();
			processUserInput(userInput);
		}
	}
	
	public static void processUserInput(String userInput) {
		byte[] adjustedUsername, adjustedChannelName, adjustedText;
		ClientRequest request = null;
		if (userInput.startsWith("/")){
			if (userInput.startsWith("/exit")) {
				
			} else if (userInput.startsWith("/join")) {
				
			} else if (userInput.startsWith("/leave")) {
				
				String delims = " ";
				String[] tokens = userInput.split(delims);
				String channelName = tokens[1];
				
				if(currentChannel.equals( channelName))
					currentChannel = ""; // need to set ignore in "say request"
				
				if(myChannel.contains(channelName)){
					myChannel.remove(channelName);//remove from alist
							
					adjustedChannelName = Utilities.fillInByteArray(channelName, 32);
					request = new ClientRequest(3, adjustedChannelName);
					sendClientRequest(request);
				}				
				else
					System.out.println("The channel u want to leave has not been joined!");		
					
			} else if (userInput.startsWith("/list")) {
				
			} else if (userInput.startsWith("/who")) {
				
			} else if (userInput.startsWith("/switch")) {
				
				String delims = " ";
				String[] tokens = userInput.split(delims);
				String channelName = tokens[1];
				
				if(myChannel.contains(channelName)){
					currentChannel = channelName ;
				}
				else
					System.out.println("The channel u want to switch has not been joined!");		
				
				
			} else {
				System.out.println("Invalid command!");
				// TODO what to do with an invalid command?
			}
		} else { // say request
			adjustedChannelName = Utilities.fillInByteArray(currentChannel, 32);
			adjustedText = Utilities.fillInByteArray(userInput, 64);
			request = new ClientRequest(adjustedChannelName, adjustedText);
			sendClientRequest(request);
		}
		
	}
	
	// send the ClientRequest to the server
	private static void sendClientRequest(ClientRequest clientRequest) {
		byte[] dataToBeSent = Utilities.getByteArray(clientRequest); // serialization occurs
		try {
			clientSocket.send(new DatagramPacket(dataToBeSent, dataToBeSent.length, 
					serverAddress, serverPort));
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
