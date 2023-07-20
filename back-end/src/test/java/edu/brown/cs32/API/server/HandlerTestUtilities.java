package edu.brown.cs32.API.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/** helper methods for handler testing make generating connections and sending requests easier! */
public class HandlerTestUtilities {

  /** Helpers for the connection of a specific API endpoint` */
  static HttpURLConnection tryRequestConnection(String param) throws IOException {


    URL url = new URL("http://localhost:111/" + param);
    System.out.println("url: " + url);
    HttpURLConnection inputConnection = (HttpURLConnection) url.openConnection();
    inputConnection.connect();
    return inputConnection;
  }

  /**
   * helper method to get the response to an api call
   *
   * @param param the api call (after the localhost call)
   * @return the String of the response body - if error occurs (like invalid filepath), returns null
   */
  static String tryRequestBody(String param)
      throws IOException, InterruptedException, URISyntaxException {
    URI uri = new URI("http://localhost:111/" + param);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
    return response.body();
  }
}
