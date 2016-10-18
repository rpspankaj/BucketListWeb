var app = angular.module('ItemsApp', ['ui.bootstrap']);

app.filter('startFrom', function () {
	return function (input, start) {
		if (input) {
			start = +start;
			return input.slice(start);
		}
		return [];
	};
});

app.controller('ItemsController', ['$scope','$http', 'filterFilter', function ($scope, $http, filterFilter) {
	
	
	
	 
	//$scope.itemList = [{"itemId":1,"itemName":"item3","gtin":"123456","price":20.0,"quantity":1},{"itemId":2,"itemName":"item2","gtin":"123436","price":20.0,"quantity":2},{"itemId":3,"itemName":"item4","gtin":"123477","price":120.0,"quantity":2},{"itemId":4,"itemName":"Bounty","gtin":"445578","price":30.0,"quantity":1},{"itemId":5,"itemName":"Snikers","gtin":"445578","price":10.0,"quantity":1},{"itemId":6,"itemName":"Sugar","gtin":"445578","price":14.0,"quantity":1},{"itemId":7,"itemName":"Milk","gtin":"445578","price":5.0,"quantity":1},{"itemId":8,"itemName":"Honey","gtin":"445578","price":8.0,"quantity":1},{"itemId":9,"itemName":"Tomato","gtin":"123456","price":2.0,"quantity":1}];
    $scope.itemList=[];
	_refreshPageData();
	 console.log("Items" , $scope.itemList); 
	// create empty search model (object) to trigger $watch on update
	$scope.search = {};

	$scope.resetFilters = function () {
		// needs to be a function or it won't trigger a $watch
		$scope.search = {};
	};

	// pagination controls
	$scope.currentPage = 1;
	$scope.totalItems = $scope.itemList.length;
	$scope.entryLimit = 8; // items per page
	$scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);

	// $watch search to update pagination
	$scope.$watch('search', function (newVal, oldVal) {
		$scope.filtered = filterFilter($scope.itemList, newVal);
		$scope.totalItems = $scope.filtered.length;
		$scope.noOfPages = Math.ceil($scope.totalItems / $scope.entryLimit);
		$scope.currentPage = 1;
	}, true);
	
	function _refreshPageData() {
        $http({
            method : 'GET',
            url : 'https://hack-rest.herokuapp.com/items'
        }).then(function successCallback(response) {
            $scope.itemList = response.data;
            console.log("Items" , $scope.itemList); 
        }, function errorCallback(response) {
            console.log("Erro calling Item Service");
        });
    }
}]);
