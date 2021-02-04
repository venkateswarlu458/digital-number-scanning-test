package digital.number.scanner.service;

import com.digital.number.scanner.service.DigitalNumberScannerService;

import java.io.IOException;
import java.util.List;

public class ScannerServiceIntegrationImplTest  extends BaseScannerServiceIntegrationTest {

    @Override
    protected List<String> performScanning(String inputFilePath) throws IOException {
        return new DigitalNumberScannerService().performScanning(inputFilePath);
    }

}
