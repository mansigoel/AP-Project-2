
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;
 
public class getLikes extends HttpServlet {
    public static String APP_ID = "198371123832208";
    public static String APP_SECRET = "7ce0a20324dea3e5165d2938da3fed92";
    private static final long serialVersionUID = 1L;
 
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
  String code = request.getParameter("code");
 
  String URLEncodedRedirectURI = URLEncoder.encode("http://localhost:8080/bitspedia-lab/FriendsListServlet");
  String MY_ACCESS_TOKEN = "";
 
  String authURL = "https://graph.facebook.com/oauth/access_token?" +
                "client_id=" + getLikes.APP_ID + "&" +
                "redirect_uri=" + URLEncodedRedirectURI + "&" +
                "client_secret=" + getLikes.APP_SECRET + "&" +
                "code=" + code;
 
  URL url = new URL(authURL);
 
  String result = readURL(url);
  String[] pairs = result.split("&");
 
  for (String pair : pairs) {
 
   String[] kv = pair.split("=");
   if (kv[0].equals("access_token")) {
    MY_ACCESS_TOKEN = kv[1];
   }
  }
 
  FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN, getLikes.APP_SECRET);
 
  Connection<User> friends = null;
  
  try {
            User loginUser = facebookClient.fetchObject("me", User.class);
            request.setAttribute("loginUser", loginUser);
            friends = facebookClient.fetchConnection("/me/friends", User.class);
 
  } catch (FacebookException e) {
    e.printStackTrace();
  }
 
  List<User> friendsList = friends.getData();
  request.setAttribute("friendsList",friendsList);
 
  getServletConfig().getServletContext().getRequestDispatcher("/FriendsList.jsp").forward(request, response);
 }
 
 private String readURL(URL url) throws IOException {
 
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  InputStream is = url.openStream();
 
  int r;
 
  while ((r = is.read()) != -1) {
   baos.write(r);
  }
 
  return new String(baos.toByteArray());
 
 }
}
//</user></user>