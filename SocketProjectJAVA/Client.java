import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;
private String username;

public Client(Socket socket,String username){
    try{
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.username = username;
    }catch (IOException ex){
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
}

public void sendMessage(){
    try{
        bufferedWriter.write(this.username);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        Scanner sc = new Scanner(System.in);
        while(socket.isConnected()){
            String messageToSend = sc.nextLine();
            System.out.println("YOU : "+messageToSend);
            bufferedWriter.write(username+": "+messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }catch (IOException ex){
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
}

    public void listenForMessage(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            String messageFromGroupChat;
            while (socket.isConnected()){
                try{
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                }catch (IOException ex){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                }
            }
        }
    }).start();
}
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Your Username for the Group Chat :: ");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();
    }
}
