package com.aca.amm;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.xmlpull.v1.XmlPullParserException;

public class MasterExceptionClass {
	private Exception ex;

	private String arrayIndexOutOfBoundException  = "Data tidak ditemukan";
	private String ioException  = "Gagal koneksi ke server";
	private String connectionTimeoutException  = "Connection timeout";
	private String socketTimeoutException  = "Koneksi ke server terputus";
	private String xmlParserException  = "Gagal mendapatkan data";
//	private String Exception  = "An unknown error occured";
	private String nullPointerException  = "Gagal mendapatkan data";
	private String generalException  = "An error occured";
	
	public MasterExceptionClass (Exception ex) {
		this.ex = ex;
	}
	
	public String getException () {
		String errorMessage = "";
		
		if (ex instanceof ArrayIndexOutOfBoundsException)
			errorMessage = arrayIndexOutOfBoundException;
		
		else if (ex instanceof NullPointerException)
			errorMessage = nullPointerException;		
		
		else if (ex instanceof ConnectTimeoutException )
			errorMessage = connectionTimeoutException;
		
		else if (ex instanceof SocketTimeoutException )
			errorMessage = socketTimeoutException;
				
		else if (ex instanceof IOException)
			errorMessage = ioException;	
		
		else if (ex instanceof XmlPullParserException)
			errorMessage = xmlParserException;
		
		else if (ex instanceof Exception)
			errorMessage = generalException + " ";
		
		return errorMessage;

		
	}
}
