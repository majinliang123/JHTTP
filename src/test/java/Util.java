import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class Util {
    public static void get(String connectURL) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOut = null;
        URL url = null;
        URLConnection httpGet = null;
        InputStream in = null;
        try {
            url = new URL(connectURL);
            httpGet =  url.openConnection();
            in = httpGet.getInputStream();
            byteArrayOut = new ByteArrayOutputStream();
            byte[] buf = new byte[512];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                byteArrayOut.write(buf, 0, l);
            }
            bytes = byteArrayOut.toByteArray();
            String str = bytes != null ? new String(bytes) : null;
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(byteArrayOut != null){
                byteArrayOut.close();
            }
            if(in != null){
                in.close();
            }
        }
    }
}
