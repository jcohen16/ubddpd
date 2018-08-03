Feature: Plan Health Home Page

	@scenario_nonportal_login_home_page
	Scenario Outline: Verify Plan Health Home Page logging in via NonPortal Page
		Given I login to Plan Health Non-Portal page site as User Type '<USER_TYPE>' with XID '<USER_XID>' with SSO Id '<USER_SSO_ID>' and Universal Plan Id '<UNIV_PLAN_ID>'
		And I validate the Page Header and Footer elements including the title for '[USER_TYPE]' '<USER_TYPE>', '[USER_FULL_NAME]' '<USER_FULL_NAME>' and '[PLAN_NAME]' '<PLAN_NAME>'
		Then the H1 tag plan name '<PLAN_NAME>' appears under Prudential logo
		And H1 tag 'Plan Health' header appears under the plan name
		And the View drop down appears under the 'Plan Health' header
		And the H2 Tag 'Plan Summary' appears under the View drop down
		
		#@Examples: