package video.servlet;

import javax.servlet.ServletException;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class MediaServletActivator {

    private String m_media_dir;

    private MediaServlet mediaServlet;

    private HttpService httpService;

    public void starting() throws ServletException, NamespaceException {
        if (mediaServlet == null) {
            mediaServlet = new MediaServlet(m_media_dir);
        }
        httpService.registerServlet("/media", mediaServlet, null, null);
        System.out.println("======== MediaServlet("+m_media_dir+") started!");
    }

    public void stopping() {
        httpService.unregister("/media");
        System.out.println("======== MediaServlet stopped!");
    }

    public String getM_media_dir() {
        return m_media_dir;
    }

    public void setM_media_dir(String m_media_dir) {
        this.m_media_dir = m_media_dir;
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

}
