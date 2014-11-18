var app = angular.module('invoice', ['ngResource','ui.bootstrap','MessageCenterModule','customer']);

app.factory('Invoice', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/invoice/:id";
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

app.controller('InvoiceListController', 
		['$scope','$modal','$http','$log','Invoice','Customer','messageCenterService',
	 function($scope, $modal, $http,$log, Invoice, Customer,messageCenterService) {
	 $scope.invoices = Invoice.query();		
}]);

app.controller('InvoiceCreateController', 
		['$scope','$modal','$log','$http','Invoice','messageCenterService',
	function($scope,$modal,$log,$http,Invoice,messageCenterService) {

	$scope.invoice = new Invoice();
	$scope.invoice.items = [];
	
	$scope.invoice.invoiceDate = new Date();
	
	$scope.changePartyTo = function() {
		$scope.changeParty = true;
	}
	
	// Any function returning a promise object can be used to load values asynchronously
	 $scope.getParty = function(val) {
		 return $http.get('/dexoffice/customer', {
		      params: {
		        q: val
		      }
		    }).then(function(response){
		    	return response.data.map(function(item){
		            //return item.currentFirstName + " " + item.currentLastName;
		    		return item;
		        });
		    });
	 };
	 
	 $scope.suggestProduct = function(val) {
		 return $http.get('/dexoffice/product', {
		      params: {
		        q: val
		      }
		    }).then(function(response){
		    	return response.data.map(function(item){
		    		return item;
		        });
		    });
	 };
	 
	 $scope.newItem = {};
	 
	 $scope.getLineTotalAmount = function(item) {
		 if(item) {
			 item.lineTotalAmount = item.unitPrice * item.quantity;
			 return item.lineTotalAmount;
		 }
	 }
	 
	 $scope.getInvoiceSubTotalAmount = function() {
		 $scope.invoiceSubTotalAmount = 0.0;
		 $scope.invoiceTotalTaxAmount = 0.0; 
		 $scope.invoiceTotalAmount = 0.0;
		 
		 for (var i = 0; i < $scope.invoice.items.length; i++) {
			var item = $scope.invoice.items[i];
			$scope.invoiceSubTotalAmount += item.lineTotalAmount;
			$scope.invoiceTotalTaxAmount += item.tax?item.tax:0.0;
			
			$scope.invoiceTotalAmount += (+$scope.invoiceSubTotalAmount) + (+$scope.invoiceTotalTaxAmount); 
		 }
		 
		 return $scope.invoiceSubTotalAmount;
	 }
	 
	 $scope.loadProductDetails = function(item) {
		 $scope.newItem.productId = item.id;
		 $scope.newItem.productName = item.productName;
		 $scope.newItem.description = item.description;
		 $scope.newItem.quantity = 1.0;
		 $scope.newItem.tax = item.tax?item.tax:0.0;
		 $scope.newItem.unitPrice = item.defaultPrice.amount;
	 }
	 
	 $scope.addNewItem = function () {
		 $scope.invoice.items.push($scope.newItem);
		 $scope.newItem = {};
	 };
	 
	 $scope.formattedProduct = function(product) {
		 if(product) {
			 return product.productName;
		 }
	 }
	 
	 //	
	 $scope.fetchAddresses = function(customer) {
			if(customer != null) {
				var found = null;
				
				if(customer.partyContactMechs != null) {
					var data = customer.partyContactMechs;
					
					for (var i = 0; i < data.length; i++) {
						var element = data[i];
						if (element.type == "POSTAL_ADDRESS") {
							found = element;
							break;
						} 
					}
				}
				return found;
			}
	}
	 
	$scope.formattedName = function(person) {
		if(person) {
			return person.currentFirstName + " " + person.currentLastName;
		}
	}
	
	//typeahead-on-select($item, $model, $label)
	$scope.onSelect = function(data) {
		if(data == null) {
			$scope.addressData = null;
			$scope.invoice.partyId = undefined;
			return;
		}
		$scope.addressData = $scope.fetchAddresses(data);
		$scope.person = data;
		$scope.changeParty = false;
		
		$scope.invoice.partyId = data.id;
  	};
	
	$scope.save = function () {
		$scope.invoice.$save();
	};

	$scope.cancel = function () {
	};
}]);

app.controller('InvoiceShowController', ['$scope','$stateParams','$modal','messageCenterService','Invoice',function($scope,$stateParams,$modal,messageCenterService,Invoice) {
	$scope.id = $stateParams.id;
	
	$scope.init = function() {
		$scope.invoice = Invoice.get({id : $scope.id},function(data) {
			$scope.addressData = $scope.fetchAddresses($scope.invoice.party);
		});
	}
	
	$scope.getInvoiceSubTotalAmount = function() {
		 $scope.invoiceSubTotalAmount = 0.0;
		 $scope.invoiceTotalTaxAmount = 0.0; 
		 $scope.invoiceTotalAmount = 0.0;
		 
		 for (var i = 0; i < $scope.invoice.items.length; i++) {
			var item = $scope.invoice.items[i];
			$scope.invoiceSubTotalAmount += item.amount;
			$scope.invoiceTotalTaxAmount += item.tax?item.tax:0.0;
			
			$scope.invoiceTotalAmount += (+$scope.invoiceSubTotalAmount) + (+$scope.invoiceTotalTaxAmount); 
		 }
		 
		 return $scope.invoiceSubTotalAmount;
	 }
	
	$scope.fetchAddresses = function(customer) {
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "POSTAL_ADDRESS") {
						found = element;
						break;
					} 
				}
			}
			return found;
		}
	}
	
	$scope.init();
}]);