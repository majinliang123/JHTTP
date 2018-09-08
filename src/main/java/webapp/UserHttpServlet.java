package webapp;

import org.messtin.jhttp.annotation.Servlet;
import org.messtin.jhttp.entity.HttpRequest;
import org.messtin.jhttp.entity.HttpResponse;
import org.messtin.jhttp.servlet.AbstractHttpServlet;

@Servlet("/user")
public class UserHttpServlet extends AbstractHttpServlet {
    @Override
    public void doService(HttpRequest request, HttpResponse response) {
        response.setBody("Hello zhushuli.".getBytes());
    }
}
