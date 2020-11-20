import java.io.File;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {
    public static final int MIN_PORT_NUMBER = 0;
    public static final int MAX_PORT_NUMBER = 65535;

    public static void main(String[] args) throws Exception {
        int port = 0;
        File file;
        Scanner in = new Scanner(System.in);
        //_______________________________________________________________________
        System.out.println("Enter fileName: ");
        String fileName = in.nextLine();
        try {
            file = new File(fileName);
            if(!file.canExecute()) {
                throw new SecurityException("can not execute");
            }
        } catch (SecurityException e){
            System.err.println("Exception: " + e);
            return;
        }
        //_______________________________________________________________________
        System.out.println("Enter ip: ");
        String ip = in.nextLine();
        ip = ip.trim();
        if ((ip.length() < 6) & (ip.length() > 15)) {
            throw new IllegalArgumentException("Invalid ip length: length = " + ip.length());
        }
        try {
            Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
            Matcher matcher = pattern.matcher(ip);
            if (true != matcher.matches()) {
                throw new IllegalArgumentException("Invalid ip");
            }
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
            return;
        }
        //_______________________________________________________________________
        System.out.println("Enter port: ");
        try {
            port = in.nextInt();
            if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
                throw new IllegalArgumentException("port is out of range : " + port);
            }
        } catch (RuntimeException e) {
            System.err.println("Exception: " + e);
            port = 6667;
        } finally {
            System.out.println("port = " + port);
        }
        //_______________________________________________________________________
        InetAddress serverIP = InetAddress.getByName(ip);
        Client client = new Client(serverIP, port, file);
        client.start();
    }
}

