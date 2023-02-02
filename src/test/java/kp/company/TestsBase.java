package kp.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import kp.company.service.CompanyServiceImpl;

/**
 * The base class for tests.
 *
 */
public abstract class TestsBase {

	@SpyBean
	private CompanyServiceImpl companyService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * The {@link MessageSourceAccessor}
	 */
	protected MessageSourceAccessor accessor;

	private AutoCloseable closeable;

	/**
	 * The constructor.
	 */
	public TestsBase() {
		super();
	}

	/**
	 * Initializes the {@link CompanyServiceImpl} data before every test.
	 * 
	 */
	@BeforeEach
	protected void initialize() {
		closeable = MockitoAnnotations.openMocks(this);
		companyService.reloadData();
		accessor = new MessageSourceAccessor(messageSource);
	}

	/**
	 * Releases the mocks after every test.
	 * 
	 * @throws Exception the {@link Exception}
	 */
	@AfterEach
	protected void finish() throws Exception {
		closeable.close();
	}
}
