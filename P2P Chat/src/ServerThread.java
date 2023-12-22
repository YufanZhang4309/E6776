package com.za.networking.peertopeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

//this  class manages the server side of the peer-to-peer communication system, accepting incoming connections,
// creating threads to handle each connection, and providing functionality to broadcast messages to all connected peers.
public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Set<com.za.networking.peertopeer.ServerThreadThread> serverThreadThreads = new HashSet<com.za.networking.peertopeer.ServerThreadThread>();
    public ServerThread(String portNumb) throws IOException{
        serverSocket = new ServerSocket(Integer.valueOf(portNumb));
    }
    public void run(){
        try{
            while (true){
                com.za.networking.peertopeer.ServerThreadThread serverThreadThread = new com.za.networking.peertopeer.ServerThreadThread(serverSocket.accept(), this);
                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    void  sendMessage(String message){
        try {serverThreadThreads.forEach(t-> t.getPrintWriter().println(message));
        } catch (Exception e){e.printStackTrace();}
    }
    public Set<com.za.networking.peertopeer.ServerThreadThread> getServerThreadThreads(){return serverThreadThreads ; }
}
