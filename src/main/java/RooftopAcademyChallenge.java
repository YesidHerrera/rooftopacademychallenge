import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RooftopAcademyChallenge {

    private static final String APP_URL = "https://rooftop-career-switch.herokuapp.com/";

    public static void main(String[] args) throws IOException {
        String token = callTokenEndPoint();
        List<String> blocks = callBlocksEndPoint(token);
        check(blocks,token);
    }

    private static List<String> check(List<String> blocks, String token) throws IOException {
        String currentBlock = blocks.remove(0);
        List<String> result = new ArrayList<>();
        result.add(currentBlock);
        while(!blocks.isEmpty()) {
            currentBlock = getNextBlock(token, currentBlock, blocks);
            if(currentBlock != null) {
                result.add(currentBlock);
            } else {
                System.out.println("Something goes wrong :(");
                break;
            }
        }
        boolean isOrdered = callCheckEndPoint(token, "{\"encoded\": \"" + String.join("",result) + "\"}");
        if (isOrdered) {
            System.out.println("Done! :)");
        }
        return result;
    }

    private static String getNextBlock(String token, String currentBlock, List<String> oldList) throws IOException {
        for(String nextBlock : oldList) {
            if(oldList.size() == 1) {
                return oldList.remove(0);
            } else if(callCheckEndPoint(token, "{\"blocks\":[ \"" + currentBlock + "\",\"" + nextBlock + "\"]}")) {
                return oldList.remove(oldList.indexOf(nextBlock));
            }
        }
        return null;
    }

    private static boolean callCheckEndPoint(String token, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "check?token=" + token).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        byte[] input = body.getBytes(StandardCharsets.UTF_8);
        OutputStream os = connection.getOutputStream();
        os.write(input, 0, input.length);
        return new ObjectMapper().readTree(connection.getInputStream()).get("message").asBoolean();
    }

    private static String callTokenEndPoint() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "token?email=yesidregis2@gmail.com").openConnection();
        connection.setRequestMethod("GET");
        return new ObjectMapper().readTree(connection.getInputStream()).get("token").asText();
    }

    private static List<String> callBlocksEndPoint(String token) throws IOException {
        List<String> blocks = new ArrayList<>();
        HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "blocks?token=" + token).openConnection();
        connection.setRequestMethod("GET");
        new ObjectMapper().readTree(connection.getInputStream()).get("data").elements().forEachRemaining(node -> blocks.add(node.textValue()));
        return blocks;
    }
}
