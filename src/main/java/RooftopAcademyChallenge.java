import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RooftopAcademyChallenge {

    private static final String APP_URL = "https://rooftop-career-switch.herokuapp.com/";

    public static void main(String[] args){
        String token = callTokenEndPoint();
        String[] blocks = callBlocksEndPoint(token);
        String[] result = check(blocks,token);
        System.out.println("Result:" + String.join("",result));
    }

    public static String[] check(String[] blocksArray, String token){
        List<String> newList = new ArrayList<>();
        if(blocksArray.length>0 && token!=null) {
            List<String> blocks = new ArrayList<>(Arrays.asList(blocksArray));
            String currentBlock = blocks.remove(0);
            newList.add(currentBlock);
            while(!blocks.isEmpty()) {
                currentBlock = getNextBlock(token, currentBlock, blocks);
                if(currentBlock != null) {
                    newList.add(currentBlock);
                } else {
                    System.out.println("Unable to find next block :(");
                    break;
                }
            }
            boolean isOrdered = newList.size()==1 || callCheckEndPoint(token, "{\"encoded\": \"" + String.join("",newList) + "\"}");
            if (isOrdered) {
                System.out.println("Check passed! :)");
            }
        }
        return newList.toArray(new String[0]);
    }

    private static String getNextBlock(String token, String currentBlock, List<String> oldList) {
        for(String nextBlock : oldList) {
            if(oldList.size() == 1) {
                return oldList.remove(0);
            } else if(nextBlock.equals(currentBlock) ||
                    callCheckEndPoint(token, "{\"blocks\":[ \"" + currentBlock + "\",\"" + nextBlock + "\"]}")) {
                return oldList.remove(oldList.indexOf(nextBlock));
            }
        }
        return null;
    }

    public static boolean callCheckEndPoint(String token, String body){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "check?token=" + token).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            OutputStream os = connection.getOutputStream();
            os.write(input, 0, input.length);
            return new ObjectMapper().readTree(connection.getInputStream()).get("message").asBoolean();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String callTokenEndPoint(){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "token?email=yesidregis@gmail.com").openConnection();
            connection.setRequestMethod("GET");
            return new ObjectMapper().readTree(connection.getInputStream()).get("token").asText();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String[] callBlocksEndPoint(String token) {
        List<String> blocks = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RooftopAcademyChallenge.APP_URL + "blocks?token=" + token).openConnection();
            connection.setRequestMethod("GET");
            new ObjectMapper().readTree(connection.getInputStream()).get("data").elements().forEachRemaining(node -> blocks.add(node.textValue()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return blocks.toArray(new String[0]);
    }
}


