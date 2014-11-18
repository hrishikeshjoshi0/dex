// app.js
var mainApp = angular.module('main', ['ui.router','ui.date','ui.bootstrap','customer','product','invoice','framework','ngResource']);

mainApp.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');
    $stateProvider
        // HOME STATES AND NESTED VIEWS ========================================
        .state('home', {
            url: '/home',
            templateUrl: 'app/home/partial-home.html'
        })
        
        // nested list with custom controller
		.state('home.list', {
	        url: '/list',
	        templateUrl: 'app/home/partial-home-list.html',
	        controller: function($scope) {
	            $scope.dogs = ['Bernese', 'Husky', 'Goldendoodle'];
	        }
	    })
	
		// nested list with just some random string data
	    .state('home.paragraph', {
	        url: '/paragraph',
	        template: 'I could sure use a drink right now.'
	    })
        
        // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
	    .state('about', {
	        url: '/about',
	        views: {
	            // the main template will be placed here (relatively named)
	            '': { templateUrl: 'app/about/partial-about.html' },
	            // the child views will be defined here (absolutely named)
	            'columnOne@about': { template: 'Look I am a column!' },
	            // for column two, we'll define a separate controller 
	            'columnTwo@about': { 
	                templateUrl: 'app/about/table-about.html',
	                controller: 'scotchController'
	            }
	        }
	    })
	    .state('customers', {
	        url: '/customers',
	        views: {
	            '': { templateUrl: 'app/customer/customer.html' },
	            'left-navigation@customers': { templateUrl: 'app/customer/left-navigation.html' },
	            'right-navigation@customers': { templateUrl: 'app/customer/right-navigation.html' },
	            'main-content@customers': { 
	                templateUrl: 'app/customer/main-content.html'
	            }
	        }
	    })
	     .state('customers.create', {
	        url: '/create',
	        templateUrl: 'app/customer/create.html',
	        controller : 'CustomerCreateController'
	    })
	     .state('customers.list', {
	        url: '/list',
	        templateUrl: 'app/customer/list.html',
	        controller : 'CustomerListController'	        	
	    })
	    .state('customers.show', {
	        url: '/show/:id',
	        templateUrl: 'app/customer/show.html',
	        controller : 'CustomerShowController'	        	
	    })
	    // PRODUCTS PAGE AND MULTIPLE NAMED VIEWS =================================
	    .state('products', {
	        url: '/products',
	        views: {
	            '': { templateUrl: 'app/product/layout/main.html' },
	            'left-navigation@products': { templateUrl: 'app/product/layout/left-navigation.html' },
	            'right-navigation@products': { templateUrl: 'app/product/layout/right-navigation.html' },
	            'main-content@products': { 
	                templateUrl: 'app/product/layout/main-content.html'
	            },
	            'actions@products.show': { 
	                templateUrl: 'app/product/views/actions.html'
	            }
	        }
	    })
	     .state('products.list', {
	        url: '/list',
	        templateUrl: 'app/product/views/list.html',
	        controller : 'ProductListController'	        	
	    })
	    .state('products.show', {
	        url: '/show/:id',
	        templateUrl: 'app/product/views/show.html',
	        controller : 'ProductShowController'	        	
	    })
	    .state('products.create', {
	        url: '/create',
	        templateUrl: 'app/product/views/create.html',
	        controller : 'ProductCreateController'	        	
	    })
	    .state('products.edit', {
	        url: '/edit/:id',
	        templateUrl: 'app/product/views/edit.html',
	        controller : 'ProductEditController'	        	
	    })
	    // INVOICE PAGE AND MULTIPLE NAMED VIEWS =================================
	    .state('invoice', {
	        url: '/invoice',
	        views: {
	            '': { templateUrl: 'app/invoice/layout/main.html' },
	            'left-navigation@invoice': { templateUrl: 'app/invoice/layout/left-navigation.html' },
	            'right-navigation@invoice': { templateUrl: 'app/invoice/layout/right-navigation.html' },
	            'main-content@invoice': { 
	                templateUrl: 'app/invoice/layout/main-content.html'
	            },
	            'actions@invoice.show': { 
	                templateUrl: 'app/invoice/views/actions.html'
	            }
	        }
	    })
	    .state('invoice.create', {
	        url: '/create',
	        templateUrl: 'app/invoice/views/create.html',
	        controller : 'InvoiceCreateController'	        	
	    })	
	     .state('invoice.list', {
	        url: '/list',
	        templateUrl: 'app/invoice/views/list.html',
	        controller : 'InvoiceListController'	        	
	    })
	    .state('invoice.show', {
	        url: '/show/:id',
	        templateUrl: 'app/invoice/views/show.html',
	        controller : 'InvoiceShowController'	        	
	    });
});

//let's define the scotch controller that we call up in the about state
mainApp.controller('scotchController', function($scope) {
    $scope.message = 'test';
    $scope.scotches = [
        {
            name: 'Macallan 12',
            price: 50
        },
        {
            name: 'Chivas Regal Royal Salute',
            price: 10000
        },
        {
            name: 'Glenfiddich 1937',
            price: 20000
        }
    ];
    
});