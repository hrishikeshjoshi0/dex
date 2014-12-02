class UrlMappings {

	static mappings = {
		//API
		//"/customer/savePostalAddress" (controller:"customer",action:"savePostalAddress",method:"POST")
		
		"/customer"(resources:"customer")
		"/customer/customerReport/:id"(controller:"customer",action:"customerReport")
		
		"/api/invoice"(resources:"invoice")
		"/api/invoice/invoiceStatusTypes"(controller:"invoice",action:"invoiceStatusTypes")
		
		"/api/paymentMethodType"(resources:"paymentMethodType")
		
		"/api/payment"(resources:"payment")
		"/api/payment/recordPaymentForInvoice"(controller:"payment",action:"recordPaymentForInvoice")
		
		"/api/product"(resources:"product")
		
		"/api/productPriceTypes"(resources:"productPriceType")
		
		"/api/paymentMethodTypes"(resources:"paymentMethodType")
		
		"/api/taxCategory"(resources:"taxCategory")
		
		"/api/$controller/$id?/$action?(.$format)?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/$controller/$id?/$action?(.$format)?"{
			constraints {
				// apply constraints here
			}
		}
		
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/person"(resources:"person")

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
