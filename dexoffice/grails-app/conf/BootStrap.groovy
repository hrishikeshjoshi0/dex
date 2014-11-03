import org.springframework.web.context.support.WebApplicationContextUtils

import party.PartyRelationshipType
import party.PartyType
import party.Person
import product.ProductPriceType;
import product.ProductType;
import core.ContactMechType
import core.Enumeration
import core.EnumerationType
import core.RoleType
import core.Uom
import core.UomType

class BootStrap {
	
	def partyService

    def init = { servletContext ->
		def springContext = WebApplicationContextUtils.getWebApplicationContext(servletContext)
		
		//Person
		if(!PartyType.findByDescription("Person")) {
			def pt = new PartyType()
			pt.description = "Person"
			pt.save(flush:true)
		}
		
		if(!PartyType.findByDescription("Organization")) {
			def pt = new PartyType()
			pt.description = "Organization"
			pt.save(flush:true)
		}
		
		//Enumeration Types
		if(!EnumerationType.findByDescription("Gender")) {
			def et = new EnumerationType(description : "Gender")
			et.save(flush:true)
		}
		
		//Enumerations
		if(!Enumeration.findByEnumCodeAndEnumerationType("M",EnumerationType.findByDescription("Gender"))) {
			def e = new Enumeration()
			e.enumCode = "M"
			e.description = "Male"
			e.enumerationType = EnumerationType.findByDescription("Gender")
			e.save(flush:true)
		}
		
		if(!Enumeration.findByEnumCodeAndEnumerationType("F",EnumerationType.findByDescription("Gender"))) {
			def e = new Enumeration()
			e.enumCode = "F"
			e.description = "Female"
			e.enumerationType = EnumerationType.findByDescription("Gender")
			e.save(flush:true)
		}
		
		//Contact Mech Types
		if(!ContactMechType.findByDescription("POSTAL_ADDRESS")) {
			def cmt = ContactMechType.findOrCreateByDescription("POSTAL_ADDRESS")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("EMAIL")) {
			def cmt = ContactMechType.findOrCreateByDescription("EMAIL")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("MOBILE_NUMBER")) {
			def cmt = ContactMechType.findOrCreateByDescription("MOBILE_NUMBER")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("RESIDENCE_NUMBER")) {
			def cmt = ContactMechType.findOrCreateByDescription("RESIDENCE_NUMBER")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("OTHER_PHONE")) {
			def cmt = ContactMechType.findOrCreateByDescription("OTHER_PHONE")
			cmt.save(flush:true)
		}
		
		//Role Type
		if(!RoleType.findByDescription("OWNER")) {
			def rt = new RoleType(description:"OWNER")
			rt.save(flush:true)
		}
		
		if(!RoleType.findByDescription("CUSTOMER")) {
			def rt = new RoleType(description:"CUSTOMER")
			rt.save(flush:true)
		}
		
		if(!RoleType.findByDescription("LEAD")) {
			def rt = new RoleType(description:"LEAD")
			rt.save(flush:true)
		}
		
		//Party Relationship Type
		if(!PartyRelationshipType.findByNameAndValidRoleTypeFromAndValidRoleTypeTo("CUSTOMER",RoleType.findByDescription("OWNER"),RoleType.findByDescription("CUSTOMER"))) {
			def prType = new PartyRelationshipType()
			prType.description = "Customer Relationship"
			prType.name = "CUSTOMER"
			prType.validRoleTypeFrom = RoleType.findByDescription("OWNER")
			prType.validRoleTypeTo = RoleType.findByDescription("CUSTOMER")
			prType.save(flush:true)
		}
		
		//Dummy Owner
		if(!Person.findByCurrentFirstNameAndCurrentLastName("Admin","User")) {
			def p = new Person()
			p.currentFirstName = "Admin"
			p.currentLastName = "User"
			p.description = "Admin User"
			partyService.createPersonAndRole(p,"OWNER",null)
		}
		
		//UOM Type
		if(!UomType.findByName("CURRENCY")) {
			def uomType = new UomType(name : "CURRENCY",description:"Currency UOM Type")
			uomType.save(flush:true)
		}
		
		if(!UomType.findByName("PRODUCT_QUANTITY")) {
			def uomType = new UomType(name : "PRODUCT_QUANTITY",description:"Product Quantity UOM Type")
			uomType.save(flush:true)
		}
		
		if(!UomType.findByName("PRODUCT_HEIGHT")) {
			def uomType = new UomType(name : "PRODUCT_HEIGHT",description:"Product Height UOM Type")
			uomType.save(flush:true)
		}
		
		if(!UomType.findByName("PRODUCT_WIDTH")) {
			def uomType = new UomType(name : "PRODUCT_WIDTH",description:"Product Width UOM Type")
			uomType.save(flush:true)
		}
		
		if(!UomType.findByName("PRODUCT_DEPTH")) {
			def uomType = new UomType(name : "PRODUCT_DEPTH",description:"Product Depth UOM Type")
			uomType.save(flush:true)
		}
		
		//UOM
		def currencyUomType = UomType.findByName("CURRENCY")
		def productQuantityUomType = UomType.findByName("PRODUCT_QUANTITY")
		
		if(!Uom.findByAbbreviationAndUomType("INR",currencyUomType)) {
			def uom = new Uom(abbreviation: "INR", description: "Rupees", uomType: currencyUomType)
			uom.save(flush:true)
		}
		
		if(!Uom.findByAbbreviationAndUomType("EACH",productQuantityUomType)) {
			def uom = new Uom(abbreviation: "EACH", description: "Each", uomType: productQuantityUomType)
			uom.save(flush:true)
		}
		
		//ProductType
		if(!ProductType.findByName("SERVICE")) {
			def pt = new ProductType(name:"SERVICE", description : "SERVICE")
			pt.save(flush:true)
		}
		
		if(!ProductType.findByName("GOOD")) {
			def pt = new ProductType(name:"GOOD",description:"GOOD")
			pt.save(flush:true)
		}
		
		if(!ProductType.findByName("DIGITAL_GOOD")) {
			def pt = new ProductType(name:"DIGITAL_GOOD",description:"DIGITAL_GOOD")
			pt.save(flush:true)
		}
		
		if(!ProductType.findByName("FINISHED_GOOD")) {
			def pt = new ProductType(name:"FINISHED_GOOD",description:"FINISHED_GOOD")
			pt.save(flush:true)
		}
		
		//Product Price Type
		if(!ProductPriceType.findByValue("DEFAULT_PRICE")) {
			def ppt = new ProductPriceType(value:"DEFAULT_PRICE",description:"Default price")
			ppt.save(flush:true)
		}
		
		if(!ProductPriceType.findByValue("LIST_PRICE")) {
			def ppt = new ProductPriceType(value: "LIST_PRICE",description:"List price")
			ppt.save(flush:true)
		}
		
		if(!ProductPriceType.findByValue("MAXIMUM_PRICE")) {
			def ppt = new ProductPriceType(value: "MAXIMUM_PRICE",description:"Maximum price")
			ppt.save(flush:true)
		}
		
		if(!ProductPriceType.findByValue("MINIMUM_PRICE")) {
			def ppt = new ProductPriceType(value : "MINIMUM_PRICE",description:"Minimum price")
			ppt.save(flush:true)
		}
		
		//Marshalling
		springContext.getBean("customObjectMarshallers").register()
    }
	
    def destroy = {
    }
}
