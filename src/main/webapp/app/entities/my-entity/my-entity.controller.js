(function() {
    'use strict';

    angular
        .module('crudApp')
        .controller('MyEntityController', MyEntityController);

    MyEntityController.$inject = ['MyEntity'];

    function MyEntityController(MyEntity) {

        var vm = this;

        vm.myEntities = [];

        loadAll();

        function loadAll() {
            MyEntity.query(function(result) {
                vm.myEntities = result;
                vm.searchQuery = null;
            });
        }
    }
})();
