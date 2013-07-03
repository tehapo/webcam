package org.vaadin.teemu.webcam.demo;

import javax.servlet.ServletException;

import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@SuppressWarnings("serial")
public class GifBoothServlet extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new SessionInitListener() {

            @Override
            public void sessionInit(SessionInitEvent event)
                    throws ServiceException {
                event.getSession().addBootstrapListener(bootstrapListener);
            }
        });
    }

    private final BootstrapListener bootstrapListener = new BootstrapListener() {

        @Override
        public void modifyBootstrapPage(BootstrapPageResponse response) {
            Element head = response.getDocument().getElementsByTag("head")
                    .get(0);
            Element link = response.getDocument().createElement("link");
            link.attr("rel", "stylesheet");
            link.attr("type", "text/css");
            link.attr("href",
                    "http://fonts.googleapis.com/css?family=Permanent+Marker");
            head.appendChild(link);
        }

        @Override
        public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
            // NOP
        }
    };

}
