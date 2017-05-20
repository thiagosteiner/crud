# crud
This application was generated using JHipster 4.2.0, you can find documentation and help at [https://jhipster.github.io/documentation-archive/v4.2.0](https://jhipster.github.io/documentation-archive/v4.2.0).

As this is a toy example, there are all configuration files and also one JWT token in my configs. This way other people can run locally with minor problems.
    For security reasons, do not place your security info on public repositories.
    If you use this project on production, please change JWT token and all passwords in configuration files. 

This configuration enforces the use of HTTPS and port 443.

Please create a keystore.12 and place it under the folder /keystore and place this folder inside your project's root directory.

Follow instructions in application-prod.yml to generate your keystore.12 and set your password on this file.

Please build for production and use docker compose for running app and MongoDB. As described bellow, 
build using:
    
    ./mvnw package -Pprod docker:build

and then run:
    
    docker-compose -f src/main/docker/app.yml up -d

Your swagger API documentations are available in:

    https://localhost:443/v2/api-docs/

There is also a swagger.json static file on this git respository root directory if you prefer.

Please use:

    http://editor.swagger.io/#/ 

to read the docs in a pretty interface (upload a json file to editor as CORS will not be enabled by default)

As there is JWT auth please authenticate in:

    https://localhost:443/api/authenticate

Default auth for admin are:
    
    {"password": "admin", "username": "admin"}

Send POST and receive JWT. All interaction with API use Authorization header with value Bearer followed by JWT token.

I created an entity called MyEntity, use:

     https://localhost:443/api/my-entities

and directions in swagger docs. (MyEntity id is self incremented by MongoDB so do not set it when creating a new entity).


## Development

To start your application in the dev profile, simply run:

    ./mvnw


For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts

## Building for production

To optimize the crud application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war


Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.
For example, to start a mongodb database in a docker container, run:

    docker-compose -f src/main/docker/mongodb.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mongodb.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod docker:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`yo jhipster:docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`yo jhipster:ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[JHipster Homepage and latest documentation]: https://jhipster.github.io
[JHipster 4.2.0 archive]: https://jhipster.github.io/documentation-archive/v4.2.0

[Using JHipster in development]: https://jhipster.github.io/documentation-archive/v4.2.0/development/
[Using Docker and Docker-Compose]: https://jhipster.github.io/documentation-archive/v4.2.0/docker-compose
[Using JHipster in production]: https://jhipster.github.io/documentation-archive/v4.2.0/production/
[Running tests page]: https://jhipster.github.io/documentation-archive/v4.2.0/running-tests/
[Setting up Continuous Integration]: https://jhipster.github.io/documentation-archive/v4.2.0/setting-up-ci/


