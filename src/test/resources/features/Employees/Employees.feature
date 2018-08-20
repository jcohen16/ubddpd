Feature: Employees

	@scenario_all_employees
	Scenario Outline: Verify All Employees Appear
		Given I open the Employees site
	    Then the page heading should be 'UDBDPD Employees'
		Given I click on the link to View All Employees
		Then the list of Employees should be as expected

	#@Examples:


	@scenario_specific_employee
	Scenario Outline: Verify Single Employee
		Given I open the Employees site
		And I click on the link to View All Employees
		Then the title of Employee with '[employeeName]' '<EmployeeName>' is '<EmployeeTitle>'

	#@Examples:
