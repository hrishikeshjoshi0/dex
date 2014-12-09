var app = angular.module('taxCategory', ['ngResource','ui.bootstrap','MessageCenterModule']);

app.factory('TaxCategory', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/taxCategory/:id";
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

app.controller('TaxCategoryListController', 
		['$scope','$modal','$http','$log','Customer','messageCenterService',
	 function($scope, $modal, $http,$log, Customer,messageCenterService) {
	 $scope.taxCategories = TaxCategory.query();		
}]);

app.controller('TaxCategoryCreateController', 
		['$scope','$modal','$log','$http','messageCenterService',
	function($scope,$modal,$log,$http,messageCenterService) {

	$scope.save = function () {
		$scope.invoice.$save();
	};

	$scope.cancel = function () {
	};
}]);

app.controller('TaxCategoryShowController', ['$scope','$stateParams','$modal','messageCenterService',function($scope,$stateParams,$modal,messageCenterService) {
	
	$scope.id = $stateParams.id;
	
	$scope.init = function() {
		
	}
	
	$scope.init();
}]);