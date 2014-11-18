class UrlMappings {

	static mappings = {
		//API
		//"/customer/savePostalAddress" (controller:"customer",action:"savePostalAddress",method:"POST")
		
		"/customer"(resources:"customer")
		"/api/invoice"(resources:"invoice")
		"/api/invoice/invoiceStatusTypes"(controller:"invoice",action:"invoiceStatusTypes")
		"/api/product"(resources:"product")
		"/api/productPriceTypes"(resources:"productPriceType")
		"/api/taxCategory"(resources:"taxCategory")
		
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
