(function() {
    'use strict';

    angular
        .module('crudApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('my-entity', {
            parent: 'entity',
            url: '/my-entity',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MyEntities'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/my-entity/my-entities.html',
                    controller: 'MyEntityController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('my-entity-detail', {
            parent: 'my-entity',
            url: '/my-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MyEntity'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/my-entity/my-entity-detail.html',
                    controller: 'MyEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MyEntity', function($stateParams, MyEntity) {
                    return MyEntity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'my-entity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('my-entity-detail.edit', {
            parent: 'my-entity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/my-entity/my-entity-dialog.html',
                    controller: 'MyEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MyEntity', function(MyEntity) {
                            return MyEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('my-entity.new', {
            parent: 'my-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/my-entity/my-entity-dialog.html',
                    controller: 'MyEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                code: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('my-entity', null, { reload: 'my-entity' });
                }, function() {
                    $state.go('my-entity');
                });
            }]
        })
        .state('my-entity.edit', {
            parent: 'my-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/my-entity/my-entity-dialog.html',
                    controller: 'MyEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MyEntity', function(MyEntity) {
                            return MyEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('my-entity', null, { reload: 'my-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('my-entity.delete', {
            parent: 'my-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/my-entity/my-entity-delete-dialog.html',
                    controller: 'MyEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MyEntity', function(MyEntity) {
                            return MyEntity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('my-entity', null, { reload: 'my-entity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
