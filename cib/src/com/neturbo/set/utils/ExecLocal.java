package com.neturbo.set.utils;

import java.io.*;

class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    OutputStream os;

    StreamGobbler(InputStream is, String type)
    {
        this(is, type, null);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + ">" + line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe)
            {
            ioe.printStackTrace();
            }
    }
}


public class ExecLocal {
  private String output;
  private String error;
  public ExecLocal() {
  }

  public String exec(String cmdStr) {
    int exitVal = 0;
    try {
      ByteArrayOutputStream baosOutput = new ByteArrayOutputStream();
      ByteArrayOutputStream baosError = new ByteArrayOutputStream();

      Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(cmdStr);
      // any error message
      StreamGobbler errorGobbler = new
          StreamGobbler(proc.getErrorStream(), "ERROR", baosError);

      // any output
      StreamGobbler outputGobbler = new
          StreamGobbler(proc.getInputStream(), "OUTPUT", baosOutput);

      // kick them off
      errorGobbler.start();
      outputGobbler.start();

      // any error
      exitVal = proc.waitFor();

      baosOutput.flush();
      output = baosOutput.toString();
      baosOutput.close();

      baosError.flush();
      error = baosError.toString();
      baosError.close();
    }
    catch (Throwable t) {
      t.printStackTrace();
      return null;
    }
    return String.valueOf(exitVal);

  }

  public String getOutput() {
    return output;
  }
  public void setOutput(String output) {
    this.output = output;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }

  /**
   * test main
   * @param args
   *
   * @author Maomao
   */
public static void main(String[] args){
      ExecLocal myExec = new ExecLocal();

      //Note: the command string must be the executiable file name with absolute path
      myExec.exec("C:\\Program Files\\EditPlus 2\\editplus.exe");
  }
}
