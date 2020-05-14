package pl.idzikpro.aliens.conf;

import pl.idzikpro.aliens.model.RandomAlien;

import java.util.Arrays;
import java.util.List;

public class AlienTestData {
    public static List<RandomAlien> list= Arrays.asList(
            new RandomAlien("sectoid","leader"),
            new RandomAlien("floater","navigator"),
            new RandomAlien("ethereal","commander")
    );
}
