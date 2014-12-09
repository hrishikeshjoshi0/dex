var app = angular.module('product', ['ngResource','MessageCenterModule']);

app.factory('Product', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/product/:id";
    return $resource(
    		baseUrl,
    		{},
    		{
    			update: {
	    			url : baseUrl + "/update", 	
	    			method: 'PUT'
	    		}
    		}
    );
}]);

app.factory('ProductTypes', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/productTypes/:id.json";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.factory('ProductPriceTypes', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/productPriceTypes/:id";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.factory('TaxCategory', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/taxCategory/:id";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.controller('ProductListController', 
		['$scope','$modal','$log','Product','messageCenterService',
		 function($scope,$modal,$log,Product,messageCenterService) {
	$scope.products = Product.query();		
}]);

app.controller('ProductEditController', 
		['$scope','$modal','$log','$stateParams','$location','Product','ProductTypes','ProductPriceTypes','TaxCategory','messageCenterService',
	function($scope,$modal,$log,$stateParams,$location,Product,ProductTypes,ProductPriceTypes,TaxCategory,messageCenterService) {
	
	$scope.productTypes = ProductTypes.query();
	$scope.productPriceTypes = ProductPriceTypes.query();
	$scope.taxCategories = TaxCategory.query();
			
	$scope.id = $stateParams.id;
	$scope.product = Product.get({id:$scope.id});
	
	$scope.save = function () {
		Product.update($scope.product,function(data) {
			$scope.product.id = data.id;
			
			messageCenterService.add('success', 'The save was successful.' 
					, { status: messageCenterService.status.next,timeout: 5000,html:true});
		    $location.path("/products/show/"+data.id);
			//messageCenterService.add('success', 'Your action has been completed!', { status: messageCenterService.status.permanent });
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem.', { status: messageCenterService.status.unseen,timeout: 5000});
	   });
	};

	$scope.cancel = function () {
		console.log('Cancel : ' + $scope.product);
	};
}]);
		

app.controller('ProductCreateController', 
		['$scope','$modal','$log','$location','Product','ProductTypes','ProductPriceTypes','TaxCategory','messageCenterService',
	function($scope,$modal,$log,$location,Product,ProductTypes,ProductPriceTypes,TaxCategory,messageCenterService) {

	$scope.product = new Product();
	
	$scope.product.introductionDate = new Date();
	$scope.productTypes = ProductTypes.query();
	$scope.productPriceTypes = ProductPriceTypes.query();
	$scope.taxCategories = TaxCategory.query();
	
	$scope.isValid = function() {
		if(!$scope.product.productName || !$scope.product.productType || !$scope.product.introductionDate
				|| !$scope.product.productPrice.fromDate || !$scope.product.productPrice.amount) {
			messageCenterService.add('success', 'Please enter all required fields.' 
					, { status: messageCenterService.status.unseen,timeout: 5000,html:true});
			return false;
		}
		
		if($scope.product.taxable) {
			if(!$scope.product.taxCategory) {
				messageCenterService.add('success', 'The product is marked as taxable, so please add the tax category.' 
						, { status: messageCenterService.status.unseen,timeout: 5000,html:true});
				return false;
			}
		}
		
		return true;
	}
			
	$scope.save = function () {
		
		if(!$scope.isValid()) {
			return;
		}
		
		$scope.product.$save(function(data) {
			$scope.product.id = data.id;
			messageCenterService.add('success', 'Product has been created.' 
					, { status: messageCenterService.status.next,timeout: 5000,html:true});
		    $location.path("/products/show/"+data.id);
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the product.', { status: messageCenterService.status.unseen,timeout: 5000});
	   });
	};

	$scope.cancel = function () {
		console.log('Cancel : ' + $scope.product);
	};
}]);

app.controller('ProductShowController', ['$scope','$stateParams','$modal','messageCenterService','Product',function($scope,$stateParams,$modal,messageCenterService,Product) {
	$scope.id = $stateParams.id;
	
	$scope.init = function() {
		$scope.product = Product.get({id : $scope.id},function(data) {
		});
	}
	
	$scope.init();
}]);