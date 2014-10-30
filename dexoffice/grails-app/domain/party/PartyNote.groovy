package party

import core.Note

class PartyNote {
	
	Party party
	Note note

    static constraints = {
		party nullable:false
		note nullable:false
    }
	
}
