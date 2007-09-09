/*
 * Copyright (c) 2005 Henri Sivonen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package nu.validator.servlet;

import org.apache.log4j.PropertyConfigurator;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpListener;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.ajp.AJP13Listener;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 * @version $Id$
 * @author hsivonen
 */
public class Main {

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(System.getProperty("nu.validator.servlet.log4j-properties", "log4j.properties"));
        new VerifierServletTransaction(null, null);
        HttpServer s = new HttpServer();
        HttpListener l;
        if (args.length > 0 && "ajp".equals(args[0])) {
            l = new AJP13Listener();
            int port = Integer.parseInt(args[1]);
            l.setPort(port);
            l.setHost("127.0.0.1");
        } else {
            l = new SocketListener();
            int port = Integer.parseInt(args[0]);
            l.setPort(port);
        }
        s.addListener(l);
        HttpContext c = new HttpContext();
        c.setContextPath("/");
        ServletHandler sh = new ServletHandler();
        sh.addServlet("/*", "nu.validator.servlet.VerifierServlet");
        c.addHandler(sh);
        s.addContext(c);
        s.start();
    }
}