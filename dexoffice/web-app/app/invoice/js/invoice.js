var app = angular.module('invoice', ['ngResource','ui.bootstrap','MessageCenterModule','customer']);

app.factory('PaymentMethodType', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/paymentMethodTypes/:id";
    return $resource(
    		baseUrl,
    		{}
    );
}]);

app.factory('Invoice', ['$resource',function($resource){
	var baseUrl = "/dexoffice/api/invoice/:id";
    return $resource(
    		baseUrl,
    		{},
    		{
    			update: {
	    			url : baseUrl + "/update", 	
	    			method: 'PUT'
	    		},
	    		invoiceStatusTypes: {
	    			url : baseUrl + "/invoiceStatusTypes", 	
	    			method: 'GET',
	    			isArray:true
	    		}
    		}
    );
}]);

app.controller('InvoiceListController', 
		['$scope','$modal','$http','$log','Invoice','Customer','messageCenterService',
	 function($scope, $modal, $http,$log, Invoice, Customer,messageCenterService) {
			
	 $scope.invoices = Invoice.query();
}]);

app.controller('ViewPaymentsController', 
		['$scope','$modal','$http','$log','$modalInstance','invoice','Invoice','Customer','messageCenterService',
	 function($scope, $modal, $http,$log, $modalInstance,invoice,Invoice, Customer,messageCenterService) {
	 
	$scope.invoice = invoice;		
	$scope.payments = invoice.payments;
	
	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('InvoiceCreateController', 
		['$scope','$modal','$log','$http','$location','$stateParams','Invoice','Customer','messageCenterService',
	function($scope,$modal,$log,$http,$location,$stateParams,Invoice,Customer,messageCenterService) {

	//typeahead-on-select($item, $model, $label)
	$scope.fetchAddresses = function(customer) {
		if (customer != null) {
			var found = null;
			if (customer.partyContactMechs != null) {
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
		  	
	//If customer is given.
	$scope.partyId = $stateParams.partyId;
	if($scope.partyId) {
		var c = Customer.get({id:$scope.partyId},function(data){
			$scope.onSelect(c);
		});
	}
	
	$scope.invoice = new Invoice();
	$scope.invoice.currentInvoiceStatus = "DRAFT"
	
	$scope.invoice.items = [];																																					

	$scope.invoice.invoiceDate = new Date();
	
	$scope.invoiceStatusTypes = Invoice.invoiceStatusTypes();
	
	$scope.recalculate = function(item) {
		item.tax = (item.quantity * item.unitPrice) * (item.taxPercentage * 0.01);
		item.amount = item.tax + (item.quantity * item.unitPrice);
	}
	
	$scope.changePartyTo = function() {																																																																																																																																																																																																																																																																																																																																																
		$scope.changeParty = true;
	}
	
	// Any function returning a promise object can be used to load values asynchronously
	 $scope.getParty = function(val) {
		 return $http.get('/dexoffice/api/customer', {
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
		 return $http.get('/dexoffice/api/product', {
		      params: {
		        q: val
		      }
		    }).then(function(response){
		    	if(response.data && response.data.length >= 0) {
		    		return response.data.map(function(item){
		    			return item;
		    		});
		    	}
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
			
			$scope.invoiceSubTotalAmount += (item.unitPrice * item.quantity);
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
		 $scope.newItem.unitPrice = item.defaultPrice.amount;
		 $scope.newItem.taxPercentage = item.taxPercentage;
		 
		 if(item.taxPercentage) {
			 $scope.newItem.tax = ($scope.newItem.quantity * $scope.newItem.unitPrice) * (item.taxPercentage * 0.01);//item.taxPercentage?item.taxPercentage:0.0 * ($scope.newItem.unitPrice);
		 } else {
			 $scope.newItem.tax = 0.0
		 }
		 $scope.newItem.amount = $scope.newItem.tax + ($scope.newItem.quantity * $scope.newItem.unitPrice);		 
	 }
	 
	 $scope.validateNewItem = function() {
		 if($scope.newItem.productId && $scope.newItem.productName && $scope.newItem.quantity > 0.0
				 && $scope.newItem.unitPrice >= 0.0 /*&& $scope.newItem.taxPercentage >= 0.0*/) {
			 return true;
		 }
		 
		 return false;
	 }
	 
	 $scope.addNewItem = function () {
		 if($scope.validateNewItem()) {
			 $scope.invoice.items.push($scope.newItem);
			 $scope.newItem = {};
		 }
	 };
	 
	 $scope.formattedProduct = function(product) {
		 if(product) {
			 return product.productName;
		 }
	 }
	 
	$scope.formattedName = function(person) {
		if(person) {
			return person.currentFirstName + " " + person.currentLastName;
		}
	}
	
  	$scope.isValid = function() {
  		//Validate the invoice.
		var valid = true;
		
		if(!$scope.invoice.partyId) {
			valid = false;
		}
		
		if(!$scope.invoice.invoiceDate) {
			valid = false;
		}
		
		if(!$scope.invoice.currentInvoiceStatus) {
			valid = false;
		}
		
		if(!$scope.invoice.items || $scope.invoice.items.length == 0) {
			valid = false;
		}
		
		return valid;
  	}
	
	$scope.save = function () {
		if($scope.isValid()) {
			$scope.invoice.$save(function(data) {
				//If success, save the invoice ID.
				$scope.invoice.id = data.id;
				
				messageCenterService.add('success', 'Invoice has been created.' 
						//+ '<a href="' + '/dexoffice/invoice/show/' + data.id + '">' + 'View' + '</a>'
						, { status: messageCenterService.status.next,timeout: 5000,html:true});
			    $location.path("/invoice/show/"+data.id);
				//messageCenterService.add('success', 'Your action has been completed!', { status: messageCenterService.status.permanent });
		    }, function(error) {
		        messageCenterService.add('warning', 'There was some problem while saving the invoice.', { status: messageCenterService.status.unseen,timeout: 5000});
		   });
		}
	};

	$scope.cancel = function () {
	};
	
	//
	$scope.addCustomer = function() {
		//Opens a modal
		$scope.customer = {};
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/quickCreate.html',
			controller : 'CustomerQuickCreateEditController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		modalInstance.result.then(function(data) {
			var c = data.data;
			
			if(c.status == 'save.success') {
				onSelect(c);
			}
			
		}, function() {
			$log.info('Modal dismissed at: ');
		});
	}
}]);

app.controller('InvoiceShowController', ['$scope','$stateParams','$modal','messageCenterService','Invoice',function($scope,$stateParams,$modal,messageCenterService,Invoice) {
	$scope.id = $stateParams.id;
	$scope.invoice = {};
	$scope.invoice.items = [];
	$scope.taxMode = "GROUP" //Or "INDIVIDUAL"
	
	$scope.init = function() {
		$scope.invoice = Invoice.get({id : $scope.id},function(data) {
			$scope.addressData = $scope.fetchAddresses($scope.invoice.party);
			$scope.calculateInvoiceTotalTaxAmount();
		});
	}
	
	$scope.invoiceStatusTypes = Invoice.invoiceStatusTypes();
	
	$scope.calculateInvoiceTotalTaxAmount = function() {
		$scope.invoiceTotalTaxAmount = 0.0;
		$scope.invoiceSubTotalAmount = 0.0;
		$scope.invoiceTotalAmount = 0.0;
		
		var tax = $scope.invoice.tax
		 
		 //Tax
		if(tax) {
			 for (var m in tax){
				//alert(m + " "+ tax[m].amount);
				//alert(tax[m].amount);	
			    var t = tax[m];
			    var amount = tax[m].amount;
			    $scope.invoiceTotalTaxAmount += amount?amount:0.0;
			 } 
		}
		
		//Invoice items.
		if($scope.invoice.items) {
		 for (var i = 0; i < $scope.invoice.items.length; i++) {
			 var item = $scope.invoice.items[i];
			 $scope.invoiceSubTotalAmount += item.unitPrice * item.quantity;
		  }
		}
		
		$scope.invoiceTotalAmount += (+$scope.invoiceSubTotalAmount) + (+$scope.invoiceTotalTaxAmount);
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
	
	$scope.viewPayments = function() {
		var modalInstance = $modal.open({
			templateUrl : 'app/invoice/views/viewPayments.html',
			controller : 'ViewPaymentsController',
			size : 'lg',
			resolve : {
				invoice : function () {
			      return $scope.invoice;
			    }
			}
		});

		modalInstance.result.then(function(data) {
			var c = data.data;
		}, function() {
			//$log.info('Modal dismissed at: ');
		});
		
	}
}]);