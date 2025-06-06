package example.cashcard;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class CashCardJSONTest {
    @Autowired
    private JacksonTester<CashCard> json;
    @Autowired
    private JacksonTester<CashCard[]> jsonList;
    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45, "sarah1"),
                new CashCard(100L, 1.00, "sarah1"),
                new CashCard(101L, 150.00, "sarah1"));
    }

    @Test
    public void cashCardSerializationTest() throws IOException {
        CashCard cashCard = cashCards[0];
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
        assertThat(json.write(cashCard)).extractingJsonPathStringValue("@.owner").isEqualTo("sarah1");
    }
    @Test
    public void cashCardDeserializationTest() throws IOException{
        String expected = """
                {
                  "id": 99,
                  "amount": 123.45,
                  "owner": "sarah1"
                }
                """;
        assertThat(json.parse(expected)).isEqualTo(cashCards[0]);
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
        assertThat(json.parseObject(expected).owner()).isEqualTo("sarah1");
    }

    @Test
    void cashCardListSerializationTest() throws IOException {
        JsonContent<CashCard[]> serialized = jsonList.write(cashCards);
        assertThat(serialized).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserializationTest() throws IOException {
        String expected = """
                [
                  {"id": 99, "amount": 123.45 , "owner": "sarah1"},
                  {"id": 100, "amount": 1.00 , "owner": "sarah1"},
                  {"id": 101, "amount": 150.00, "owner": "sarah1" }
                ]
                """;
        ObjectContent<CashCard[]> deserialized = jsonList.parse(expected);
        assertThat(deserialized).isEqualTo(cashCards);
    }
}
