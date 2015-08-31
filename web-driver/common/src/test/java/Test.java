import com.test.selenium.webdriver.common.ProcessUtil;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;

public class Test {



    @org.junit.Test
    public void testProcessUtil() throws IOException {
        int processId = 1840;
        System.out.println(ProcessUtil.getPrivateMemory(processId));
        System.out.println(ProcessUtil.getAllProcessIds(processId));
        System.out.println(ProcessUtil.getProcessStatusWithSubprocesses(processId, 0));
    }
}
