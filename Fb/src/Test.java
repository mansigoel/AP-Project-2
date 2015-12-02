

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;
import com.restfb.types.Post.Likes;
import com.restfb.types.User;
import com.restfb.Connection;

/**
 * Servlet implementation class Test
 */

public class Test  {
	public static String APP_ID = "198371123832208";
    public static String APP_SECRET = "7ce0a20324dea3e5165d2938da3fed92";
    private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("shvsjskj");
		//String code = request.getParameter("code");
		// System.out.println("shvskshwvffwje");
		//  String URLEncodedRedirectURI = URLEncoder.encode("http://localhost:8080/Potato/Test");
		  String MY_ACCESS_TOKEN = "CAACEdEose0cBABaaFiQdSOrQzz3qZCAZBhGpjLhtGE1DDXc3ZC5Otmltq5unu2DZAxenZA6Ah4OZBb32AjXvNdfwp65hchfwMZCZCzgDfGKQd2Lboi3dTe8VqEmTfUyAlJrWPMDFa1CYZANYQX9MPcJwHXynzHmlZCwL5WjUZCyGQSmuenuCLLrZAoCDM3K0p8htf0QPWNZA9rUrkGgYbCtGZBa7bP";
		 
		  ArrayList<String> stopwords = new ArrayList();
	        FileInputStream fstream;
	        try {
	            fstream = new FileInputStream("src/stop-words_english_1_en.txt");
	            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	            String strLine;
	            while ((strLine = br.readLine()) != null)   {
	                  // Print the content on the console
	                  stopwords.add(strLine);
	                }
	            //System.out.println(stopwords);
	            br.close();
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		 int j;
		  HashMap<Post, Integer> postVsLikeList = new HashMap<Post, Integer>();
		  HashMap<Post, ArrayList<String>> postVsFriendsLikeList = new HashMap<Post, ArrayList<String>>();
		  HashMap<String, Integer> friendsVsLikeCountList = new HashMap<String, Integer>();
		  HashMap<String, ArrayList<String>> friendsVsWordsList = new HashMap<String, ArrayList<String>>();
		  HashMap<Post, ArrayList<String>> postVsProminentWords = new HashMap<Post, ArrayList<String>>();
		  Connection<Post> myFeed;
		  
		  try {
			  FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		            User loginUser = facebookClient.fetchObject("me", User.class);
		            //request.setAttribute("loginUser", loginUser);
		            //friends = facebookClient.fetchConnection("/me/friends", User.class);
		            myFeed = facebookClient.fetchConnection(loginUser.getId() + "/feed", Post.class, Parameter.with("fields", "likes.limit(150),comments.summary(true),type,with_tags,updated_time,shares,picture,message_tags,name,description,message,link"));
		            
		            //JsonObject p = facebookClient.fetchObject("me/photos", JsonObject.class);
		            JsonObject mj = facebookClient.fetchObject("me/posts", JsonObject.class);
		            String firstPhotoUrl = mj.getJsonArray("data").getJsonObject(0).toString();
		            //System.out.println(firstPhotoUrl);
		            //System.out.println(mj);
		            //response.getWriter().append("Served at: ").append(facebookClient.fetchObject("me",User.class).getName());
		            ArrayList<String> Words = new ArrayList<String>();
		            //ArrayList<String> words = new ArrayList<String>();
		            for(List<Post> plist: myFeed)
		  		  {
		            	//System.out.println(plist.size());
		  			  for(Post post: plist){
		  				  
		  				//System.out.println("------------------------------------------------------------------");
		  				  if(post.getLikes()!=null){
		  					String s = post.getLikes().getData().toString();
		  					List<String> friendslike= new ArrayList<String>(Arrays.asList(s.split(",")));
		  					for(String friend: friendslike) {
		  						if(!friendsVsLikeCountList.containsKey(friend)) {
		  							friendsVsLikeCountList.put(friend, 1);
		  						}
		  						else {
		  							int val = friendsVsLikeCountList.get(friend);
		  							friendsVsLikeCountList.put(friend, val+1);

		  						}
		  					}
		  					postVsFriendsLikeList.put(post, (ArrayList<String>) friendslike);
		  					if(post.getMessage()!=null) {
		  					//System.out.println((post.getLikes().getData()));
		  					String des = post.getMessage().toString().toLowerCase();
		  					//System.out.println(des);
		  					
		  		            List<String> parts= new ArrayList<String>(Arrays.asList(des.split(" ")));
		  		          List<String> words= new ArrayList<String>(Arrays.asList(des.split(" ")));
		  		            for(String p:(ArrayList<String>)parts){
		  		            	//System.out.println(post.getMessage());
		  		                for(String s1:stopwords){
		  		                	//System.out.println(s1);
		  		                    if(p.equals(s1)){
		  		                    	//System.out.println(p+"yuy");
		  		                        words.remove(p);
		  		                    }
		  		                }
		  		            }
		  					
		  					
		  					for(String friend: friendslike) {
		  						if(!friendsVsWordsList.containsKey(friend)) {
		  							Words = (ArrayList<String>)words;
		  							friendsVsWordsList.put(friend, Words);
		  						}
		  						else {
		  							//int val = friendsVsLikeCountList.get(friend);
		  							Words = friendsVsWordsList.get(friend);
		  							Words.addAll(words);
		  							friendsVsWordsList.put(friend, Words);

		  						}
		  					}
		  					int count=0; 						//LikeCount
		  					int i=0;
		  					while(i<s.length()) {
		  						if(s.charAt(i) == ',') {
		  							count++;
		  						}
		  						i++;
		  					}
		  					 
		  		            postVsProminentWords.put(post, (ArrayList<String>) Words);
		  					//String[] parts = s.split(",");
		  					//Connection<Likes> like;
		  					//like = facebookClient.fetchConnection(post.getId(), Likes.class, Parameter.with("fields", "total_count"));
		  					//System.out.println(postVsProminentWords);
		  					postVsLikeList.put(post, count);
		  					//postVsFriendsLikeList.put(post, (ArrayList<String>) friendslike);
		  					//Long x = post.getLikesCount();
		  					  System.out.println(postVsLikeList);
		  				  }
		  				  
		  			  }
		  			  }
		  		  }
		  		  
		  } catch (FacebookException e) {
		    e.printStackTrace();
		  }
		  
		 
		 // getServletConfig().getServletContext().getRequestDispatcher("/FriendsList.jsp").forward(request, response);
		//response.getWriter().append("Served at: ").append("sdhdu");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

	
}
