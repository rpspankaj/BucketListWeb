angular.module('ItemsApp', [])


.controller('ItemsController', function($scope, $http) {
$http.get("https://hack-rest.herokuapp.com/items").success(function (data) 
{ 
   $scope.itemList = data; 
   console.log("Items" , $scope.itemList); 
}); 
});