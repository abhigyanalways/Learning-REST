import com.google.gson.Gson;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LearningREST {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {

        //preparing the DTO
        Transcript transcript=new Transcript();
        transcript.setAudio_url( "https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");

        //preparing DTO : converting it to JSON string using GSON
        Gson gson=new Gson();
        String JsonRequest = gson.toJson(transcript);
        System.out.println("the request json is: "+JsonRequest);

        //preparing the request (earlier we had HttpUrlConnection class , helped via streams)
        //This is the post request for submitting the audio file ... We will require a get request to get the transcript .
        HttpRequest postRequest = HttpRequest.newBuilder()//this is called builder pattern
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", "2570d3d9edd6424d871443eeef8cbfba")
                .POST(HttpRequest.BodyPublishers.ofString(JsonRequest))
                .build(); //BodyPublisher : Handles body of request [see]

        //preparing to send , HttpClient is mandatory
        HttpClient httpClient= HttpClient.newHttpClient();
        //The HttpResponse class is a generic type , Unlike the HttpRequest class with pre-defined fields
        HttpResponse<String> postResponse =httpClient.send(postRequest , HttpResponse.BodyHandlers.ofString());//BodyHandler : Halo body of response [see]
        System.out.println("the post request response : "+postResponse.body());

        //Now we shall work on setting up the get request to finally obtain the transcript
        //To remember : We will be utilising the ID returned after posting the audio file to access the transcript ,
        //This will be done by mapping the Json response of the post request to the dto and obtaining the id field only to pass it in the get request

        //Converting Json to same Java object transcript DTO for extracting id
        transcript=gson.fromJson(postResponse.body(), Transcript.class);//Syntax warning , We re-use the transcript variable!

        //preparing the  GET request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId()))
                .header("Authorization", "2570d3d9edd6424d871443eeef8cbfba")
                //.GET() is default
                .build();


        while(true) {// We will keep on sending the request unless the status is not changed (queued -> completed/error)
            //Storing the response of the get request as a JSON string.
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            //To remember JSON ~ String , therefore:


            transcript = gson.fromJson(getResponse.body(), Transcript.class);//syntax warning , We used the same transcript reference again

            System.out.println(transcript.getStatus()); // keeps printing queued
           if("completed".equals(transcript.getStatus()) || "error".equals( (transcript.getStatus()) ) ) {
               break; //and finally breaks the loop
           }
           Thread.sleep(1000); //fancy ways
        }// If the looping has stopped, it must mean that we finally have a fully completed transcript object
        // However, the field which is of prime importance is the text field :

        System.out.println("Transcription completed: \n"+transcript.getText());





    }
}
