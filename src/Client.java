import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.IOException;

public class Client {
    private Socket clientSocket;
    private File file;

    Client(InetAddress ip, int port, File file) throws IOException {
        this.clientSocket  = new Socket(ip, port);
        this.file = file;
    }

    public void start() throws Exception{
        byte[] bytearray = new byte[1024];
        DataOutputStream socketOutputStream = null;
        DataInputStream socketInputStream = null;
        FileInputStream fin = null;

        try {
            fin = new FileInputStream(file.getPath());

            byte[] fileName = file.getName().getBytes();

            socketOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            socketInputStream = new DataInputStream(clientSocket.getInputStream());

            short lengthFileName = (short) fileName.length;

            socketOutputStream.writeShort(lengthFileName);
            socketOutputStream.write(fileName, 0, fileName.length);

            long lenght = file.length();
            socketOutputStream.writeLong(lenght);

            int number = 0;
            while ((number = fin.read(bytearray)) != -1){
                socketOutputStream.write(bytearray,0, number);
            }
            socketOutputStream.flush();
            System.out.println("Закончили передавать");

            String result = socketInputStream.readUTF();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socketOutputStream != null)
                socketOutputStream.close();
            if(socketInputStream != null)
                socketInputStream.close();
            fin.close();
            if(clientSocket != null)
                clientSocket.close();
        }
    }
}
