Feature: Asset Allocations

	@scenario_old_asset_allocations
	Scenario Outline: Asset Allocations on current QA Site
		Given the user is logged in to B2B using <userId>
			And the user closes the tutorial popup if it appears
			And I click on the link to Plan '<planName>'
      # And I validate the header is correct on the 'Plan Health' page
		Then 'Asset Allocation' module is displayed on old Plan Health Page
        And I store the Asset Allocation Details for Plan '<planName>' based on the ESB Service
        And the title of the Asset Allocation module is 'ASSET ALLOCATION'
        And a text field showing 'As of MM/DD/YY' appears with the same date returned by ESB
        And there is an arrow icon in the top header
        And there is a text heading with the text '% OF HOLDINGS'
        And the Asset Allocation module has the expected values based on ESB and colors for this Plan on the old Plan Health Page
			| Stable Value | background-color: #9F7F45 |
			| Fixed Income | background-color: #0478C2 |
			| Balanced     | background-color: #162B6D |
			| Large Cap Stock | background-color: #98C5DF |
			| Mid Cap Stock   | background-color: #2AB1BD |
			| Small Cap Stock | background-color: #F38944 |
			| Global Stock    | background-color: #B461AF |
			| International Stock | background-color: #F0C3B6 |
			 #@Examples
			