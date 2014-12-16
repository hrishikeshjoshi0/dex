import invoice.InvoiceItemType

import org.springframework.web.context.support.WebApplicationContextUtils

import party.Organization
import party.PartyRelationshipType
import party.PartyType
import payment.PaymentMethodType
import payment.PaymentType
import product.ProductPriceType
import product.ProductType
import tax.TaxAuthority
import tax.TaxCategory
import tax.TaxRate
import tax.TaxType
import core.ContactMechType
import core.Enumeration
import core.EnumerationType
import core.Geo
import core.GeoType
import core.RoleType
import core.Setting
import core.SettingType
import core.Status
import core.StatusType
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
		//Company
		if(!RoleType.findByDescription("PARENT_ORGANIZATION")) {
			def rt = new RoleType(description:"PARENT_ORGANIZATION")
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
		if(!PartyRelationshipType.findByNameAndValidRoleTypeFromAndValidRoleTypeTo("CUSTOMER",RoleType.findByDescription("PARENT_ORGANIZATION"),RoleType.findByDescription("CUSTOMER"))) {
			def prType = new PartyRelationshipType()
			prType.description = "Customer Relationship"
			prType.name = "CUSTOMER"
			prType.validRoleTypeFrom = RoleType.findByDescription("PARENT_ORGANIZATION")
			prType.validRoleTypeTo = RoleType.findByDescription("CUSTOMER")
			prType.save(flush:true)
		}
		
		//Dummy Owner
		if(!Organization.findByName("-COMPANY-")) {
			def o = new Organization()
			o.name = "-COMPANY-"
			partyService.createOrganizationAndRole(o,"PARENT_ORGANIZATION",null)
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
		
		//Geo Types
		if(!GeoType.findByName("COUNTRY")) {
			def geoType = new GeoType(name:"COUNTRY")
			geoType.save(flush:true)
		}
		
		if(!GeoType.findByName("STATE")) {
			def geoType = new GeoType(name:"STATE")
			geoType.save(flush:true)
		}
		
		if(!GeoType.findByName("CITY")) {
			def geoType = new GeoType(name:"CITY")
			geoType.save(flush:true)
		}
		
		if(!Geo.findByGeoNameAndGeoType("INDIA",GeoType.findByName("COUNTRY"))) {
			def geo = new Geo(geoName:"INDIA",geoCode:"IN")
			
			def countryGeoType = GeoType.findByName("COUNTRY")
			geo.geoType = countryGeoType
			geo.save(flush:true)
		}
		
		//Tax
		if(!TaxType.findByName("SERVICE_TAX")) {
			def taxType= new TaxType(name:"SERVICE_TAX")
			taxType.save(flush:true)
		}
		
		//Create organization 
		if(!Organization.findByName("Ministry of Finance")) {
			def organization = new Organization(name:"Ministry of Finance")
			organization.save(flush:true)
		}
		
		//Create tax authority
		if(!TaxAuthority.findByGeoAndParty(Geo.findByGeoName("INDIA"),
			Organization.findByName("Ministry of Finance"))) {
			def taxAuthority = new TaxAuthority(geo:Geo.findByGeoName("INDIA"),party:
				Organization.findByName("Ministry of Finance"))
			taxAuthority.save(flush:true)
		}
			
		//Create Tax Category
		if(!TaxCategory.findByName("SERVICE_TAX")) {
			def taxCategory = new TaxCategory(name:"SERVICE_TAX",description:"Service Tax")
			taxCategory.save(flush:true)
		}
		
		//StatusType=INVOICE_STATUS
		if(!StatusType.findByDescription("INVOICE_STATUS")) {
			def statusType = new StatusType()
			statusType.description = "INVOICE_STATUS"
			statusType.save(flush:true)
		}
		
		//StatusType=PAYMENT_STATUS
		if(!StatusType.findByDescription("PAYMENT_STATUS")) {
			def statusType = new StatusType()
			statusType.description = "PAYMENT_STATUS"
			statusType.save(flush:true)
		}
		
		//StatusItems for INVOICE_STATUS
		def invoiceStatusType = StatusType.findByDescription("INVOICE_STATUS")
		
		if(!Status.findByStatusCodeAndStatusType("DRAFT",
			invoiceStatusType)) {
			def status = 
				new Status(statusCode:"DRAFT",
					statusType:invoiceStatusType,description:"Draft")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("DRAFT",invoiceStatusType)) {
			def status =
				new Status(statusCode:"DRAFT",
					statusType:invoiceStatusType,description:"Draft")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("CANCELLED",
			invoiceStatusType)) {
			def status =
				new Status(statusCode:"CANCELLED",
					statusType:invoiceStatusType,
					description:"Cancelled")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("APPROVED",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"APPROVED",
				statusType:invoiceStatusType,
				description:"Approved")
			status.save(flush:true)
		}
	
		if(!Status.findByStatusCodeAndStatusType("SENT",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"SENT",
				statusType:invoiceStatusType,
				description:"Sent")
			status.save(flush:true)
		}
	
		if(!Status.findByStatusCodeAndStatusType("READY",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"READY",
				statusType:invoiceStatusType,
				description:"Ready for Posting")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("WRITE_OFF",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"WRITE_OFF",
				statusType:invoiceStatusType,
				description:"Write Off")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("PAID",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"PAID",
				statusType:invoiceStatusType,
				description:"Paid")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("PARTIALLY_PAID",
			invoiceStatusType)) {
			def status =
			new Status(statusCode:"PARTIALLY_PAID",
				statusType:invoiceStatusType,
				description:"Partially Paid")
			status.save(flush:true)
		}
			
		//Payment status
		def paymentStatusType = StatusType.findByDescription("PAYMENT_STATUS")
		
		if(!Status.findByStatusCodeAndStatusType("RECEIVED",
			paymentStatusType)) {
			def status =
			new Status(statusCode:"RECEIVED",
				statusType:paymentStatusType,
				description:"Received")
			status.save(flush:true)
		}
			
		if(!Status.findByStatusCodeAndStatusType("CONFIRMED",
			paymentStatusType)) {
			def status =
			new Status(statusCode:"CONFIRMED",
				statusType:paymentStatusType,
				description:"Confirmed")
			status.save(flush:true)
		}
			
		//InvoiceItemType
		if(!InvoiceItemType.findByName("INV_DISCOUNT_ADJ"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_DISCOUNT_ADJ",description:"Invoice Discount")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INV_DPROD_ITEM"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_DPROD_ITEM",description:"Invoice Digital Good Item")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INV_MISC_CHARGE"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_MISC_CHARGE",description:"Invoice Miscellaneous Charges")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INV_PROD_ITEM"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_PROD_ITEM",description:"Invoice Product Item")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INV_SALES_TAX"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_SALES_TAX",description:"Invoice Sales Tax")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INV_SHIPPING_CHARGES"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INV_SHIPPING_CHARGES",description:"Invoice Shipping and Handling")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INVOICE_ADJ"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INVOICE_ADJ",description:"Invoice Adjustment")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("INVOICE_ITM_ADJ"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"INVOICE_ITM_ADJ",description:"Invoice Item Adjustment")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("ITM_DISCOUNT_ADJ"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"ITM_DISCOUNT_ADJ",description:"Invoice Item Discount")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("ITM_REPLACE_ADJ"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"ITM_REPLACE_ADJ",description:"Invoice Item Replacement")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("ITM_SHIPPING_CHARGES"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"ITM_SHIPPING_CHARGES",description:"Invoice Item Shipping and Handling")
			invoiceItemType.save(flush:true)
		}
		
		if(!InvoiceItemType.findByName("ITM_SERVICE_TAX"))	 {
			def invoiceItemType = new InvoiceItemType(
				name:"ITM_SERVICE_TAX",description:"Invoice Item Service Tax")
			invoiceItemType.save(flush:true)
		}
		
		//Tax Rate
		if(!TaxRate.findByNameAndTaxCategory("Service Tax",TaxCategory.findByName("SERVICE_TAX"))) {
			def tr = new TaxRate()
			tr.name = "Service Tax"
			tr.taxCategory = TaxCategory.findByName("SERVICE_TAX")
			tr.taxAuthority = TaxAuthority.findByGeoAndParty(Geo.findByGeoName("INDIA"),
						Organization.findByName("Ministry of Finance"))
			tr.taxPercentage = 12.36
			tr.fromDate = Date.parse("yyyy-MM-dd", "2014-01-01")
			tr.taxType = TaxType.findByName("SERVICE_TAX")
			tr.save(flush:true)
		}
		
		//Payment Method Type
		if(!PaymentMethodType.findByCodeAndDescription("CASH", "Cash")) {
			def pmt = new PaymentMethodType(code:"CASH",description:"Cash")
			pmt.save(flush:true)
		}
		
		if(!PaymentMethodType.findByCodeAndDescription("PERSONAL_CHECK", "Personal Check")) {
			def pmt = new PaymentMethodType(code:"PERSONAL_CHECK",description:"Personal Check")
			pmt.save(flush:true)
		}
		
		//PaymentType
		if(!PaymentType.findByCodeAndDescription("RECEIPT","Receipt")) {
			def pt = new PaymentType(code:"RECEIPT",description:"Receipt")
			pt.save(flush:true)
		}
		
		if(!PaymentType.findByCodeAndDescription("TAX_PAYMENT","Tax payment")) {
			def pt = new PaymentType(code:"TAX_PAYMENT",description:"Tax payment")
			pt.save(flush:true)
		}
		
		if(!PaymentType.findByCodeAndDescription("CUSTOMER_PAYMENT","Customer payment")) {
			def pt = new PaymentType(code:"CUSTOMER_PAYMENT",description:"Customer payment")
			pt.save(flush:true)
		}
		
		if(!PaymentType.findByCodeAndDescription("CUSTOMER_REFUND","Customer refund")) {
			def pt = new PaymentType(code:"CUSTOMER_REFUND",description:"Customer refund")
			pt.save(flush:true)
		}
		
		//Setting Types
		if(!SettingType.findByCodeAndDescription("INVOICE_SETTINGS","Invoice Settings")) {
			def st = new SettingType(code:"INVOICE_SETTINGS",description:"Invoice Settings")
			st.save(flush:true)
		}
		
		if(!Setting.findByCodeAndDescription("INVOICE_SEQUENCE_NUMBER","Invoice Sequence Number")) {
			def st = new Setting(code:"INVOICE_SEQUENCE_NUMBER",description:"Invoice Sequence Number")
			st.settingType = SettingType.findByCode("INVOICE_SETTINGS")
			st.value = "1"
			st.fromDate = new Date()
			st.thruDate = null
			st.save(flush:true)
		}
		
		if(!Setting.findByCodeAndDescription("INVOICE_NUMBER_MASK","Invoice Number Mask")) {
			def st = new Setting(code:"INVOICE_NUMBER_MASK",description:"Invoice Number Mask")
			st.settingType = SettingType.findByCode("INVOICE_SETTINGS")
			st.value = "INV%05d"
			st.fromDate = new Date()
			st.thruDate = null
			st.save(flush:true)
		}
		
		//Marshalling
		springContext.getBean("customObjectMarshallers").register()
    }
	
    def destroy = {
    }
}
