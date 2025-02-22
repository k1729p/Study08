package kp.company.mvc.base;

import kp.company.base.TestsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The base class for tests with server-side support.
 */
public abstract class MVCTestsBase extends TestsBase {
    /**
     * {@link MockMvc}.
     */
    @Autowired
    protected MockMvc mockMvc;
}