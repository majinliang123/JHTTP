package webapp;

import org.messtin.jhttp.Core;

public class App {

    public static void main(String[] args) throws Exception{
        Core.init(LoginHttpServlet.class, UserHttpServlet.class);
    }
}
