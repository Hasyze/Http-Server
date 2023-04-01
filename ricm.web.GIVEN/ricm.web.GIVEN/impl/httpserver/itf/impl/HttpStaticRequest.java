package httpserver.itf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

/*
 * This class allows to build an object representing an HTTP static request
 */
public class HttpStaticRequest extends HttpRequest {
	static final String DEFAULT_FILE = "index.html";
	
	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}
	
	public void process(HttpResponse resp) throws Exception {
	// TO COMPLETE
		
		if (m_method.equals("GET")) {
			
			if (m_ressname.equals("/FILES/")) {
				m_ressname += "index.html";
			}
			else if (m_ressname.equals("/FILES")) {
				m_ressname += "/" + "index.html";
			}
			
			File f = new File (m_hs.getFolder(),m_ressname);
			FileInputStream fs= new FileInputStream(f);
			resp.setReplyOk();
			resp.setContentLength((int)f.length());
			resp.setContentType(getContentType(m_ressname));
			PrintStream ps = resp.beginBody();
			byte[] buff = new byte[1024];
			
			while (fs.read(buff)>=0) {
				ps.write(buff);
			}
			ps.flush();
			fs.close();
		}
	}

}
