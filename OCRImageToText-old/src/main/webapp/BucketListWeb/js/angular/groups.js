
angular.module('GroupApp', [])


.controller('GroupController', function($scope, $http) {
$http.get("https://hack-rest.herokuapp.com/groups?userId=2").success(function (data) 
{ 
   $scope.groupList = data; 
   console.log("groupList" , $scope.groupList); 
}); 
});
