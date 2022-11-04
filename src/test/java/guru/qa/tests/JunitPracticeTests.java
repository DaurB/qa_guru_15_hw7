package guru.qa.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import guru.qa.data.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class JunitPracticeTests {
    @BeforeAll
    static void beforeSettings() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
    }

    @ValueSource(strings = {"The Lord of the Rings", "Tarantino"})
    @ParameterizedTest(name = "Поиск по запросу {0}")
    void kinopoiskSearchTest(String testData) {
        open("https://www.kinopoisk.ru/");
        $("input[type = 'text']").setValue(testData);
        $("button[type = 'submit']").click();
        $(".search_results").shouldHave(text(testData));
    }


    @CsvSource(value = {
            "Johnny Depp, 1998, Terry Gilliam, Fear and Loathing in Las Vegas"
    })
    @ParameterizedTest(name = "Расширенный поиск")
    void kinopoiskAdvancedSearchTest(String actorName, String year, String directorName, String filmName) {
        open("https://www.kinopoisk.ru/s/");
        $("input[name = 'm_act[actor]']").setValue(actorName);
        $("#year").setValue(year);
        $("input[name = 'm_act[cast]']").setValue(directorName);
        $("#formSearchMain input[type = 'button']").click();
        $(".styles_originalTitle__JaNKM").shouldHave((text(filmName)));
    }


    static Stream<Arguments> chessButtonTest() {
        return Stream.of(
                Arguments.of(List.of("Играть в шахматы онлайн на сайте #1!"), Locale.Русский),
                Arguments.of(List.of("Play Chess Online on the #1 Site!"), Locale.English)
        );
    }

    @MethodSource("chessButtonTest")
    @ParameterizedTest(name = "Проверка отображения названия кнопок при смене языка: {1}")
    void chessButtonTest(List<String> titleText, Locale locale) {
        open("https://www.chess.com/");
        $(".nav-menu-area button[type = 'button']").click();
        $$(".modal-language-picker-list .modal-language-picker-item a div").find(text(locale.name())).click();
        $$(".index-title").shouldHave(CollectionCondition.textsInAnyOrder(titleText));
    }


}
