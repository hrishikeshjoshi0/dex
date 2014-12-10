class UrlMappings {

	static mappings = {
		//API
		//"/customer/savePostalAddress" (controller:"customer",action:"savePostalAddress",method:"POST")
		
		"/api/customer"(resources:"customer")
		"/api/customer/$id/invoices"(controller:"customer",action:"invoices")
		"/api/customer/$id/payments"(controller:"customer",action:"payments")
		/*"/api/customer/customerReport/:id"(controller:"customer",action:"customerReport")
		"/api/customer/saveEmail"(controller:"customer",action:"saveEmail")
		"/api/customer/savePostalAddress"(controller:"customer",action:"savePostalAddress")
		"/api/customer/saveTelecomNumber"(controller:"customer",action:"saveTelecomNumber")*/
		
		"/api/settingType"(resources:"settingType")
		"/api/setting"(resources:"setting")
		
		"/api/invoice"(resources:"invoice")
		"/api/invoice/invoiceStatusTypes"(controller:"invoice",action:"invoiceStatusTypes")
		
		"/api/paymentMethodType"(resources:"paymentMethodType")
		
		"/api/payment"(resources:"payment")
		"/api/payment/recordPaymentForInvoice"(controller:"payment",action:"recordPaymentForInvoice")
		
		"/api/product"(resources:"product")
		"/api/productType"(resources:"productType")
		
		"/api/productPriceTypes"(resources:"productPriceType")
		
		"/api/paymentMethodTypes"(resources:"paymentMethodType")
		
		"/api/taxCategory"(resources:"taxCategory")
		
		"/api/$controller/$action?/$id?(.$format)?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/person"(resources:"person")

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
