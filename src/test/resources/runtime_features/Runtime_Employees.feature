Feature: Employees

	@scenario_all_employees
	Scenario Outline: Verify All Employees Appear
		Given I open the Employees site
	    Then the page heading should be 'UDBDPD Employees'
		Given I click on the link to View All Employees
		Then the list of Employees should be as expected

    Examples:
      |userId|password|
      #Basic Iteration
      #|test|test|
      


	@scenario_specific_employee
	Scenario Outline: Verify Single Employee
		Given I open the Employees site
		And I click on the link to View All Employees
		Then the title of Employee with '[employeeName]' '<EmployeeName>' is '<EmployeeTitle>'

    Examples:
      |EmployeeName|EmployeeTitle|
      #Single Employee 1
      |Jack S|Technical Leader|
      #Single Employee 2
      |Richard P|Sr Software Engineer|
      
