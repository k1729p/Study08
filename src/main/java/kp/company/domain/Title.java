package kp.company.domain;

/**
 * Represents the job title of an employee.
 * 
 */
public enum Title {
	/**
	 * Analyst
	 */
	ANALYST("Analyst"),
	/**
	 * Developer
	 */
	DEVELOPER("Developer"),
	/**
	 * Manager
	 */
	MANAGER("Manager");

	private final String name;

	/**
	 * Parameterized constructor.
	 * 
	 * @param name the name of the title
	 */
	Title(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the title.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}