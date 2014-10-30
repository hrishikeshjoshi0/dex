package party

class PartyClassification {
	
	PartyClassificationGroup partyClassificationGroup
	Date fromDate
	Date thruDate

	static belongsTo = [party:Party]

    static constraints = {
		partyClassificationGroup nullable:false
		fromDate nullable:false
		thruDate nullable:true
    }
}
