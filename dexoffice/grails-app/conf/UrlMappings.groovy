class UrlMappings {

	static mappings = {
		
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
		
		//API
		"/customer"(resources:"customer") 
		
		"/person"(resources:"person")

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
