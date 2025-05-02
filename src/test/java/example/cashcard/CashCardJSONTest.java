package example.cashcard;
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
    @Test
    public void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(1L, 123.45);
        JsonContent<CashCard> serialized = json.write(cashCard);
        assertThat(serialized).isStrictlyEqualToJson("expected.json");
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
}
