package com.za.networking.peertopeer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread{
    private com.za.networking.peertopeer.ServerThread serverThread;
    private Socket socket;
    private PrintWriter printWriter;

    // Constructor for the ServerThreadThread class.
    public ServerThreadThread(Socket socket, com.za.networking.peertopeer.ServerThread serverThread){
        this.serverThread = serverThread;
        this.socket = socket;
    }

    // The main execution logic of the thread.
    public void run(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);

            // Continuously reads messages from the client and broadcasts them to all connected peers.
            while (true) serverThread.sendMessage(bufferedReader.readLine());
        }catch (Exception e){serverThread.getServerThreadThreads().remove(this);}
    }

    // Getter method for the PrintWriter instance.
    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
