package com.za.networking.peertopeer;


import javax.json.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

public class Peer {

    public static void main(String[] args) throws Exception {
        //Prompts the user for user name and port number
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> enter username & port # for this peer:");
        String[] setupValues = bufferedReader.readLine().split(" ");
        com.za.networking.peertopeer.ServerThread serverThread =new com.za.networking.peertopeer.ServerThread(setupValues[1]);
        serverThread.start();
        new Peer().updateListenToPeers(bufferedReader, setupValues[0], serverThread);
    }

    //Get user input, entering "s" will skip
    public void updateListenToPeers(BufferedReader bufferedReader, String username, com.za.networking.peertopeer.ServerThread serverThread)throws Exception{
        System.out.println("> enter (space separated) hostname:port#");
        System.out.println(" peers to receive messagers from(s to skip):");
        String input = bufferedReader.readLine();
        String[] inputValues = input.split(" ");

        if (!input.equals("s")) for (int i = 0; i < inputValues.length; i++){
            String[] address = inputValues[i].split(":");
            Socket socket = null;
            try{
                socket = new Socket(address[0], Integer.valueOf(address[1]));
                new com.za.networking.peertopeer.PeerThread(socket).start();
            }catch (Exception e){
                if(socket != null) socket.close();
                else System.out.println("invalid input. skipping to next step.");
            }
        }
        communicate(bufferedReader, username, serverThread);
    }

    //In the communication, enter "e" to exit, enter "c" to change the user name and port number.
    public void communicate(BufferedReader bufferedReader, String username, com.za.networking.peertopeer.ServerThread serverThread){
        try {
            System.out.print("> you can now communicate (e to exit, c to change)");
            boolean flag = true;
            while (flag){
                String message = bufferedReader.readLine();
                if(message.equals("e")){
                    flag = false;
                    break;
                }else if(message.equals("c")){
                    updateListenToPeers(bufferedReader, username, serverThread);
                }else{
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder().add("username", username).add("message", message).build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        }catch (Exception e){}
    }
}
