package core

class Geo {
	
	String geoName
	String geoCode
	String abbreviation
	
	static belongsTo = [geoType : GeoType]
	
    static constraints = {
		geoName nullable:false,blank:false
		geoCode nullable:true,blank:true
		abbreviation nullable:true,blank:true
    }
}
