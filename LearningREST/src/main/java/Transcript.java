
//Today I learned a few fascinating properties of dto and the Java object to Json Serializer Deserializer Libraries :
//In a nutshell, transcript object here is a dto that would be used for not only the request body, but also for the incoming response body
//It has to be noticed that the library can easily handle absence of fields, thereby enabling us to use the dto with whatever number of fields we might want to populate This makes the dto a two way DTO essentially
//However,as a matter of fact , in this case we will be converting the Java object to Json (via gson) which will obviously follow the standards of rest
public class Transcript {

    //When this object would be transferred as a Json body it would have an audio_field key and the empty id is ignored by gson
    private String audio_url;

    //Edited this field later. This time the DTO would be used for the response to Json mapping
    private String id;

    //Edited this at an even later point in time ....This time, this dto will store the response of the latter get request Therefore, we will need sufficient fields

    private String status;

    private String text;

    public String getAudio_url(){
        return audio_url;
    }

    public void setAudio_url(String audio_field) {
        this.audio_url= audio_field;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Transcript{" +
                "audio_url='" + audio_url + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

