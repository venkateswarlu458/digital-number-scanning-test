package digital.number.scanner.service;

import com.digital.number.scanner.service.DigitalNumberScannerService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ScannerServiceUnitTest {

    @Test
    public void testFinalNumber()
    {
        List<String>  list = Arrays.asList("    _  _     _  _  _  _  _ ","  | _| _||_||_ |_   ||_||_|","  ||_  _|  | _||_|  ||_| _|");
        Assert.assertEquals("Number","123456789",new DigitalNumberScannerService().getFinalNumber(list));
    }

    @Test
    public void testFinalNumberWithInCorrectChar()
    {
        List<String>  list = Arrays.asList("|   _  _     _  _  _  _  _ ","  | _| _||_||_ |_   ||_||_|","  ||_  _|  | _||_|  ||_| _|");
        Assert.assertEquals("Number","?23456789ILL",new DigitalNumberScannerService().getFinalNumber(list));
    }

    @Test
    public void testFinalNumberWithInCorrectLineLength()
    {
        List<String>  list = Arrays.asList("    _  _     _  _  _  _  _ ","  | _| _||_||_ |_   ||_||_| |","  ||_  _|  | _||_|  ||_| _|");
        Assert.assertEquals("Number","123456789",new DigitalNumberScannerService().getFinalNumber(list));
    }
}
