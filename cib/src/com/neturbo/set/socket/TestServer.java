package com.neturbo.set.socket;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is another example service.  It reads lines of input from the
 * client, and sends them back, reversed.  It also displays a welcome
 * message and instructions, and closes the connection when the user
 * enters a '.' on a line by itself.
 **/
public class TestServer extends Service {
  public void serve(InputStream i, OutputStream o) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(i));
    PrintWriter out =
        new PrintWriter(new BufferedWriter(new OutputStreamWriter(o)));
    out.print("Welcome to the line reversal server.\n");
    out.print("Enter lines.  End with a '.' on a line by itself.\n");
    for (; ; ) {
      out.print("> ");
      out.flush();
      String line = in.readLine();
      if ( (line == null) || line.equals(".")) {
        break;
      }
      for (int j = line.length() - 1; j >= 0; j--) {
        out.print(line.charAt(j));
      }
      out.print("\n");
    }
    out.close();
    in.close();
  }
}
