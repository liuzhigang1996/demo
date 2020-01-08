package com.neturbo.set.socket;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class is a generic framework for a flexible, multi-threaded server.
 * It listens on any number of specified ports, and, when it receives a
 * connection on a port, passes input and output streams to a specified Service
 * object which provides the actual service.  It can limit the number of
 * concurrent connections, and logs activity to a specified stream.
 **/
public class Server {
  /**
   * A main() method for running the server as a standalone program.  The
   * command-line arguments to the program should be pairs of servicenames
   * and port numbers.  For each pair, the program will dynamically load the
   * named Service class, instantiate it, and tell the server to provide
   * that Service on the specified port.  The special -control argument
   * should be followed by a password and port, and will start special
   * server control service running on the specified port, protected by the
   * specified password.
   **/
  public static void main(String[] args) {

    try{
      Server s = new Server(System.out, 10);
        Service service = (Service) new TestServer();
        int port = 7771;
        s.addService(service, port);
    }
    catch (Exception e) { // Display a message if anything goes wrong
      System.err.println("Server error");
      System.exit(1);
    }
  }

  // This is the state for the server
  Map services; // Hashtable mapping ports to Listeners
  Set connections; // The set of current connections
  int maxConnections; // The concurrent connection limit
  ThreadGroup threadGroup; // The threadgroup for all our threads
  PrintWriter logStream; // Where we send our logging output to

  /**
   * This is the Server() constructor.  It must be passed a stream
   * to send log output to (may be null), and the limit on the number of
   * concurrent connections.
   **/
  public Server(OutputStream logStream, int maxConnections) {
    setLogStream(logStream);
    log("Starting server");
    threadGroup = new ThreadGroup(Server.class.getName());
    this.maxConnections = maxConnections;
    services = new HashMap();
    connections = new HashSet(maxConnections);
  }

  /**
   * A public method to set the current logging stream.  Pass null
   * to turn logging off
   **/
  public synchronized void setLogStream(OutputStream out) {
    if (out != null) {
      logStream = new PrintWriter(out);
    }
    else {
      logStream = null;
    }
  }

  /** Write the specified string to the log */
  protected synchronized void log(String s) {
    if (logStream != null) {
      logStream.println("[" + new Date() + "] " + s);
      logStream.flush();
    }
  }

  /** Write the specified object to the log */
  protected void log(Object o) {
    log(o.toString());
  }

  /**
   * This method makes the server start providing a new service.
   * It runs the specified Service object on the specified port.
   **/
  public synchronized void addService(Service service, int port) throws
      IOException {
    Integer key = new Integer(port); // the hashtable key
    // Check whether a service is already on that port
    if (services.get(key) != null) {
      throw new IllegalArgumentException("Port " + port +
                                         " already in use.");
    }
    // Create a Listener object to listen for connections on the port
    Listener listener = new Listener(threadGroup, port, service);
    // Store it in the hashtable
    services.put(key, listener);
    // Log it
    log("Starting service " + service.getClass().getName() +
        " on port " + port);
    // Start the listener running.
    listener.start();
  }

  /**
   * This method makes the server stop providing a service on a port.
   * It does not terminate any pending connections to that service, merely
   * causes the server to stop accepting new connections
   **/
  public synchronized void removeService(int port) {
    Integer key = new Integer(port); // hashtable key
    // Look up the Listener object for the port in the hashtable
    final Listener listener = (Listener) services.get(key);
    if (listener == null) {
      return;
    }
    // Ask the listener to stop
    listener.pleaseStop();
    // Remove it from the hashtable
    services.remove(key);
    // And log it.
    log("Stopping service " + listener.service.getClass().getName() +
        " on port " + port);
  }

  /**
   * This nested Thread subclass is a "listener".  It listens for
   * connections on a specified port (using a ServerSocket) and when it gets
   * a connection request, it calls the servers addConnection() method to
   * accept (or reject) the connection.  There is one Listener for each
   * Service being provided by the Server.
   **/
  public class Listener
      extends Thread {
    ServerSocket listen_socket; // The socket to listen for connections
    int port; // The port we're listening on
    Service service; // The service to provide on that port
    volatile boolean stop = false; // Whether we've been asked to stop

    /**
     * The Listener constructor creates a thread for itself in the
     * threadgroup.  It creates a ServerSocket to listen for connections
     * on the specified port.  It arranges for the ServerSocket to be
     * interruptible, so that services can be removed from the server.
     **/
    public Listener(ThreadGroup group, int port, Service service) throws
        IOException {
      super(group, "Listener:" + port);
      listen_socket = new ServerSocket(port);
      // give it a non-zero timeout so accept() can be interrupted
      listen_socket.setSoTimeout(600000);
      this.port = port;
      this.service = service;
    }

    /**
     * This is the polite way to get a Listener to stop accepting
     * connections
     ***/
    public void pleaseStop() {
      this.stop = true; // Set the stop flag
      this.interrupt(); // Stop blocking in accept()
      try {
        listen_socket.close();
      } // Stop listening.
      catch (IOException e) {}
    }

    /**
     * A Listener is a Thread, and this is its body.
     * Wait for connection requests, accept them, and pass the socket on
     * to the addConnection method of the server.
     **/
    public void run() {
      while (!stop) { // loop until we're asked to stop.
        try {
          Socket client = listen_socket.accept();
          addConnection(client, service);
        }
        catch (InterruptedIOException e) {}
        catch (IOException e) {
          log(e);
        }
      }
    }
  }

  /**
   * This is the method that Listener objects call when they accept a
   * connection from a client.  It either creates a Connection object
   * for the connection and adds it to the list of current connections,
   * or, if the limit on connections has been reached, it closes the
   * connection.
   **/
  protected synchronized void addConnection(Socket s, Service service) {
    // If the connection limit has been reached
    if (connections.size() >= maxConnections) {
      try {
        // Then tell the client it is being rejected.
        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.print("Connection refused; " +
                  "the server is busy; please try again later.\n");
        out.flush();
        // And close the connection to the rejected client.
        s.close();
        // And log it, of course
        log("Connection refused to " +
            s.getInetAddress().getHostAddress() +
            ":" + s.getPort() + ": max connections reached.");
      }
      catch (IOException e) {
        log(e);
      }
    }
    else { // Otherwise, if the limit has not been reached
      // Create a Connection thread to handle this connection
      Connection c = new Connection(s, service);
      // Add it to the list of current connections
      connections.add(c);
      // Log this new connection
      log("Connected to " + s.getInetAddress().getHostAddress() +
          ":" + s.getPort() + " on port " + s.getLocalPort() +
          " for service " + service.getClass().getName());
      // And start the Connection thread to provide the service
      c.start();
    }
  }

  /**
   * A Connection thread calls this method just before it exits.  It removes
   * the specified Connection from the set of connections.
   **/
  protected synchronized void endConnection(Connection c) {
    connections.remove(c);
    log("Connection to " + c.client.getInetAddress().getHostAddress() +
        ":" + c.client.getPort() + " closed.");
  }

  /** Change the current connection limit */
  public synchronized void setMaxConnections(int max) {
    maxConnections = max;
  }

  /**
   * This method displays status information about the server on the
   * specified stream.  It can be used for debugging, and is used by the
   * Control service later in this example.
   **/
  public synchronized void displayStatus(PrintWriter out) {
    // Display a list of all Services that are being provided
    Iterator keys = services.keySet().iterator();
    while (keys.hasNext()) {
      Integer port = (Integer) keys.next();
      Listener listener = (Listener) services.get(port);
      out.print("SERVICE " + listener.service.getClass().getName()
                + " ON PORT " + port + "\n");
    }

    // Display the current connection limit
    out.print("MAX CONNECTIONS: " + maxConnections + "\n");

    // Display a list of all current connections
    Iterator conns = connections.iterator();
    while (conns.hasNext()) {
      Connection c = (Connection) conns.next();
      out.print("CONNECTED TO " +
                c.client.getInetAddress().getHostAddress() +
                ":" + c.client.getPort() + " ON PORT " +
                c.client.getLocalPort() + " FOR SERVICE " +
                c.service.getClass().getName() + "\n");
    }
  }

  /**
   * This class is a subclass of Thread that handles an individual
   * connection between a client and a Service provided by this server.
   * Because each such connection has a thread of its own, each Service can
   * have multiple connections pending at once.  Despite all the other
   * threads in use, this is the key feature that makes this a
   * multi-threaded server implementation.
   **/
  public class Connection
      extends Thread {
    Socket client; // The socket to talk to the client through
    Service service; // The service being provided to that client

    /**
     * This constructor just saves some state and calls the superclass
     * constructor to create a thread to handle the connection.  Connection
     * objects are created by Listener threads.  These threads are part of
     * the server's ThreadGroup, so all Connection threads are part of that
     * group, too.
     **/
    public Connection(Socket client, Service service) {
      super("Server.Connection:" +
            client.getInetAddress().getHostAddress() +
            ":" + client.getPort());
      this.client = client;
      this.service = service;
    }

    /**
     * This is the body of each and every Connection thread.
     * All it does is pass the client input and output streams to the
     * serve() method of the specified Service object.  That method is
     * responsible for reading from and writing to those streams to
     * provide the actual service.  Recall that the Service object has
     * been passed from the Server.addService() method to a Listener
     * object to the addConnection() method to this Connection object, and
     * is now finally being used to provide the service.  Note that just
     * before this thread exits it always calls the endConnection() method
     * to remove itself from the set of connections
     **/
    public void run() {
      try {
        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        service.serve(in, out);
      }
      catch (IOException e) {
        log(e);
      }
      finally {
        endConnection(this);
      }
    }
  }


}
