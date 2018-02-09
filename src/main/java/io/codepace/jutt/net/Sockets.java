package io.codepace.jutt.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is a mainly for socket abstraction and other various raw socket utilities
 * What needs to be tested:
 *      1. the {@link #readBytes(Socket, int)} method
 *      2. If calling {@link Socket#close()} will break anything
 *      3. If the methods need to return the socket back in an open state
 */
public class Sockets {

    /**
     * This class should not be instantiated
     */
    public Sockets(){
        super();
    }

    public static void send(Socket socket, byte[] data, String terminator) throws IOException{
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.write(new String(data) + terminator);
    }

    public static void sendLine(Socket socket, String data) throws IOException{
        send(socket, data.getBytes(), "\n");
    }

    public static void send(Socket socket, String data) throws IOException{
        send(socket, data.getBytes(), "");
    }

    public static void send(Socket socket, byte[] data) throws IOException{
        send(socket, data, "");
    }

    public static void sendLine(Socket socket, byte[] data) throws IOException{
        send(socket, data, "\n");
    }


    //-----------------------

    /**
     * Reads a line from the given socket
     * @param socket The socket to read from
     * @return The line read from the socket
     * @throws IOException If the input stream was unable to be gotten (from the socket)
     */
    public static String readLine(Socket socket) throws IOException{
       return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
    }

    /**
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     * THIS NEEDS TO BE TESTED
     *
     * @param socket
     * @param amt
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(Socket socket, int amt) throws IOException{
        DataInputStream din = new DataInputStream(socket.getInputStream());
        byte[] read = new byte[amt];
        din.readFully(read, 0, amt);
        return read;
    }

    /**
     * TODO
     * @param socket
     * @return
     * @throws IOException
     */
    public static Object[] getConnectionInfo(Socket socket) throws IOException{
        return new Object[]{socket.getInetAddress(), socket.getPort(), socket.getLocalAddress(), socket.getKeepAlive()};
    }

    public static Socket startListenerAndWaitForConnection(int port){
        try {
            ServerSocket ss = new ServerSocket(port);
            return ss.accept();
        } catch (IOException e) {
            System.err.println("Unable to start listener on port " + port + ". (" + e.getMessage() + ")");
            return null;
        }
    }


}
