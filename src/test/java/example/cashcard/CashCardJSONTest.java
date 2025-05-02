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
                new CashCard(99L, 123.45),
                new CashCard(100L, 1.00),
                new CashCard(101L, 150.00));
    }

    @Test
    public void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(1L, 123.45);
        JsonContent<CashCard> serialized = json.write(cashCard);
        assertThat(serialized).isStrictlyEqualToJson("single.json");
        assertThat(serialized).hasJsonPathNumberValue("@.id");
        assertThat(serialized).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(serialized).hasJsonPathNumberValue("@.amount");
        assertThat(serialized).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
    }
    @Test
    public void cashCardDeserializationTest() throws IOException{
        CashCard cashCard = new CashCard(1L, 123.45);
        String expected = """
        {
            "id": 1,
            "amount": 123.45
        }
        """;
        ObjectContent<CashCard> deserialized = json.parse(expected);
        assertThat(deserialized).isEqualTo(cashCard);
        assertThat(deserialized.getObject().id()).isEqualTo(1);
        assertThat(deserialized.getObject().amount()).isEqualTo(123.45);
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
            { "id": 99, "amount": 123.45 },
            { "id": 100, "amount": 1.00 },
            { "id": 101, "amount": 150.00 }
         ]
         """;
        ObjectContent<CashCard[]> deserialized = jsonList.parse(expected);
        assertThat(deserialized).isEqualTo(cashCards);
    }
}
