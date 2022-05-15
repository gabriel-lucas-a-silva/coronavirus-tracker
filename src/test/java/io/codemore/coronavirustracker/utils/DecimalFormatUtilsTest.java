package io.codemore.coronavirustracker.utils;

import io.codemore.coronavirustracker.helpers.TestContainer;
import org.junit.Test;

import static io.codemore.coronavirustracker.utils.DecimalFormatUtils.decimalFormatter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DecimalFormatUtilsTest extends TestContainer {

    @Test
    public void shouldTransformAnIntegerIntoADecimalFormatWithThousandsSeparators() {
        int number = 150620703;

        String actualNumber = decimalFormatter(number);

        assertThat(actualNumber, is(equalTo("150.620.703")));
    }

}
