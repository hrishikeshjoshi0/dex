var framework = angular.module('framework', []);

var disabled = true;

framework.config(function ($httpProvider) {
  $httpProvider.responseInterceptors.push('myHttpInterceptor');
  var spinnerFunction = function spinnerFunction(data, headersGetter) {
    //$("#spinner").fadeIn(3000);
    return data;
  };

  $httpProvider.defaults.transformRequest.push(spinnerFunction);
});

framework.factory('myHttpInterceptor', function ($q, $window) {
  return function (promise) {
    return promise.then(function (response) {
      //$("#spinner").fadeOut(3000);
      return response;
    }, function (response) {
    	//$("#spinner").fadeOut(3000);
      return $q.reject(response);
    });
  };
});

app.directive('resize', function ($window) {
    return function (scope, element) {
        var w = angular.element($window);
        scope.getWindowDimensions = function () {
            return { 'h': w.height(), 'w': w.width() };
        };
        scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
            scope.windowHeight = newValue.h;
            //scope.windowWidth = newValue.w;

            scope.style = function () {
                return { 
                    'height': (newValue.h) + 'px',
                    //'width': (newValue.w - 100) + 'px' 
                };
            };

        }, true);

        w.bind('resize', function () {
            scope.$apply();
        });
    }
});