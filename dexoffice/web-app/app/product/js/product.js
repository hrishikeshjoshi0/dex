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
		['$scope','$modal','$log','$stateParams','Product','ProductTypes','ProductPriceTypes','TaxCategory','messageCenterService',
	function($scope,$modal,$log,$stateParams,Product,ProductTypes,ProductPriceTypes,TaxCategory,messageCenterService) {
	
	$scope.productTypes = ProductTypes.query();
	$scope.productPriceTypes = ProductPriceTypes.query();
	$scope.taxCategories = TaxCategory.query();
			
	$scope.id = $stateParams.id;
	$scope.product = Product.get({id:$scope.id});
	
	$scope.save = function () {
		Product.update($scope.product);
	};

	$scope.cancel = function () {
		console.log('Cancel : ' + $scope.product);
	};
}]);
		

app.controller('ProductCreateController', 
		['$scope','$modal','$log','Product','ProductTypes','ProductPriceTypes','TaxCategory','messageCenterService',
	function($scope,$modal,$log,Product,ProductTypes,ProductPriceTypes,TaxCategory,messageCenterService) {

	$scope.product = new Product();
	
	$scope.productTypes = ProductTypes.query();
	$scope.productPriceTypes = ProductPriceTypes.query();
	$scope.taxCategories = TaxCategory.query();
			
	$scope.save = function () {
		$scope.product.$save();
		console.log('Save : ' + $scope.product);
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