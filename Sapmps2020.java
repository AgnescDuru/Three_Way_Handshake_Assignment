package sampProgrammingAssignment;

import java.io.*;
import java.net.*;


public class  Sapmps2020 {

	  // argv[0] is server ip addr, argv[1] is server port

	  public static void main(String[] argv)
	    {
	      Socket cfd;      // communication socket "fd" is carry-over from C
	      DataInputStream din;   // binary communicators
	      DataOutputStream dout;  
	      byte[] buffer = new byte[128];  // data buffer
	      int x, y;
	      byte[] iaIpAddress;
	      int ibHostPort = 0;
	      int icNatPort =0;
	      byte idProtocol=0;
	     	      

	      try
	      {
		  // connect to server:
		  cfd = new Socket(argv[0],Integer.parseInt(argv[1])); //ipaddress and integer - port numbers to make connection  10.1.0.4 port in doc 
	 	  din = new DataInputStream(cfd.getInputStream()); 
		  dout = new DataOutputStream(cfd.getOutputStream()); //Gets what is returned 
		  
		  //Get the response 

		  // now in ESTABLISHED state --> Initiates three way handshake
		  x = din.readInt();
		  readFully(din,buffer,0,x);
		  dout.write(buffer,120,8);
		  System.out.println("complete");
		  
		  
        //1. Convert byte arguments to the servers
		 
		  // 4 bytes representing the internal (client) host's ip address Ex/ 10.1.0.4
		       iaIpAddress  = ipbytes(argv[2]);
		      		    
		       		       
		       //read and write to change the bytes
		     //2 bytes representing the internal host's port where packets need to be
			  // mapped to Ex/80	  
		       dout.writeShort((short)ibHostPort);
		            
		      
		      //2 bytes representing the requested port on the NAT box that should be
		      //mapped to the internal port. Ex/8000
		       dout.writeShort((short)icNatPort);
                
		      
		      //1 byte representing the protocol (6=tcp, 17=udp). Only 6 and 17 are allowed.
		       dout.writeByte(idProtocol);

		  
       //2.Pass bytes to the server using write
		      dout.write(iaIpAddress);
		      dout.write(ibHostPort);
		      dout.write(icNatPort);
		      dout.write(idProtocol);
	  
		  
		  //3. Handle  error value 
		      //byte[] E = new byte[2];
		int E = din.readUnsignedShort();
		  if (E != 0 ) {
		System.out.println("Port: " + E);
		  }else {
		      
		      byte[] F = new byte[4];
	         din.read(F,0,4);
	         
			  for(int i=0;i<4;i++) 
			  {
				     if (F[i]>0) System.out.println("error code: "+F[i]);
			  }
		  }
		 
		  	  
		  cfd.close();
	      } catch (Exception ee) {ee.printStackTrace(); System.exit(1);}
	    } // main

	  
	    // the following function only returns after EXACTLY n bytes are read:

	    static void readFully(DataInputStream din, byte[] buffer, int start, int n)
	           throws IOException
	    {
	        int r = 0; // number of bytes read
		while (r<n)
		    { 
			r += din.read(buffer,start+r,n-r);
		    }
	    }//readFully
	    
	    
	    // function to convert string format IP into 4-byte array
	    public static byte[] ipbytes(String addr)
	    {
		byte[] B =  new byte[4]; // array to be constructed
		String[] S = addr.split("\\p{Punct}",4); // splits string into 4 parts
		//System.out.printf("after split: %s:%s:%s:%s\n",S[0],S[1],S[2],S[3]);
		for(int i=0;i<4;i++) 
		    B[i] = (byte)Integer.parseInt(S[i]);
		return B;
	    } //ipbytes


	    //function to return string-format IP address given byte array:
	    public static String ipstring(byte[] B)
	    {
		String addr = ""; // string to be returned
		for(int i=0;i<4;i++)
		    {
			int x = B[i];
			if (x<0) x = x+256;  // signed to unsigned conversion
			addr = addr + ""+ x;
			if (i<3) addr = addr+".";
		    }
		return addr;
	    }// ipstring
	    
	}
