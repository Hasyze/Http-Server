package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest{
	
	protected HashMap<String, String> argments;
	protected HashMap<String, String> cookies;
	protected Session session;
	
	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname, BufferedReader br) throws IOException {
		super(hs, method, ressname, br);
		argments = new HashMap<String, String>();
		cookies = new HashMap<String, String>();
		String line = br.readLine();
		while(!(line.equals(""))) {
			if (line.startsWith("Cookie:")) {
				String cookie = line.split(" ",2)[1];
				String[] parametres = cookie.split("; ");
				for (int i= 0; i != parametres.length; i++) {
					String[] args = parametres[i].split("=");
					cookies.put(args[0], args[1]);
				}
			}
			line = br.readLine();
		}
		if (getCookie("session-id") != null) {
			for(int i = 0 ; i < m_hs.sessionList.size(); i++) {
				Session session = m_hs.sessionList.get(i);
				if(session.getId().equals(getCookie("session-id"))) {
					this.session = session;
					session.newTimer();
					return;
				}
			}
		}
		String nb = String.valueOf(m_hs.sessionList.size());
		session = new Session(nb, m_hs);
		m_hs.sessionList.add(session);
	}

	@Override
	public HttpSession getSession() { 
		return session;
	}

	@Override
	public String getArg(String name) { 
		return argments.get(name);
	}

	@Override
	public String getCookie(String name) { 
		return cookies.get(name);
	}

	@Override
	public void process(HttpResponse resp) throws Exception { 
		String[] classes = this.m_ressname.split("\\?");
	    if (classes.length > 1) {
	        String[] parametres = classes[1].split("&");
	        for (int i = 0; i < parametres.length; i++) {
	            String[] args = parametres[i].split("=");   
	            if (args.length == 2) {
	                argments.put(args[0], args[1]);
	            }
	        }
	    }
	    String classe = classes[0].replace('/', '.');
	    if (classe.startsWith(".")) {
	        classe = classe.substring(1);
	    }
	    HttpRicmlet ricmlet = m_hs.getInstance(classe);
	    ricmlet.doGet(this, (HttpRicmletResponseImpl) resp);
	}

}
