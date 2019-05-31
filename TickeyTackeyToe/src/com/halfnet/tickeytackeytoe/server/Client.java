package com.halfnet.tickeytackeytoe.server;

import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private final CommandSupplier commandSupplier;
    private Socket sock;
    
    public Client(CommandSupplier cs){
        this.commandSupplier = cs;
    }
    
    public void send(String address) throws IOException{
        if(this.sock != null){
            System.err.println("Socket already activated");
            return;
        }
        this.sock = new Socket(address, Host.SERVER_PORT);
        ObjectOutputStream out = new ObjectOutputStream(this.sock.getOutputStream());
        out.writeObject(this.commandSupplier);
        out.flush();
        int read = this.sock.getInputStream().read();
        if(read == 1){
            System.out.println("Success!");
        } else {
            System.err.println("Failure sending");
        }
        this.sock.close();
    }
}
