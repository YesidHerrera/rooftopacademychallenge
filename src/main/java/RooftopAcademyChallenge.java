import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RooftopAcademyChallenge {

    private static final String APP_URL = "https://rooftop-career-switch.herokuapp.com/";

    public static void main(String[] args) {
        String token = getAuthToken();
        if(token !=null) {
            List<String> blocks = getBlocks(token);
            // TODO order blocks
        }
    }

    private static String getAuthToken(){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "token?email=yesidregis@gmail.com").openConnection();
            connection.setRequestMethod("GET");
            return new ObjectMapper().readTree(connection.getInputStream()).get("token").asText();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private static List<String> getBlocks(String token) {
        List<String> blocks = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "blocks?token=" + token).openConnection();
            connection.setRequestMethod("GET");
            new ObjectMapper().readTree(connection.getInputStream()).get("data").elements().forEachRemaining(node -> blocks.add(node.textValue()));
        } catch (IOException e) {
            System.out.println(e);
        }
        return blocks;
    }
}
