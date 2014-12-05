var app = angular.module('customer', ['ngResource','MessageCenterModule','ui.bootstrap','ui.grid']);

app.filter('customerName', function() {
	return function(customer) {
		if(customer != null) {
			return customer.currentFirstName + " " + customer.currentLastName;
		} else {
			return "";
		}
	};
});

app.filter('postalAddress', function() {
	return function(customer) {
		if(customer != null) {
			var found = null;
			
			console.log(customer.partyContactMechs);
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					
					console.log(element);
					
					if (element.type == "POSTAL_ADDRESS") {
						found = element;
					} 
				}
			}
			return found;
		} else {
			return "";
		}
	};
});

app.factory('Customer', ['$resource',function($resource){
	var baseUrl = "/dexoffice/customer/:id";
    return $resource(
    		baseUrl,
    		{},
    		{
        		savePostalAddress : {
        			url : baseUrl + '/savePostalAddress', 	
        			method: 'POST'
        		},
        		saveEmail : {
        			url : baseUrl + '/saveEmail', 	
        			method: 'POST'
        		},
        		saveTelecomNumber : {
        			url : baseUrl +  '/saveTelecomNumber', 	
        			method: 'POST'
        		},
        		invoices : {
        			url : baseUrl +  '/invoices', 	
        			method: 'GET',
        			isArray : true
        		},
        		payments : {
        			url : baseUrl +  '/payments', 	
        			method: 'GET',
        			isArray : true
        		}        		
    		}
    );
}]);


app.controller('CustomerCreateTelecomNumberController', ['$scope','$modalInstance','customer','mobile','Customer','messageCenterService', function($scope,$modalInstance,customer,mobile,Customer,messageCenterService) {
	$scope.customer = customer;

	if(mobile) {
		$scope.mobile = mobile;
	} else {
		$scope.mobile = {};
	}

	//$scope.contactMechPurposes = [{label:"Default",value:"COMMUNICATION"}]
	
	$scope.save = function () {
		var mobileData = {}
		
		var mobile = $scope.mobile;
		mobileData.contactNumber = mobile.value;
		mobileData.id = mobile.id;
		mobileData.partyId = $scope.customer.id;
		//mobileData.contactMechPurpose = mobile.contactMechPurpose.value;
		
		Customer.saveTelecomNumber(mobileData,function(data) {
			messageCenterService.add('success', 'Mobile number saved.', { status: messageCenterService.status.unseen,timeout: 5000});
		   	$modalInstance.close("save.success");
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close("save.error");
	   });
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerCreateEmailController', ['$scope','$modalInstance','customer','email','Customer','messageCenterService', function($scope,$modalInstance,customer,email,Customer,messageCenterService) {
	$scope.customer = customer;
	
	if(email) {
		$scope.email = email;
	} else {
		$scope.email = {};
	}
	
	$scope.save = function () {
		var emailData = {}
		var email = $scope.email

		emailData.email = email.value;
		emailData.id = email.id;
		emailData.partyId = $scope.customer.id;
		
		Customer.saveEmail(emailData,function(data) {
			messageCenterService.add('success', 'Email address saved.', { status: messageCenterService.status.unseen,timeout: 5000});
		   	$modalInstance.close("save.success");
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close("save.error");
	   });
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerCreatePostalAddressController', ['$scope','$modalInstance','customer','Customer','messageCenterService', function($scope,$modalInstance,customer,Customer,messageCenterService) {
	$scope.customer = customer;
	
	$scope.save = function () {
		var postalAddress = $scope.customer.postalAddress
		postalAddress.partyId = $scope.customer.id
		console.log("Postal address " + postalAddress.id);
		Customer.savePostalAddress(postalAddress,function(data) {
			messageCenterService.add('success', 'Postal address saved.', { status: messageCenterService.status.unseen,timeout: 5000});
		   	$modalInstance.close("save.success");
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close("save.error");
	   });
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerQuickCreateEditController', ['$scope','$modalInstance','customer','Customer','messageCenterService', function($scope,$modalInstance,customer,Customer,messageCenterService) {
	if(customer.id) {
		$scope.customer = customer;
	} else {
		$scope.customer = new Customer();
	}
	
	$scope.save = function () {
	   $scope.customer.$save({},function(data) {
		   	messageCenterService.add('success', 'Customer has been added.', { status: messageCenterService.status.unseen,timeout: 5000});
		   	var result = {};
		   	result.status = "save.success";
		   	result.data = data;
		   	
		   	$modalInstance.close(result);
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while saving the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close("save.error");
	   });
	};

	$scope.cancel = function () {
	   $modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerDeleteController', ['$scope','$modalInstance','Customer','customer','messageCenterService',function($scope,$modalInstance,Customer,customer,messageCenterService) {
	$scope.customer = customer;
	
	$scope.delete = function () {
		Customer.delete({id:$scope.customer.id},function(data) {
		   	messageCenterService.add('success', 'Customer was deleted.', { status: messageCenterService.status.unseen,timeout: 5000});
			$modalInstance.close('delete.success');
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while deleting the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	        $modalInstance.close('delete.error');
	    });	
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

app.controller('CustomerCreateController', ['$scope','Customer',function($scope,Customer) {
	$scope.customer = new Customer();
	
	$scope.save = function () {
		$scope.customer.$save(function(data) {
			$scope.customer.id = data.id;
			
			messageCenterService.add('success', 'The customer has been created.' 
					, { status: messageCenterService.status.next,timeout: 5000,html:true});
		    $location.path("/customer/show/"+$scope.customer.id);
	    }, function(error) {
	        messageCenterService.add('warning', 'There was some problem while creating the customer.', { status: messageCenterService.status.unseen,timeout: 5000});
	   });
	};

	$scope.cancel = function () {
		$scope.customer = new Customer();
	};
}]);

app.controller('CustomerShowController', ['$scope','$stateParams','$modal','messageCenterService','Customer',function($scope,$stateParams,$modal,messageCenterService,Customer) {
	$scope.id = $stateParams.id;
	
	$scope.init = function() {
		$scope.customer = Customer.get({id : $scope.id},function(data) {
			$scope.addressData = $scope.fetchAddresses($scope.customer);
			$scope.mobilePhoneData = $scope.fetchMobileNumbers($scope.customer);
			$scope.emailData = $scope.fetchEmails($scope.customer);
			$scope.getInvoices();
			$scope.getPayments();
		});
	}
	
	$scope.init();
	
	//
	$scope.getInvoices = function() {
		$scope.invoices = Customer.invoices({id:$scope.customer.id});
	}
	
	$scope.getPayments = function() {
		$scope.payments = Customer.payments({id:$scope.id});
	}
	
	//Edit methods for Address, Contact Mechs
	$scope.createOrUpdatePostalAddress = function(customer) {
		//Opens a modal
		$scope.customer = customer;
		$scope.customer.postalAddress = $scope.fetchAddresses(customer);
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/modalCreatePostalAddress.html',
			controller : 'CustomerCreatePostalAddressController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		modalInstance.result.then(function(data) {
		   if(data == 'save.success') {
			   $scope.init();
		   }
		}, function() {
			//$log.info('Modal dismissed at: ');
		});
	}
	
	$scope.saveEmail = function(customer,email) {
		//Opens a modal
		$scope.customer = customer;
		$scope.email = email;
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/modalCreateEmail.html',
			controller : 'CustomerCreateEmailController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    },
			    email : function () {
				  return $scope.email;
				}
			}
		});

		modalInstance.result.then(function(data) {
		   if(data == 'save.success') {
			   $scope.init();
		   }
		}, function() {
			//$log.info('Modal dismissed at: ');
		});
	}
	
	$scope.saveMobileNumber = function(customer,mobile) {
		$scope.customer = customer;
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/modalCreateTelecomNumber.html',
			controller : 'CustomerCreateTelecomNumberController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    },				
				mobile : function () {
				  return mobile;
			    }
			}
		});

		modalInstance.result.then(function(data) {
		   if(data == 'save.success') {
			   $scope.init();
		   }
		}, function() {
			//$log.info('Modal dismissed at: ');
		});
	}
	
	$scope.fetchEmails = function(customer) {
		if(customer != null) {
			var found = [];
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "EMAIL") {
						found.push(element);
					} 
				}
			}
			return found;
		}
	}
	
	$scope.fetchMobileNumbers = function(customer) {
		if(customer != null) {
			var found = [];
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "MOBILE_NUMBER") {
						found.push(element);
					} 
				}
			}
			
			return found;
		}
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
}]);

app.controller('CustomerListController', ['$scope','$modal','$log','Customer','messageCenterService',function($scope,$modal,$log,Customer,messageCenterService) {
	$scope.customers = [];
	
	$scope.mobilePhone = function(customer) {
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "MOBILE_NUMBER") {
						found = element;
					} 
				}
			}
			return found;
		}
	}
	
	$scope.email = function(customer) {
		
		if(customer != null) {
			var found = null;
			
			if(customer.partyContactMechs != null) {
				var data = customer.partyContactMechs;
				
				for (var i = 0; i < data.length; i++) {
					var element = data[i];
					if (element.type == "EMAIL") {
						found = element;
					} 
				}
			}
			return found;
		}
	}
	
	$scope.listAllCustomers = function() {
		Customer.query(function(data) {
			$scope.customers = data;
		},function(error){
		    messageCenterService.add('warning', 'There was some error..', { status: messageCenterService.status.permanent,timeout: 5000});
		});
	}
	
	$scope.confirmDelete = function(cust) {
		$scope.customer = cust;
		
		var modalInstance = $modal.open({
			templateUrl : 'app/customer/deleteConfirmation.html',
			controller : 'CustomerDeleteController',
			size : 'lg',
			resolve : {
				customer : function () {
			      return $scope.customer;
			    }
			}
		});

		
		modalInstance.result.then(function(data) {
			$scope.listAllCustomers();
		}, function() {
			//$log.info('Modal dismissed at: ' + customer);
		});
	}
	
	$scope.edit = function(cust) {
		$scope.customer = cust;
		
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
			$scope.listAllCustomers();
		}, function() {
			$log.info('Modal dismissed at: ' + customer);
		});
	}
	
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
		   $scope.listAllCustomers();
		}, function() {
			$log.info('Modal dismissed at: ');
		});
	}
	
	//List all customers
	$scope.listAllCustomers();
}]);

//
app.controller('CustomerInvoiceListController', 
		['$scope','$stateParams','$modal','$http','$log','Invoice','Customer','messageCenterService',
	 function($scope, $stateParams,$modal, $http,$log, Invoice, Customer,messageCenterService) {
	 $scope.id = $stateParams.id;
	 
	 $scope.customer = Customer.get({id : $scope.id},function(data) {
		$scope.invoices = Customer.invoices({id:$scope.id});
	 });
}]);

app.controller('CustomerPaymentListController', 
		['$scope','$stateParams','$modal','$http','$log','Invoice','Customer','messageCenterService',
	 function($scope, $stateParams,$modal, $http,$log, Invoice, Customer,messageCenterService) {
	 $scope.id = $stateParams.id;
	 
	 $scope.customer = Customer.get({id : $scope.id},function(data) {
		$scope.payments = Customer.payments({id:$scope.id});
	 });
}]);