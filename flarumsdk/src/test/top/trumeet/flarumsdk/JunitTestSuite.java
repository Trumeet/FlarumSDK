package top.trumeet.flarumsdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import top.trumeet.flarumsdk.internal.parser.jsonapi.ParserTest;

/**
 * Created by Trumeet on 2017/10/16.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FlarumTest.class ,ParserTest.class
})
public class JunitTestSuite {
}
