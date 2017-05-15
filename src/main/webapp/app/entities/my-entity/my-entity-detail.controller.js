(function() {
    'use strict';

    angular
        .module('crudApp')
        .controller('MyEntityDetailController', MyEntityDetailController);

    MyEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MyEntity'];

    function MyEntityDetailController($scope, $rootScope, $stateParams, previousState, entity, MyEntity) {
        var vm = this;

        vm.myEntity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('crudApp:myEntityUpdate', function(event, result) {
            vm.myEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
