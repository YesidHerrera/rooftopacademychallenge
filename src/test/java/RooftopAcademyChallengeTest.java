import org.junit.jupiter.api.Test;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

class RooftopAcademyChallengeTest {

    private final String DUMMY_TOKEN = "dummy token";

    @Test
    void run_rooftop_default_test() {
        String[] result = RooftopAcademyChallenge.check(new String[]{"f319", "3720", "4e3e", "46ec", "c7df", "c1c7", "80fd", "c4ea"}, "b93ac073-eae4-405d-b4ef-bb82e0036a1d");
        String[] expected = new String[]{"f319", "46ec", "c1c7", "3720", "c7df", "c4ea", "4e3e", "80fd"};
        assertArrayEquals(result, expected, "Result and expected doesn't match");
    }

    @Test
    void test_when_block_size_is_one() {
        String[] test = new String[]{"test"};
        String[] result = RooftopAcademyChallenge.check(test, DUMMY_TOKEN);
        assertEquals(1, result.length, "Result must contain only 1 item");
        assertArrayEquals(result, test, "Result and expected doesn't match");
    }

    @Test
    void test_when_empty_blocks() {
        String[] result = RooftopAcademyChallenge.check(new String[]{}, DUMMY_TOKEN);
        assertEquals(0, result.length, "Result must be empty");
    }

    @Test
    void test_when_no_token() {
        String[] result = RooftopAcademyChallenge.check(new String[]{"test"}, null);
        assertEquals(0, result.length, "Result must be empty");
    }
}