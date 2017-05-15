(function() {
    'use strict';

    angular
        .module('crudApp')
        .controller('MyEntityDeleteController',MyEntityDeleteController);

    MyEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'MyEntity'];

    function MyEntityDeleteController($uibModalInstance, entity, MyEntity) {
        var vm = this;

        vm.myEntity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MyEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
