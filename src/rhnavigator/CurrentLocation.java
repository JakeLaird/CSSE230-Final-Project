package rhnavigator;
import java.net.*;
import java.io.*;

import org.json.JSONObject;

import rhnavigator.map.Map;






public class CurrentLocation {
	//source:http://stackoverflow.com/questions/5015844/parsing-json-object-in-java
	/**
	 * You could get ip, country_code,country_name,region_code,region_name,city,zip_code,time_zone,latitude,longitude,metro_code
	 * @param requirement
	 * @return String
	 * @throws Exception
	 */
	public static String get(String requirement)throws Exception{
		URL geoLocation = new URL("http://freegeoip.net/json/");
        URLConnection l = geoLocation.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                l.getInputStream()));
        String inputLine = in.readLine(); 
        in.close();
        
        JSONObject obj = new JSONObject(inputLine);
        return (String) obj.get(requirement);
	}
	
	public static MapPoint getMapPoint(Map map)throws Exception{
		URL geoLocation = new URL("http://freegeoip.net/json/");
        URLConnection l = geoLocation.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                l.getInputStream()));
        String inputLine = in.readLine(); 
        in.close();
        
        JSONObject obj = new JSONObject(inputLine);
        return  map.getNearest((double)obj.get("latitude"), (double)obj.get("longitude"));
	}
	
//Source:http://stackoverflow.com/questions/1359689/how-to-send-http-request-in-java
public static String excutePost(String targetURL, String urlParameters) {
    URL url;
    HttpURLConnection connection = null;  
    try {
      //Create connection
      url = new URL(targetURL);
      connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", 
           "application/x-www-form-urlencoded");

      connection.setRequestProperty("Content-Length", "" + 
               Integer.toString(urlParameters.getBytes().length));
      connection.setRequestProperty("Content-Language", "en-US");  

      connection.setUseCaches (false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      //Send request
      DataOutputStream wr = new DataOutputStream (
                  connection.getOutputStream ());
      wr.writeBytes (urlParameters);
      wr.flush ();
      wr.close ();

      //Get Response    
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      String line;
      StringBuffer response = new StringBuffer(); 
      while((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();
      return response.toString();

    } catch (Exception e) {

      e.printStackTrace();
      return null;

    } finally {

      if(connection != null) {
        connection.disconnect(); 
      }
    }
}
}