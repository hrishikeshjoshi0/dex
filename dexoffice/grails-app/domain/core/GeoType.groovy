package core

class GeoType {
	
	String name
	String description
	boolean hasTable
	
	static belongsTo = [ parent : UomType ]
	static hasMany = [children:UomType,geo:Geo]
	static mappedBy = [ parent: "none"]

	static constraints = {
		name nullable:true,blank:true
		description nullable:true,blank:true
		parent nullable:true
	}
}
